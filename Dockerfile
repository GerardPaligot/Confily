# Packing the application
FROM gradle:8.14-jdk21 AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle :backend:build -i --stacktrace

FROM gradle:8.13-jdk21 AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :backend:shadowJar --no-daemon

FROM amazoncorretto:21 AS runtime
EXPOSE 8080
RUN mkdir /app

# Create directory for Google Cloud credentials
RUN mkdir -p /app/config

COPY --from=build /home/gradle/src/backend/build/libs/*-all.jar /app/application.jar

# Copy and setup entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]