package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.screens.MainMenuScreen
import com.nopalsoft.zombiekiller.screens.Screens
import com.nopalsoft.zombiekiller.shop.DialogShop

class DialogGameover(currentScreen: Screens) : Dialog(currentScreen, 350f, 310f, 100f, Assets.backgroundSmallWindow) {
    var btMenu: Button? = null
    var btShop: Button? = null
    var btTryAgain: Button? = null

    var buttonSize: Int = 55

    var ventanaShop: DialogShop

    init {
        ventanaShop = DialogShop(screen)

        val lbShop = Label(idiomas!!.get("game_over"), Assets.labelStyleGrande)
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
        content.add<Button?>(btShop)
        content.add<Button?>(btTryAgain)

        addActor(content)
    }

    private fun initButtons() {
        btMenu = Button(Assets.btMenu)
        btMenu!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addPressEffect(btMenu)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        btShop = Button(Assets.btShop)
        btShop!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addPressEffect(btShop)
        btShop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaShop.show(screen.stage)
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        btTryAgain!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addPressEffect(btTryAgain)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, (screen as GameScreen).level, game)
            }
        })
    }

    override fun show(stage: Stage) {
        super.show(stage)
    }
}
