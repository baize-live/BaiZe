# 基于 tomcat
FROM tomcat:9.0.29
# 作者
MAINTAINER MUTOU
# 定义变量
ENV DIR_WEBAPP /usr/local/tomcat/webapps/
# 切换工作路径
WORKDIR $DIR_WEBAPP
# 添加本地的 war 包到远程容器中
ADD ./ByZe.war ByZe.war
# 暴露接口
EXPOSE 8080
# 启动 Tomcat
CMD ["../bin/catalina.sh","run"]