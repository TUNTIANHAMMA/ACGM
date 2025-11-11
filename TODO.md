# TODO — ACGM Media Tracker

> 目标：在保持现有 Spring Boot + MyBatis 架构的基础上，按优先级逐步补齐原始文档与当前实现之间的差异，使整体业务逻辑完整、可交付。
>
> Objective: bring the codebase to feature parity with the original spec by tackling the gaps listed below in priority order.

## P0 · 核心差异（必须优先解决）
1. **REST 控制层 / REST Controllers**
   - Controller 目录缺失，所有 CRUD/统计 API 未暴露。
   - 需设计请求/响应 DTO、统一返回格式、全局异常处理。
2. **认证与账号体系 / Auth & Account**
   - 缺注册/登录流程、密码加密、JWT/Session；`users` 表未含 `preference` JSON。
   - 需要引入 Spring Security 或等效方案，并落地偏好字段及业务逻辑。
3. **标签多对多支撑 / Media-Tag Relation**
   - SQL 中有 `media_tag_rel`，但 Java 实体/Mapper/Service 未实现。
   - 需完善增删改查流程、在媒体 CRUD 中整合标签同步。
4. **漫画进度管理 / Progress (Comic)**
   - 原规范包含 `progress_comic`，当前 schema/代码都缺失。
   - 需扩展数据库、Mapper、Service，并确定与媒体类型绑定逻辑。
5. **外部 API 集成骨架 / External API Integration**
   - `media_api_info` 仅作为缓存表存在，无实际同步/client 调用。
   - 定义抽象 client/service，至少完成一个 demo 集成（如 Bangumi）。

## P1 · 业务完善与数据一致性
1. **Analytics & 统计接口**
   - 原文档要求完成率、类型分布、趋势等统计；当前缺逻辑/SQL。
   - 设计聚合查询或视图，提供 `/api/stats` 等端点。
2. **媒体生命周期增强**
   - 增加逻辑删除/回收站、批量导入、乐观锁校验（`row_version`）。
   - Service 层需要更多校验与业务规则（状态机、类型进度匹配）。
3. **用户偏好/导出**
   - 完善 `users.preference` JSON 字段的 CRUD，以及数据导出/导入能力。
4. **Admin & 系统配置**
   - 管理后台接口、系统主题/配置、日志审计尚未实现。

## P2 · 工程化与质量保障
1. **测试覆盖 / Testing**
   - 扩展 Mapper & Service 层单测；为未来 Controller 写集成测试。
   - 需要针对 SQL 触发器/存储过程编写验证脚本或测试。
2. **配置分环境 / Config Profiles**
   - `application.properties` 直接硬编码生产参数；需拆分 dev/test/prod，改用环境变量。
3. **文档同步 / Documentation**
   - 确保 `ACGM Media Tracker New.md`、本文档、API 文档与代码保持一致。
4. **CI/CD 与质量门禁**
   - 建立基本流水线：编译、测试、格式检查；集成测试报告。

## 建议工作流 (Suggested Workflow)
1. 依据 P0 列表创建独立 Issue / 需求单，明确验收标准。
2. 每项功能交付前补充对应测试与文档变更。
3. 完成 P0 后，再评估 P1/P2 的拆分与排期，保持迭代的可验证性。
