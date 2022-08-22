FROM openjdk:11
ARG IS_CLOUD=true
ARG BASE_URL_CONFERENCE_HALL=conference-hall.io
ARG PROJECT_ID

WORKDIR /usr/src/app

COPY . .

ENV PROJECT_ID=$PROJECT_ID
ENV IS_CLOUD=$IS_CLOUD
ENV GOOGLE_APPLICATION_CREDENTIALS=key.json

RUN ./gradlew :backend:assemble --stacktrace --info --no-daemon

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "backend/build/libs/backend-all.jar"]
