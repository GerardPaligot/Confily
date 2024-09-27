package com.paligot.confily.core

interface QrCodeGenerator {
    fun generate(text: String): ByteArray
}
