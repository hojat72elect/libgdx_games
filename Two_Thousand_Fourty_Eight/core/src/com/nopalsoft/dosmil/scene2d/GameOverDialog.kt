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

class GameOverDialog(var screen: Screens, didWin: Boolean, time: Int, score: Long) : Group() {
    init {
        setSize(420f, 450f)
        setOrigin(width / 2f, height / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 260f)
        setScale(.5f)

        val background = Image(Assets.scoresBackgroundAtlasRegion)
        background.setSize(width, height)
        addActor(background)

        var title = Assets.languagesBundle!!["gameOver"]
        if (didWin) title = Assets.languagesBundle!!["congratulations"]

        val labelCongrats = Label(title, Assets.labelStyleLarge)
        labelCongrats.setAlignment(Align.center)
        labelCongrats.setFontScale(.50f)
        labelCongrats.setPosition(width / 2f - labelCongrats.width / 2f, 365f)
        addActor(labelCongrats)

        val scoreTable = Table()
        scoreTable.setSize(width, 180f)
        scoreTable.y = 170f
        scoreTable.padLeft(15f).padRight(15f)

        // ACTUAL TIME
        val labelTime = Label(Assets.languagesBundle!!["time"], Assets.labelStyleSmall)
        labelTime.setAlignment(Align.left)

        val labelNumTime = Label(time.toString() + Assets.languagesBundle!!["secondAbbreviation"], Assets.labelStyleSmall)
        labelNumTime.setAlignment(Align.right)

        // ACTUAL SCORE
        val labelScore = Label(Assets.languagesBundle!!["score"], Assets.labelStyleSmall)
        labelScore.setAlignment(Align.left)

        val labelNumScore = Label(score.toString() + "", Assets.labelStyleSmall)
        labelNumScore.setAlignment(Align.right)

        // BEST MOVES
        val labelBestScore = Label(Assets.languagesBundle!!["bestScore"], Assets.labelStyleSmall)
        labelBestScore.setAlignment(Align.left)

        val labelBestNumScore = Label(Settings.bestScore.toString() + "", Assets.labelStyleSmall)
        labelBestNumScore.setAlignment(Align.right)

        scoreTable.add(labelTime).left()
        scoreTable.add(labelNumTime).right().expand()

        scoreTable.row()
        scoreTable.add(labelScore).left()
        scoreTable.add(labelNumScore).right().expand()

        scoreTable.row()
        scoreTable.add(labelBestScore).left()
        scoreTable.add(labelBestNumScore).right().expand()

        val buttonShareTwitter = Button(Assets.buttonTwitter)
        buttonShareTwitter.setSize(50f, 50f)
        buttonShareTwitter.setPosition(155f, 110f)
        screen.addPressEffect(buttonShareTwitter)


        // Facebook Twitter
        val buttonShareFacebook = Button(Assets.buttonFacebook)
        buttonShareFacebook.setSize(50f, 50f)
        buttonShareFacebook.setPosition(225f, 110f)
        screen.addPressEffect(buttonShareFacebook)

        val labelMainMenu = Label(Assets.languagesBundle!!["menu"], Assets.labelStyleLarge)
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
