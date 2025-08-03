package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.game.GameScreen
import com.nopalsoft.ponyrace.menuobjetos.BotonNube
import com.nopalsoft.ponyrace.screens.BaseScreen
import com.nopalsoft.ponyrace.screens.LoadingScreen
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen

class VentanaPause(currentScreen: BaseScreen) : Ventana(currentScreen) {
    var gameScreen: GameScreen

    init {
        setSize(450f, 300f)
        setY(90f)
        setBackGround()

        gameScreen = currentScreen as GameScreen

        val lbTitle = Label(
            "Paused", LabelStyle(
                oAssetsHandler.fontGde,
                Color.WHITE
            )
        )
        lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 255f)

        val btResume = BotonNube(
            oAssetsHandler.nube!!, "Resume",
            oAssetsHandler.fontChco!!
        )
        btResume.setSize(150f, 100f)
        btResume.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 150f)
        btResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btResume.wasSelected = true
                btResume.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btResume.accionInicial, Actions.run {
                            hide()
                            gameScreen.setRunning()
                        }
                    )
                )
            }
        })

        val btTryAgain = BotonNube(
            oAssetsHandler.nube!!, "Try again",
            oAssetsHandler.fontChco!!
        )
        btTryAgain.setSize(150f, 100f)
        btTryAgain.setPosition(60f, 30f)
        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btTryAgain.wasSelected = true
                btTryAgain.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btTryAgain.accionInicial, Actions.run {
                            hide()
                            game.setScreen(
                                LoadingScreen(
                                    game,
                                    GameScreen::class.java,
                                    gameScreen.nivelTiled
                                )
                            )
                        }
                    )
                )
            }
        })

        val btMainMenu = BotonNube(
            oAssetsHandler.nube!!, "Menu",
            oAssetsHandler.fontChco!!
        )
        btMainMenu.setSize(150f, 100f)
        btMainMenu.setPosition(240f, 30f)
        btMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btMainMenu.wasSelected = true
                btMainMenu.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btMainMenu.accionInicial, Actions.run {
                            hide()
                            game.setScreen(
                                LoadingScreen(
                                    game,
                                    WorldMapTiledScreen::class.java
                                )
                            )
                            screen!!.dispose()
                        }
                    )
                )
            }
        })

        if (gameScreen.nivelTiled != 1000)  // Si es el mundo secreto no agrego el try again
            addActor(btTryAgain)

        addActor(btResume)
        addActor(btMainMenu)
        addActor(lbTitle)
    }
}
