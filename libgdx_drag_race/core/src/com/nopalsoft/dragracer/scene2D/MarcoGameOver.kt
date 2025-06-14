package com.nopalsoft.dragracer.scene2D

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.screens.Screens

class MarcoGameOver(screen: Screens, distance: Int, coins: Int) : Group() {
    init {
        setSize(420f, 350f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 900f)
        addAction(Actions.moveTo(x, 390f, 1f, Interpolation.bounceOut))
        val background = Image(Assets.scoresBackgroundDrawable)
        background.setSize(width, height)
        addActor(background)

        val labelScore = Label(
            """
                Distance
                ${distance}m
                """.trimIndent(),
            Assets.labelStyleLarge
        )
        labelScore.setAlignment(Align.center)
        labelScore.setFontScale(1.3f)
        labelScore.setPosition(width / 2f - labelScore.width / 2f, 210f)
        addActor(labelScore)

        val bestScoreTable = Table()
        bestScoreTable.setSize(width, 110f)
        bestScoreTable.y = 80f
        bestScoreTable.padLeft(15f).padRight(15f)

        val lblBestScore = Label("Best distance", Assets.labelStyleLarge)
        lblBestScore.setAlignment(Align.left)
        lblBestScore.setFontScale(.75f)

        val lblNumBestScore = Label(
            Settings.bestScore.toString() + "m",
            Assets.labelStyleLarge
        )
        lblNumBestScore.setAlignment(Align.right)
        lblNumBestScore.setFontScale(.75f)

        val lblCoins = Label("Coins", Assets.labelStyleLarge)
        lblCoins.setAlignment(Align.left)
        lblCoins.setFontScale(.75f)

        val lblNumBestCoins = Label(coins.toString() + "", Assets.labelStyleLarge)
        lblNumBestCoins.setAlignment(Align.right)
        lblNumBestCoins.setFontScale(.75f)

        bestScoreTable.add(lblBestScore).left()
        bestScoreTable.add(lblNumBestScore).right().expand()

        bestScoreTable.row()
        bestScoreTable.add(lblCoins).left()
        bestScoreTable.add(lblNumBestCoins).right().expand()

        val btShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        btShareTwitter.setSize(50f, 50f)
        btShareTwitter.setPosition(155f, 20f)
        screen.addPressEffect(btShareTwitter)


        // Facebook Twitter
        val btShareFacebook = Button(
            TextureRegionDrawable(
                Assets.buttonFacebook
            )
        )
        btShareFacebook.setSize(50f, 50f)
        btShareFacebook.setPosition(225f, 20f)
        screen.addPressEffect(btShareFacebook)

        addActor(bestScoreTable)
        addActor(btShareTwitter)
        addActor(btShareFacebook)
    }
}
