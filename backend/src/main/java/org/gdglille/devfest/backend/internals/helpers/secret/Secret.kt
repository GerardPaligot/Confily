package org.gdglille.devfest.backend.internals.helpers.secret

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient

interface Secret {
    operator fun get(id: String): String

    object Factory {
        fun create(projectId: String, client: SecretManagerServiceClient): Secret =
            SecretManagerImpl(projectId, client)
    }
}
