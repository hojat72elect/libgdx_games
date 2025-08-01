package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.screens.MainMenuScreen
import com.nopalsoft.zombiedash.screens.Screens

class VentanaPause(currentScreen: Screens) : Ventana(currentScreen, 350f, 310f, 100f, Assets.backgroundSmallWindow) {
    var gameScreen: GameScreen

    var btMenu: Button? = null
    var btTryAgain: Button? = null

    var buttonSize: Int = 55

    init {
        setCloseButton(305f, 265f, 45f)

        gameScreen = currentScreen as GameScreen

        val lbShop = Label(idiomas!!.get("pause"), Assets.labelStyleGrande)
        lbShop.setFontScale(1.5f)
        lbShop.setAlignment(Align.center)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 210f)
        addActor(lbShop)

        initButtons()

        val content = Table()
        content.setSize(250f, 90f)
        content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 80f)

        // content.debug();
        content.defaults().expandX().uniform()

        content.add<Button?>(btMenu)

        content.add<Button?>(btTryAgain)

        addActor(content)
    }

    private fun initButtons() {
        btMenu = Button(Assets.btMenu)
        btMenu!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addEfectoPress(btMenu!!)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        btTryAgain!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addEfectoPress(btTryAgain!!)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })
    }

    override fun hide() {
        gameScreen.setRunning()
        super.hide()
    }
}
