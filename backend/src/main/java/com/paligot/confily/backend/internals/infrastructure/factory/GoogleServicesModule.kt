package com.paligot.confily.backend.internals.infrastructure.factory

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.StorageOptions
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object GoogleServicesModule {
    private fun getCredentials(): GoogleCredentials {
        val json = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON")
        if (!json.isNullOrBlank()) {
            return ServiceAccountCredentials.fromStream(json.byteInputStream())
        }
        return GoogleCredentials.getApplicationDefault()
    }

    val cloudStorage by lazy {
        StorageOptions.getDefaultInstance().toBuilder().run {
            setProjectId(SystemEnv.GoogleProvider.projectId)
            setCredentials(getCredentials())
            build()
        }.service
    }

    val drive by lazy {
        Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(
                getCredentials().createScoped(setOf(DriveScopes.DRIVE))
            )
        )
            .setApplicationName(SystemEnv.GoogleProvider.projectId)
            .build()
    }
}
