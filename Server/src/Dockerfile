# 基于 JDK8
FROM openjdk:8
# 作者
MAINTAINER BaiZe
# 时区设置
ENV TZ=Asia/Shanghai
# 添加jar包到远程容器中
ADD ./Server-0.1.jar /www/Server.jar
# 暴露接口
EXPOSE 80
# 切换工作路径
WORKDIR /www/
# 启动
CMD ["java", "-jar", "Server.jar"]