FROM gradle:8.10-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew serverJar

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/server-1.0.jar ./server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]
