package com.nopalsoft.dosmil.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.Settings
import com.nopalsoft.dosmil.screens.MainMenuScreen
import com.nopalsoft.dosmil.screens.Screens

class ScreenGameOver(screen: Screens, didWin: Boolean, time: Int, score: Long) : Group() {

    init {
        setSize(420f, 450f)
        setOrigin(width / 2f, height / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 260f)
        setScale(.5f)

        val background = Image(Assets.scoresBackground)
        background.setSize(width, height)
        addActor(background)

        var gameOverTitle = Assets.languages?.get("gameOver")
        if (didWin) gameOverTitle = Assets.languages?.get("congratulations")

        val labelCongratulations = Label(gameOverTitle, Assets.labelStyleLarge)
        labelCongratulations.setAlignment(Align.center)
        labelCongratulations.setFontScale(.50f)
        labelCongratulations.setPosition(width / 2f - labelCongratulations.width / 2f, 365f)
        addActor(labelCongratulations)

        val scoreTable = Table()
        scoreTable.setSize(width, 180f)
        scoreTable.y = 170f
        scoreTable.padLeft(15f).padRight(15f)


        // ACTUAL TIME
        val labelTime = Label(Assets.languages?.get("time"), Assets.labelStyleSmall)
        labelTime.setAlignment(Align.left)

        val labelNumTime = Label(time.toString() + (Assets.languages?.get("secondAbbreviation")), Assets.labelStyleSmall)
        labelNumTime.setAlignment(Align.right)


        // ACTUAL SCORE
        val labelScore = Label(Assets.languages?.get("score"), Assets.labelStyleSmall)
        labelScore.setAlignment(Align.left)

        val labelNumScore = Label(score.toString() + "", Assets.labelStyleSmall)
        labelNumScore.setAlignment(Align.right)


        // BEST MOVES
        val labelBestScore = Label(Assets.languages?.get("bestScore"), Assets.labelStyleSmall)
        labelBestScore.setAlignment(Align.left)

        val labelBestNumScore = Label(Settings.bestScore.toString(), Assets.labelStyleSmall)
        labelBestNumScore.setAlignment(Align.right)

        scoreTable.add(labelTime).left()
        scoreTable.add(labelNumTime).right().expand()

        scoreTable.row()
        scoreTable.add(labelScore).left()
        scoreTable.add(labelNumScore).right().expand()



        scoreTable.row()
        scoreTable.add(labelBestScore).left()
        scoreTable.add(labelBestNumScore).right().expand()

        // Twitter
        val buttonShareTwitter = Button(Assets.buttonTwitter)
        buttonShareTwitter.setSize(50f, 50f)
        buttonShareTwitter.setPosition(155f, 110f)
        screen.addPressEffect(buttonShareTwitter)
        buttonShareTwitter.addListener(object : ClickListener() {
        })


        // Facebook
        val buttonShareFacebook = Button(Assets.buttonFacebook)
        buttonShareFacebook.setSize(50f, 50f)
        buttonShareFacebook.setPosition(225f, 110f)
        screen.addPressEffect(buttonShareFacebook)
        buttonShareFacebook.addListener(object : ClickListener() {
        })

        val labelMainMenu = Label(Assets.languages?.get("menu"), Assets.labelStyleLarge)
        labelMainMenu.width = width - 10
        labelMainMenu.setFontScale(.75f)
        labelMainMenu.setPosition(width / 2f - labelMainMenu.width / 2f, 30f)
        labelMainMenu.wrap = true
        labelMainMenu.setAlignment(Align.center)
        screen.addPressEffect(labelMainMenu)
        labelMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, screen.game)
            }
        })

        addAction(Actions.sequence(Actions.scaleTo(1f, 1f, .2f), Actions.run {
            addActor(scoreTable)
            addActor(buttonShareTwitter)
            addActor(buttonShareFacebook)
            addActor(labelMainMenu)
        }))

    }
}