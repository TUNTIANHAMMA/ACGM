# TODO — ACGM Media Tracker

> 目标：在保持现有 Spring Boot + MyBatis 架构的基础上，按优先级逐步补齐原始文档与当前实现之间的差异，使整体业务逻辑完整、可交付。
>
> Objective: bring the codebase to feature parity with the original spec by tackling the gaps listed below in priority order.

## P0 · 核心差异（必须优先解决）
1. **标签多对多支撑 / Media-Tag Relation**
   - ✅ Entity/Mapper/XML/Service 以及 Mapper 级测试（含 create_time）已就绪。
   - 下一步：在媒体 CRUD/DTO 中串联标签关系（批量插入、幂等删除），补 Service 层单测，并规划事务策略。
2. **漫画进度管理 / Progress (Comic)**
   - ✅ Schema（`progress_comic` + trigger）与 Entity/Mapper/Service 已落地，可等待后续 Controller/业务编排。
   - 下一步：在媒体 CRUD 中串联漫画进度创建/更新逻辑，并补测试。
3. **认证与账号体系 / Auth & Account**
    - ✅ Spring Security、PasswordEncoder、AuthService + DTO 已具备注册/登录核心逻辑。
    - 下一步：实现 REST Controller、JWT/Session 管理、用户偏好 CRUD，以及集成测试（密码校验、重复账号校验）。
4. **外部 API 集成骨架 / External API Integration**
   - `media_api_info` 表与 Mapper 可用，但无具体 Client 与同步 Service。
   - 下一步：定义抽象 Client 接口，先挑选 Bangumi/RAWG 之一完成 demo，同步写缓存落库与失败重试策略。
5. **REST 控制层 / REST Controllers**
   - `controller/` 目录仍无实现，缺少任何 `@RestController`、请求 DTO、全局异常处理。
   - 下一步：先实现 Auth + Media Controller 雏形（含统一响应包装与校验），再扩展统计/标签接口。

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
   - Mapper 层已有多项集成测试，Service 与未来 Controller 仍缺用例。
   - 下一步重点是补 Service/Controller 的单元与集成测试，并针对 SQL 触发器/存储过程编写验证脚本或测试。
2. **配置分环境 / Config Profiles**
   - `application.properties` 目前直连本地 `acgm` 数据库；需拆分 dev/test/prod，并改用环境变量/配置中心管理。
3. **文档同步 / Documentation**
   - 确保 `ACGM Media Tracker New.md`、本文档、API 文档与代码保持一致。
4. **CI/CD 与质量门禁**
   - 建立基本流水线：编译、测试、格式检查；集成测试报告。

## 建议工作流 (Suggested Workflow)
1. 依据 P0 列表创建独立 Issue / 需求单，明确验收标准。
2. 每项功能交付前补充对应测试与文档变更。
3. 完成 P0 后，再评估 P1/P2 的拆分与排期，保持迭代的可验证性。
