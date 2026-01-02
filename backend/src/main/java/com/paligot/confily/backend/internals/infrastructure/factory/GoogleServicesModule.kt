package com.paligot.confily.backend.internals.infrastructure.factory

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretManagerServiceSettings
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object GoogleServicesModule {
    val cloudFirestore: Lazy<Firestore> = lazy {
        FirestoreOptions.getDefaultInstance().toBuilder().run {
            if (!SystemEnv.DatabaseConfig.isCloud) {
                setEmulatorHost("localhost:8081")
            }
            setProjectId(SystemEnv.GoogleProvider.projectId)
            setCredentials(GoogleCredentials.getApplicationDefault())
            build()
        }.service
    }

    val cloudStorage: Lazy<Storage> = lazy {
        StorageOptions.getDefaultInstance().toBuilder().run {
            setProjectId(SystemEnv.GoogleProvider.projectId)
            setCredentials(GoogleCredentials.getApplicationDefault())
            build()
        }.service
    }

    val secretManager: Lazy<SecretManagerServiceClient> = lazy {
        SecretManagerServiceClient.create(
            SecretManagerServiceSettings.newBuilder().apply {
                this.credentialsProvider =
                    SecretManagerServiceSettings.defaultCredentialsProviderBuilder().build()
            }.build()
        )
    }

    val drive: Lazy<Drive> = lazy {
        Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(
                GoogleCredentials.getApplicationDefault().createScoped(setOf(DriveScopes.DRIVE))
            )
        )
            .setApplicationName(SystemEnv.GoogleProvider.projectId)
            .build()
    }
}
