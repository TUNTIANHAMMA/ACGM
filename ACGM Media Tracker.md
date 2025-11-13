# ACGM Media Tracker

—— 多类型娱乐内容收藏与管理系统

## 一、项目概述

### 项目背景

随着 ACG（Anime、Comic、Game、Music）文化的普及，越来越多的用户同时接触多种娱乐媒介，如动画、漫画、游戏与音乐。
然而，目前这些内容的记录与管理往往分散在多个平台中（如 Bangumi、Steam、Spotify 等），缺乏一个**统一、可自定义、可扩展的个人记录系统**。

本项目旨在开发一个集**内容记录、进度追踪、评分统计与外部信息集成**于一体的综合性娱乐记录系统 ——
**ACGM Media Tracker**，帮助用户在一个平台中统一管理与展示各类娱乐内容。

### 项目目标

- 为用户提供一个**统一的 ACGM 内容记录与展示中心**；
- 支持多类型媒体内容（动画 / 漫画 / 游戏 / 音乐）；
- 实现灵活的记录、筛选、统计与可视化分析功能；
- 集成外部 API（如天气、番剧、游戏或音乐信息）以丰富系统内容；
- 采用**前后端分离架构（Spring Boot + ( Nodejs ) + React）**，便于后期扩展与维护。

## 二、系统架构与功能结构

### 系统结构（逻辑层）

``` txt
用户层（前端展示） ↓ 业务层（Spring Boot 后端服务） ↓ 数据层（MySQL 数据库存储）
```

系统采用三层架构模式，实现前后端分离设计，通过 RESTful API 进行数据交互。
后端提供统一接口服务，前端负责界面交互与动态渲染。

### 功能模块结构

系统整体采用模块化设计，分为五大核心模块：

| 模块名称       | 简要说明                                       |      |
| -------------- | ---------------------------------------------- | ---- |
| 用户模块       | 管理用户账户、资料与偏好设置                   |      |
| 内容管理模块   | 系统核心，用于录入、浏览、筛选与追踪 ACGM 条目 |      |
| 数据统计模块   | 提供多维度可视化分析与趋势图表                 |      |
| API 集成模块   | 调用第三方 API 丰富系统内容与展示效果          |      |
| 管理与扩展模块 | 系统维护、备份导出与功能扩展                   |      |

---



```plaintext
ACGM Media Tracker
├── 用户模块（User Module）
│   ├── 用户注册与登录（JWT / Session）
│   ├── 个人信息管理（头像、签名、偏好）
│   ├── 偏好设置（ACG 偏好、默认展示类型）
│   └── 数据同步与导出（JSON 备份 / 导入）
│
├── 内容管理模块（Media Module）
│   ├── 内容录入
│   │   ├── 手动录入
│   │   └── 外部导入（Bangumi / RAWG / 网易云 API）
│   │
│   ├── 内容浏览与筛选
│   │   ├── 浏览（分页、排序、详情页）
│   │   ├── 筛选（类型、状态、评分、标签）
│   │   └── 搜索（标题、关键字）
│   │
│   ├── 内容编辑与删除
│   │   ├── 编辑信息（评分、进度、状态）
│   │   └── 删除条目（逻辑删除）
│   │
│   ├── 标签与评分系统
│   │   ├── 自定义标签（多对多）
│   │   └── 用户评分（1–10 分）
│   │
│   └── 进度管理
│       ├── 游戏：时长、完成度
│       ├── 动漫：集数进度
│       ├── 漫画：章节进度
│       └── 音乐：播放状态（已听 / 未听）
│
├── 数据统计模块（Analytics Module）
│   ├── 内容总览统计（数量、完成率）
│   ├── 类型分布图（动画 / 游戏 / 音乐比例）
│   ├── 平均评分分析（Top 榜单）
│   ├── 时间趋势图（月度新增内容）
│   └── 用户活跃数据（可选：记录频率、偏好趋势）
│
├── API 集成模块（Integration Module）
│   ├── 天气信息显示（OpenWeather API）
│   ├── 动画 / 游戏信息导入（Bangumi / RAWG）
│   ├── 音乐封面 / 专辑信息（网易云 / Spotify）
│   └── 随机推荐模块（基于用户偏好或外部 API）
│
└── 管理与扩展模块（Admin & System）
   ├── 管理员后台（用户、内容、API 日志管理）
   ├── 数据导出 / 备份（JSON / CSV / PDF）
   ├── 系统配置（主题、缓存、日志）
   └── 开发者扩展（预留 API、插件接口）
```

## 功能模块说明

### 用户模块

| 功能           | 描述                       | 技术要点                 |      |
| -------------- | -------------------------- | ------------------------ | ---- |
| 用户注册与登录 | 支持邮箱或用户名注册登录   | JWT 认证、BCrypt 加密    |      |
| 个人信息管理   | 修改头像、昵称、签名、偏好 | 文件上传、数据库更新     |      |
| 偏好设置       | 设置默认展示类型、主题模式 | 用户表字段存储 JSON 配置 |      |
| 数据备份与导入 | 导出/导入用户媒体数据      | JSON 文件打包与解析      |      |

### 内容管理模块（核心）

| 功能       | 描述                                | 技术要点                    |
| ---------- | ----------------------------------- | --------------------------- |
| 内容录入   | 用户手动录入或通过外部 API 导入条目 | 表单提交 / 外部接口调用     |
| 内容浏览   | 分页展示内容，支持排序与详情页查看  | 后端分页查询                |
| 筛选与搜索 | 按类型、状态、标签、评分等筛选      | RESTful 参数过滤            |
| 编辑与删除 | 修改条目信息或逻辑删除              | CRUD 操作、软删除标记       |
| 标签管理   | 用户自定义标签并与条目关联          | 多对多关系表                |
| 进度管理   | 不同类型对应不同进度格式            | 多态映射设计（章节 / 时长） |

### 数据统计模块

| 功能                 | 描述                   | 展示方式      |
| -------------------- | ---------------------- | ------------- |
| 内容总览统计         | 汇总条目总数、完成率   | 饼图 / 柱状图 |
| 类型分布分析         | 各媒体类型占比         | 环形图        |
| 平均评分与榜单       | 展示最高评分 Top 条目  | 条形图        |
| 时间趋势统计         | 每月新增内容变化趋势   | 折线图        |
| 用户使用数据（可选） | 分析记录频率、偏好倾向 | 折线 / 区域图 |

> 使用 ECharts 实现可视化效果，后端统计计算后返回 JSON 数据。

### API 集成模块

| 模块                | 描述                       | 数据来源                |
| ------------------- | -------------------------- | ----------------------- |
| 天气信息显示        | 展示当前城市天气、温度     | OpenWeather API         |
| 动画 / 游戏信息导入 | 自动拉取条目信息与封面     | Bangumi / RAWG          |
| 音乐封面查询        | 获取歌曲封面与专辑信息     | 网易云 / Spotify API    |
| 随机推荐模块        | 根据偏好或热门数据随机推荐 | 本地算法 / API 数据融合 |

### 管理与扩展模块（可选）

- **系统设置**：夜间模式、主题切换、缓存刷新。
- **导出报告**：可生成年度娱乐总结报告（PDF / CSV）。
- **管理员后台**：管理用户与数据、查看日志。
- **开发扩展接口**：预留插件或移动端同步支持。

## 数据库设计

### ① users 表 — 用户信息表（核心用户数据）

| 字段名                               | 类型         | 约束                                 | 说明                                 |
| ------------------------------------ | ------------ | ------------------------------------ | ------------------------------------ |
| id                                   | BIGINT       | PK, AUTO_INCREMENT                   | 用户主键                             |
| username                             | VARCHAR(32)  | UNIQUE, NOT NULL                     | 用户名                               |
| password                             | VARCHAR(128) | NOT NULL                             | 加密密码（BCrypt）                   |
| email                                | VARCHAR(64)  | UNIQUE, NOT NULL                     | 邮箱                                 |
| avatar_url                           | VARCHAR(255) | DEFAULT NULL                         | 头像地址                             |
| signature                            | VARCHAR(100) | DEFAULT NULL                         | 个性签名                             |
| preference                           | JSON         | DEFAULT NULL                         | 用户偏好（默认展示类型、主题设置等） |
| role                                 | VARCHAR(20)  | DEFAULT 'user'                       | 用户角色（user/admin）               |
| created_at / updated_at / deleted_at | DATETIME     | (ON UPDATE)DEFAULT CURRENT_TIMESTAMP | 创建更新删除时间                     |

**索引：**

- `idx_user_username`
- `idx_user_email`

### ② media_items 表 — ACGM 内容主表（系统核心）

| 字段名                             | 类型                                                   | 约束                                 | 说明                   |
| ---------------------------------- | ------------------------------------------------------ | ------------------------------------ | ---------------------- |
| id                                 | BIGINT                                                 | PK, AUTO_INCREMENT                   | 条目主键               |
| user_id                            | BIGINT                                                 | FK → users(id)                       | 所属用户               |
| title                              | VARCHAR(128)                                           | NOT NULL                             | 内容标题               |
| type                               | ENUM('anime','comic','game','music')                   | NOT NULL                             | 内容 类型              |
| status                             | ENUM('not_started','in_progress','completed','paused') | DEFAULT 'not_started'                | 状态                   |
| rating                             | DECIMAL(3,1)                                           | DEFAULT NULL                         | 用户评分（1–10）       |
| platform                           | VARCHAR(64)                                            | DEFAULT NULL                         | 来源平台               |
| start_date                         | DATE                                                   | DEFAULT NULL                         | 开始时间               |
| finish_date                        | DATE                                                   | DEFAULT NULL                         | 完成时间               |
| cover_url                          | VARCHAR(255)                                           | DEFAULT NULL                         | 封面链接               |
| notes                              | TEXT                                                   | DEFAULT NULL                         | 备注 / 简评            |
| source                             | VARCHAR(16)                                            | DEFAULT 'manual'                     | 数据来源（manual/api） |
| created_at/ updated_at/ deleted_at | DATETIME                                               | (ON UPDATE)DEFAULT CURRENT_TIMESTAMP | 创建更新删除时间       |

**索引：**

- `idx_media_user_type`
- `idx_media_status`
- `idx_media_rating`

**约束：**

- `FK_media_user` 外键：user_id → users.id（ON DELETE CASCADE）

#### progress_anime 表 — 动画进度表（1:1 关联）

| 字段名          | 类型   | 约束                     | 说明                            |
| --------------- | ------ | ------------------------ | ------------------------------- |
| media_id        | BIGINT | PK, FK → media_items(id) | 条目主键（与 media_items 关联） |
| current_episode | INT    | DEFAULT 0                | 当前集数                        |
| total_episodes  | INT    | DEFAULT NULL             | 总集数（未知时为 NULL）         |

**外键约束：**

- `FK_progress_anime_media` 外键：media_id → media_items.id（ON DELETE CASCADE）

#### progress_comic 表 — 漫画进度表（1:1 关联）

| 字段名          | 类型   | 约束                     | 说明                            |
| --------------- | ------ | ------------------------ | ------------------------------- |
| media_id        | BIGINT | PK, FK → media_items(id) | 条目主键（与 media_items 关联） |
| current_chapter | INT    | DEFAULT 0                | 当前章节                        |
| total_chapters  | INT    | DEFAULT NULL             | 总章节数                        |
| current_volume  | INT    | DEFAULT NULL             | 当前卷数（可选）                |

**外键约束：**

- `FK_progress_comic_media` 外键：media_id → media_items.id（ON DELETE CASCADE）

#### progress_game 表 — 游戏进度表（1:1 关联）

| 字段名             | 类型          | 约束                     | 说明                            |
| ------------------ | ------------- | ------------------------ | ------------------------------- |
| media_id           | BIGINT        | PK, FK → media_items(id) | 条目主键（与 media_items 关联） |
| play_hours         | DECIMAL(7, 1) | DEFAULT 0.0              | 游玩时长（例如 40.5 小时）      |
| completion_percent | INT           | DEFAULT 0                | 完成度（0-100）                 |
| custom_status      | VARCHAR(100)  | DEFAULT NULL             | 自定义进度（如 "白金中"）       |

**外键约束：**

- `FK_progress_game_media` 外键：media_id → media_items.id（ON DELETE CASCADE）

#### progress_music 表 — 音乐进度表（1:1 关联）

| 字段名        | 类型                                    | 约束                     | 说明                            |
| ------------- | --------------------------------------- | ------------------------ | ------------------------------- |
| media_id      | BIGINT                                  | PK, FK → media_items(id) | 条目主键（与 media_items 关联） |
| listen_status | ENUM('listened', 'listened', 'partial') | DEFAULT 'unheard'        | 播放状态                        |
| play_count    | INT                                     | DEFAULT 0                | 播放次数                        |

**外键约束：**

- `FK_progress_music_media` 外键：media_id → media_items.id（ON DELETE CASCADE）

### ③ tags 表 — 标签定义表

| 字段名    | 类型        | 约束                      | 说明         |
| --------- | ----------- | ------------------------- | ------------ |
| id        | BIGINT      | PK, AUTO_INCREMENT        | 标签主键     |
| user_id   | BIGINT      | FK → users(id)            | 所属用户     |
| name      | VARCHAR(50) | NOT NULL                  | 标签名称     |
| color     | VARCHAR(16) | DEFAULT '#999999'         | 标签显示颜色 |
| create_at | DATETIME    | DEFAULT CURRENT_TIMESTAMP | 创建时间     |

**索引：**

- `idx_tag_user_name`（user_id, name）

### ④ media_tag_rel 表 — 内容与标签的多对多关系表

| 字段名      | 类型     | 约束                      | 说明     |
| ----------- | -------- | ------------------------- | -------- |
| media_id    | BIGINT   | FK → media_items(id)      | 内容 ID  |
| tag_id      | BIGINT   | FK → tags(id)             | 标签 ID  |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

**复合主键：** `(media_id, tag_id)`
**外键约束：**

- `FK_media_tag_media`：ON DELETE CASCADE
- `FK_media_tag_tag`：ON DELETE CASCADE

### ⑤ reviews 表 — 内容评论 / 简评表（扩展）

| 字段名      | 类型         | 约束                        | 说明                     |
| ----------- | ------------ | --------------------------- | ------------------------ |
| id          | BIGINT       | PK, AUTO_INCREMENT          | 评论主键                 |
| user_id     | BIGINT       | FK → users(id)              | 评论用户                 |
| media_id    | BIGINT       | FK → media_items(id)        | 评论条目                 |
| rating      | DECIMAL(3,1) | DEFAULT NULL                | 二次评分（与主评分分离） |
| content     | TEXT         | NOT NULL                    | 评论内容                 |
| create_time | DATETIME     | DEFAULT CURRENT_TIMESTAMP   | 创建时间                 |
| update_time | DATETIME     | ON UPDATE CURRENT_TIMESTAMP | 更新时间                 |

**索引：**

- `idx_review_media`
- `idx_review_user_media`

### ⑥ favorites 表 — 用户收藏表（选做）

| 字段名      | 类型         | 约束                      | 说明     |
| ----------- | ------------ | ------------------------- | -------- |
| id          | BIGINT       | PK, AUTO_INCREMENT        | 收藏主键 |
| user_id     | BIGINT       | FK → users(id)            | 用户 ID  |
| media_id    | BIGINT       | FK → media_items(id)      | 收藏条目 |
| remark      | VARCHAR(255) | DEFAULT NULL              | 收藏备注 |
| create_time | DATETIME     | DEFAULT CURRENT_TIMESTAMP | 收藏时间 |

**复合唯一约束：** `(user_id, media_id)`

### ⑦ media_api_info 表 — 外部API信息表（缓存用）

| 字段名     | 类型        | 约束                      | 说明                       |
| ---------- | ----------- | ------------------------- | -------------------------- |
| id         | BIGINT      | PK, AUTO_INCREMENT        | 主键                       |
| media_id   | BIGINT      | FK → media_items(id)      | 对应本地内容               |
| api_source | VARCHAR(50) | NOT NULL                  | API来源（Bangumi、RAWG等） |
| api_id     | VARCHAR(50) | DEFAULT NULL              | 对应API的唯一ID            |
| api_json   | JSON        | DEFAULT NULL              | 原始API响应数据（缓存）    |
| last_sync  | DATETIME    | DEFAULT CURRENT_TIMESTAMP | 最近同步时间               |

## 表间关系总结

| 关系类型 | 实体关系                             | 描述                 |
| -------- | ------------------------------------ | -------------------- |
| 一对多   | users → media_items                  | 一个用户拥有多个条目 |
| 一对多   | users → tags                         | 一个用户定义多个标签 |
| 多对多   | media_items ↔ tags                   | 条目可有多个标签     |
| 一对多   | media_items → reviews                | 条目可有多个评论     |
| 多对多   | users ↔ media_items（via favorites） | 用户可收藏多个条目   |
| 一对一   | media_items → progress_anime         | 动画进度（1:1 关联） |
| 一对一   | media_items → progress_comic         | 漫画进度（1:1 关联） |
| 一对一   | media_items → progress_game          | 游戏进度（1:1 关联） |
| 一对一   | media_items → progress_music         | 音乐进度（1:1 关联） |

### 数据示例（简要）

**users** 表

| id   | username | email                                   | preference                              |
| ---- | -------- | --------------------------------------- | --------------------------------------- |
| 1    | Alice    | [alice@demo.com](mailto:alice@demo.com) | {"theme":"dark","default_type":"anime"} |

**media_items** 表

| id   | user_id | title                    | type  | status      | rating |
| ---- | ------- | ------------------------ | ----- | ----------- | ------ |
| 1    | 1       | 《进击的巨人》           | anime | completed   | 9.5    |
| 2    | 1       | 《塞尔达传说：旷野之息》 | game  | in_progress | 9.0    |

**progress_anime** 表

| media_id | current_episode | total_episodes |
| -------- | --------------- | -------------- |
| 1        | 87              | 87             |

**tags** 表

| id   | user_id | name | color                                          |
| ---- | ------- | ---- | ---------------------------------------------- |
| 1    | 1       | 热血 | [#FF5555](app://obsidian.md/index.html#FF5555) |
| 2    | 1       | 冒险 | [#66CCFF](app://obsidian.md/index.html#66CCFF) |

**media_tag_rel** 表

| media_id | tag_id |
| -------- | ------ |
| 1        | 1      |
| 1        | 2      |

## 接口概要设计（RESTful）

| 功能         | Method          | Endpoint            | 说明             |
| ------------ | --------------- | ------------------- | ---------------- |
| 注册         | POST            | /api/users/register | 创建用户         |
| 登录         | POST            | /api/users/login    | 获取 JWT         |
| 获取用户信息 | GET             | /api/users/me       | 返回个人资料     |
| 添加内容     | POST            | /api/media          | 创建内容条目     |
| 获取内容     | GET             | /api/media          | 支持分页与筛选   |
| 修改内容     | PUT             | /api/media/{id}     | 更新条目信息     |
| 删除内容     | DELETE          | /api/media/{id}     | 逻辑删除         |
| 获取统计数据 | GET             | /api/stats          | 返回数据分析结果 |
| 标签管理     | GET/POST/DELETE | /api/tags           | 标签增删查       |

## 核心 API 详细设计（内容管理）

### 接口 1: 创建内容条目（添加新记录）

该接口用于用户向系统中添加一个新的 ACGM 记录。

| **功能**     | **Method** | **Endpoint** | **说明**                                           | **数据库操作**                                               |
| ------------ | ---------- | ------------ | -------------------------------------------------- | ------------------------------------------------------------ |
| **创建内容** | **POST**   | `/api/media` | 创建一个新内容条目，并同时创建对应的进度子表记录。 | **事务性操作**：`media_items` (INSERT) + `progress_*` (INSERT) |

#### 请求体 (Request Body) DTO 结构

由于进度信息是动态的（取决于 `type`），我们使用一个通用的嵌套对象 `progress_info` 来传递数据。

| **字段名**          | **类型**     | **约束** | **示例值**            | **说明**                                   |
| ------------------- | ------------ | -------- | --------------------- | ------------------------------------------ |
| `title`             | `String`     | 必需     | `"赛博朋克 2077"`     | 内容标题                                   |
| `type`              | `String`     | 必需     | `"game"`              | 内容类型（`anime`/`comic`/`game`/`music`） |
| `status`            | `String`     | 必需     | `"in_progress"`       | 当前状态                                   |
| `rating`            | `Number`     | 可选     | `8.5`                 | 用户评分                                   |
| `platform`          | `String`     | 可选     | `"Steam"`             | 来源平台                                   |
| `notes`             | `String`     | 可选     | `"夜之城太迷人了..."` | 备注 / 简评                                |
| `cover_url`         | `String`     | 可选     | `http://.../img.jpg`  | 封面链接                                   |
| **`progress_info`** | **`Object`** | **必需** | *见下方*              | **动态进度数据（核心）**                   |

**`progress_info` 动态结构示例：**

| **类型 (type)** | **progress_info 结构示例 (Game)**                            | **progress_info 结构示例 (Anime)**                           |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **`game`**      | `{ "play_hours": 45.3, "completion_percent": 60, "custom_status": "主线已通" }` |                                                              |
| **`anime`**     |                                                              | `{ "current_episode": 10, "total_episodes": 12 }`            |
| **`comic`**     |                                                              | `{ "current_chapter": 200, "total_chapters": 250, "current_volume": 18 }` |
| **`music`**     |                                                              | `{ "listen_status": "Listened", "play_count": 5 }`           |

#### 响应体 (Response Body) DTO 结构

成功创建后，返回包含新生成的 `id` 在内的完整条目数据。



``` JSON
{
  "code": 201, // HTTP 201 Created
  "message": "内容条目创建成功",
  "data": {
    "id": 1001,
    "title": "赛博朋克 2077",
    "type": "game",
    "status": "in_progress",
    "rating": 8.5,
    // ... 其它通用字段
    "progress_info": { // 包含创建成功的进度数据
      "play_hours": 45.3,
      "completion_percent": 60
    },
    "create_time": "2025-11-05T09:30:00Z"
  }
}
```

### 6.2. 接口 2: 获取内容列表（分页、筛选与排序）

该接口是用户查看自己收藏列表的核心接口，复杂度最高。

| **功能**     | **Method** | **Endpoint** | **说明**                               | **数据库操作**                                               |
| ------------ | ---------- | ------------ | -------------------------------------- | ------------------------------------------------------------ |
| **获取列表** | **GET**    | `/api/media` | 获取用户的内容列表，支持多种查询参数。 | `media_items` (SELECT) + **动态 JOIN** `progress_*` (SELECT) |

#### 请求参数 (Request Query Parameters)

| **参数名**    | **类型**  | **约束** | **示例值**         | **备注**                         |
| ------------- | --------- | -------- | ------------------ | -------------------------------- |
| `type`        | `String`  | 可选     | `anime`            | 内容类型筛选                     |
| `status`      | `String`  | 可选     | `completed`        | 状态筛选                         |
| `tags`        | `String`  | 可选     | `冒险,热血`        | 标签筛选（逗号分隔）             |
| `rating_min`  | `Number`  | 可选     | `8.0`              | 最小评分                         |
| **`sort_by`** | `String`  | 可选     | `finish_date,desc` | **排序字段和顺序** (`字段,方向`) |
| `keyword`     | `String`  | 可选     | `EVA`              | 标题/备注模糊搜索                |
| `page`        | `Integer` | 必需     | `1`                | 当前页码 (默认 1)                |
| `size`        | `Integer` | 必需     | `20`               | 每页数量 (默认 20)               |

#### 响应体 (Response Body) DTO 结构

返回一个标准的分页结构，列表中的每个条目都包含了其动态进度信息。


``` JSON
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total_count": 123, // 总条目数
    "total_pages": 7,   // 总页数
    "current_page": 1,
    "items": [
      {
        "id": 1001,
        "title": "赛博朋克 2077",
        "type": "game",
        "status": "in_progress",
        "rating": 8.5,
        "tags": ["赛博朋克", "开放世界"], // 额外 JOIN 出来的标签列表
        "progress_info": { // 动态加载的游戏进度
          "play_hours": 45.3,
          "completion_percent": 60
        }
      },
      {
        "id": 1002,
        "title": "进击的巨人",
        "type": "anime",
        "status": "completed",
        "rating": 9.5,
        "tags": ["热血", "致郁"],
        "progress_info": { // 动态加载的动画进度
          "current_episode": 87,
          "total_episodes": 87
        }
      }
      // ... 更多条目
    ]
  }
}
```
