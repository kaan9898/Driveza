# ---------- BUILD STAGE ----------
FROM maven:3.9.12-eclipse-temurin-25 AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B -q dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -DskipTests clean package

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]