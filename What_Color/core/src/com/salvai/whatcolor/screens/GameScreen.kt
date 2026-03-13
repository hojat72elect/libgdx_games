package com.salvai.whatcolor.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.BLOCK_IMAGE
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.global.SKIN_NAME
import com.salvai.whatcolor.global.createAllPatterns
import com.salvai.whatcolor.global.getAllPaterns
import com.salvai.whatcolor.ui.Chooser
import com.salvai.whatcolor.ui.GameOverTable
import com.salvai.whatcolor.ui.LevelTable
import com.salvai.whatcolor.ui.MenuTable
import com.salvai.whatcolor.ui.SettingsTable
import com.salvai.whatcolor.ui.TimeBarAndScoreLabelTable
import com.salvai.whatcolor.utils.GameFlowManager


class GameScreen(game: WhatColor) : MyScreen(game) {

    private var levelTable: LevelTable
    private var levelScrollPane: ScrollPane
    private var menuTable: MenuTable
    private var settingsTable: SettingsTable
    val timeBarAndScoreLabelTable: TimeBarAndScoreLabelTable
    val gameOverTable: GameOverTable
    val chooser: Chooser


    init {
        assignAssets(game)
        settingsTable = SettingsTable(game)
        menuTable = MenuTable(game)
        levelTable = LevelTable(game)
        gameOverTable = GameOverTable(game)
        chooser = Chooser(game)
        timeBarAndScoreLabelTable = TimeBarAndScoreLabelTable(game)
        levelScrollPane = ScrollPane(levelTable, game.skin).apply {
            this.setBounds(-SCREEN_WIDTH, SCREEN_HEIGHT * 0.1f, SCREEN_WIDTH, SCREEN_HEIGHT * 0.85f)
            fadeScrollBars = true
            isTransform = true
        }

        game.stage.addActor(menuTable)
        game.stage.addActor(chooser)
        game.stage.addActor(levelScrollPane)
        game.stage.addActor(gameOverTable)
        game.stage.addActor(settingsTable)
        game.stage.addActor(timeBarAndScoreLabelTable)

        menuTable.addAction(Actions.moveBy(0f, -SCREEN_HEIGHT))
        menuTable.addAction(Actions.moveBy(0f, SCREEN_HEIGHT, MENU_ANIMATION_TIME, Interpolation.circle))

    }


    private fun assignAssets(game: WhatColor) {
        game.skin = game.myAssetsManager.manager.get(SKIN_NAME, Skin::class.java)
        game.gameFlowManager = GameFlowManager(game, this)
        game.patternsData = getAllPaterns(game.myAssetsManager)
        game.patterns = createAllPatterns(game.patternsData, game.myAssetsManager.manager.get(BLOCK_IMAGE, Texture::class.java))
        game.words = game.myAssetsManager.manager.get("i18n/words", I18NBundle::class.java)
    }


    override fun render(delta: Float) {
        super.render(delta)
        if (game.gameState == GameState.PLAY_SHOW_PATTERN || game.gameState == GameState.PLAY_RUNNING)
            game.gameFlowManager.update(delta)
    }

    fun showLevelTable() {
        hideMenu()
        levelTable.setUp()
        game.gameState = GameState.LEVEL_CHOOSE
        showLevelScrollPane()
    }

    fun hideLevelScrollPane(showMenu: Boolean) {
        if (showMenu)
            showMenu(true)
        levelScrollPane.addAction(Actions.moveBy(-SCREEN_WIDTH, 0f, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun showLevelScrollPane() {
        levelScrollPane.addAction(Actions.moveBy(SCREEN_WIDTH, 0f, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun hideGameOverTable() {
        gameOverTable.addAction(Actions.moveBy(SCREEN_WIDTH, 0f, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun showGameOverTable() {
        gameOverTable.addAction(Actions.moveBy(-SCREEN_WIDTH, 0f, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun hideMenu() {
        menuTable.addAction(Actions.moveBy(0f, -SCREEN_HEIGHT, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun hideSettingsTable() {
        showMenu(false)
        settingsTable.addAction(Actions.moveBy(0f, -SCREEN_HEIGHT, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun showSettingsTable() {
        hideMenu()
        game.gameState = GameState.SETTINGS
        settingsTable.addAction(Actions.moveBy(0f, SCREEN_HEIGHT, MENU_ANIMATION_TIME, Interpolation.circle))
    }


    fun showMenu(hideLogo: Boolean) {
        menuTable.addAction(Actions.moveBy(0f, SCREEN_HEIGHT, MENU_ANIMATION_TIME, Interpolation.circle))
        if (hideLogo)
            game.logo.addAction(Actions.moveBy(0f, -game.logo.height * 1.25f, MENU_ANIMATION_TIME, Interpolation.circle))
        game.gameState = GameState.MENU
    }

    fun hideLogo() {
        game.logo.addAction(Actions.moveBy(0f, game.logo.height * 1.25f, MENU_ANIMATION_TIME, Interpolation.circle))
    }

}
