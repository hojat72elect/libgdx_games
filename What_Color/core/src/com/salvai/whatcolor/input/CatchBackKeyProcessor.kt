package com.salvai.whatcolor.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState


class CatchBackKeyProcessor(val game: WhatColor) : InputAdapter() {


    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            when (game.gameState) {
                GameState.LEVEL_CHOOSE -> game.gameFlowManager.gameScreen.hideLevelScrollPane(true)
                GameState.GAME_OVER -> {
                    game.gameFlowManager.gameScreen.showMenu(true)
                    game.gameFlowManager.gameScreen.hideGameOverTable()
                }

                GameState.SETTINGS -> game.gameFlowManager.gameScreen.hideSettingsTable()
                GameState.MENU -> Gdx.app.exit()
                else -> {
                    print("Should not happen")
                }
            }
        }
        return true
    }
}
