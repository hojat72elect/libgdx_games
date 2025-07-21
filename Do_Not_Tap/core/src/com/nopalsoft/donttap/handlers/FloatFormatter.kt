package com.nopalsoft.donttap.handlers

fun interface FloatFormatter {
    fun format(format: String, number: Float): String
}
