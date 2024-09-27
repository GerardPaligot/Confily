package com.paligot.confily.core

actual class Platform {
    actual val hasSupportSVG: Boolean = true
}

actual class DecimalFormat {
    actual fun format(number: Int): String {
        val formatter = java.text.DecimalFormat()
        formatter.minimumIntegerDigits = 2
        formatter.isGroupingUsed = false
        formatter.maximumFractionDigits = 2
        formatter.isDecimalSeparatorAlwaysShown = false
        return formatter.format(number)
    }
}
