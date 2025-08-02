package com.nopalsoft.ponyrace.handlers

fun interface FloatFormatter {
    fun format(format: String, number: Float): String
}