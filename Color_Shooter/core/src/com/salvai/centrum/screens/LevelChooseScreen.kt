package com.salvai.centrum.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.enums.GameType
import com.salvai.centrum.input.CatchBackKeyProcessor
import com.salvai.centrum.utils.Constants

class LevelChooseScreen(private val game: CentrumGameClass) : ScreenAdapter() {
    private val COLUMNS = 5
    private val COLORS = 4
    private val SIZE = 0.15f //size of buttons % to screen height

    @JvmField
    var stage: Stage
    var width: Float
    var height: Float
    private val table: Table

    init {
        height = Gdx.graphics.getHeight() * 0.8f
        width = Gdx.graphics.getWidth() * 0.9f

        stage = Stage()

        table = Table(game.skin)
        table.setSize(width, height)
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f)

        setUpTable()


        //.setDebug(true);
        val scrollPane = ScrollPane(table, game.skin)
        scrollPane.setFadeScrollBars(false)
        scrollPane.setSize(width, height)
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f)
        stage.addActor(scrollPane)

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(stage)
        multiplexer.addProcessor(CatchBackKeyProcessor(game, this)) // Your screen
        Gdx.input.setInputProcessor(multiplexer)


        stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    private fun setUpTable() {
        val endlessButton = Button(game.skin, "infinity")
        endlessButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.gameType = GameType.ENDLESS
                        if (game.showTutorial) game.setScreen(TutorialScreen(game))
                        else game.setScreen(GameScreen(game))
                        dispose()
                    }
                })))
            }
        })

        table.add<Button?>(endlessButton).colspan(COLUMNS).pad(width * 0.01f).size(width * SIZE * 3).spaceBottom(width * 0.05f)
        table.row()
        for (i in 0..<game.levels.size) {
            setUpLevelButtons(i, game.levelStars[i])
            if ((i + 1) % COLUMNS == 0) setupStarsRow(i, game.levelStars)
        }
    }

    private fun setUpLevelButtons(level: Int, levelStars: Int) {
        if (levelStars >= 0) {
            val levelButton = TextButton("" + (level + 1), game.skin, "level-" + ((level) % COLORS))
            levelButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                        override fun run() {
                            game.level = level
                            if (game.showTutorial) game.setScreen(TutorialScreen(game))
                            else game.setScreen(GameScreen(game))
                            dispose()
                        }
                    })))
                }
            })
            table.add<TextButton?>(levelButton).size(width * SIZE).pad(width * 0.01f)
        } else { //locked
            val levelButton = Button(game.skin, "locked-" + ((level) % COLORS))
            table.add<Button?>(levelButton).size(width * SIZE).pad(width * 0.01f)
        }
    }

    private fun setupStarsRow(i: Int, levelStars: IntArray) {
        //new row
        if ((i + 1) % COLUMNS == 0) {
            table.row().padTop(-height * 0.04f).height(height * 0.04f).padBottom(0f) //TODO fix stars
            //draw stars
            for (j in i - COLUMNS + 1..i) {
                if (levelStars[j] >= 0) {
                    val starsButton: Button?
                    when (levelStars[j]) {
                        3 -> starsButton = Button(game.skin, "three-star")
                        2 -> starsButton = Button(game.skin, "two-star")
                        1 -> starsButton = Button(game.skin, "one-star")
                        else -> starsButton = Button(game.skin, "no-star")
                    }
                    starsButton.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                                override fun run() {
                                    game.level = i
                                    if (game.showTutorial) game.setScreen(TutorialScreen(game))
                                    else game.setScreen(GameScreen(game))
                                    dispose()
                                }
                            })))
                        }
                    })
                    table.add<Button?>(starsButton)
                }
            }
            table.row()
        }
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
