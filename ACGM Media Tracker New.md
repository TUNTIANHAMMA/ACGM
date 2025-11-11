# ACGM Media Tracker New

> This document updates the original "ACGM Media Tracker" specification to reflect the **current Spring Boot + MyBatis implementation** (截至 2024-XX-XX)。
> 所有差异均基于代码仓库 `/src/main` 与数据库脚本 `acgm_my_sql_schema_query_templates_my_sql_8_x.sql` 的真实状态。

## 1. 文档说明 (Document Notes)

- `ACGM Media Tracker.md` 描述的是完整产品愿景；本文件强调 **已落地** 与 **尚未实现** 的部分，方便后续排期。
- 若条目仅存在于原始文档而代码缺失，将以 "待实现 / Planned" 标注；已经有代码的功能会给出模块与文件路径。
- 版本控制：此文档将随代码一同维护，必要时可再次与原始文档对比。

## 2. 实现状态概览 (Implementation Snapshot)

| 模块 / Feature | 原始要求 | 当前实现 | 备注 / 代码路径 |
| --- | --- | --- | --- |
| 数据模型 & DAO | MySQL 8.x + MyBatis，覆盖用户、媒体、标签、收藏、评论、进度、API 缓存 | ✅ 已实现：实体、Mapper 与 XML、Service 层齐全；覆盖 `users`、`media_items`、`tags`、`media_tag_rel`(仅SQL)、`favorites`、`reviews`、`media_api_info`、`progress_anime/game/music` | `src/main/Java/.../entity|mapper|service`，SQL 脚本位于`src/main/resources` |
| 业务服务层 | Service + Impl 负责 CRUD、幂等校验、异常抛出 | ✅ 已实现：九个 Service Impl，使用 `ResourceNotFoundException`、`ServiceBeanUtils` | `service/**/*.java`, `service/impl/**/*.java` |
| 控制器 / REST API | 用户注册登录、媒体 CRUD、统计等 RESTful 接口 | ❌ 未实现：`controller/` 目录为空，无 `@RestController` | 需新增 Spring MVC 层 |
| 认证与安全 | JWT / Session、偏好设置 | ❌ 未实现：`users` 表无 `preference` 字段，也无 Security 配置 | 需引入 Spring Security 或自定义过滤器 |
| Analytics / 统计 | 图表、完成率、趋势等 | ❌ 未实现：无统计 Service/Mapper，也无 SQL 视图消费 | 可基于 `media_items` 聚合实现 |
| 外部 API 集成 | Bangumi / RAWG / Spotify、天气、推荐 | ⚠️ 部分准备：`media_api_info` 表和 Mapper 已有，但无集成逻辑或调度 | 需编写 Client + Service |
| 标签多对多 | 记录标签与内容映射 | ⚠️ SQL 已建 `media_tag_rel`，但尚无 Java 实体、Mapper、Service | 需新增 `MediaTagRel` 相关代码 |
| 进度 - 漫画 | 支持章节/卷管理 | ❌ 未实现：数据库无 `progress_comic`，代码亦无 | 需扩展 schema + Mapper |
| 存储过程 & Queries | 模板划分 queries.SQL | ✅ 最新拆分：`queries.sql` 保留模板示例，不影响核心开发 | 位于 `src/main/resources/queries.sql` |
| 测试层 | 覆盖 Mapper/Service | 🟡 部分：仅有 `UsersMapperTest` 学示例 | 应补充其余 Mapper/Service 测试 |

## 3. 数据模型 (Data Model)

以下内容来自 `acgm_my_sql_schema_query_templates_my_sql_8_x.sql`，为当前可用表结构。

### 3.1 已落地表 (Implemented Tables)

#### users

| 字段 | 类型 | 约束 / 描述 |
| --- | --- | --- |
| id | BIGINT UNSIGNED | PK, AUTO_INCREMENT |
| username | VARCHAR(32) | NOT NULL, UNIQUE |
| email | VARCHAR(64) | NOT NULL, UNIQUE |
| email_norm | VARCHAR(64) | 生成列，LOWER(TRIM(email))，保证唯一 |
| password | VARCHAR(255) | NOT NULL |
| role | ENUM('user','admin') | 默认 'user' |
| created_at | DATETIME | 默认 CURRENT_TIMESTAMP |
| updated_at | DATETIME | 自动更新时间 |

> 差异：原始文档中的 `preference JSON` 列尚未创建。

#### media_items

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT UNSIGNED | PK |
| user_id | BIGINT UNSIGNED | FK → users.id |
| type | ENUM('anime','manga','game','music','movie','tv') | 必填 |
| status | ENUM('planned','in_progress','completed','dropped','on_hold') | 默认 planned |
| title / notes / rating | VARCHAR / TEXT / DECIMAL | 标题、备注、评分 |
| start_date / finish_date | DATE | 进度日期 |
| cover_url / source | VARCHAR | 可选信息来源 |
| created_at / updated_at / deleted_at | DATETIME | 审计字段 |
| row_version | BIGINT | 并发控制 |
| finish_month | INT | 生成列：YYYYMM |

#### tags

| 字段 | 类型 | 备注 |
| --- | --- | --- |
| id | BIGINT UNSIGNED | PK |
| user_id | BIGINT UNSIGNED | FK → users.id |
| name | VARCHAR(64) | 用户内唯一 |
| created_at | DATETIME | 默认当前时间 |

> 差异：原方案中的 `color` 列未实现。

#### media_tag_rel

| 字段 | 类型 | 备注 |
| --- | --- | --- |
| media_id | BIGINT UNSIGNED | FK → media_items.id |
| tag_id | BIGINT UNSIGNED | FK → tags.id |
| PRIMARY KEY (media_id, tag_id) |

> 差异：无 `create_time` 列；Java 侧缺少实体/Mapper。

#### favorites

| 字段 | 类型 | 备注 |
| id | BIGINT UNSIGNED | PK |
| user_id | BIGINT UNSIGNED | FK |
| media_id | BIGINT UNSIGNED | FK |
| created_at | DATETIME | 默认当前时间 |
| UNIQUE KEY | (user_id, media_id) |

#### reviews

| 字段 | 类型 | 备注 |
| id | BIGINT UNSIGNED | PK |
| user_id / media_id | BIGINT UNSIGNED | FK |
| rating | DECIMAL(3,1) | 可空 |
| content | TEXT | 可空 |
| created_at | DATETIME | 默认当前时间 |

#### media_api_info

| 字段 | 类型 | 备注 |
| id | BIGINT UNSIGNED | PK |
| media_id | BIGINT UNSIGNED | FK |
| api_source | VARCHAR(32) | 必填 |
| api_id | VARCHAR(128) | 必填 |
| payload | JSON | 可空 |
| last_sync | DATETIME | 可空 |

#### progress_anime

| 字段 | 类型 | 备注 |
| media_id | BIGINT UNSIGNED | PK, FK |
| current_episode | INT UNSIGNED | 默认 0 |
| total_episodes | INT UNSIGNED | 可空 |
| updated_at | DATETIME | 自动更新时间 |

#### progress_game

| 字段 | 类型 | 备注 |
| media_id | BIGINT UNSIGNED | PK, FK |
| platform | VARCHAR(64) | 可空 |
| completion_percent | TINYINT UNSIGNED | 0-100 |
| play_time_hours | INT UNSIGNED | 默认 0 |
| updated_at | DATETIME | 自动更新时间 |

#### progress_music

| 字段 | 类型 | 备注 |
| media_id | BIGINT UNSIGNED | PK, FK |
| play_count | INT UNSIGNED | 默认 0 |
| listen_status | ENUM('listened','unheard','partial') | 默认 'unheard' |
| updated_at | DATETIME | 自动更新时间 |

### 3.2 尚未落地的 Schema / 差异列表

- `progress_comic`：表结构与业务逻辑均缺失。
- `users.preference`、`tags.color`、`media_tag_rel.create_time` 等规划字段尚未出现在 SQL 与实体中。
- `MediaTagRel`（多对多关系）没有对应的 Entity / Mapper / Service。
- 原文档提到的触发器、统计视图、存储过程示例仅在 SQL 模板中，尚未在代码中引用。

## 4. 持久化与服务实现 (Persistence & Services)

- **Entity 层**：`src/main/java/com/acgm/acgmmediatracker/entity`，每张落地表均有对应 POJO。
- **Mapper 接口 + XML**：`mapper/` 包 + `src/main/resources/mapper/*.xml`，包含常规 CRUD（`selectById/selectAll/insert/update/delete`）。
- **Service 接口**：`service/` 目录定义业务接口，主要职责是包装 Mapper 的 CRUD 并抛出 `ResourceNotFoundException`。
- **Service 实现**：`service/impl/` 采用构造器注入 + `@Transactional`，复用 `ServiceBeanUtils.copyNonNullProperties` 做差异更新。
- **缺口**：无 `MediaTagRel`、无跨表聚合、无业务校验（例如媒体类型与进度表一致由 DB Trigger 负责）。

## 5. 配置、查询模板与测试 (Config & Tests)

- **配置**：`src/main/resources/application.properties` 直连本地 MySQL（`jdbc:mysql://localhost:3306/acgm_tracker`），尚未区分 dev/test/prod，也未引入环境变量。
- **MyBatis 配置**：`mybatis.mapper-locations=classpath:mapper/*.xml`，`map-underscore-to-camel-case=true`。
- **查询模板**：`queries.sql` 仅保存示例 SQL / 存储过程，不作为运行期依赖。
- **测试**：`UsersMapperTest` 通过事务回滚示范插入 + 查询；其余 Mapper/Service 暂无测试覆盖。

## 6. API / 控制层差异 (API & Controller Gap)

- 原始文档第 5-6 章列出的注册、登录、媒体 CRUD、统计等 REST 接口 **尚未在代码中实现**。
- `src/main/java/.../controller` 目录为空，无 Spring MVC 依赖代码、无 Request/Response DTO。
- 目前也没有全局异常处理器、校验注解响应、或响应封装（`ResponseEntity`）。
- 引入 Controller 前需：
  1. 设计 DTO 与转换层，避免直接暴露 Entity；
  2. 决定认证方式（JWT / Session / Basic Auth）并配置 `spring-security`；
  3. 定义统一响应格式与错误码。

## 7. 未完成的业务能力 (Backlog from Original Spec)

1. **用户体系**：注册/登录、密码加密策略、偏好设置、资料编辑、数据导入导出。
2. **内容管理扩展**：按类型自适应的 DTO、媒体 + 标签多对多的增删改查、逻辑删除与回收站、批量导入。
3. **进度管理**：漫画进度表、跨类型的进度聚合视图、事务性创建（媒体条目 + 对应进度）
4. **统计分析**：完成率、类型分布、趋势图、Top 榜；可选择使用 SQL 视图或 Spring Data 投影。
5. **外部 API 集成**：Bangumi/RAWG/Spotify/OpenWeather 等 Client、同步任务、缓存策略（`media_api_info`）。
6. **管理/系统模块**：后台页面、日志审计、主题 / 国际化设置、任务调度。
7. **测试与质量**：Mapper/Service 层单测、集成测试、SQL 触发器/存储过程的验证用例。

## 8. 参考文件 (Key Files)

- `pom.xml` — Spring Boot 3.5.7 + MyBatis + MySQL。
- `src/main/resources/acgm_my_sql_schema_query_templates_my_sql_8_x.sql` — 最新 schema。
- `src/main/resources/mapper/*.xml` — Mapper SQL。
- `src/main/java/com/acgm/acgmmediatracker/service/**` — 业务实现。
- `src/test/java/com/acgm/acgmmediatracker/mapper/UsersMapperTest.java` — 当前示例测试。

> 下一步建议：在保持此文档同步更新的同时，为每个 Backlog 特性建立 Issue / 任务列表，逐步实现原始愿景。
