package com.nopalsoft.dragracer.scene2D

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.screens.BaseScreen

class GameOverGroup(screen: BaseScreen, distance: Int, coins: Int) : Group() {
    init {
        setSize(420f, 350f)
        setPosition(BaseScreen.SCREEN_WIDTH / 2f - width / 2f, 900f)
        addAction(Actions.moveTo(x, 390f, 1f, Interpolation.bounceOut))
        val background = Image(Assets.scoresBackground)
        background.setSize(width, height)
        addActor(background)

        val labelScore = Label("Distance\n${distance}m", Assets.labelStyleLarge)
        labelScore.setAlignment(Align.center)
        labelScore.setFontScale(1.3f)
        labelScore.setPosition(width / 2f - labelScore.width / 2f, 210f)
        addActor(labelScore)

        val bestScoreTable = Table()
        bestScoreTable.setSize(width, 110f)
        bestScoreTable.y = 80f
        bestScoreTable.padLeft(15f).padRight(15f)

        val labelBestScore = Label("Best distance", Assets.labelStyleLarge)
        labelBestScore.setAlignment(Align.left)
        labelBestScore.setFontScale(.75f)

        val labelNumBestScore = Label("${Settings.bestScore}m", Assets.labelStyleLarge)
        labelNumBestScore.setAlignment(Align.right)
        labelNumBestScore.setFontScale(.75f)

        val labelCoins = Label("Coins", Assets.labelStyleLarge)
        labelCoins.setAlignment(Align.left)
        labelCoins.setFontScale(.75f)

        val labelNumBestCoins = Label(coins.toString(), Assets.labelStyleLarge)
        labelNumBestCoins.setAlignment(Align.right)
        labelNumBestCoins.setFontScale(.75f)

        bestScoreTable.add(labelBestScore).left()
        bestScoreTable.add(labelNumBestScore).right().expand()
        bestScoreTable.row()
        bestScoreTable.add(labelCoins).left()
        bestScoreTable.add(labelNumBestCoins).right().expand()

        val buttonShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        buttonShareTwitter.setSize(50f, 50f)
        buttonShareTwitter.setPosition(155f, 20f)
        screen.addPressEffect(buttonShareTwitter)
        buttonShareTwitter.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {

            }
        })


        // Facebook + Twitter
        val buttonShareFacebook = Button(TextureRegionDrawable(Assets.buttonFacebook))
        buttonShareFacebook.setSize(50f, 50f)
        buttonShareFacebook.setPosition(225f, 20f)
        screen.addPressEffect(buttonShareFacebook)
        buttonShareFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {

            }
        })

        addActor(bestScoreTable)
        addActor(buttonShareTwitter)
        addActor(buttonShareFacebook)

    }
}