FROM openjdk:21

WORKDIR /app

COPY build/libs/RepositoryAPI-1.0-SNAPSHOT.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]