package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.game.GameScreen
import com.nopalsoft.ponyrace.menuobjetos.BotonNube
import com.nopalsoft.ponyrace.screens.BaseScreen
import com.nopalsoft.ponyrace.screens.LoadingScreen
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen

class VentanaTimesUp(currentScreen: BaseScreen) : Ventana(currentScreen) {
    var gameScreen: GameScreen

    init {
        setSize(450f, 300f)
        setY(90f)
        setBackGround()

        gameScreen = currentScreen as GameScreen

        val timeUp = Image(oAssetsHandler.timeUp)
        timeUp.setPosition(getWidth() / 2f - timeUp.getWidth() / 2f, 250f)
        addActor(timeUp)

        val btTryAgain = BotonNube(
            oAssetsHandler.nube, "Try again",
            oAssetsHandler.fontChco
        )
        btTryAgain.setSize(150f, 100f)
        btTryAgain.setPosition(
            getWidth() / 2f - btTryAgain.getWidth() / 2f,
            150f
        )
        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btTryAgain.wasSelected = true
                btTryAgain.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btTryAgain.accionInicial, Actions.run(object : Runnable {
                            override fun run() {
                                hide()
                                game.setScreen(
                                    LoadingScreen(
                                        game,
                                        GameScreen::class.java,
                                        gameScreen.nivelTiled
                                    )
                                )
                            }
                        })
                    )
                )
            }
        })

        val btMainMenu = BotonNube(
            oAssetsHandler.nube, "Menu",
            oAssetsHandler.fontChco
        )
        btMainMenu.setSize(150f, 100f)
        btMainMenu
            .setPosition(getWidth() / 2f - btMainMenu.getWidth() / 2f, 30f)
        btMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btMainMenu.wasSelected = true
                btMainMenu.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btMainMenu.accionInicial, Actions.run(object : Runnable {
                            override fun run() {
                                hide()
                                game.setScreen(
                                    LoadingScreen(
                                        game,
                                        WorldMapTiledScreen::class.java
                                    )
                                )
                                screen!!.dispose()
                            }
                        })
                    )
                )
            }
        })

        if (gameScreen.nivelTiled != 1000)  // Si es el mundo secreto no agrego el try again
            addActor(btTryAgain)

        addActor(btMainMenu)
        addActor(timeUp)
    }
}
