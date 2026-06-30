# ==========================================
# STEP 1: Build the Application (Build Stage)
# ==========================================
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

# Copy the Maven wrapper files first (for caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Give execution permissions to the wrapper script
RUN chmod +x mvnw

# Copy your source code
COPY src ./src

# Build using the wrapper instead of the global 'mvn' command
RUN ./mvnw clean package -DskipTests

# ==========================================
# STEP 2: Run the Application (Run Stage)
# ==========================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the compiled JAR from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]