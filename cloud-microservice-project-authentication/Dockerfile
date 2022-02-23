FROM maven:3.8.4-jdk-8 AS build
WORKDIR /app
COPY ./pom.xml /app/pom.xml
RUN mvn -e -B dependency:resolve
COPY ./src /app/src
RUN mvn -e -B package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /app/target/authmanager-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
CMD ["java","-Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]
