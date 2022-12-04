package org.gdglille.devfest.backend.internals.helpers.secret

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient

class SecretManagerImpl(
    private val projectId: String,
    private val client: SecretManagerServiceClient
) : Secret {
    override fun get(id: String): String =
        client.accessSecretVersion("projects/$projectId/secrets/$id/versions/latest")
            .payload.data.toString(Charsets.UTF_8)
}
