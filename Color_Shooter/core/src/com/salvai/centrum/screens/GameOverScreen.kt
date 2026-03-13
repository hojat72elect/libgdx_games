package com.salvai.centrum.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.enums.GameType
import com.salvai.centrum.input.CatchBackKeyProcessor
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.GameTheme

class GameOverScreen(private val game: CentrumGameClass) : ScreenAdapter() {
    @JvmField
    var stage: Stage
    private var table: Table? = null
    private var scoreLabel: Label? = null
    private val height: Float
    private val width: Float
    private var nextLevelButton: Button? = null
    private var retryButton: Button? = null
    private var leftBottomButton: Button? = null
    private var homeButton: Button? = null
    private val leftStar: Image
    private val centreStar: Image
    private val rightStar: Image

    init {
        leftStar = Image(game.assetsManager.manager.get<Texture?>(Constants.LEVEL_STARS_IMAGE_NAME, Texture::class.java))
        centreStar = Image(game.assetsManager.manager.get<Texture?>(Constants.LEVEL_STARS_IMAGE_NAME, Texture::class.java))
        rightStar = Image(game.assetsManager.manager.get<Texture?>(Constants.LEVEL_STARS_IMAGE_NAME, Texture::class.java))


        if (game.gameType == GameType.LEVEL) {
            levelUnlock()
            setStarsColor()
        } else assignHighscore()

        game.savePreferences()


        height = Gdx.graphics.getHeight() * 0.8f
        width = Gdx.graphics.getWidth() * 0.6f

        stage = Stage()

        setUpMainButtons()

        if (game.gameType == GameType.ENDLESS) setUpEndlessTable()
        else setUpLevelTable()

        stage.addActor(table)
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(stage)
        multiplexer.addProcessor(CatchBackKeyProcessor(game, this)) // Your screen
        Gdx.input.setInputProcessor(multiplexer)

        stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    private fun assignHighscore() {
        //save highscore

        if (game.highScore < game.score) {
            game.highScore = game.score
            game.preferences.putInteger("best", game.score)
            game.preferences.flush()
        }
    }

    private fun setStarsColor() {
        if (game.levelSucceed && game.score == game.currentLevel.thirdStarScore) {
            leftStar.setColor(GameTheme.get(2))
            centreStar.setColor(GameTheme.get(2))
            rightStar.setColor(GameTheme.get(2))
        } else if (game.levelSucceed && game.score >= game.currentLevel.secondStarScore) {
            leftStar.setColor(GameTheme.get(2))
            centreStar.setColor(GameTheme.get(2))
        } else if (game.levelSucceed) leftStar.setColor(GameTheme.get(2))
    }

    private fun setUpLevelTable() {
        scoreLabel = Label("LEVEL " + (game.level + 1), game.skin, "default")
        scoreLabel!!.setAlignment(Align.center)

        table = Table(game.skin)
        table!!.setSize(width, height)
        table!!.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f)
        table!!.defaults().height(height * 0.1f).expandX()


        table!!.add<Label?>(scoreLabel).colspan(6).fillX()
        table!!.row().spaceBottom(height * .05f)
        table!!.add<Image?>(leftStar).colspan(2).width(height * 0.2f).height(height * 0.2f).padRight(-width * 0.1f)
        table!!.add<Image?>(centreStar).colspan(2).width(height * 0.2f).height(height * 0.2f)
        table!!.add<Image?>(rightStar).colspan(2).width(height * 0.2f).height(height * 0.2f).padLeft(-width * 0.1f)
        table!!.row().spaceBottom(height * .3f)
        table!!.add<Button?>(retryButton).width(height * .2f).height(height * 0.2f).colspan(3)
        table!!.add<Button?>(nextLevelButton).width(height * 0.2f).height(height * 0.2f).colspan(3)
        table!!.row()
        table!!.add<Button?>(leftBottomButton).width(height * 0.1f).colspan(2)
        table!!.add<Button?>(homeButton).width(height * 0.1f).colspan(2)
    }

    private fun setUpEndlessTable() {
        scoreLabel = Label("SCORE " + game.score, game.skin, "default")
        scoreLabel!!.setAlignment(Align.center)

        table = Table(game.skin)
        table!!.setSize(width, height)
        table!!.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f)
        table!!.defaults().height(height * 0.1f).expandX()

        table!!.add<Label?>(scoreLabel).colspan(3).spaceBottom(height * 0.2f).fillX()
        table!!.row()
        table!!.add<Button?>(retryButton).width(height * 0.3f).height(height * 0.3f).colspan(3).spaceBottom(height * 0.25f)
        table!!.row()
        table!!.add<Button?>(leftBottomButton).width(height * 0.1f)
        table!!.add<Button?>(homeButton).width(height * 0.1f)
    }

    private fun setUpMainButtons() {
        retryButton = Button(game.skin, "replay")
        retryButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(GameScreen(game))
                        dispose()
                    }
                })))
            }
        })

        if (game.level + 1 < game.levelStars.size) {
            if (game.levelStars[game.level + 1] == -1) nextLevelButton = Button(game.skin, "locked-white")
            else {
                nextLevelButton = Button(game.skin, "play")
                nextLevelButton!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                            override fun run() {
                                game.level++
                                game.setScreen(GameScreen(game))
                                dispose()
                            }
                        })))
                    }
                })
            }
        }


        if (game.gameType == GameType.ENDLESS) {
            leftBottomButton = Button(game.skin, "highscore")
        } else {
            leftBottomButton = Button(game.skin, "levels")
            leftBottomButton!!.addListener(object : ClickListener() {
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

        homeButton = Button(game.skin, "home")
        homeButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(MenuScreen(game))
                        dispose()
                    }
                })))
            }
        })
    }

    private fun levelUnlock() {
        //unlock nextLevel
        if (game.levelSucceed) {
            if (game.level + 1 < Constants.MAX_LEVEL && game.levelStars[game.level + 1] == -1) game.levelStars[game.level + 1] = 0
            //assign stars
            if (game.score == game.currentLevel.thirdStarScore && game.levelStars[game.level] < 3) game.levelStars[game.level] = 3
            else if (game.score >= game.currentLevel.secondStarScore && game.levelStars[game.level] < 2) game.levelStars[game.level] = 2
            else if (game.levelStars[game.level] < 1) game.levelStars[game.level] = 1
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
