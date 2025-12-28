#  TeamSync (不好用项目协同)

> 基于 Spring Boot 3 + Vue 3 的项目协同平台。


---

##  技术栈 (Tech Stack)

本项目采用前后端分离架构，前端页面打包在后端静态资源中，实现**开箱即用**的单体部署体验。

### 后端 (Backend)
- **核心框架**: Spring Boot 3.5.9
- **ORM 框架**: MyBatis Plus
- **权限认证**: JWT + Redis (支持单点登录、强制下线)
- **实时通信**: WebSocket (全双工看板同步)
- **消息队列**: RabbitMQ (异步邀请通知)
- **对象存储**: MinIO (头像、附件存储)
- **接口文档**: Knife4j (Swagger 3)

### 前端 (Frontend)
- **核心框架**: Vue 3 (Composition API)
- **UI 组件库**: Element Plus
- **架构**: 单页面应用 (SPA)，文件位于 `src/main/resources/static/index.html`

### 基础设施 (Infrastructure)
- **Docker Compose**: 一键编排所有服务（Java App + MySQL + Redis + RabbitMQ + MinIO）

---

##  核心功能 (Features)

### 1. 用户中心
- **安全认证**：JWT 无状态认证 + Redis 有状态管理。
- **身份补全**：登录后自动同步头像与 ID，解决前端状态丢失问题。
- **头像上传**：集成 MinIO，支持上传新头像自动清理旧文件。

### 2. 项目管理 (核心)
- **私有/公开模式**：私有项目仅成员可见，公开项目全服可见。
- **项目大厅**：支持关键词搜索，查看全服公开项目。
- **点赞排行榜**：基于 Redis  实现点赞功能。
- **成员协作**：WebSocket实现实时协作，支持多人同时操作同一个看板。
- **邀请成员**：RabbitMQ 异步发送邀请通知。
- **处理邀请**：被邀请人可在通知中心选择“接受”或“拒绝”。
- **权限控制**：RBAC 逻辑，只有 Owner 能删除项目、邀请成员。

### 3. 任务看板 (Kanban)
- **可视化流转**：待办 -> 进行中 -> 已完成，点击箭头一键流转。
- **WebSocket 同步**：多人同时操作同一个看板，状态实时自动刷新。
- **任务指派**：Owner 指派特定成员执行任务。
- **附件管理**：上传任意格式附件，点击即下载。
- **评论互动**：任务详情页实时评论，支持删除自己的评论。

---

##  极速启动 (Quick Start)

### 1. 环境准备
确保本地已安装 **Docker Desktop**。

### 2. 一键启动
在项目根目录运行以下命令：

```bash
# 编译镜像并启动所有服务（包括 Java 后端）
docker-compose up -d --build
```

### 3. 访问体验
启动成功后，直接访问浏览器：

*   **前端页面**: http://localhost:8080/
*   **接口文档**: http://localhost:8080/doc.html
*   **RabbitMQ 控制台**: http://localhost:15672 (账号: guest / guest)
*   **MinIO 控制台**: http://localhost:9001 (账号: minioadmin / minioadmin)

---

##  开发者指南 (Development)

如果你想修改代码并在本地调试（而不使用 Docker 里的 Java App）：

1.  修改 `docker-compose.yml`，注释掉 `app` 服务（防止端口冲突）。
2.  运行 `docker-compose up -d` 启动中间件。
3.  在本地 IDE 中运行 `DemoApplication.java`。
4.  注意：本地运行时，`application.properties` 中的 Redis/MySQL 地址需配置为 `localhost`。

---

##  目录结构

```text
com.mule.demo
├── common          # 通用工具 (Result, JwtUtils, UserContext)
├── config          # 配置类 (Web, Redis, RabbitMQ, MinIO, WebSocket)
├── controller      # 控制层 (API 接口)
├── entity          # 数据库实体
├── exception       # 全局异常处理
├── mapper          # DAO 层
├── model           # 数据模型
│   ├── dto         # 数据传输对象 (接收前端参数)
│   └── vo          # 视图对象 (返回前端数据)
├── mq              # 消息队列消费者
├── service         # 业务逻辑层
└── websocket       # WebSocket 服务端
```