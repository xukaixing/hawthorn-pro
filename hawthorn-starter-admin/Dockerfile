#指定JDK
FROM openjdk:8-jre-alpine

#作者信息
MAINTAINER xukaixing<andy.ten@tom.com>

#设置环境变量
#以下命令切换语言，解决docker容器中中文乱码
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

#构建镜像时执行的命令
RUN /bin/ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

#用于指定传递给构建运行时的变量
ARG JAR_FILE=hawthorn-starter-admin-1.1.2-SNAPSHOT.jar

RUN echo ${JAR_FILE}

#创建存放应用的目录
RUN mkdir -p /home/app/jars

#将本地文件添加到容器中，tar类型文件会自动解压(网络压缩资源不会被解压)，可以访问网络资源，类似wget
ADD /target/${JAR_FILE} /home/app/jars/admin.jar

#应用需要暴露的端口
EXPOSE 4701

#工作目录，类似于cd命令
WORKDIR /home/app/jars

#构建容器后调用，也就是在容器启动时才进行调用
#-Djava.security.egd=file:/dev/./urandom 可解决tomcat可能启动慢的问题
CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-jar" ,"admin.jar"]
