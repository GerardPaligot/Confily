package org.gdglille.devfest.android

import android.app.Application
import io.openfeedback.android.OpenFeedbackConfig

class MainApplication: Application() {
    lateinit var openFeedbackConfig: OpenFeedbackConfig

    override fun onCreate() {
        super.onCreate()
        openFeedbackConfig = OpenFeedbackConfig(
            context = this,
            openFeedbackProjectId = BuildConfig.OPENFEEDBACK_PROJECT_ID,
            firebaseConfig = OpenFeedbackConfig.FirebaseConfig(
                projectId = BuildConfig.FIREBASE_PROJECT_ID,
                applicationId = BuildConfig.APPLICATION_ID,
                apiKey = BuildConfig.FIREBASE_API_KEY,
                databaseUrl = "https://${BuildConfig.FIREBASE_PROJECT_ID}.firebaseio.com"
            )
        )
    }
}