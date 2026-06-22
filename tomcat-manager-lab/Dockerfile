FROM tomcat:9.0-jdk17-temurin

COPY conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY ROOT/ /usr/local/tomcat/webapps/ROOT/

RUN cp -a /usr/local/tomcat/webapps.dist/manager /usr/local/tomcat/webapps/manager \
    && sed -i '/RemoteCIDRValve/{N;d;}' /usr/local/tomcat/webapps/manager/META-INF/context.xml

EXPOSE 8080
