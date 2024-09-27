package com.paligot.confily.core

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual class Platform {
    actual val hasSupportSVG: Boolean = false
}

actual class DecimalFormat {
    actual fun format(number: Int): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 2u
        formatter.numberStyle = 1u
        formatter.minimumIntegerDigits = 2u
        return formatter.stringFromNumber(NSNumber(number))!!
    }
}
