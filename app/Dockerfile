FROM eclipse-temurin:17-jre

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV SERVER_PORT=8081
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/app.jar"]
