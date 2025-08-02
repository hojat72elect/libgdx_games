package com.salvai.snake.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.salvai.snake.SnakeIt
import com.salvai.snake.input.CatchBackKeyProcessor
import com.salvai.snake.screens.helper.PreviewSnake
import com.salvai.snake.screens.helper.SpeedChooser
import com.salvai.snake.utils.Constants

class SettingsScreen(var game: SnakeIt) : ScreenAdapter() {
    //Snake colors
    private val snakeTexture: Texture?
    private val previewSnakes: Array<PreviewSnake>
    private val speedChooser: SpeedChooser
    var width: Float
    var height: Float
    private var table: Table? = null
    private var soundButton: Button? = null
    private var vibrationButton: Button? = null


    init {
        width = game.worldWidth
        height = game.worldHeight


        previewSnakes = Array<PreviewSnake>()
        snakeTexture = game.assetsManager!!.manager.get<Texture?>(Constants.BLOCK_IMAGE_NAME, Texture::class.java)

        game.stage!!.clear()
        game.setUpTopBar(Constants.SCREEN.SETTINGS)
        speedChooser = SpeedChooser(game)

        setUpButtons()
        setUpTable()
        setUpSnakeTable()

        game.stage!!.addActor(table)

        setUpInputMultiplexer()
        game.stage!!.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    fun setUpInputMultiplexer() {
        Gdx.input.isCatchBackKey = true
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(game.stage)
        multiplexer.addProcessor(game.topBarStage)
        multiplexer.addProcessor(CatchBackKeyProcessor(game, this))
        Gdx.input.inputProcessor = multiplexer
    }


    private fun setUpTable() {
        table = Table(game.skin)
        table!!.setSize(width * 0.8f, height * 0.8f)
        table!!.setPosition(width * 0.1f, height * 0.1f)
        table!!.defaults().space(width * 0.025f).pad(40f).fill()

        table!!.add<Button?>(soundButton).size(height * 0.12f).colspan(3)
        table!!.add<Button?>(vibrationButton).size(height * 0.12f).colspan(3)
        table!!.row()
        table!!.add<Slider?>(speedChooser.slider).colspan(6).height(height * 0.14f)
    }

    private fun setUpSnakeTable() {
        val snakeTable = Table(game.skin)
        snakeTable.defaults().size(width * 0.07f).spaceBottom(width * 0.05f)
        snakeTable.row()
        for (i in 0..<Constants.COLORS_SIZE) {
            val item = i
            //preview
            val PREVIEW_BLOCKS = 6
            val previewSnake = PreviewSnake(item, PREVIEW_BLOCKS, snakeTexture!!, game.selectedColor == item)
            for (image in previewSnake.previews) snakeTable.add<Image?>(image)

            for (image in previewSnake.previews) image.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (game.selectedColor != item) {
                        game.selectColor(item)
                        selectPreview(item)
                    }
                }
            })

            previewSnakes.add(previewSnake)
            snakeTable.row()
        }

        table!!.row()
        table!!.add<Table?>(snakeTable).colspan(6)
    }

    private fun selectPreview(index: Int) {
        for (previewSnake in previewSnakes) previewSnake.stopAnimation()
        previewSnakes.get(index).startAnimation()
    }

    private fun setUpButtons() {
        soundButton = Button(game.skin, "sound")
        soundButton!!.setChecked(!game.soundOn)
        soundButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.soundOn = !game.soundOn
                soundButton!!.setChecked(!game.soundOn)
                game.savePreferences()
            }
        })


        vibrationButton = Button(game.skin, "vibration")
        vibrationButton!!.setChecked(!game.vibrationOn)
        vibrationButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.vibrationOn = !game.vibrationOn
                if (game.vibrationOn) {
                    vibrationButton!!.setChecked(false)
                    Gdx.input.vibrate(Constants.VIBRATION_DURATION)
                } else vibrationButton!!.setChecked(true)
                game.savePreferences()
            }
        })
    }

    override fun render(delta: Float) {
        setupScreen()
        game.draw(delta)
    }

    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        game.stage!!.viewport.update(width, height, true)
    }
}
