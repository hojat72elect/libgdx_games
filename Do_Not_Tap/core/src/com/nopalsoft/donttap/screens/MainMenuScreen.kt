package com.nopalsoft.donttap.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.DoNotTapGame

class MainMenuScreen(game: DoNotTapGame) : Screens(game) {
    var buttonPlay: TextButton
    var buttonRate: TextButton
    var buttonLeaderboards: TextButton
    var buttonFacebook: Button

    init {
        addBackGround()

        val titleImage = Image(Assets.title)
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 620f)

        val menu = Table()
        menu.setSize(350f, 400f)
        menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 130f)
        menu.defaults().center().expand()

        buttonPlay = TextButton("Play", Assets.textButtonStyleSmall)
        addPressEffect(buttonPlay)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(SelectScreen::class.java, game)
            }
        })

        buttonRate = TextButton("Rate", Assets.textButtonStyleSmall)
        addPressEffect(buttonRate)

        buttonLeaderboards = TextButton(
            "Leaderboards",
            Assets.textButtonStyleSmall
        )
        addPressEffect(buttonLeaderboards)

        buttonFacebook = Button(Assets.buttonFacebook)
        buttonFacebook.setSize(55f, 55f)
        buttonFacebook.setPosition((SCREEN_WIDTH - 67).toFloat(), 7f)
        addPressEffect(buttonFacebook)

        menu.add<TextButton?>(buttonPlay)

        menu.row()
        menu.add<TextButton?>(buttonRate)

        menu.row()
        menu.add<TextButton?>(buttonLeaderboards)

        stage.addActor(titleImage)
        stage.addActor(menu)
        stage.addActor(buttonFacebook)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }

    override fun draw(delta: Float) {
    }

    override fun update(delta: Float) {
    }
}
