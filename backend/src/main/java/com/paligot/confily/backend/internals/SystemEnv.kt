package com.paligot.confily.backend.internals

object SystemEnv {
    val projectName = System.getenv("PROJECT_NAME") ?: "confily"
    val isCloud = System.getenv("IS_CLOUD") == "true"
    val gcpProjectId = System.getenv("PROJECT_ID")
}
