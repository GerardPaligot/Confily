package com.paligot.confily.backend.internals

object SystemEnv {
    val isCloud: Boolean = getEnv("IS_CLOUD") ?: true
    val projectName: String = getEnv("PROJECT_NAME") ?: "confily"
    val gcpProjectId: String = getEnv("PROJECT_ID")
        ?: throw IllegalStateException("PROJECT_ID is required")
    val conferenceHallUrl: String = getEnv("BASE_URL_CONFERENCE_HALL")
        ?: "conference-hall.io"
    val openPlannerUrl: String = getEnv("BASE_URL_OPEN_PLANNER")
        ?: "storage.googleapis.com/conferencecenterr.appspot.com"
    val openFeedbackUrl: String = getEnv("BASE_URL_OPEN_FEEDBACK")
        ?: "openfeedback.io"
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
