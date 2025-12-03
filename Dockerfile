FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY ./techchallenge/ .

RUN mvn dependency:go-offline -B

RUN mvn clean package

FROM eclipse-temurin:21-alpine-3.22 AS app

ARG SERVICE

WORKDIR /app

COPY --from=build /app/${SERVICE}/target/${SERVICE}*.jar app.jar

RUN adduser -D app_user \
    && chown -R app_user:app_user /app \
    && chmod 500 app.jar

USER app_user

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]