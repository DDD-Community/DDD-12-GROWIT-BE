FROM public.ecr.aws/awsguru/aws-lambda-adapter:0.9.1 AS lambda-adapter

FROM eclipse-temurin:17-jre

COPY --from=lambda-adapter /lambda-adapter /opt/extensions/lambda-adapter

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV SERVER_PORT=8081
ENV AWS_LWA_PORT=8081
ENV AWS_LWA_READINESS_CHECK_PATH=/actuator/health/liveness
ENV AWS_LWA_ASYNC_INIT=true
ENV SPRING_MAIN_LAZY_INITIALIZATION=true
ENV JAVA_TOOL_OPTIONS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/app.jar"]
