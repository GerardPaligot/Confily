package com.paligot.confily.core

import com.paligot.confily.models.ui.Image

interface QrCodeGenerator {
    fun generate(text: String): Image
}
