FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8082
ENV AI_OPENAI_MODEL=gpt-4o-mini
ENTRYPOINT ["java","-jar","/app/app.jar"]
