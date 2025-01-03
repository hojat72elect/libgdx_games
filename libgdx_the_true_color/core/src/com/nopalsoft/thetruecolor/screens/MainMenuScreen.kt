package com.nopalsoft.thetruecolor.screens

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Assets.loadAssetsWithSettings
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.Settings.save
import com.nopalsoft.thetruecolor.TrueColorGame
import com.nopalsoft.thetruecolor.game.GameScreen
import com.nopalsoft.thetruecolor.leaderboard.DialogRanking
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings

class MainMenuScreen(game: TrueColorGame) : BaseScreen(game) {

    private val imageTitle = Image(Assets.title)
    private val dialogRanking = DialogRanking(this)
    private val buttonPlay = ImageButton(ImageButtonStyle(Assets.buttonPlay, null, null, Assets.play, null, null))
    private val menuUI = Table()
    private val buttonRate = Button(Assets.buttonRate)
    private val buttonLeaderboard = Button(Assets.buttonLeaderboard)
    private val buttonAchievement = Button(Assets.buttonAchievement)
    private val buttonHelp = Button(Assets.buttonHelp)
    private val helpWindowSettings = DialogHelpSettings(this)

    init {
        imageTitle.setPosition(SCREEN_WIDTH / 2f - imageTitle.width / 2f, 610f)
        addPressEffect(buttonPlay)
        buttonPlay.imageCell.padLeft(10f).size(47f, 54f) // I center the play image with the pad, and set the size.
        buttonPlay.setSize(288f, 72f)
        buttonPlay.setPosition(SCREEN_WIDTH / 2f - buttonPlay.width / 2f, 120f)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })


        addPressEffect(buttonRate)
        buttonRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
            }
        })

        addPressEffect(buttonLeaderboard)
        buttonLeaderboard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
            }
        })

        addPressEffect(buttonAchievement)
        buttonAchievement.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
            }
        })

        addPressEffect(buttonHelp)
        buttonHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                helpWindowSettings.show(stage)
            }
        })

        menuUI.setSize(SCREEN_WIDTH, 70f)
        menuUI.setPosition(0f, 35f)
        menuUI.defaults().size(70f).expand()

        if (Gdx.app.type != ApplicationType.WebGL) {
            menuUI.add(buttonRate)
            menuUI.add(buttonLeaderboard)
            menuUI.add(buttonAchievement)
        }
        menuUI.add(buttonHelp)

        stage.addActor(imageTitle)
        stage.addActor(dialogRanking)
        stage.addActor(buttonPlay)
        stage.addActor(menuUI)

        if (game.arrPerson != null) updateLeaderboard()
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher.begin()
        batcher.draw(Assets.header, 0f, 780f, 480f, 20f)
        batcher.draw(Assets.header, 0f, 0f, 480f, 20f)
        batcher.end()
    }

    private fun updateLeaderboard() {
        dialogRanking.clearLeaderboard()
        game.arrPerson!!.sort() // Arrange from largest to smallest
        for (obj in game.arrPerson!!) {
            dialogRanking.addPerson(obj)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            Gdx.app.exit()
            return true
        } else if (keycode == Input.Keys.E) {
            Settings.selectedLanguage = DialogHelpSettings.Languages.ENGLISH
            save()
            loadAssetsWithSettings()
            game.screen = MainMenuScreen(game)
        } else if (keycode == Input.Keys.R) {
            Settings.selectedLanguage = DialogHelpSettings.Languages.SPANISH
            save()
            loadAssetsWithSettings()
            game.screen = MainMenuScreen(game)
        } else if (keycode == Input.Keys.T) {
            Settings.selectedLanguage = DialogHelpSettings.Languages.CHINESE_TAIWAN
            save()
            loadAssetsWithSettings()
            game.screen = MainMenuScreen(game)
        } else if (keycode == Input.Keys.Y) {
            Settings.selectedLanguage = DialogHelpSettings.Languages.DEFAULT
            save()
            loadAssetsWithSettings()
            game.screen = MainMenuScreen(game)
        }

        return super.keyDown(keycode)
    }
}
