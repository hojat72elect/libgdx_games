package com.nopalsoft.fifteen.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.MainFifteen

class HelpScreen(game: MainFifteen) : Screens(game) {
    var lbTextHelp: Label = Label(
        "The object of the puzzle is to place the tiles in order (see diagram below) by making sliding moves that use the empty space.",
        Assets.labelStyleChico
    )
    var imgPuzzle: Image

    var btBack: Button

    init {
        lbTextHelp.setWrap(true)
        lbTextHelp.setWidth((SCREEN_WIDTH - 20).toFloat())
        lbTextHelp.setPosition(
            SCREEN_WIDTH / 2f - lbTextHelp.getWidth() / 2f,
            660f
        )
        lbTextHelp.setScale(1.1f)

        imgPuzzle = Image(Assets.puzzleSolved)
        imgPuzzle.setSize(350f, 350f)
        imgPuzzle
            .setPosition(SCREEN_WIDTH / 2f - imgPuzzle.getWidth() / 2f, 200f)

        btBack = Button(Assets.btAtras)
        btBack.setSize(60f, 60f)
        btBack.setPosition(SCREEN_WIDTH / 2f - 30, 115f)
        addEfectoPress(btBack)
        btBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })
        stage!!.addActor(lbTextHelp)
        stage!!.addActor(btBack)
        stage!!.addActor(imgPuzzle)
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.fondo, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.end()
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
        }
        return super.keyDown(keycode)
    }
}
