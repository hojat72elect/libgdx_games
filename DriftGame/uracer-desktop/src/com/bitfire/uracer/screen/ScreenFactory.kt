package com.bitfire.uracer.screen

interface ScreenFactory {
    fun createScreen(screenId: ScreenId): Screen?

    interface ScreenId {
        fun id(): Int
    }
}
