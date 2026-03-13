package com.salvai.whatcolor.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.BUTTON_HEIGHT
import com.salvai.whatcolor.global.BUTTON_SPACE
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH

class MenuTable(game: WhatColor) : Table(game.skin) {



    init {
        val playButton: TextButton
        val settingsButton: TextButton

        isTransform = true
        setBounds((SCREEN_WIDTH - PATTERN_SCREEN_SIZE) * 0.5f, SCREEN_HEIGHT * .1f, PATTERN_SCREEN_SIZE, SCREEN_HEIGHT * .4f)
        setOrigin(width * 0.5f, height * 0.5f)
        defaults().growX().space(BUTTON_SPACE).height(BUTTON_HEIGHT)

        playButton = TextButton("PLAY", game.skin, "default").apply {
            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.gameState == GameState.MENU)
                        game.gameFlowManager.showLevelTable()
                }
            })
        }

        settingsButton = TextButton(game.words["settings"], game.skin, "default").apply {
            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.gameState == GameState.MENU)
                        game.gameFlowManager.gameScreen.showSettingsTable()
                }
            })
        }

        add(playButton)
        row()
        add(settingsButton)

    }

}