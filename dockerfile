FROM gradle:9.2.1-jdk17 AS BUILD

WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/usuario.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/usuario.jar"]
