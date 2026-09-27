package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.java.Java

object SupabaseServicesModule {
    val client by lazy {
        createSupabaseClient(
            supabaseUrl = SystemEnv.SupabaseProvider.url
                ?: throw IllegalStateException("SUPABASE_URL is required"),
            supabaseKey = SystemEnv.SupabaseProvider.serviceRoleKey
                ?: throw IllegalStateException("SUPABASE_SERVICE_ROLE_KEY is required")
        ) {
            httpEngine = Java.create()
            install(Storage)
        }
    }
}
