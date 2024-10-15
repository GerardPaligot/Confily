package com.paligot.confily.core

actual class Platform actual constructor() {
    actual val hasSupportSVG: Boolean = true
}

actual class DecimalFormat actual constructor() {
    actual fun format(number: Int): String = number.toString().padStart(2, '0')
}
