package com.nopalsoft.sokoban.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.MainSokoban
import com.nopalsoft.sokoban.scene2d.LevelSelector

/**
 * This is the main menu screen of the game. It includes the {@link LevelSelector} and buttons
 * for going to the next and previous pages holding levels of the game. It also contains buttons for
 * accessing "leaderboard", "achievements", "facebook", "settings" and "more".
 */
class MainMenuScreen(game: MainSokoban) : BaseScreen(game) {

    private val levelSelector = LevelSelector(this)

    private val buttonPreviousPage = Button(Assets.buttonIzq, Assets.btIzqPress)
    private val buttonNextPage = Button(Assets.buttonDer, Assets.buttonDerPress)

    private val buttonLeaderboard = Button(Assets.btLeaderboard, Assets.btLeaderboardPress)
    private val buttonAchievements = Button(Assets.btAchievement, Assets.btAchievementPress)
    private val buttonFacebook = Button(Assets.btFacebook, Assets.btFacebookPress)
    private val buttonSettings = Button(Assets.btSettings, Assets.btSettingsPress)
    private val buttonMore = Button(Assets.btMas, Assets.btMasPress)

    private val tableMenu = Table()

    init {
        buttonPreviousPage.setSize(75f, 75f)
        buttonPreviousPage.setPosition(65f, 220f)
        buttonPreviousPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                right()
            }
        })

        buttonNextPage.setSize(75f, 75f)
        buttonNextPage.setPosition(660f, 220f)
        buttonNextPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                left()
            }
        })

        tableMenu.defaults().size(80f).pad(7.5f)
        tableMenu.add(buttonAchievements)
        tableMenu.add(buttonFacebook)
        tableMenu.add(buttonSettings)
        tableMenu.add(buttonMore)

        tableMenu.pack()
        tableMenu.setPosition(SCREEN_WIDTH / 2f - tableMenu.width / 2f, 20f)

        stage?.addActor(levelSelector)
        stage?.addActor(tableMenu)
        stage?.addActor(buttonPreviousPage)
        stage?.addActor(buttonNextPage)
    }


    override fun draw(delta: Float) {
        Assets.background.render(delta)
    }

    override fun update(delta: Float) {}

    override fun right() {
        levelSelector.previousPage()
    }

    override fun left() {
        levelSelector.nextPage()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> {
                right()
            }

            Input.Keys.RIGHT, Input.Keys.D -> {
                left()
            }

            Input.Keys.ESCAPE, Input.Keys.BACK -> {
                Gdx.app.exit()
            }
        }

        return true
    }
}