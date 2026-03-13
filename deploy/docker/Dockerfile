# ==============================================================================
# Stage 1: Build the application with Gradle
# ==============================================================================
FROM eclipse-temurin:8-jdk AS builder

WORKDIR /workspace

# Copy build configuration files first for Docker layer caching.
# Changes to source code won't invalidate the dependency download layer.
COPY gradlew settings.gradle build.gradle script-plugin.gradle gradle.properties ./
COPY gradle/ gradle/

# Copy each submodule's build.gradle individually for caching
COPY biz-service-impl/build.gradle biz-service-impl/build.gradle
COPY biz-shared/build.gradle biz-shared/build.gradle
COPY common-dal/build.gradle common-dal/build.gradle
COPY common-service-facade/build.gradle common-service-facade/build.gradle
COPY common-service-integration/build.gradle common-service-integration/build.gradle
COPY common-util/build.gradle common-util/build.gradle
COPY core-model/build.gradle core-model/build.gradle
COPY core-service/build.gradle core-service/build.gradle

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# Copy full source code
COPY . .

# Build the bootJar (skip tests — they require Testcontainers/Docker-in-Docker)
RUN chmod +x gradlew && ./gradlew :biz-service-impl:bootJar -x test --no-daemon

# ==============================================================================
# Stage 2: Lightweight runtime image
# ==============================================================================
FROM eclipse-temurin:8-jre

RUN groupadd --system app && useradd --system --gid app app

WORKDIR /app

COPY --from=builder /workspace/biz-service-impl/build/libs/biz-service-impl*.jar /app/app.jar

RUN chown -R app:app /app

USER app

EXPOSE 8502

# JAVA_OPTS can be injected via K8s ConfigMap environment variable
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS:-} -jar /app/app.jar"]
