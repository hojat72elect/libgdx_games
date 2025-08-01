package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.screens.Screens

class VentanaRate(currentScreen: Screens) : Ventana(currentScreen, 400f, 310f, 100f, Assets.backgroundSmallWindow) {
    var btYes: TextButton? = null
    var btLater: TextButton? = null

    init {
        val lbShop = Label(idiomas!!.get("support_this_game"), Assets.labelStyleGrande)
        lbShop.setFontScale(1.2f)
        lbShop.setAlignment(Align.center)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 230f)
        addActor(lbShop)

        val lbContent = Label(idiomas!!.get("support_description"), Assets.labelStyleChico)
        lbContent.setWrap(true)
        lbContent.setWidth(getWidth() - 60)
        lbContent.setPosition(35f, 150f)
        addActor(lbContent)

        initButtons()

        val content = Table()
        content.setSize(375f, 90f)
        content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 40f)

        // content.debug();
        content.defaults().expandX().uniform()

        content.add<TextButton?>(btYes)
        content.add<TextButton?>(btLater)

        addActor(content)
    }

    private fun initButtons() {
        btYes = TextButton(idiomas!!.get("OK"), Assets.styleTextButtonPurchased)
        screen.addEfectoPress(btYes)
        btYes!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        btLater = TextButton(idiomas!!.get("not_now"), Assets.styleTextButtonPurchased)
        screen.addEfectoPress(btLater)
        btLater!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })
    }

}
