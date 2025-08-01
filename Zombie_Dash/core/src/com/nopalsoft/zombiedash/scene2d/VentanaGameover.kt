package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.game.WorldGame
import com.nopalsoft.zombiedash.screens.MainMenuScreen
import com.nopalsoft.zombiedash.screens.Screens
import com.nopalsoft.zombiedash.shop.VentanaShop

class VentanaGameover(currentScreen: Screens) : Ventana(currentScreen, 450f, 410f, 30f, Assets.backgroundSmallWindow) {
    var btMenu: Button? = null
    var btShop: Button? = null
    var btTryAgain: Button? = null
    var btShare: TextButton

    var buttonSize: Int = 55

    var oWorld: WorldGame

    var ventanaShop: VentanaShop

    init {
        oWorld = (currentScreen as GameScreen).oWorld

        ventanaShop = VentanaShop(screen)

        val lbDistance = Label(idiomas!!.format("distance_num", oWorld.distance.toInt()), Assets.labelStyleGrande)
        lbDistance.setFontScale(1.5f)
        lbDistance.setAlignment(Align.center)
        lbDistance.setPosition(getWidth() / 2f - lbDistance.getWidth() / 2f, 310f)
        addActor(lbDistance)

        val lbBestDistance = Label(idiomas!!.format("best_distance", Settings.bestScore), Assets.labelStyleChico)
        lbBestDistance.setAlignment(Align.center)
        lbBestDistance.setPosition(getWidth() / 2f - lbBestDistance.getWidth() / 2f, 270f)
        addActor(lbBestDistance)

        initButtons()

        val tbButtons = Table()
        tbButtons.setSize(250f, 90f)
        tbButtons.setPosition(getWidth() / 2f - tbButtons.getWidth() / 2f, 180f)

        // content.debug();
        tbButtons.defaults().expandX().uniform()

        tbButtons.add<Button?>(btMenu)
        tbButtons.add<Button?>(btShop)
        tbButtons.add<Button?>(btTryAgain)

        addActor(tbButtons)

        val lbShare = Label(idiomas!!.format("share_on_Facebook", Settings.NUM_GEMS_SHARE_FACEBOOK), Assets.labelStyleChico)
        lbShare.setSize(getWidth() - 100, 120f)
        lbShare.setWrap(true)
        lbShare.setAlignment(Align.center)
        lbShare.setPosition(getWidth() / 2f - lbShare.getWidth() / 2f, 90f)

        btShare = TextButton(idiomas!!.get("share"), Assets.styleTextButtonShare)
        screen.addEfectoPress(btShare)
        btShare.setPosition(getWidth() / 2f - btShare.getWidth() / 2f, 50f)
        btShare.style.fontColor = Color.WHITE

        addActor(lbShare)
        addActor(btShare)
    }

    private fun initButtons() {
        btMenu = Button(Assets.btMenu)
        btMenu!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addEfectoPress(btMenu)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        btShop = Button(Assets.btShop)
        btShop!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addEfectoPress(btShop)
        btShop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaShop.show(screen.stage)
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        btTryAgain!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        screen.addEfectoPress(btTryAgain)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })
    }

}
