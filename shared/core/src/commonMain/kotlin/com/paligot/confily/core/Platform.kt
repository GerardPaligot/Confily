package com.paligot.confily.core

expect class Platform() {
    val hasSupportSVG: Boolean
}

expect class DecimalFormat() {
    fun format(number: Int): String
}
