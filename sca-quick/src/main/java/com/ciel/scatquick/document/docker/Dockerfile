FROM centos
MAINTAINER xiapeixin

# 新建目录
RUN mkdir /usr/local/java

# 将jdk文件拷贝到容器/usr/local/java/并解压
ADD Alibaba_Dragonwell_8.4.4-GA_Linux_x64.tar.gz /usr/local/java/

# 软连接
RUN ln -s /usr/local/java/jdk1.8.0_181 /usr/local/java/jdk

# 设置环境变量
ENV JAVA_HOME /usr/local/java/jdk
ENV CLASSPATH .:${JAVA_HOME}/lib
ENV PATH ${JAVA_HOME}/bin:$PATH

docker run -d --name=lhzsb2b-registry -p 192.168.110.151:8761:8761 -v /b2b/timezone:/etc/timezone -v /data/simsun.ttc:/usr/share/fonts/truetype/dejavu/simsun.ttc -v /b2b/lhzsb2b-registry-exec.jar:/deply/lhzsb2b-registry-exec.jar java:8 java -jar /deply/lhzsb2b-registry-exec.jar
