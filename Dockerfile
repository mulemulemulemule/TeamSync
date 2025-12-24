# 使用轻量级的 Java 21 运行时作为基础镜像
FROM eclipse-temurin:21-jre-alpine

# 设置容器内的工作目录
WORKDIR /app

# 将本地构建好的 JAR 包复制到容器中
COPY target/*.jar app.jar

# 设置时区为亚洲/上海
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 暴露 8080 端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
