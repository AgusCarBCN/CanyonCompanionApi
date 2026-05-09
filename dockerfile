FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]