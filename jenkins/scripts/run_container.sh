#!/usr/bin/env bash

echo "=== 构建信息 ==="
echo "目标环境: $DATASOURCE_URL"
echo "数据库user: $DATASOURCE_USERNAME"
echo "github id: $GITHUB_CLIENT_ID"
# 启用严格模式：遇到错误立即退出，未定义变量报错，管道失败也报错
set -euo pipefail


# ============ 步骤 2：从 build.gradle 提取项目名称和版本 ============
echo "正在从 build.gradle 中提取项目信息..."

# 使用 Gradle 的 help 任务输出项目属性，并通过 grep 提取 name 和 version
# 注意：这依赖于在 build.gradle 中定义了 project.name 和 version 属性

# 提取项目名称（如果没有设置，默认使用目录名）
NAME=$(./gradlew properties --no-daemon | grep "^name:" | awk '{$1=""; gsub(/^ */, ""); print}' | head -n1)
if [ -z "$NAME" ]; then
  echo "错误：无法获取项目名称，请检查 build.gradle 是否定义了 name 属性。"
  exit 1
fi

# 提取版本号
VERSION=$(./gradlew properties --no-daemon | grep "^version:" | awk '{$1=""; gsub(/^ */, ""); print}' | head -n1)
if [ -z "$VERSION" ]; then
  VERSION="unspecified"  # 如果没定义版本，默认设为 unspecified
  echo "警告：未检测到版本号，使用默认值 'unspecified'"
fi

echo "项目名称: $NAME"
echo "项目版本: $VERSION"


# ============ 步骤 3：确定 JAR 文件路径 ============
# Gradle 默认将 JAR 打包为 build/libs/<name>-<version>.jar
JAR_FILE="build/libs/${NAME}-${VERSION}.jar"

# 检查 JAR 文件是否存在
if [ ! -f "$JAR_FILE" ]; then
  echo "错误：找不到 JAR 文件: $JAR_FILE"
  echo "请确认构建是否成功，或检查 build.gradle 中的 jar 任务配置。"
  exit 1
fi

echo "找到 JAR 文件: $JAR_FILE"


# ============ 步骤 4：创建临时目录用于 Docker 构建上下文 ============
BUILD_CONTEXT="./docker-build-context"
mkdir -p $BUILD_CONTEXT
cp "$JAR_FILE" "$BUILD_CONTEXT/app.jar"  # 复制 JAR 并重命名为 app.jar，避免名字太长

# 创建 Dockerfile
cat > $BUILD_CONTEXT/Dockerfile << EOF
# 使用官方 OpenJDK 运行时作为基础镜像
FROM openjdk:21

ENV DATASOURCE_URL=$DATASOURCE_URL
ENV DATASOURCE_USR=$DATASOURCE_USR
ENV DATASOURCE_PSW=$DATASOURCE_PSW
ENV GITHUB_CLIENT_ID=$GITHUB_CLIENT_ID
ENV GITHUB_CLIENT_SECRET=$GITHUB_CLIENT_SECRET
ENV FILE_PATH=$FILE_PATH
# 设置工作目录
WORKDIR /app

# 将本地的 JAR 文件复制到容器中
COPY app.jar app.jar

# 暴露应用运行的端口（例如 8101）
EXPOSE 8101

# 容器启动时运行 JAR 文件
CMD ["java", "-jar", "app.jar"]
EOF

echo "Docker 上下文已准备完毕。"


# ============ 步骤 5：构建 Docker 镜像 ============
IMAGE_NAME="${NAME,,}"  # 转换为小写（Docker 镜像名必须小写）
IMAGE_TAG="$VERSION"

echo "正在构建 Docker 镜像: $IMAGE_NAME:$IMAGE_TAG"
docker build -t "$IMAGE_NAME:$IMAGE_TAG" $BUILD_CONTEXT
docker save -o "${IMAGE_NAME}.tar" "$IMAGE_NAME:$IMAGE_TAG"

# ============ 步骤 6：运行 Docker 容器 ============
echo "正在启动容器..."
# 如果已有同名容器运行，先停止并删除
docker rm -f "$IMAGE_NAME" || true

# 启动新容器，映射端口 8101，并以后台模式运行
docker run -d --name "$IMAGE_NAME" -p 8101:8101 -v "$FILE_PATH":"$FILE_PATH"  --restart unless-stopped "$IMAGE_NAME:$IMAGE_TAG"

echo "容器已启动：镜像=$IMAGE_NAME:$IMAGE_TAG，容器名=$IMAGE_NAME，端口映射 8101:8101"

# 输出运行中的容器状态
docker ps --filter "name=$IMAGE_NAME" --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"

# 清理临时文件（可选）
# rm -rf $BUILD_CONTEXT

echo "部署完成！"