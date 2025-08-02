package com.salvai.snake.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.salvai.snake.SnakeIt
import com.salvai.snake.screens.helper.MyDialog
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.UrlController.openWebsite

class MenuScreen(private val game: SnakeIt) : ScreenAdapter() {
    private val width: Float
    private val height: Float

    @JvmField
    var exitDialog: MyDialog? = null

    @JvmField
    var playButton: Button? = null
    var websiteButton: TextButton? = null
    private var table: Table? = null


    init {
        width = game.worldWidth
        height = game.worldHeight

        game.stage.clear()
        game.setUpTopBar(Constants.SCREEN.MENU)


        setUpMainButtons()

        setUpExitDialog()
        setUpTable()

        //table.setDebug(true);
        game.stage.addActor(table)

        Gdx.input.isCatchBackKey = false

        setUpInputMultiplexer()

        game.stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }


    fun setUpExitDialog() {
        exitDialog = MyDialog("", game.skin, Label("Exit?", game.skin))
        exitDialog!!.getContentTable().padBottom(Constants.DIALOG_BUTTON_PAD.toFloat())
        val noButton = Button(game.skin, "no")
        val yesButton = Button(game.skin, "yes")
        noButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                exitDialog!!.hide()
                playButton!!.addAction(Actions.fadeIn(Constants.FADE_TIME))
                noButton.setChecked(false)
            }
        })


        yesButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.savePreferences()
                Gdx.app.exit()
            }
        })

        exitDialog!!.getButtonTable().add<Button?>(noButton)
        exitDialog!!.getButtonTable().add<Button?>(yesButton)
    }

    fun setUpInputMultiplexer() {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(game.stage)
        multiplexer.addProcessor(game.topBarStage)
        Gdx.input.inputProcessor = multiplexer
    }

    private fun setUpTable() {
        table = Table(game.skin)
        table!!.setSize(width, height * 0.8f)
        table!!.setPosition(0f, height * 0.1f)

        table!!.add<Button?>(playButton).spaceBottom(height * 0.15f).size(height * 0.35f)
        table!!.row()
        table!!.add<TextButton?>(websiteButton).height(height * 0.07f).width(width * 0.77f)
    }

    private fun setUpMainButtons() {
        playButton = Button(game.skin, "play")
        playButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(LevelChooseScreen(game))
                        dispose()
                    }
                })))
            }
        })

        websiteButton = TextButton("simondalvai.com", game.skin)
        websiteButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                openWebsite()
            }
        })
    }


    override fun render(delta: Float) {
        setupScreen()
        game.draw(delta)
        game.stage.act()
    }

    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
    }
}
