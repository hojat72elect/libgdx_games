package com.nopalsoft.thetruecolor.game

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.Settings.save
import com.nopalsoft.thetruecolor.Settings.setNewScore
import com.nopalsoft.thetruecolor.TrueColorGame
import com.nopalsoft.thetruecolor.objects.Word
import com.nopalsoft.thetruecolor.objects.Word.Companion.getRandomColor
import com.nopalsoft.thetruecolor.scene2d.CountDown
import com.nopalsoft.thetruecolor.scene2d.TableProgressbarTimer
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class GameScreen(game: TrueColorGame) : BaseScreen(game) {

    private var state = STATE_READY
    private var initialTimeByWord = INITIAL_TIME_PER_WORD
    private val buttonTrue = Button(Assets.buttonTrue)
    private val buttonFalse = Button(Assets.buttonFalse)
    private val tableMenu = Table()
    private val buttonBack = Button(Assets.buttonBack)
    private val buttonTryAgain = Button(Assets.buttonTryAgain)
    private val buttonShare = Button(Assets.buttonShare)
    private val labelScore = Label("0", Label.LabelStyle(Assets.fontSmall, Color.WHITE))
    private var score = 0
    private var scoreAnterior = 0
    private val word = Word()
    private val tableProgressbarTimer = TableProgressbarTimer(SCREEN_WIDTH / 2f - TableProgressbarTimer.WIDTH / 2f, 300F)


    init {
        labelScore.color = Color.RED
        labelScore.setPosition(10f, 735f)

        val buttonSize = 90

        addPressEffect(buttonTrue)
        buttonTrue.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonTrue.setPosition((240 + 80).toFloat(), 60f)
        buttonTrue.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                checkWord(true)
            }
        })

        addPressEffect(buttonFalse)
        buttonFalse.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonFalse.setPosition((240 - 170).toFloat(), 60f)
        buttonFalse.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                checkWord(false)
            }
        })

        addPressEffect(buttonBack)
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!buttonBack.isDisabled) {
                    changeScreenWithFadeOut(MainMenuScreen::class.java, game)
                }
            }
        })

        addPressEffect(buttonTryAgain)
        buttonTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!buttonTryAgain.isDisabled) {
                    changeScreenWithFadeOut(GameScreen::class.java, game)
                }
            }
        })

        addPressEffect(buttonShare)
        buttonShare.addListener(object : ClickListener() {
        })

        tableMenu.setSize(SCREEN_WIDTH, 90f)
        tableMenu.setPosition(0f, 60f)
        tableMenu.defaults().expandX().size(90f)

        tableMenu.add(buttonBack)
        tableMenu.add(buttonTryAgain)


        // fix that can't be done because the app crashes and they don't accept it in the store.
        if (Gdx.app.type != ApplicationType.iOS) {
            tableMenu.add(buttonShare)
        }

        stage.addActor(buttonTrue)
        stage.addActor(buttonFalse)
        stage.addActor(labelScore)

        setReady()
    }

    fun createNewWord() {
        word.initialize()

        tableProgressbarTimer.remove()
        tableProgressbarTimer.initialize(word.getCurrentWordColor(), initialTimeByWord)
        stage.addActor(tableProgressbarTimer)
        stage.addActor(word.image)
    }

    private fun checkWord(selection: Boolean) {
        if (state == STATE_RUNNING) {
            if ((word.color == word.text && selection) || (word.color != word.text && !selection)) {
                score++


                initialTimeByWord -= if (score < 10) {
                    .14f // 1.8seg menos
                } else if (score < 40) {
                    .05f // 1.5seg menos
                } else if (score < 70) {
                    .015f // .54seg menos
                } else {
                    .0075f
                }

                if (initialTimeByWord < MINIMUM_TIME_PER_WORD) {
                    initialTimeByWord = MINIMUM_TIME_PER_WORD
                }
                createNewWord()
            } else {
                setGameOver()
            }
        }
    }


    override fun draw(delta: Float) {
        batcher.begin()
        batcher.draw(Assets.header, 0f, 780f, 480f, 20f)
        batcher.draw(Assets.header, 0f, 0f, 480f, 20f)
        batcher.end()
    }

    override fun update(delta: Float) {
        if (score > scoreAnterior) {
            scoreAnterior = score

            labelScore.color = getRandomColor()
            labelScore.setText(scoreAnterior.toString())
        }

        if (tableProgressbarTimer.timeIsOver) {
            setGameOver()
        }
    }

    private fun setReady() {
        state = STATE_READY
        stage.addActor(CountDown(this))
    }

    fun setRunning() {
        if (state == STATE_READY) {
            state = STATE_RUNNING
            createNewWord()
        }
    }

    private fun setGameOver() {
        if (state == STATE_RUNNING) {
            state = STATE_GAME_OVER

            val animationTime = .8f

            buttonFalse.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))
            buttonTrue.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            tableProgressbarTimer.timeIsOver = true
            tableProgressbarTimer.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            word.image.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            val scoreText = Assets.languages["score"]

            val lblScore: Label = getLabel(scoreText)
            lblScore.color.a = 0f

            lblScore.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime)))

            tableMenu.color.a = 0f

            buttonBack.isDisabled = true
            buttonTryAgain.isDisabled = true
            buttonShare.isDisabled = true

            tableMenu.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime), Actions.run {
                buttonBack.isDisabled = false
                buttonTryAgain.isDisabled = false
                buttonShare.isDisabled = false
            }))

            stage.addActor(lblScore)
            stage.addActor(tableMenu)
            setNewScore(score)

            Settings.numTimesPlayed++


            save()
        }
    }

    private fun getLabel(scoreText: String): Label {
        val scoreTextColor = StringBuilder()

        // HOT FIX : TO PUT COLORS BETWEEN THE LETTERS IS OBVIOUSLY WRONG BUT I COULD NOT THINK OF ANYTHING ELSE
        val append = arrayOf(
            "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
            "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
            "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
            "[ORANGE]"
        )
        for (i in 0 until scoreText.length) {
            scoreTextColor.append(append[i])
            scoreTextColor.append(scoreText[i])
        }
        scoreTextColor.append(append[scoreText.length])

        val labelScore = Label(
            """
            $scoreTextColor
            $score
            """.trimIndent(), LabelStyle(Assets.fontSmall, Color.WHITE)
        )
        labelScore.setAlignment(Align.center)
        labelScore.setFontScale(2.5f)
        labelScore.pack()
        labelScore.setPosition(SCREEN_WIDTH / 2f - labelScore.width / 2f, 380f)
        return labelScore
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            return true
        }
        return super.keyDown(keycode)
    }

    companion object {
        private const val STATE_READY = 0
        private const val STATE_RUNNING = 1
        private const val STATE_GAME_OVER = 2

        private const val MINIMUM_TIME_PER_WORD = .62F
        private const val INITIAL_TIME_PER_WORD = 5F
    }
}