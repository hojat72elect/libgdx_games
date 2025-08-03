package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.menuobjetos.BotonNube

class LeaderboardChooseScreen(game: PonyRacingGame) : BaseScreen(game) {
    var btLeaderBoard: BotonNube? = null
    var btAchievements: BotonNube? = null
    var btBack: BotonNube? = null
    var btSignOut: TextButton? = null

    init {
        cargarBotones()

        val contenedor = Table()
        contenedor.setPosition(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f)
        contenedor.add<BotonNube?>(btLeaderBoard).fillX()
        contenedor.row().pad(50f, 0f, 0f, 0f)
        contenedor.add<BotonNube?>(btAchievements).fillX()

        stage.addActor(contenedor)
    }

    private fun cargarBotones() {
        btLeaderBoard = BotonNube(
            assetsHandler.nube!!, "LeaderBoards",
            assetsHandler.fontChco!!
        )
        btLeaderBoard!!.setSize(290f, 140f)
        btLeaderBoard!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btLeaderBoard!!.wasSelected = true
                btLeaderBoard!!.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btLeaderBoard!!.accionInicial
                    )
                )
            }
        })

        btAchievements = BotonNube(
            assetsHandler.nube!!, "Achievements",
            assetsHandler.fontChco!!
        )
        btAchievements!!.setSize(290f, 140f)
        btAchievements!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btAchievements!!.wasSelected = true
                btAchievements!!.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btAchievements!!.accionInicial
                    )
                )
            }
        })

        btBack = BotonNube(assetsHandler.nube!!, "Back", assetsHandler.fontGde!!)
        btBack!!.setSize(150f, 100f)
        btBack!!.setPosition(645f, 5f)
        btBack!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btBack!!.wasSelected = true
                btBack!!.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btBack!!.accionInicial, Actions.run(object : Runnable {
                            override fun run() {
                                this@LeaderboardChooseScreen.game!!
                                    .setScreen(
                                        LoadingScreen(
                                            this@LeaderboardChooseScreen.game!!,
                                            MainMenuScreen::class.java
                                        )
                                    )
                            }
                        })
                    )
                )
            }
        })

        val stilo = TextButtonStyle(
            assetsHandler.btSignInUp,
            assetsHandler.btSignInDown, null,
            assetsHandler.skin!!.getFont("default-font")
        )
        btSignOut = TextButton("Sign out", stilo)
        btSignOut!!.setPosition(5f, 5f)
        btSignOut!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                this@LeaderboardChooseScreen.game!!
                    .setScreen(
                        LoadingScreen(
                            this@LeaderboardChooseScreen.game!!,
                            MainMenuScreen::class.java
                        )
                    )
            }
        })


        stage.addActor(btSignOut)
        stage.addActor(btBack)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        camera!!.update()
        batch!!.setProjectionMatrix(camera!!.combined)

        batch!!.disableBlending()
        batch!!.begin()
        batch!!.draw(assetsHandler.fondoMainMenu, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.end()

        stage.act(delta)
        stage.draw()
    }

    override fun show() {
        // TODO Auto-generated method stub
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            this.game!!.setScreen(LoadingScreen(game!!, MainMenuScreen::class.java))

            return true
        }
        return false
    }
}
