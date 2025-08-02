package com.salvai.snake.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.salvai.snake.SnakeIt
import com.salvai.snake.screens.LevelChooseScreen
import com.salvai.snake.screens.MenuScreen
import com.salvai.snake.screens.SettingsScreen
import com.salvai.snake.utils.Constants

class CatchBackKeyProcessor(private val game: SnakeIt, private val screen: Screen?) : InputAdapter() {
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            when (screen) {
                is LevelChooseScreen -> {
                    screen.game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run {
                        game.setScreen(MenuScreen(game))
                        screen.dispose()
                    }))
                }

                is SettingsScreen -> {
                    screen.game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run {
                        game.setScreen(MenuScreen(game))
                        screen.dispose()
                    }))
                }

                else -> return screen !is MenuScreen
            }
        }
        return true
    }
}
