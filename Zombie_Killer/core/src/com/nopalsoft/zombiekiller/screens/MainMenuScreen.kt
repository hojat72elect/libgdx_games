package com.nopalsoft.zombiekiller.screens

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.scene2d.DialogSelectLevel
import com.nopalsoft.zombiekiller.shop.DialogShop

class MainMenuScreen(game: MainZombie) : Screens(game) {
    var buttonPlay: Button
    var buttonLeaderboard: Button
    var buttonAchievement: Button?
    var buttonFacebook: Button
    var buttonTwitter: Button
    var buttonSettings: Button
    var buttonShop: Button

    var buttonMusic: Button
    var buttonSound: Button

    var dialogShop: DialogShop
    var dialogSelectLevel: DialogSelectLevel

    var titleImage: Image

    init {
        music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/musicMenu.mp3"))
        music!!.isLooping = true
        if (Settings.isMusicOn) music!!.play()

        dialogShop = DialogShop(this)
        dialogSelectLevel = DialogSelectLevel(this)

        titleImage = Image(Assets.zombieKillerTitulo)
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f - 30, SCREEN_HEIGHT.toFloat())
        titleImage.setOrigin(titleImage.getWidth() / 2f, titleImage.getHeight() / 2f)
        titleImage.setScale(.85f)
        titleImage.addAction(
            Actions.parallel(
                Actions.moveTo(titleImage.getX(), SCREEN_HEIGHT - titleImage.getHeight(), .5f, Interpolation.swing),
                Actions.scaleTo(1f, 1f, .5f)
            )
        )

        buttonPlay = Button(Assets.btPlay)
        addPressEffect(buttonPlay)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                dialogSelectLevel.show(stage!!)
            }
        })

        buttonShop = Button(Assets.btShop)
        addPressEffect(buttonShop)
        buttonShop.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                dialogShop.show(stage!!)
            }
        })

        buttonLeaderboard = Button(Assets.btLeaderboard)
        addPressEffect(buttonLeaderboard)
        buttonLeaderboard.addListener(object : ClickListener() {
        })

        buttonAchievement = Button(Assets.btAchievement)
        addPressEffect(buttonAchievement!!)


        buttonSettings = Button(Assets.btSettings)
        addPressEffect(buttonSettings)
        buttonSettings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(SettingsScreen::class.java, game)
            }
        })

        val containerBt = Table()
        containerBt.setBackground(Assets.containerButtons)

        containerBt.defaults().size(100f).padBottom(40f).padLeft(10f).padRight(10f)
        containerBt.setSize(700f, 100f)
        containerBt.add<Button?>(buttonPlay)
        containerBt.add<Button?>(buttonShop)
        containerBt.add<Button?>(buttonLeaderboard)
        containerBt.add<Button?>(buttonAchievement)
        containerBt.add<Button?>(buttonSettings)


        containerBt.setPosition(SCREEN_WIDTH / 2f - containerBt.getWidth() / 2f, 0f)

        buttonFacebook = Button(Assets.btFacebook)
        buttonFacebook.setSize(50f, 50f)
        addPressEffect(buttonFacebook)
        buttonFacebook.setPosition((SCREEN_WIDTH - 55).toFloat(), (SCREEN_HEIGHT - 55).toFloat())

        buttonTwitter = Button(Assets.btTwitter)
        buttonTwitter.setSize(50f, 50f)
        addPressEffect(buttonTwitter)
        buttonTwitter.setPosition((SCREEN_WIDTH - 55).toFloat(), (SCREEN_HEIGHT - 120).toFloat())


        buttonMusic = Button(Assets.styleButtonMusic)
        buttonMusic.setSize(50f, 50f)
        buttonMusic.setPosition(5f, (SCREEN_HEIGHT - 55).toFloat())
        buttonMusic.setChecked(!Settings.isMusicOn)
        Gdx.app.log("Musica", Settings.isMusicOn.toString() + "")
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.setChecked(!Settings.isMusicOn)
                if (Settings.isMusicOn) music!!.play()
                else music!!.pause()
            }
        })

        buttonSound = Button(Assets.styleButtonSound)
        buttonSound.setSize(50f, 50f)
        buttonSound.setPosition(5f, (SCREEN_HEIGHT - 120).toFloat())
        buttonSound.setChecked(!Settings.isSoundOn)
        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                buttonSound.setChecked(!Settings.isSoundOn)
            }
        })

        stage!!.addActor(containerBt)
        if (Gdx.app.type != ApplicationType.WebGL) { // En web no se muestran todos los botones
            stage!!.addActor(buttonFacebook)
            stage!!.addActor(buttonTwitter)
        }
        stage!!.addActor(buttonSound)
        stage!!.addActor(buttonMusic)
        stage!!.addActor(titleImage)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.draw(Assets.moon, 450f, 220f, 350f, 255f)

        batcher!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (dialogSelectLevel.isVisible) dialogSelectLevel.hide()
            else if (dialogShop.isVisible) {
                dialogShop.hide()
            } else Gdx.app.exit()
            return true
        }
        return false
    }
}
