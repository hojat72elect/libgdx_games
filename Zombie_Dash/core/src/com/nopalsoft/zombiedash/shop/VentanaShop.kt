package com.nopalsoft.zombiedash.shop

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.scene2d.NumItemsBar
import com.nopalsoft.zombiedash.scene2d.Ventana
import com.nopalsoft.zombiedash.screens.Screens

class VentanaShop(currentScreen: Screens) : Ventana(currentScreen, 650f, 450f, 20f, Assets.backgroundBigWindow) {
    var btPersonajes: Button? = null
    var btMejoras: Button? = null
    var btGems: Button? = null
    var btNoAds: Button? = null

    var buttonSize: Int = 55

    var scroll: ScrollPane
    var contenedor: Table

    var numGemsBar: NumItemsBar
    var numBulletsBar: NumItemsBar

    init {
        setCloseButton(570f, 320f, 65f)

        val lbShop = Label(idiomas!!.get("shop"), Assets.labelStyleGrande)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 380f)
        lbShop.setFontScale(1.2f)
        addActor(lbShop)

        initButtons()

        numGemsBar = NumItemsBar(Assets.itemGem, 210f, 350f)
        numBulletsBar = NumItemsBar(Assets.weaponSmall, 350f, 350f)

        contenedor = Table()
        // contenedor.debug();
        scroll = ScrollPane(contenedor, Assets.styleScrollPane)
        scroll.setFadeScrollBars(false)
        scroll.setSize(380f, 280f)
        scroll.setPosition(175f, 55f)
        scroll.variableSizeKnobs = false

        addActor(btPersonajes)
        addActor(btMejoras)
        addActor(btGems)
        addActor(btNoAds)
        addActor(scroll)
        addActor(numGemsBar)
        addActor(numBulletsBar)

        UpgradesSubMenu(contenedor, game)
    }

    override fun act(delta: Float) {
        super.act(delta)
        numGemsBar.updateNumGems(Settings.gemsTotal)
        numBulletsBar.updateNumGems(Settings.numBullets)
    }

    private fun initButtons() {
        btMejoras = Button(Assets.btFire)
        btMejoras!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        btMejoras!!.setPosition(100f, 270f)
        screen.addEfectoPress(btMejoras!!)
        btMejoras!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UpgradesSubMenu(contenedor, game)
            }
        })

        btPersonajes = Button(Assets.btPlayer)
        btPersonajes!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        btPersonajes!!.setPosition(100f, 205f)
        screen.addEfectoPress(btPersonajes!!)
        btPersonajes!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PersonajesSubMenu(contenedor, game)
            }
        })

        btGems = Button(Assets.btGems)
        btGems!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        btGems!!.setPosition(100f, 140f)
        screen.addEfectoPress(btGems!!)
        btGems!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                GetGemsSubMenu(screen.game!!, contenedor)
            }
        })

        btNoAds = Button(Assets.btMore)
        btNoAds!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        btNoAds!!.setPosition(100f, 75f)
        screen.addEfectoPress(btNoAds!!)
    }
}
