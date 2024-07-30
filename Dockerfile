FROM gradle:8.9-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/build/libs/RepositoryAPI-1.0-SNAPSHOT.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]
