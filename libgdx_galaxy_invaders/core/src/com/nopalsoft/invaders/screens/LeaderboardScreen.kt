package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.GalaxyInvadersGame

class LeaderboardScreen(game: GalaxyInvadersGame) : Screens(game) {
    var buttonLeaderBoard: TextButton
    var buttonAchievements: TextButton
    var buttonBack: TextButton = TextButton(Assets.languagesBundle!!["back"], Assets.styleTextButtonBack)
    var buttonSignOut: TextButton
    var leftEllipse: Image

    init {
        buttonBack.pad(0f, 15f, 35f, 0f)
        buttonBack.setSize(63f, 63f)
        buttonBack.setPosition((SCREEN_WIDTH - 63).toFloat(), (SCREEN_HEIGHT - 63).toFloat())
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        buttonLeaderBoard = TextButton(Assets.languagesBundle!!["leaderboard"], Assets.styleTextButtonMenu)
        buttonLeaderBoard.height = 50f // Height 50
        buttonLeaderBoard.setSize(50f, 0f) // We add 50 to the current width
        buttonLeaderBoard.setPosition(0f, 245f)
        buttonLeaderBoard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
            }
        })

        buttonAchievements = TextButton(Assets.languagesBundle!!["achievements"], Assets.styleTextButtonMenu)
        buttonAchievements.height = 50f // Height 50
        buttonAchievements.setSize(50f, 0f) // We add 50 to the current width
        buttonAchievements.setPosition(0f, 150f)
        buttonAchievements.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
            }
        })

        buttonSignOut = TextButton(Assets.languagesBundle!!["sign_out"], TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15))
        buttonSignOut.label.wrap = true
        buttonSignOut.width = 140f
        buttonSignOut.setPosition(2f, 2f)
        buttonSignOut.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        leftEllipse = Image(Assets.leftMenuEllipse)
        leftEllipse.setSize(18.5f, 250.5f)
        leftEllipse.setPosition(0f, 105f)

        stage.addActor(buttonSignOut)
        stage.addActor(buttonAchievements)
        stage.addActor(buttonLeaderBoard)
        stage.addActor(buttonBack)
        stage.addActor(leftEllipse)
    }

    override fun draw(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.disableBlending()
        Assets.backgroundLayer!!.render(delta)
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keyPressed: Int): Boolean {
        if (keyPressed == Input.Keys.BACK || keyPressed == Input.Keys.ESCAPE) {
            playSound(Assets.clickSound!!)
            game.screen = MainMenuScreen(game)
            return true
        }
        return false
    }
}
