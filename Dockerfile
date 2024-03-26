FROM eclipse-temurin:21
LABEL authors="hroberts"

RUN mkdir /opt/fileshare
RUN mkdir /files
COPY target/*.jar /opt/fileshare/app.jar

ENTRYPOINT ["java", "-jar", "/opt/fileshare/app.jar"]