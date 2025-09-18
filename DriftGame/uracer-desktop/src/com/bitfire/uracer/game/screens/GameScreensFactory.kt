package com.bitfire.uracer.game.screens

import com.bitfire.uracer.screen.Screen
import com.bitfire.uracer.screen.ScreenFactory
import com.bitfire.uracer.screen.ScreenFactory.ScreenId

class GameScreensFactory : ScreenFactory {

    private val types = ScreenType.entries.toTypedArray()

    override fun createScreen(screenId: ScreenId): Screen? {

        val screen = when (types[screenId.id()]) {
            ScreenType.GameScreen -> GameScreen()
            ScreenType.MainScreen -> MainScreen()
            ScreenType.OptionsScreen -> OptionsScreen()
            ScreenType.ExitScreen -> {null}
            else -> {null}
        }

        if (screen != null) {
            if (screen.init()) {
                if (screenId === ScreenType.GameScreen) {
                    screen.tick()
                    screen.tickCompleted()
                }
            }
        }

        return screen
    }

    enum class ScreenType : ScreenId {
        NoScreen, ExitScreen, MainScreen, OptionsScreen, GameScreen;

        override fun id() = this.ordinal
    }
}
