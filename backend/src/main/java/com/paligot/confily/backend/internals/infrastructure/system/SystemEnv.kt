package com.paligot.confily.backend.internals.infrastructure.system

import org.jetbrains.exposed.crypt.Algorithms

object SystemEnv {
    val projectName: String = getEnv("PROJECT_NAME") ?: "confily"

    object DatabaseConfig {
        val isCloud: Boolean = getEnv("IS_CLOUD") ?: true
        val hasPostgres: Boolean = getEnv("EXPOSED_ENABLED") ?: false
    }

    object Exposed {
        val dbUrl: String = getEnv("EXPOSED_DB_URL") ?: "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1"
        val dbDriver: String = getEnv("EXPOSED_DB_DRIVER") ?: "org.h2.Driver"
        val dbUser: String = getEnv("EXPOSED_DB_USER") ?: ""
        val dbPassword: String = getEnv("EXPOSED_DB_PASSWORD") ?: ""
    }

    object Crypto {
        val key: String = System.getenv("CRYPTO_KEY") ?: "crypto-key"
        val salt: String = System.getenv("CRYPTO_SALT") ?: "5531e2129e"
        val algorithm = Algorithms.AES_256_PBE_GCM(password = key, salt = salt)
    }

    object GoogleProvider {
        val projectId: String = getEnv("PROJECT_ID")
            ?: throw IllegalStateException("PROJECT_ID is required")
        val storageBucket: String = getEnv("GOOGLE_STORAGE_BUCKET")
            ?: throw IllegalStateException("GOOGLE_STORAGE_BUCKET is required")
        val geocodeApiKey: String = getEnv("GEOCODE_API_KEY")
            ?: throw IllegalStateException("GEOCODE_API_KEY is required")
    }

    object OpenFeedbackProvider {
        val baseUrl: String = getEnv("BASE_URL_OPEN_FEEDBACK")
            ?: "openfeedback.io"
    }

    object OpenPlannerProvider {
        val baseUrl: String = getEnv("BASE_URL_OPEN_PLANNER")
            ?: "storage.googleapis.com/conferencecenterr.appspot.com"
    }
}

@Suppress("ReturnCount")
private inline fun <reified T> getEnv(name: String): T? {
    if (hasEnv(name).not()) {
        return null
    }
    val env = System.getenv(name)
    if (T::class == Int::class && env.toDoubleOrNull() != null) {
        return env.toInt() as T
    } else if (T::class == Long::class && env.toDoubleOrNull() != null) {
        return env.toLong() as T
    } else if (T::class == Double::class && env.toDoubleOrNull() != null) {
        return env.toDouble() as T
    } else if (T::class == Boolean::class && env == "true" || env == "false") {
        return env.toBoolean() as T
    } else if (T::class == String::class) {
        return env as T
    } else if (T::class == List::class) {
        return env.split(",").map { it.trim() } as T
    } else if (T::class == Map::class) {
        return env.split(",").map {
            val (key, value) = it.split("=")
            key to value
        }.toMap() as T
    } else if (T::class == Set::class) {
        return env.split(",").map { it.trim() }.toSet() as T
    }
    throw IllegalArgumentException("Type not supported")
}

private fun hasEnv(name: String): Boolean =
    System.getenv(name) != null && System.getenv(name).trim() != ""
