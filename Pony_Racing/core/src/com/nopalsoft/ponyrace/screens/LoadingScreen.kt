package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.graphics.Color
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.game.GameScreen

class LoadingScreen : BaseScreen {
    var clase: Class<*>? = null

    var cargaActual: Int = 0
    var nivelTiled: Int = 0

    constructor(game: PonyRacingGame, clase: Class<*>?, nivelTiled: Int) : super(game) {
        create(game, clase, nivelTiled)
    }

    constructor(game: PonyRacingGame, clase: Class<*>?) : super(game) {
        create(game, clase, -100)
    }

    fun create(game: PonyRacingGame?, clase: Class<*>?, nivelTiled: Int) {
        this.clase = clase
        this.game = game
        cargaActual = 0
        this.nivelTiled = nivelTiled

        when (clase) {
            MainMenuScreen::class.java -> {
                assetsHandler.loadMenus()
            }

            LeaderboardChooseScreen::class.java -> {
                assetsHandler.loadMenus()
            }

            WorldMapTiledScreen::class.java -> {
                assetsHandler.loadMenus()
            }

            ShopScreen::class.java -> {
                assetsHandler.loadMenus()
            }

            GameScreen::class.java -> {
                assetsHandler.loadGameScreenTiled(nivelTiled)
            }
        }
    }

    override fun update(delta: Float) {
        if (assetsHandler.update()) {
            when (clase) {
                MainMenuScreen::class.java -> {
                    assetsHandler.cargarMenus()
                    game!!.setScreen(MainMenuScreen(game!!))
                }

                LeaderboardChooseScreen::class.java -> {
                    assetsHandler.cargarMenus()
                    game!!.setScreen(LeaderboardChooseScreen(game!!))
                }

                WorldMapTiledScreen::class.java -> {
                    assetsHandler.cargarMenus()
                    game!!.setScreen(WorldMapTiledScreen(game!!))
                }

                ShopScreen::class.java -> {
                    assetsHandler.cargarMenus()
                    game!!.setScreen(ShopScreen(game!!))
                }

                GameScreen::class.java -> {
                    assetsHandler.cargarGameScreenTiled()
                    game!!.setScreen(GameScreen(game!!, nivelTiled))
                }
            }
        } else {
            cargaActual = (game!!.assetsHandler!!.getProgress() * 100).toInt()
        }
    }

    override fun draw(delta: Float) {
        camera!!.update()
        batch!!.setProjectionMatrix(camera!!.combined)

        batch!!.begin()
        assetsHandler.fontChco!!.color = Color.WHITE
        glyphLayout!!.setText(assetsHandler.fontChco, "%")
        assetsHandler.fontChco!!.draw(
            batch, "$cargaActual%", (SCREEN_WIDTH / 2f)
                    - (glyphLayout!!.width / 2), SCREEN_HEIGHT / 2f - glyphLayout!!.height / 2
        )
        batch!!.end()
    }

    override fun show() {
    }
}
