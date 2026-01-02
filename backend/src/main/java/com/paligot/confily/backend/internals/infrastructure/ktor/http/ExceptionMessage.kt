package com.paligot.confily.backend.internals.infrastructure.ktor.http

import kotlinx.serialization.Serializable

@Serializable
class ExceptionMessage(val message: String, val stack: List<String> = emptyList())
