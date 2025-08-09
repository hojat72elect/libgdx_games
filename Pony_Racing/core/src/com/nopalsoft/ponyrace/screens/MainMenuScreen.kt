package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.menuobjetos.BotonNube

class MainMenuScreen(game: PonyRacingGame) : BaseScreen(game) {
    var btFacebook: ImageButton? = null
    var btSonido: ImageButton? = null
    var btMusica: ImageButton? = null
    var btJugar2: BotonNube? = null
    var btMore: BotonNube? = null
    var btLeaderBoard: BotonNube? = null

    init {
        cargarBotones()

        val actionLogoMenu = Actions.action(MoveToAction::class.java)
        actionLogoMenu.interpolation = Interpolation.swingOut
        actionLogoMenu.setPosition(235f, 270f)
        actionLogoMenu.duration = .9f

        val contenedor = Table()
        contenedor.setPosition(SCREEN_WIDTH / 2f, 140f)
        contenedor.add<BotonNube?>(btJugar2).fillX()
        contenedor.add().width(130f)
        contenedor.add<BotonNube?>(btMore).fillX()
        contenedor.row()
        contenedor.add<BotonNube?>(btLeaderBoard).colspan(3)

        stage.addActor(contenedor)
        stage.addActor(btSonido)
        stage.addActor(btMusica)

        assetsHandler.skeletonMenuTitle!!.setX(400f)
        assetsHandler.skeletonMenuTitle!!.setY(370f)
    }

    fun cargarBotones() {
        btJugar2 = BotonNube(assetsHandler.nube!!, "Play", assetsHandler.fontGde!!)
        btJugar2!!.setSize(200f, 130f)

        btJugar2!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btJugar2!!.wasSelected = true
                btJugar2!!.addAction(Actions.sequence(Actions.delay(.2f), btJugar2!!.accionInicial, Actions.run {
                    this@MainMenuScreen.game!!.setScreen(
                        LoadingScreen(
                            game!!,
                            WorldMapTiledScreen::class.java
                        )
                    )
                }))
            }
        })

        btMore = BotonNube(assetsHandler.nube!!, "More", assetsHandler.fontGde!!)
        btMore!!.setSize(200f, 130f)
        btMore!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btMore!!.wasSelected = true
                btMore!!.addAction(Actions.sequence(Actions.delay(.2f), btMore!!.accionInicial))
            }
        })

        btLeaderBoard = BotonNube(assetsHandler.nube!!, "LeaderBoards", assetsHandler.fontChco!!)
        btLeaderBoard!!.setSize(290f, 140f)
        btLeaderBoard!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btLeaderBoard!!.wasSelected = true
                btLeaderBoard!!.addAction(Actions.sequence(Actions.delay(.2f), btLeaderBoard!!.accionInicial, Actions.run {
                    game!!.setScreen(
                        LoadingScreen(
                            game!!,
                            LeaderboardChooseScreen::class.java
                        )
                    )
                }))
            }
        })

        btFacebook = ImageButton(assetsHandler.btnFacebook)
        btFacebook!!.setSize(50f, 50f)
        btFacebook!!.setPosition(750f, 0f)

        btSonido = ImageButton(assetsHandler.btSonidoOff, null, assetsHandler.btSonidoON)
        btSonido!!.setSize(60f, 60f)
        btSonido!!.setPosition(5f, 5f)
        btSonido!!.setChecked(Settings.isSoundOn)
        btSonido!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                super.clicked(event, x, y)
            }
        })

        btMusica = ImageButton(assetsHandler.btMusicaOff, null, assetsHandler.btMusicaON)
        btMusica!!.setSize(60f, 60f)
        btMusica!!.setPosition(70f, 2f)
        btMusica!!.setChecked(Settings.isMusicOn)
        btMusica!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                assetsHandler.playMusicMenus()
                super.clicked(event, x, y)
            }
        })
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        camera!!.update()
        batch!!.setProjectionMatrix(camera!!.combined)

        batch!!.disableBlending()
        batch!!.begin()
        batch!!.draw(assetsHandler.fondoMainMenu, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())

        batch!!.enableBlending()
        renderFlagTitle(delta)
        batch!!.end()

        stage.act(delta)
        stage.draw()

        // Table.drawDebug(stage);
    }

    private fun renderFlagTitle(delta: Float) {
        assetsHandler.animationMenuTitle!!.apply(assetsHandler.skeletonMenuTitle, screenLastStateTime, ScreenStateTime, true, null)
        assetsHandler.skeletonMenuTitle!!.updateWorldTransform()
        assetsHandler.skeletonMenuTitle!!.update(delta)
        skeletonRenderer!!.draw(batch!!, assetsHandler.skeletonMenuTitle!!)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit()
            return true
        }
        return false
    }

    override fun show() {
    }

    override fun hide() {
        super.hide()
    }
}
