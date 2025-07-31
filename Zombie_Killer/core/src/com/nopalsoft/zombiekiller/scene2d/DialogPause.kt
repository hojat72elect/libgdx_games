package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.screens.MainMenuScreen
import com.nopalsoft.zombiekiller.screens.Screens

class DialogPause(currentScreen: Screens) : Dialog(currentScreen, 350f, 310f, 100f, Assets.backgroundSmallWindow) {
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

        content.defaults().expandX().uniform()

        content.add<Button?>(btMenu)

        content.add<Button?>(btTryAgain)

        addActor(content)
    }

    private fun initButtons() {
        btMenu = Button(Assets.btMenu)
        btMenu!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addPressEffect(btMenu!!)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        btTryAgain!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addPressEffect(btTryAgain!!)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, gameScreen.level, game)
            }
        })
    }

    override fun hide() {
        gameScreen.setRunning()
        super.hide()
    }
}
