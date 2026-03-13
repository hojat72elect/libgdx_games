package com.nopalsoft.impossibledial.screens

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.MainGame
import com.nopalsoft.impossibledial.scene2d.VentanaDificultad

class MainMenuScreen(game: MainGame) : Screens(game) {
    var titulo = Image(Assets.titulo)
    var circulo: Image
    var arrow: Image


    var btJugar: ImageButton

    var menuUI: Table
    var btRate: Button?
    var btLeaderboard: Button?
    var btAchievement: Button?
    var btFacebook: Button?

    var ventanaDificultad: VentanaDificultad = VentanaDificultad(this)

    init {
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 610f)

        circulo = Image(Assets.circle)
        circulo.setSize(325f, 325f)
        circulo.setPosition(SCREEN_WIDTH / 2f - circulo.getWidth() / 2f, 240f)

        arrow = Image(Assets.arrowBlue)
        arrow.setSize(8f, 150f)
        arrow.setPosition(SCREEN_WIDTH / 2f - arrow.getWidth() / 2f, 240 + circulo.getHeight() / 2f)
        arrow.setOrigin(4f, 0f)

        val run = Runnable {
            val rotation = MathUtils.random(180, 360).toFloat()
            arrow.addAction(Actions.sequence(Actions.rotateBy(rotation, 3f), Actions.rotateBy(-rotation, 3f)))
        }
        arrow.addAction(Actions.forever(Actions.sequence(Actions.run(run), Actions.delay(6f))))


        btJugar = ImageButton(ImageButtonStyle(Assets.btJugar, null, null, Assets.play, null, null))
        addEfectoPress(btJugar)
        btJugar.imageCell.padLeft(10f).size(47f, 54f) // Centro la imagen play con el pad, y le pongo el tamano
        btJugar.setSize(288f, 72f)
        btJugar.setPosition(SCREEN_WIDTH / 2f - btJugar.getWidth() / 2f, 120f)
        btJugar.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaDificultad.show(stage!!)
            }
        })

        btRate = Button(Assets.btRate)
        addEfectoPress(btRate!!)


        btLeaderboard = Button(Assets.btLeaderboard)
        addEfectoPress(btLeaderboard!!)

        btAchievement = Button(Assets.btAchievement)
        addEfectoPress(btAchievement!!)


        btFacebook = Button(Assets.btFacebook)
        addEfectoPress(btFacebook!!)


        menuUI = Table()
        menuUI.setSize(SCREEN_WIDTH.toFloat(), 70f)
        menuUI.setPosition(0f, 35f)
        menuUI.defaults().size(70f).expand()

        if (Gdx.app.getType() != ApplicationType.WebGL) {
            menuUI.add<Button?>(btRate)
            menuUI.add<Button?>(btLeaderboard)
            menuUI.add<Button?>(btAchievement)
        }
        menuUI.add<Button?>(btFacebook)

        stage!!.addActor(titulo)
        stage!!.addActor(circulo)
        stage!!.addActor(arrow)
        stage!!.addActor(btJugar)
        stage!!.addActor(menuUI)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.header!!, 0f, 780f, 480f, 20f)
        batcher!!.draw(Assets.header!!, 0f, 0f, 480f, 20f)
        batcher!!.end()
    }


    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            Gdx.app.exit()
            return true
        }
        return super.keyDown(keycode)
    }
}
