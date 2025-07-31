package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.game.WorldGame
import com.nopalsoft.zombiekiller.screens.Screens
import com.nopalsoft.zombiekiller.shop.DialogShop

class DialogNextLevel(currentScreen: Screens) : Dialog(currentScreen, 380f, 390f, 50f, Assets.backgroundSmallWindow) {
    var oWorld: WorldGame

    var btLevels: Button? = null
    var btShop: Button? = null
    var btTryAgain: Button? = null
    var buttonSize: Int = 55
    var ventanaShop: DialogShop
    var ventanaSelectLevel: DialogSelectLevel

    var skulls: Int

    init {
        oWorld = (currentScreen as GameScreen).worldGame
        ventanaShop = DialogShop(screen)
        ventanaSelectLevel = DialogSelectLevel(screen)

        skulls = oWorld.skulls

        val lbShop = Label(idiomas!!.get("congratulations"), Assets.labelStyleGrande)
        lbShop.setFontScale(1.3f)
        lbShop.setAlignment(Align.center)
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 300f)
        addActor(lbShop)

        initButtons()

        val tableSkulls = Table()
        tableSkulls.setSize(210f, 60f)
        tableSkulls.setPosition(getWidth() / 2f - tableSkulls.getWidth() / 2f, 225f)
        tableSkulls.defaults().expandX().uniform()

        for (i in 1..3) {
            val imageSkull = Image(Assets.upgradeOff)
            if (skulls >= i) imageSkull.setDrawable(TextureRegionDrawable(Assets.itemSkull))
            tableSkulls.add<Image?>(imageSkull).size(55f)
        }

        val tableStatistics = Table()
        tableStatistics.setSize(getWidth(), 95f)
        tableStatistics.setPosition(0f, 130f)

        val labelZombiesKilled = Label(idiomas!!.get("zombies_killed"), Assets.labelStyleChico)
        val labelZombiesKilledNum = Label(oWorld.totalZombiesKilled.toString() + "/" + WorldGame.TOTAL_ZOMBIES_LEVEL, Assets.labelStyleChico)

        val labelGemsCollected = Label(idiomas!!.get("gems"), Assets.labelStyleChico)
        val labelGemsCollectedNum = Label(oWorld.gems.toString() + "", Assets.labelStyleChico)

        val labelBonus = Label(idiomas!!.get("bonus_gems"), Assets.labelStyleChico)
        val labelBonusNum = Label(oWorld.bonus.toString() + "", Assets.labelStyleChico)

        tableStatistics.defaults().pad(0f, 45f, 0f, 45f).expandX()

        tableStatistics.add<Label?>(labelZombiesKilled).left()
        tableStatistics.add<Label?>(labelZombiesKilledNum).right()

        tableStatistics.row()
        tableStatistics.add<Label?>(labelGemsCollected).left()
        tableStatistics.add<Label?>(labelGemsCollectedNum).right()

        tableStatistics.row()
        tableStatistics.add<Label?>(labelBonus).left()
        tableStatistics.add<Label?>(labelBonusNum).right()

        // Buttons
        val tableButtons = Table()
        tableButtons.setSize(250f, 90f)
        tableButtons.setPosition(getWidth() / 2f - tableButtons.getWidth() / 2f, 40f)

        tableButtons.defaults().expandX().uniform()

        tableButtons.add<Button?>(btShop)
        tableButtons.add<Button?>(btTryAgain)
        tableButtons.add<Button?>(btLevels)

        addActor(tableButtons)
        addActor(tableSkulls)
        addActor(tableStatistics)
    }

    private fun initButtons() {
        btLevels = Button(Assets.btRight)
        btLevels!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addPressEffect(btLevels!!)
        btLevels!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaSelectLevel.show(screen.stage!!)
            }
        })

        btShop = Button(Assets.btShop)
        btShop!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addPressEffect(btShop!!)
        btShop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaShop.show(screen.stage!!)
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        btTryAgain!!.setSize(buttonSize.toFloat(), buttonSize.toFloat())

        screen.addPressEffect(btTryAgain!!)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, (screen as GameScreen).level, game)
            }
        })
    }

}
