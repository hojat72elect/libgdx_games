package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.screens.Screens

class VentanaRevive(currentScreen: Screens, var priceRevive: Int) : Ventana(currentScreen, 250f, 250f, 100f, Assets.backgroundSmallWindow) {
    var btOK: TextButton? = null
    var btNo: TextButton? = null
    var totalGems: NumItemsBar?

    var gameScreen: GameScreen

    init {
        gameScreen = currentScreen as GameScreen

        totalGems = NumItemsBar(Assets.itemGem, 150f, 220f)
        totalGems!!.updateNumGems(Settings.gemsTotal)
        addActor(totalGems)

        val lbShop = Label(idiomas!!.get("revive"), Assets.labelStyleGrande)
        lbShop.setFontScale(1f)
        lbShop.setAlignment(Align.center)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 170f)
        addActor(lbShop)

        val tbGemsPrice = Table()
        tbGemsPrice.add<Image?>(Image(Assets.itemGem)).size(25f)
        tbGemsPrice.add<Label?>(Label("x" + priceRevive, Assets.labelStyleChico)).pad(5f).center()

        tbGemsPrice.pack()
        tbGemsPrice.setPosition(getWidth() / 2f - tbGemsPrice.getWidth() / 2f, 120f)
        addActor(tbGemsPrice)

        initButtons()

        val content = Table()
        content.setSize(getWidth() - 50, 90f)
        content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 30f)

        // content.debug();
        content.defaults().expandX().uniform().pad(5f, 5f, 5f, 5f).fill()

        content.add<TextButton?>(btOK).height(50f)
        content.add<TextButton?>(btNo).height(50f)

        addActor(content)
    }

    private fun initButtons() {
        btOK = TextButton(idiomas!!.get("OK"), Assets.styleTextButtonBuy)
        screen.addEfectoPress(btOK)
        btOK!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                gameScreen.setRevive()
                // Ya revise antes de poner el dialogo que si se pudiera hacer esta resta
                Settings.gemsTotal -= priceRevive
            }
        })

        btNo = TextButton(idiomas!!.get("no"), Assets.styleTextButtonBuy)
        screen.addEfectoPress(btNo)
        btNo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                gameScreen.setGameover()
            }
        })
    }

}
