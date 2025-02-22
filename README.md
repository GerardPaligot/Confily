# Confily

Real life Kotlin Multiplatform project with an iOS application
developed in Swift with SwiftUI, an Android application developed
in Kotlin with Jetpack Compose and a backed in Kotlin hosted on
AppEngine.

## Backend Features

* Create the agenda of your conference, optionally from [Conference Hall](https://github.com/conference-hall/conference-hall)
* Create your feedback forms from [OpenFeedback.io](https://github.com/HugoGresse/open-feedback)
* Create your partners from [CMS4Conference](https://github.com/devlille/CMS4Conference)
* Import planning from [OpenPlanner](https://openplanner.fr/)
* Import a participant ticket from [BilletWeb](https://www.billetweb.fr/)
* Import partner jobs from [WeLoveDevs](https://welovedevs.com/)

## Mobile Features

* Select your conference
* Conference schedule
* Bookmarking of talks
* Speaker page with biography, socials and talks
* Networking space which privacy respect from mobile to mobile
* Partners with description, socials, jobs, location and short presentation video
* Event page with socials, location, menus, q&a and code of conduct
* Scan your event ticket
* Android wearable companion app
* Enjoy your conference!

## Build With

* [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern toolkit for building native UI
* [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) - Multiplatform approach based on Jetpack Compose
* [Accompanist](https://github.com/google/accompanist) - Collection of extension libraries for Jetpack Compose
* [Adaptive Layout](https://developer.android.com/develop/ui/compose/layouts/adaptive) - Create adaptive layouts for different screen sizes
* [ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html) - Stores UI-related data that isn't destroyed on UI changes
* [Navigation Compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html) - Allow users to navigate across, into, and back out from the different pieces of content within your app
* [Coil](https://github.com/coil-kt/coil) - Media management and image loading framework for Android
* [Swift](https://www.swift.org/) - First class and official programming language for iOS development
* [SwiftUI](https://developer.apple.com/xcode/swiftui/) - Build apps across all Apple platforms with Swift
* [Koin](https://insert-koin.io/) - Pragmatic Kotlin & Kotlin Multiplatform Dependency Injection framework
* [Kotlinx Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) - Light-weight threads
* [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin Multiplatform / multi format serialization
* [SQLDelight](https://github.com/cashapp/sqldelight) - Generates typesafe Kotlin APIs from SQL
* [Okio](https://github.com/square/okio) - A modern I/O library for Android, Java, and Kotlin Multiplatform
* [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - Kotlin Multiplatform library for saving simple key-value data
* [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) - Gradle plugin to generate build config Kotlin file.
* [ktor](https://github.com/ktorio/ktor) - Client to make HTTP request and HTTP server routing
* [Firestore](https://github.com/googleapis/java-firestore) - JVM client to make request on Firestore
* [Storage](https://github.com/googleapis/java-storage) - JVM client to make request on Storage
* [Drive](https://developers.google.com/drive/api/guides/about-sdk) - JVM client to handle documents in a Google Drive

## Testing

### Running in local

Start firebase emulators inside a terminal with Firestore service.

```bash
firebase login # If you are not yet logged
firebase emulators:start --project $RANDOM_FIREBASE_PROJECT_ID
```

Start appengine server inside another terminal to interact with the
local instance of your Firebase.

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/key.json
export PROJECT_ID=$RANDOM_FIREBASE_PROJECT_ID
export BASE_URL_CONFERENCE_HALL=conference-hall.io
./gradlew :backend:installDist && ./backend/build/install/backend/bin/backend
```

Now, you can start to interact with the backend.

### Deploy in GCP

#### Prerequisites

* Gcloud app created: `gcloud app create`
* Billing account enabled on your GCP project
* Cloud Build service enabled
* Cloud Firestore service enabled
* Secret Manager enabled
* Geocoding enabled
* Add secret `GEOCODE_API_KEY` in Secret Manager with geocoding api key
* Grant your GCP service account with these permissions:
  * Cloud Run Invoker
  * Secret Manager Secret Accessor
  * Storage Object Creator
  * Storage Object Viewer

#### Deploy with Cloud Run

```bash
export PROJECT_ID=<your-project-id>
# If you are not yet logged
gcloud auth login
gcloud config set project $PROJECT_ID
# Deploy
gcloud run deploy confily \
  --source . \
  --platform managed \
  --port 8080 \
  --region europe-west1 \
  --set-env-vars=PROJECT_ID=$PROJECT_ID \
  --allow-unauthenticated
```

## References

* [Building for multi-form factor devices in Android: The optimal architecture](https://medium.com/proandroiddev/building-for-multi-form-factor-devices-in-android-the-optimal-architecture-6311221463ab)
* [String resources API for Compose Multiplatform](https://medium.com/proandroiddev/string-resources-api-for-compose-multiplatform-9e0bf6618506)
* [Why is adaptive layout a nightmare?](https://speakerdeck.com/gerardpaligot/why-is-adaptive-layout-a-nightmare)
* [SwiftUI vs Jetpack Compose by an Android Engineer](https://proandroiddev.com/swiftui-vs-jetpack-compose-by-an-android-engineer-6b48415f36b3)
* [Devfest Lille Android](https://play.google.com/store/apps/details?id=org.gdglille.devfest.android)
* [Devfest Lille iOS](https://apps.apple.com/fr/app/apple-store/id1624758676)
* [Tech Day 2022](https://play.google.com/store/apps/details?id=com.decathlon.tech.day.android)

## License

    Copyright 2022-2025 Gérard Paligot.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
