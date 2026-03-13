package com.salvai.whatcolor

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.salvai.whatcolor.actors.Pattern
import com.salvai.whatcolor.actors.PatternData
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.EDIT_LEVELS
import com.salvai.whatcolor.global.GAME_NAME
import com.salvai.whatcolor.global.PATTERN_SIZE
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.screens.SplashScreen
import com.salvai.whatcolor.utils.GameFlowManager
import com.salvai.whatcolor.utils.MyAssetsManager


class WhatColor : Game() {

    var gameState: GameState = GameState.MENU
    lateinit var camera: OrthographicCamera
    lateinit var viewport: FitViewport
    lateinit var myAssetsManager: MyAssetsManager
    lateinit var skin: Skin
    lateinit var stage: Stage
    lateinit var backgroundStage: Stage
    lateinit var preferences: Preferences
    lateinit var logo: Image
    lateinit var patternsData: Array<PatternData>
    lateinit var patterns: Array<Pattern>
    lateinit var gameFlowManager: GameFlowManager
    lateinit var highscores: Array<Int>
    lateinit var words: I18NBundle
    var vibrate: Boolean = true


    override fun create() {
        preferences = Gdx.app.getPreferences(GAME_NAME)

        camera = OrthographicCamera()
        viewport = FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera)

        myAssetsManager = MyAssetsManager()
        myAssetsManager.loadSplashScreen()

        stage = Stage(viewport)
        backgroundStage = Stage()

        highscores = Array()

        loadPreferences()

        setScreen(SplashScreen(this))
    }

    fun loadPreferences() {
        highscores.add(preferences.getInteger("highscore0", 0)) //unlock first level
        for (i in 1 until PATTERN_SIZE) highscores.add(preferences.getInteger("highscore$i", if (EDIT_LEVELS) 0 else -1))
    }

    fun savePreferences() {
        for (i in 0 until PATTERN_SIZE) preferences.putInteger("highscore$i", highscores.get(i))
        preferences.putBoolean("vibrate", vibrate)
        preferences.flush()
    }

    override fun dispose() {
        savePreferences()
        myAssetsManager.dispose()
        stage.dispose()
        backgroundStage.dispose()
    }

}
