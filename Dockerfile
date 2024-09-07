# Packing the application
FROM openjdk:21 as builder

WORKDIR /usr/src/app

COPY . .

RUN microdnf install -y findutils
RUN ./gradlew :backend:assemble --stacktrace --info --no-daemon

# Running the application in OpenJDK container
FROM openjdk:21

ARG IS_CLOUD=true
ARG BASE_URL_CONFERENCE_HALL=conference-hall.io
ARG PROJECT_ID

WORKDIR /usr/src/app
COPY --from=builder /usr/src/app/backend/build/libs ./

ENV PROJECT_ID=$PROJECT_ID
ENV IS_CLOUD=$IS_CLOUD
ENV BASE_URL_CONFERENCE_HALL=$BASE_URL_CONFERENCE_HALL

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./backend-all.jar"]
