FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Переменные окружения
ENV SPRING_PROFILES_ACTIVE=prod

# Команды для запуска
ENTRYPOINT ["java", "-jar", "app.jar"]
