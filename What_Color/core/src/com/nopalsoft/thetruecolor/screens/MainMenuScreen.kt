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
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages

class MainMenuScreen(game: TrueColorGame) : BaseScreen(game) {
    var titleImage = Image(Assets.titleDrawable)
    var dialogRanking: DialogRanking

    var startButton: ImageButton

    var menuUITable: Table
    var buttonRate: Button
    var buttonLeaderboard: Button
    var buttonAchievement: Button
    var buttonHelp: Button

    var helpDialog: DialogHelpSettings

    init {
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 610f)

        helpDialog = DialogHelpSettings(this)
        dialogRanking = DialogRanking(this)

        startButton = ImageButton(ImageButtonStyle(Assets.buttonPlayDrawable, null, null, Assets.playDrawable, null, null))
        addPressEffect(startButton)
        startButton.imageCell.padLeft(10f).size(47f, 54f) // Centro la imagen play con el pad, y le pongo el tamano
        startButton.setSize(288f, 72f)
        startButton.setPosition(SCREEN_WIDTH / 2f - startButton.getWidth() / 2f, 120f)
        startButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        buttonRate = Button(Assets.buttonRateDrawable)
        addPressEffect(buttonRate)

        buttonLeaderboard = Button(Assets.buttonLeaderBoardDrawable)
        addPressEffect(buttonLeaderboard)

        buttonAchievement = Button(Assets.buttonAchievementDrawable)
        addPressEffect(buttonAchievement)

        buttonHelp = Button(Assets.buttonHelpDrawable)
        addPressEffect(buttonHelp)
        buttonHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                helpDialog.show(stage!!)
            }
        })

        menuUITable = Table()
        menuUITable.setSize(SCREEN_WIDTH.toFloat(), 70f)
        menuUITable.setPosition(0f, 35f)
        menuUITable.defaults().size(70f).expand()

        if (Gdx.app.getType() != ApplicationType.WebGL) {
            menuUITable.add<Button?>(buttonRate)
            menuUITable.add<Button?>(buttonLeaderboard)
            menuUITable.add<Button?>(buttonAchievement)
        }
        menuUITable.add<Button?>(buttonHelp)

        stage?.addActor(titleImage)
        stage?.addActor(dialogRanking)
        stage?.addActor(startButton)
        stage?.addActor(menuUITable)

        if (game.persons != null) updateLeaderboard()
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batch?.begin()
        batch?.draw(Assets.header!!, 0f, 780f, 480f, 20f)
        batch?.draw(Assets.header!!, 0f, 0f, 480f, 20f)
        batch?.end()
    }

    fun updateLeaderboard() {
        dialogRanking.clearLeaderboard()
        game!!.persons!!.sort() // Arrange from largest to smallest
        for (obj in game!!.persons!!) {
            dialogRanking.addPerson(obj)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            Gdx.app.exit()
            return true
        } else if (keycode == Input.Keys.E) {
            Settings.selectedLanguage = Languages.ENGLISH
            save()
            loadAssetsWithSettings()
            game?.setScreen(MainMenuScreen(game!!))
        } else if (keycode == Input.Keys.R) {
            Settings.selectedLanguage = Languages.SPANISH
            save()
            loadAssetsWithSettings()
            game?.setScreen(MainMenuScreen(game!!))
        } else if (keycode == Input.Keys.T) {
            Settings.selectedLanguage = Languages.CHINESE
            save()
            loadAssetsWithSettings()
            game?.setScreen(MainMenuScreen(game!!))
        } else if (keycode == Input.Keys.Y) {
            Settings.selectedLanguage = Languages.DEFAULT
            save()
            loadAssetsWithSettings()
            game?.setScreen(MainMenuScreen(game!!))
        }

        return super.keyDown(keycode)
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}
