package com.nopalsoft.zombiekiller.shop

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.scene2d.Dialog
import com.nopalsoft.zombiekiller.screens.Screens

class DialogShop(currentScreen: Screens) : Dialog(currentScreen, 650f, 450f, 20f, Assets.backgroundBigWindow) {
    var buttonPlay: Button? = null
    var buttonUpgrade: Button? = null
    var buttonGems: Button? = null
    var buttonNoAds: Button? = null

    var buttonSize: Int = 55

    var scroll: ScrollPane
    var containerTable: Table

    var labelCoins: Label

    init {
        setCloseButton(570f, 320f, 65f)

        val labelShop = Label(idiomas!!.get("shop"), Assets.labelStyleGrande)
        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 380f)
        labelShop.setFontScale(1.2f)
        addActor(labelShop)

        initButtons()

        val coinsTable = Table()
        coinsTable.setPosition(getWidth() / 2f - coinsTable.getWidth() / 2f, 365f)

        val imgGem = Image(Assets.itemGem)
        imgGem.setSize(20f, 20f)

        labelCoins = Label("x0", Assets.labelStyleChico)

        coinsTable.add<Image?>(imgGem).size(20f)
        coinsTable.add<Label?>(labelCoins).padLeft(5f)

        containerTable = Table()
        scroll = ScrollPane(containerTable, Assets.styleScrollPane)
        scroll.setFadeScrollBars(false)
        scroll.setSize(380f, 280f)
        scroll.setPosition(175f, 55f)
        scroll.variableSizeKnobs = false

        addActor(buttonPlay)
        addActor(buttonUpgrade)
        if (Gdx.app.type != ApplicationType.WebGL) { // En web no se muestran todos los botones
            addActor(buttonGems)
            addActor(buttonNoAds)
        }
        addActor(scroll)
        addActor(coinsTable)

        UpgradesSubMenu(containerTable, game)
    }

    override fun act(delta: Float) {
        super.act(delta)
        labelCoins.setText("x" + Settings.gemsTotal)
    }

    private fun initButtons() {
        buttonUpgrade = Button(Assets.btFire)
        buttonUpgrade!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonUpgrade!!.setPosition(100f, 270f)
        screen.addPressEffect(buttonUpgrade!!)
        buttonUpgrade!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UpgradesSubMenu(containerTable, game)
            }
        })

        buttonPlay = Button(Assets.btPlayer)
        buttonPlay!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonPlay!!.setPosition(100f, 205f)
        screen.addPressEffect(buttonPlay!!)
        buttonPlay!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PlayersSubMenu(containerTable, game)
            }
        })

        buttonGems = Button(Assets.btGems)
        buttonGems!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonGems!!.setPosition(100f, 140f)
        screen.addPressEffect(buttonGems!!)
        buttonGems!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                GetGemsSubMenu(screen.game!!, containerTable)
            }
        })

        buttonNoAds = Button(Assets.btMore)
        buttonNoAds!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonNoAds!!.setPosition(100f, 75f)
        screen.addPressEffect(buttonNoAds!!)
    }
}
