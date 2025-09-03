package com.bitfire.uracer.utils

object OrdinalUtils {

    fun getOrdinalFor(value: Int): String {
        val hundredRemainder = value % 100
        val tenRemainder = value % 10
        if (hundredRemainder - tenRemainder == 10) return "th"

        return when (tenRemainder) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}
