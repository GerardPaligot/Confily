package com.paligot.confily.core.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual val httpClientEngine: HttpClientEngine
    get() = Android.create()
