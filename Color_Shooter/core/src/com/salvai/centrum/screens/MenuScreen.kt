package com.salvai.centrum.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.enums.GameType
import com.salvai.centrum.utils.Constants

class MenuScreen(private val game: CentrumGameClass) : ScreenAdapter() {
    private val stage: Stage
    private var table: Table? = null
    private val highScoreLabel: Label
    private val height: Float
    private val width: Float

    //buttons
    private var playButton: Button? = null
    private var soundButton: Button? = null
    private var highscoreButton: Button? = null

    init {
        height = Gdx.graphics.getHeight() * 0.8f
        width = Gdx.graphics.getWidth() * 0.8f

        stage = Stage()

        setUpMainButtons()
        setUpSettingButtons()

        highScoreLabel = Label("BEST " + game.highScore, game.skin, "default")
        highScoreLabel.setAlignment(Align.center)

        setUpTable()

        //table.setDebug(true);
        stage.addActor(table)
        Gdx.input.setInputProcessor(stage)

        stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    private fun setUpTable() {
        table = Table(game.skin)
        table!!.setSize(width, height)
        table!!.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f)
        table!!.defaults().expandX()

        table!!.add<Label?>(highScoreLabel).colspan(4).height(height * 0.1f).spaceBottom(height * 0.2f).fillX()
        table!!.row()
        table!!.add<Button?>(playButton).colspan(4).spaceBottom(height * 0.25f).width(height * 0.3f).height(height * 0.3f)
        table!!.row()
        table!!.add<Button?>(soundButton).width(height * 0.1f).height(height * 0.1f)
        table!!.add<Button?>(highscoreButton).width(height * 0.1f).height(height * 0.1f)
    }

    private fun setUpMainButtons() {
        playButton = Button(game.skin, "play")
        playButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.gameType = GameType.LEVEL
                        game.setScreen(LevelChooseScreen(game))
                        dispose()
                    }
                })))
            }
        })
    }

    private fun setUpSettingButtons() {
        soundButton = Button(game.skin, "sound")
        soundButton!!.setChecked(!game.soundOn)
        soundButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.soundOn = !game.soundOn
                soundButton!!.setChecked(!game.soundOn)
                game.savePreferences()
            }
        })
        highscoreButton = Button(game.skin, "highscore")
    }


    override fun render(delta: Float) {
        setupScreen()
        game.batch.begin()
        game.drawBackground(delta)
        game.batch.end()
        stage.act(delta)
        stage.draw()
    }

    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


    override fun dispose() {
        stage.dispose()
    }
}
