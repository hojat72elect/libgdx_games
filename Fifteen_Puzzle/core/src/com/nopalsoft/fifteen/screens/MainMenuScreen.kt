package com.nopalsoft.fifteen.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.Assets.pauseMusic
import com.nopalsoft.fifteen.Assets.playMusic
import com.nopalsoft.fifteen.MainFifteen
import com.nopalsoft.fifteen.Settings
import com.nopalsoft.fifteen.game.GameScreen

class MainMenuScreen(game: MainFifteen) : Screens(game) {
    var imgTitulo: Image?

    var lbPlay: Label
    var lbHelp: Label
    var lbLeaderboard: Label
    var lbRate: Label

    var btMusica: Button
    var btSonido: Button
    var btFacebook: Button

    init {
        imgTitulo = Image(Assets.titulo)
        imgTitulo!!.setPosition(
            SCREEN_WIDTH / 2f - imgTitulo!!.getWidth() / 2f,
            630f
        )

        lbPlay = Label("Play", Assets.labelStyleGrande)
        lbPlay.setPosition(SCREEN_WIDTH / 2f - lbPlay.getWidth() / 2f, 450f)
        addEfectoPress(lbPlay)
        lbPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        // Help
        lbHelp = Label("Help", Assets.labelStyleGrande)
        lbHelp.setPosition(SCREEN_WIDTH / 2f - lbHelp.getWidth() / 2f, 350f)
        addEfectoPress(lbHelp)
        lbHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(HelpScreen::class.java, game)
            }
        })

        // Rate
        lbRate = Label("Rate", Assets.labelStyleGrande)
        lbRate.setPosition(SCREEN_WIDTH / 2f - lbRate.getWidth() / 2f, 250f)
        addEfectoPress(lbRate)

        // Leaderboard
        lbLeaderboard = Label("Leaderboard", Assets.labelStyleGrande)
        lbLeaderboard.setFontScale(.85f)
        lbLeaderboard.setPosition(
            SCREEN_WIDTH / 2f - lbLeaderboard.getWidth()
                    / 2f, 150f
        )
        lbLeaderboard.setAlignment(Align.center)

        addEfectoPress(lbLeaderboard)

        btMusica = Button(Assets.styleButtonMusica)
        btMusica.setPosition(5f, 5f)
        btMusica.setChecked(!Settings.isMusicOn)
        Gdx.app.log("Musica", Settings.isMusicOn.toString() + "")
        btMusica.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                btMusica.setChecked(!Settings.isMusicOn)
                if (Settings.isMusicOn) playMusic()
                else pauseMusic()
            }
        })

        btSonido = Button(Assets.styleButtonSonido)
        btSonido.setPosition(75f, 5f)
        btSonido.setChecked(!Settings.isSoundOn)
        btSonido.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                btSonido.setChecked(!Settings.isSoundOn)
            }
        })

        btFacebook = Button(Assets.btFacebook)
        btFacebook.setSize(50f, 50f)
        btFacebook.setPosition(SCREEN_WIDTH - btFacebook.getWidth() - 5, 10f)
        addEfectoPress(btFacebook)

        stage!!.addActor(imgTitulo)
        stage!!.addActor(lbPlay)
        stage!!.addActor(lbHelp)
        stage!!.addActor(lbLeaderboard)
        stage!!.addActor(lbRate)
        stage!!.addActor(btMusica)
        stage!!.addActor(btSonido)
        stage!!.addActor(btFacebook)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.fondo, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }
}
