package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.ponyrace.game.GameScreen
import com.nopalsoft.ponyrace.game.TileMapHandler
import com.nopalsoft.ponyrace.screens.BaseScreen

class VentanaTryAgain(currentScreen: BaseScreen) : Ventana(currentScreen) {
    var gameScreen: GameScreen
    var oWorld: TileMapHandler

    var lbCoinsNum: Label
    var lbTimeLeftNum: Label

    init {
        setSize(460f, 320f)
        setY(60f)
        setBackGround()

        gameScreen = currentScreen as GameScreen
        oWorld = gameScreen.world

        var medalla: Image? = null
        if (oWorld.oPony.lugarEnLaCarrera == 2) medalla = Image(oAssetsHandler.medallaSegundoLugar)
        else if (oWorld.oPony.lugarEnLaCarrera == 3) medalla = Image(oAssetsHandler.medallaTercerLugar)

        val content = Table()
        content.setSize(320f, 180f)
        if (medalla != null) {
            content.setPosition(140f, 70f)
            medalla.setScale(.9f)
            medalla.setPosition(15f, getHeight() / 2f - medalla.getHeight() / 2f)
            addActor(medalla)
        } else {
            val youLose = Image(oAssetsHandler.youLose)
            youLose.setPosition(getWidth() / 2f - youLose.getWidth() / 2f, 250f)
            addActor(youLose)

            content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50f)
        }

        // content.debug();
        val lbLapTime = Label(
            "Lap time", LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE
            )
        )

        val lbLapTimeNum = Label(
            gameScreen.lapTime, LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE
            )
        )

        val lbTimeLeft = Label(
            "Time left", LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE
            )
        )

        lbTimeLeftNum = Label(
            "", LabelStyle(
                oAssetsHandler.fontChco,
                Color.WHITE
            )
        )

        val lbCoins = Label(
            "Coins", LabelStyle(
                oAssetsHandler.fontChco,
                Color.WHITE
            )
        )

        lbCoinsNum = Label(
            "",
            LabelStyle(oAssetsHandler.fontChco, Color.WHITE)
        )

        content.row()
        content.add<Label?>(lbLapTime).left()
        content.add<Label?>(lbLapTimeNum).expand()

        content.row()
        content.add<Label?>(lbTimeLeft).left()
        content.add<Label?>(lbTimeLeftNum).expand()

        content.row()
        content.add<Label?>(lbCoins).left()
        content.add<Label?>(lbCoinsNum).expand()

        addActor(content)
    }

    override fun act(delta: Float) {
        lbCoinsNum.setText(gameScreen.stringMonedasRecolectadas)
        lbTimeLeftNum.setText(gameScreen.stringTiempoLeft)
        super.act(delta)
    }
}
