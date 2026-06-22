FROM tomcat:9.0-jdk17-temurin

WORKDIR /tmp/build

COPY src/ ./src/

RUN rm -rf /usr/local/tomcat/webapps/ROOT \
    && mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes /usr/local/tomcat/webapps/ROOT/uploads \
    && javac -cp /usr/local/tomcat/lib/servlet-api.jar -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes src/main/java/lab/UploadServlet.java \
    && chmod -R 777 /usr/local/tomcat/webapps/ROOT/uploads

EXPOSE 8080

