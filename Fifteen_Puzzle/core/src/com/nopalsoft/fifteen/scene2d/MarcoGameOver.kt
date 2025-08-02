package com.nopalsoft.fifteen.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.Settings
import com.nopalsoft.fifteen.screens.MainMenuScreen
import com.nopalsoft.fifteen.screens.Screens

class MarcoGameOver(var screen: Screens, time: Int, moves: Int) : Group() {

    init {
        setSize(420f, 450f)
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260f)
        setScale(.5f)

        val background = Image(Assets.fondoPuntuaciones)
        background.setSize(getWidth(), getHeight())
        addActor(background)

        val lbCongratulations = Label(
            "Congratulations!",
            Assets.labelStyleGrande
        )
        lbCongratulations.setAlignment(Align.center)
        lbCongratulations.setFontScale(.50f)
        lbCongratulations.setPosition(
            getWidth() / 2f - lbCongratulations.getWidth() / 2f, 365f
        )
        addActor(lbCongratulations)

        val scoreTable = Table()
        scoreTable.setSize(getWidth(), 180f)
        scoreTable.setY(170f)
        scoreTable.padLeft(15f).padRight(15f)

        // scoreTable.debug();

        // ACTUAL TIME
        val lbTime = Label("Time", Assets.labelStyleChico)
        lbTime.setAlignment(Align.left)

        val lblNumTime = Label(time.toString() + "s", Assets.labelStyleChico)
        lblNumTime.setAlignment(Align.right)

        // BEST TIME
        val lbBestTime = Label("Best time", Assets.labelStyleChico)
        lbBestTime.setAlignment(Align.left)

        val lblNumBestTime = Label(
            Settings.bestTime.toString() + "s",
            Assets.labelStyleChico
        )
        lblNumBestTime.setAlignment(Align.right)

        // ACTUAL MOVES
        val lbMoves = Label("Moves", Assets.labelStyleChico)
        lbMoves.setAlignment(Align.left)

        val lbNumMoves = Label(moves.toString() + "", Assets.labelStyleChico)
        lbNumMoves.setAlignment(Align.right)

        // lbNumMoves.setFontScale(.75f);

        // BEST MOVES
        val lbBestMoves = Label("Best moves", Assets.labelStyleChico)
        lbBestMoves.setAlignment(Align.left)

        val lbBestNumMoves = Label(
            Settings.bestMoves.toString() + "",
            Assets.labelStyleChico
        )
        lbBestNumMoves.setAlignment(Align.right)

        scoreTable.add<Label?>(lbTime).left()
        scoreTable.add<Label?>(lblNumTime).right().expand()

        scoreTable.row()
        scoreTable.add<Label?>(lbBestTime).left()
        scoreTable.add<Label?>(lblNumBestTime).right().expand()

        scoreTable.row()
        scoreTable.add<Label?>(lbMoves).left()
        scoreTable.add<Label?>(lbNumMoves).right().expand()

        scoreTable.row()
        scoreTable.add<Label?>(lbBestMoves).left()
        scoreTable.add<Label?>(lbBestNumMoves).right().expand()

        val btShareTwitter = Button(Assets.btTwitter)
        btShareTwitter.setSize(50f, 50f)
        btShareTwitter.setPosition(155f, 110f)
        screen.addEfectoPress(btShareTwitter)

        // Facebook Twitter
        val btShareFacebook = Button(Assets.btFacebook)
        btShareFacebook.setSize(50f, 50f)
        btShareFacebook.setPosition(225f, 110f)
        screen.addEfectoPress(btShareFacebook)

        val lbMainMenu = Label("Main menu", Assets.labelStyleGrande)
        lbMainMenu.setWidth(getWidth() - 10)
        lbMainMenu.setFontScale(.75f)
        lbMainMenu
            .setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 30f)
        lbMainMenu.setWrap(true)
        lbMainMenu.setAlignment(Align.center)
        screen.addEfectoPress(lbMainMenu)
        lbMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    MainMenuScreen::class.java,
                    screen.game!!
                )
            }
        })

        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, .2f),
                Actions.run {
                    addActor(scoreTable)
                    addActor(btShareTwitter)
                    addActor(btShareFacebook)
                    addActor(lbMainMenu)
                }
            )
        )
    }
}
