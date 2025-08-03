package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.GameScreen
import com.nopalsoft.ponyrace.screens.BaseScreen
import com.nopalsoft.ponyrace.screens.LoadingScreen
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen

class VentanaNextLevel(currentScreen: BaseScreen) : Ventana(currentScreen) {
    var gameScreen: GameScreen

    var lbCoinsNum: Label
    var lbTimeLeftNum: Label

    init {
        setSize(600f, 370f)
        setY(50f)
        setBackGround()

        gameScreen = currentScreen as GameScreen

        val trofeo = Image(oAssetsHandler.medallaPrimerLugar)
        trofeo.setScale(.9f)
        trofeo.setPosition(0f, getHeight() / 2f - trofeo.getHeight() / 2f)
        addActor(trofeo)

        val congratulations = Image(oAssetsHandler.congratulations)
        congratulations.setPosition(getWidth() / 2f - congratulations.getWidth() / 2f, 300f)
        addActor(congratulations)

        val content = Table()
        content.setSize(320f, 200f)
        content.setPosition(280f, 70f)

        // content.debug();
        val lbLapTime = Label("Lap time", LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        val lbLapTimeNum = Label(gameScreen.lapTime, LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        val lbTimeLeft = Label("Time left", LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        lbTimeLeftNum = Label("", LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        val lbCoins = Label("Coins", LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        lbCoinsNum = Label("", LabelStyle(oAssetsHandler.fontChco, Color.WHITE))

        val lbShare = Label(
            "Share your time and get " + Settings.MONEDAS_REGALO_SHARE_FACEBOOK + " coins", LabelStyle(
                oAssetsHandler.fontChco,
                Color.WHITE
            )
        )
        lbShare.setAlignment(Align.center)
        lbShare.setWrap(true)

        val btShare = TextButton(
            "Share", TextButtonStyle(
                oAssetsHandler.btShareFacebookUp, oAssetsHandler.btShareFacebookDown, null,
                oAssetsHandler.fontChco
            )
        )
        btShare.setPosition(390f, 15f)
        btShare.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                game.setScreen(LoadingScreen(game, WorldMapTiledScreen::class.java))
                screen!!.dispose()
            }
        })

        content.row()
        content.add<Label?>(lbLapTime).left()
        content.add<Label?>(lbLapTimeNum).expand()

        content.row()
        content.add<Label?>(lbTimeLeft).left()
        content.add<Label?>(lbTimeLeftNum).expand()

        content.row()
        content.add<Label?>(lbCoins).left()
        content.add<Label?>(lbCoinsNum).expand()

        content.row().colspan(2)
        content.add<Label?>(lbShare).expand().fill().center()

        addActor(content)
        addActor(btShare)

        if (Gdx.app.type == ApplicationType.iOS) {
            lbShare.isVisible = false
            btShare.isVisible = false
        }
    }

    override fun act(delta: Float) {
        lbCoinsNum.setText(gameScreen.stringMonedasRecolectadas)
        lbTimeLeftNum.setText(gameScreen.stringTiempoLeft)
        super.act(delta)
    }
}
