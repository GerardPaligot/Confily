package com.paligot.confily.backend.internals.helpers.secret

import com.google.api.gax.rpc.ApiException
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretPayload

class SecretManagerImpl(
    private val projectId: String,
    private val client: SecretManagerServiceClient
) : Secret {
    override fun get(id: String): String? {
        return try {
            val data = client
                .accessSecretVersion("projects/$projectId/secrets/$id/versions/latest")
                .payload
            if (data == SecretPayload.getDefaultInstance()) {
                return null
            }
            data.data.toString(Charsets.UTF_8)
        } catch (ex: ApiException) {
            null
        }
    }
}
