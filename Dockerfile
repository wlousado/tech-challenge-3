FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY techchallenge/pom.xml .

COPY techchallenge/core ./core
COPY techchallenge/notification-ms ./notification-ms
COPY techchallenge/scheduling-ms ./scheduling-ms

RUN mvn dependency:go-offline -B

RUN mvn clean package

FROM eclipse-temurin:21-alpine-3.22 AS scheduling-ms

WORKDIR /app

COPY --from=build /app/scheduling-ms/target/scheduling-ms*.jar app.jar

RUN adduser -D app_user \
    && chown -R app_user:app_user /app \
    && chmod 500 app.jar

USER app_user

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-alpine-3.22 AS notification-ms

WORKDIR /app

COPY --from=build /app/notification-ms/target/notification-ms*.jar app.jar

RUN adduser -D app_user \
    && chown -R app_user:app_user /app \
    && chmod 500 app.jar

USER app_user

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]