# Conferences4Hall

Import Conference Hall event data to create an agenda and show it 
in an Android and iOS application with KMM technology and
multiplatform modules to share models between mobile apps and the
back-end.

## Running in local

Start firebase emulators inside a terminal with Firestore service.

```bash
firebase login # If you are not yet logged
firebase emulators:start --project $RANDOM_FIREBASE_PROJECT_ID
```

Start appengine server inside another terminal to interact with the
local instance of your Firebase.

```bash
# Export credential of a random GCP account.
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/key.json
./gradlew :backend:installDist && ./backend/build/install/backend/bin/backend
```

Now, you can start to interact with the backend.

## Deploy in GCP

```bash
# If you are not yet logged
gcloud auth login
gcloud config set project $PROJECT_ID
# Deploy in production
./gradlew :backend:appengineDeploy
```
