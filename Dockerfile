# --- 第一阶段：构建阶段 (Build Stage) ---
# 使用 JDK 镜像进行编译
FROM eclipse-temurin:21-jdk-alpine AS builder

# 设置工作目录
WORKDIR /build

# 1. 复制 Maven 核心文件（利用 Docker 缓存加速）
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# 2. 赋予 mvnw 执行权限并下载依赖（如果依赖没变，这一步会跳过）
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# 3. 复制所有源代码
COPY src ./src

# 4. 执行打包命令，跳过测试
RUN ./mvnw clean package -DskipTests

# --- 第二阶段：运行阶段 (Run Stage) ---
# 使用轻量级的 JRE 镜像运行程序
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从构建阶段复制生成的 JAR 包
# 注意：这里根据 pom.xml 里的 artifactId 匹配，通常是 target/*.jar
COPY --from=builder /build/target/*.jar app.jar

# 设置时区为亚洲/上海
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 暴露端口
EXPOSE 8080

# 启动程序
ENTRYPOINT ["java", "-jar", "app.jar"]