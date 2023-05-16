FROM openjdk:11
VOLUME /tmp
EXPOSE 19000
ARG JAR_FILE=target/car-workflow-cloud-app-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]