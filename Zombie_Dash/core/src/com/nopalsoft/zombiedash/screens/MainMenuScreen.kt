package com.nopalsoft.zombiedash.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.shop.VentanaShop

class MainMenuScreen(game: MainZombieDash) : Screens(game) {
    var btPlay: Button
    var btLeaderboard: Button?
    var btAchievement: Button?
    var btFacebook: Button
    var btTwitter: Button
    var btSettings: Button
    var btShop: Button

    var btMusica: Button
    var btSonido: Button

    var ventanaShop: VentanaShop

    var titulo: Image

    init {
        music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/musicMenu.mp3"))
        music!!.isLooping = true
        if (Settings.isMusicOn) music!!.play()

        ventanaShop = VentanaShop(this)

        titulo = Image(Assets.zombieDashTitulo)

        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f + 10, SCREEN_HEIGHT.toFloat())
        titulo.setOrigin(titulo.getWidth() / 2f, titulo.getHeight() / 2f)
        titulo.setScale(.85f)
        titulo.addAction(Actions.parallel(Actions.moveTo(titulo.getX(), 135f, .5f, Interpolation.swing), Actions.scaleTo(1f, 1f, .5f)))

        val containerBt = Table()
        containerBt.setSize(700f, 100f)
        val imgBack = Image(Assets.containerButtons)
        imgBack.setSize(containerBt.getWidth(), containerBt.getHeight())
        containerBt.addActor(imgBack)
        containerBt.setPosition(SCREEN_WIDTH / 2f - containerBt.getWidth() / 2f, 0f)

        btPlay = Button(Assets.btPlay)
        addEfectoPress(btPlay)
        btPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        btShop = Button(Assets.btShop)
        addEfectoPress(btShop)
        btShop.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaShop.show(stage)
            }
        })

        btLeaderboard = Button(Assets.btLeaderboard)
        addEfectoPress(btLeaderboard!!)


        btAchievement = Button(Assets.btAchievement)
        addEfectoPress(btAchievement!!)

        btSettings = Button(Assets.btSettings)
        addEfectoPress(btSettings)
        btSettings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(SettingsScreen::class.java, game)
            }
        })

        containerBt.defaults().size(100f).padBottom(40f).padLeft(20f)
        containerBt.add<Button?>(btPlay)
        containerBt.add<Button?>(btShop)
        containerBt.add<Button?>(btLeaderboard)
        containerBt.add<Button?>(btAchievement)
        containerBt.add<Button?>(btSettings)

        btFacebook = Button(Assets.btFacebook)
        btFacebook.setSize(50f, 50f)
        addEfectoPress(btFacebook)
        btFacebook.setPosition((SCREEN_WIDTH - 55).toFloat(), (SCREEN_HEIGHT - 55).toFloat())

        btTwitter = Button(Assets.btTwitter)
        btTwitter.setSize(50f, 50f)
        addEfectoPress(btTwitter)
        btTwitter.setPosition((SCREEN_WIDTH - 55).toFloat(), (SCREEN_HEIGHT - 120).toFloat())


        btMusica = Button(Assets.styleButtonMusic)
        btMusica.setSize(50f, 50f)
        btMusica.setPosition(5f, (SCREEN_HEIGHT - 55).toFloat())
        btMusica.setChecked(!Settings.isMusicOn)
        Gdx.app.log("Musica", Settings.isMusicOn.toString() + "")
        btMusica.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                btMusica.setChecked(!Settings.isMusicOn)
                if (Settings.isMusicOn) music!!.play()
                else music!!.pause()
            }
        })

        btSonido = Button(Assets.styleButtonSound)
        btSonido.setSize(50f, 50f)
        btSonido.setPosition(5f, (SCREEN_HEIGHT - 120).toFloat())
        btSonido.setChecked(!Settings.isSoundOn)
        btSonido.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                btSonido.setChecked(!Settings.isSoundOn)
            }
        })

        stage.addActor(containerBt)
        stage.addActor(btFacebook)
        stage.addActor(btTwitter)
        stage.addActor(btSonido)
        stage.addActor(btMusica)
        stage.addActor(titulo)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        Assets.parallaxBackground.render(delta)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (ventanaShop.isVisible) {
                ventanaShop.hide()
            } else Gdx.app.exit()
            return true
        }
        return false
    }
}
