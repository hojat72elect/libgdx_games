package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.screens.BaseScreen

class VentanaRate(currentScreen: BaseScreen) : Ventana(currentScreen) {
    init {
        setSize(450f, 280f)
        setY(90f)
        setBackGround()

        val lbTitle = Label(
            "Support this game", LabelStyle(
                oAssetsHandler.fontGde, Color.WHITE
            )
        )
        lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 235f)

        val lbContenido = Label(
            "Hello, thank you for playing Pony racing.\nHelp us to support this game. Just rate us at the app store.",
            game.assetsHandler!!.skin
        )
        lbContenido.setSize(getWidth() - 20, 170f)
        lbContenido.setPosition(
            getWidth() / 2f - lbContenido.getWidth() / 2f,
            70f
        )
        lbContenido.setWrap(true)

        val btRate = TextButton("Rate", game.assetsHandler!!.skin)
        btRate.label.setWrap(true)
        btRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        val btNotNow = TextButton("Not now", game.assetsHandler!!.skin)
        btNotNow.label.setWrap(true)
        btNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        val tbBotones = Table()
        tbBotones.setSize(getWidth() - 20, 40f)
        tbBotones.setPosition(getWidth() / 2f - tbBotones.getWidth() / 2f, 10f)

        tbBotones.defaults().uniform().expandX().center().fill().pad(10f)
        tbBotones.add<TextButton?>(btRate)
        tbBotones.add<TextButton?>(btNotNow)

        addActor(lbContenido)
        addActor(tbBotones)
        addActor(lbTitle)
    }
}
