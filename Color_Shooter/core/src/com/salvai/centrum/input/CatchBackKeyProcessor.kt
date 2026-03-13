package com.salvai.centrum.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.enums.GameType
import com.salvai.centrum.screens.GameOverScreen
import com.salvai.centrum.screens.LevelChooseScreen
import com.salvai.centrum.screens.MenuScreen
import com.salvai.centrum.utils.Constants

class CatchBackKeyProcessor(private val game: CentrumGameClass, private val screen: Screen?) : InputAdapter() {
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            if (screen is LevelChooseScreen) {
                screen.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(MenuScreen(game))
                        screen.dispose()
                    }
                })))
            } else if (screen is MenuScreen) return false
            else if (screen is GameOverScreen) {
                screen.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        if (game.gameType == GameType.LEVEL) game.setScreen(LevelChooseScreen(game))
                        else game.setScreen(MenuScreen(game))
                        screen.dispose()
                    }
                })))
            }
        }
        return true
    }
}
