package com.paligot.confily.core

import java.text.DecimalFormat

actual class Platform actual constructor() {
    actual val hasSupportSVG: Boolean = true
}

actual class DecimalFormat actual constructor() {
    actual fun format(number: Int): String {
        val formatter = DecimalFormat()
        formatter.minimumIntegerDigits = 2
        formatter.isGroupingUsed = false
        formatter.maximumFractionDigits = 2
        formatter.isDecimalSeparatorAlwaysShown = false
        return formatter.format(number)
    }
}
