# Multi-stage build para Spring Boot
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

# Copiar todo o projeto (necessário para build multi-módulo)
WORKDIR /app
COPY pom.xml .
COPY core/ ./core/
COPY scheduling-ms/ ./scheduling-ms/
COPY notification-ms/ ./notification-ms/

# Construir apenas o módulo notification-ms (e suas dependências)
RUN mvn clean package -pl notification-ms -am -DskipTests

# Etapa final - runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar o JAR construído
COPY --from=builder /app/notification-ms/target/*.jar app.jar

# Expor a porta 8080
EXPOSE 8080

# Configurar Java options para containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
