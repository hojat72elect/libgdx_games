package com.nopalsoft.lander.screens

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.dialogs.VentanaShop

class MainMenuScreen(game: MainLander) : Screens(game) {
    var btPlay = TextButton("Play", Assets.styleTextButtonMenu)
    var btSettings: TextButton? = null
    var btMore: TextButton? = null

    var dialogShop: VentanaShop

    init {

        val botonWidth = 300f
        val botonX = SCREEN_WIDTH / 2f - botonWidth / 2f


        btPlay.setSize(botonWidth, 100f)
        btPlay.setPosition(botonX, -10f)
        btPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreen(LevelScreen::class.java)
            }
        })

        btSettings = TextButton("Settings", Assets.styleTextButtonMenu)
        btSettings!!.setSize(botonWidth, 100f)
        btSettings!!.setPosition(botonX, -50f)
        btSettings!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                dialogShop.show(stage)
            }
        })

        btMore = TextButton("More", Assets.styleTextButtonMenu)
        btMore!!.setSize(botonWidth, 100f)
        btMore!!.setPosition(botonX, -90f)

        addActionToButtonEnter(btPlay, botonX, 420f)
        addActionToButtonEnter(btSettings!!, botonX, 300f)
        addActionToButtonEnter(btMore!!, botonX, 180f)

        val action = Actions.action(MoveToAction::class.java)
        action.interpolation = Interpolation.linear
        action.setPosition(5f, 540f)
        action.duration = .75f
        val scAction = Actions.action(ScaleToAction::class.java)
        scAction.interpolation = Interpolation.fade
        scAction.duration = 1f
        scAction.setScale(1f)
        val titulo = Image(Assets.titulo)
        titulo.setSize(447f, 225f)
        titulo.setPosition(5f, 1000f)
        titulo.setScale(.3f)
        titulo.addAction(Actions.parallel(action, scAction))

        stage.addActor(btPlay)
        stage.addActor(btSettings)
        stage.addActor(btMore)
        stage.addActor(titulo)

        dialogShop = VentanaShop(game)
    }

    fun addActionToButtonEnter(bt: TextButton, x: Float, y: Float) {
        val action = Actions.action(MoveToAction::class.java)
        action.interpolation = Interpolation.exp10Out
        action.setPosition(x, y)
        action.duration = .75f
        bt.addAction(action)
    }

    fun changeScreen(screen: Class<*>) {
        addActionToButtonLeave(btPlay, btPlay.getX(), -100f)
        addActionToButtonLeave(btSettings!!, btSettings!!.getX(), -100f)
        addActionToButtonLeave(btMore!!, btMore!!.getX(), -100f)

        stage.addAction(Actions.sequence(Actions.delay(.75f), Actions.run {
            if (screen == LevelScreen::class.java) {
                game.setScreen(LevelScreen(game))
            }
        }))
    }

    fun addActionToButtonLeave(bt: TextButton, x: Float, y: Float) {
        val action = Actions.action(MoveToAction::class.java)
        action.interpolation = Interpolation.exp10In
        action.setPosition(x, y)
        action.duration = .75f
        bt.addAction(action)
    }

    override fun draw(delta: Float) {
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)
        batcher.begin()
        batcher.disableBlending()
        batcher.draw(Assets.fondo, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher.end()
    }

    override fun update(delta: Float) {
    }
}
