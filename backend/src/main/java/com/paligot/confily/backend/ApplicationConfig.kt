package com.paligot.confily.backend

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

data class ApplicationConfig(
    val databaseUrl: String = SystemEnv.Exposed.dbUrl,
    val databaseDriver: String = SystemEnv.Exposed.dbDriver,
    val databaseUser: String = SystemEnv.Exposed.dbUser,
    val databasePassword: String = SystemEnv.Exposed.dbPassword
)
