#  TeamSync  (不好用项目协同)

> 基于 Spring Boot 3 + Vue 3 的项目协同平台。
>。

---

##  技术栈 (Tech Stack)

本项目采用前后端分离架构，后端遵循 RESTful 规范，利用 Docker 容器化部署中间件。

### 后端 (Backend)
- **核心框架**: Spring Boot 3.5.9
- **ORM 框架**: MyBatis Plus
- **权限认证**: JWT + Redis (单点登录、强制下线)
- **实时通信**: WebSocket (全双工看板同步)
- **消息队列**: RabbitMQ (异步邀请通知)
- **对象存储**: MinIO (头像、附件存储)
- **工具库**: Hutool, Lombok
- **接口文档**: Knife4j (Swagger 3)

### 前端 (Frontend)
- **核心框架**: Vue 3 (Composition API)
- **UI 组件库**: Element Plus
- **网络请求**: Axios (自动携带 Token)
- **架构**: 单页面应用 (SPA)

### 中间件 (Middleware / Docker)
- **MySQL 8.0**: 业务数据存储
- **Redis**: 缓存、Token 黑名单、点赞
- **RabbitMQ**: 异步解耦
- **MinIO**: 自建对象存储服务

---

##  核心功能 (Features)

### 1. 用户中心
-  **注册/登录**：JWT 无状态认证 + Redis 有状态管理。
-  **身份补全**：登录后自动同步头像与 ID。
-  **头像上传**：集成 MinIO，支持上传新头像自动清理旧文件。

### 2. 项目管理 (核心)
-  **私有/公开模式**：私有项目仅成员可见，公开项目全服可见。
-  **项目大厅**：支持关键词搜索，查看全服公开项目。
-  **点赞**：基于 Redis ZSet 实现的点赞功能。
-  **成员协作**：
- **邀请成员**：RabbitMQ 异步发送邀请通知。
- **处理邀请**：被邀请人可在通知中心选择“接受”或“拒绝”。
- **权限控制**：只有 Owner 能删除项目、邀请成员。

### 3. 任务看板 (看板)
-  **可视化流转**：待办 -> 进行中 -> 已完成，点击箭头一键流转。
-  **WebSocket 同步**：多人同时操作同一个看板，状态实时自动刷新。
-  **任务指派**：Owner 指派特定成员执行任务。
-  **附件管理**：上传任意格式附件，点击下载。
-  **评论互动**：任务详情页实时评论与删除。

---

##  快速开始 (Quick Start)

### 1. 环境准备
确保本地已安装 **Docker Desktop** 和 **JDK 21**。

启动中间件：
```bash
docker-compose up -d
```


### 2. 后端配置
检查 `src/main/resources/application.properties`，数据库连接指向 Docker 映射的端口 (3307)。

### 3. 启动项目
在项目根目录运行：
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

### 4. 访问体验
*   **前端页面**: http://localhost:8080/
*   **接口文档**: http://localhost:8080/doc.html
*   **RabbitMQ 控制台**: http://localhost:15672 (guest/guest)
*   **MinIO 控制台**: http://localhost:9001 (minioadmin/minioadmin)

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
---




