package com.paligot.confily.core.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java

actual val httpClientEngine: HttpClientEngine
    get() = Java.create()
