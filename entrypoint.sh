#!/bin/bash
set -e

# If GOOGLE_APPLICATION_CREDENTIALS_JSON is set, create the credentials file
if [ ! -z "$GOOGLE_APPLICATION_CREDENTIALS_JSON" ]; then
    echo "$GOOGLE_APPLICATION_CREDENTIALS_JSON" > /app/config/gcp-service-account.json
    export GOOGLE_APPLICATION_CREDENTIALS=/app/config/gcp-service-account.json
fi

# Start the application
exec java -jar /app/application.jar
