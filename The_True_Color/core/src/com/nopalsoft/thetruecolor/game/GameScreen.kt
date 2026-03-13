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
import com.nopalsoft.thetruecolor.game_objects.ColoredWord
import com.nopalsoft.thetruecolor.game_objects.ColoredWord.Companion.randomColor
import com.nopalsoft.thetruecolor.scene2d.CountDown
import com.nopalsoft.thetruecolor.scene2d.ProgressbarTimer
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class GameScreen(game: TrueColorGame) : BaseScreen(game) {
    var state: Int = 0

    var initialTimePerWord: Float

    var buttonTrue: Button
    var buttonFalse: Button

    var tableMenu: Table
    var buttonBack: Button
    var buttonTryAgain: Button
    var buttonShare: Button

    var labelScore: Label = Label("0", LabelStyle(Assets.fontSmall, Color.WHITE))

    var score: Int = 0
    var previousScore: Int = 0

    var word: ColoredWord = ColoredWord()
    var wordTimer: ProgressbarTimer

    init {
        labelScore.setColor(Color.RED)
        labelScore.setPosition(10f, 735f)

        initialTimePerWord = INITIAL_TIME_PER_WORD

        wordTimer = ProgressbarTimer(SCREEN_WIDTH / 2f - ProgressbarTimer.WIDTH / 2f, 300f)

        val buttonSize = 90

        buttonTrue = Button(Assets.buttonTrueDrawable)
        addPressEffect(buttonTrue)
        buttonTrue.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonTrue.setPosition((240 + 80).toFloat(), 60f)
        buttonTrue.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                checkWord(true)
            }
        })

        buttonFalse = Button(Assets.buttonFalseDrawable)
        addPressEffect(buttonFalse)
        buttonFalse.setSize(buttonSize.toFloat(), buttonSize.toFloat())
        buttonFalse.setPosition((240 - 170).toFloat(), 60f)
        buttonFalse.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                checkWord(false)
            }
        })

        buttonBack = Button(Assets.buttonBackDrawable)
        addPressEffect(buttonBack)
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!buttonBack.isDisabled()) {
                    changeScreenWithFadeOut(MainMenuScreen::class.java, game)
                }
            }
        })

        buttonTryAgain = Button(Assets.buttonTryAgainDrawable)
        addPressEffect(buttonTryAgain)
        buttonTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!buttonTryAgain.isDisabled()) {
                    changeScreenWithFadeOut(GameScreen::class.java, game)
                }
            }
        })

        buttonShare = Button(Assets.buttonShareDrawable)
        addPressEffect(buttonShare)

        tableMenu = Table()
        tableMenu.setSize(SCREEN_WIDTH.toFloat(), 90f)
        tableMenu.setPosition(0f, 60f)
        tableMenu.defaults().expandX().size(90f)

        tableMenu.add<Button?>(buttonBack)
        tableMenu.add<Button?>(buttonTryAgain)
        tableMenu.add<Button?>(buttonShare)

        stage!!.addActor(buttonTrue)
        stage!!.addActor(buttonFalse)
        stage!!.addActor(labelScore)

        setReady()
    }

    fun createNewPalabra() {
        word.initialize()

        wordTimer.remove()
        wordTimer.initialize(word.colorActualPalabra, initialTimePerWord)
        stage!!.addActor(wordTimer)
        stage!!.addActor(word.wordLabel)
    }

    private fun checkWord(isSelectionCorrect: Boolean) {
        if (state == STATE_RUNNING) {
            if ((word.color == word.wordText && isSelectionCorrect) || (word.color != word.wordText && !isSelectionCorrect)) {
                score++

                initialTimePerWord -= if (score < 10) {
                    .14f
                } else if (score < 40) {
                    .05f
                } else if (score < 70) {
                    .015f
                } else {
                    .0075f
                }

                if (initialTimePerWord < MINIMUM_TIME_PER_WORD) {
                    initialTimePerWord = MINIMUM_TIME_PER_WORD
                }
                createNewPalabra()
            } else {
                setGameover()
            }
        }
    }

    override fun update(delta: Float) {
        if (score > previousScore) {
            previousScore = score

            labelScore.setColor(randomColor)
            labelScore.setText(previousScore.toString() + "")
        }

        if (wordTimer.isTimeOver) {
            setGameover()
        }
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.header!!, 0f, 780f, 480f, 20f)
        batch!!.draw(Assets.header!!, 0f, 0f, 480f, 20f)

        batch!!.end()
    }

    private fun setReady() {
        state = STATE_READY
        stage!!.addActor(CountDown(this))
    }

    fun setRunning() {
        if (state == STATE_READY) {
            state = STATE_RUNNING
            createNewPalabra()
        }
    }

    private fun setGameover() {
        if (state == STATE_RUNNING) {
            state = STATE_GAMEOVER

            val animationTime = .8f

            buttonFalse.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))
            buttonTrue.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            wordTimer.isTimeOver = true
            wordTimer.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            word.wordLabel.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))

            val scoreText = Assets.languagesBundle!!.get("score")

            val scoreTextColor = StringBuilder()

            // HOT FIX TO PUT COLORS BETWEEN THE LETTERS IS OBVIOUSLY WRONG BUT I COULDN'T THINK OF ANYTHING ELSE
            val apend = arrayOf<String?>(
                "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]"
            )
            for (i in 0..<scoreText.length) {
                scoreTextColor.append(apend[i])
                scoreTextColor.append(scoreText[i])
            }
            scoreTextColor.append(apend[scoreText.length])

            val labelScore = Label(scoreTextColor.toString() + "\n" + score, LabelStyle(Assets.fontSmall, Color.WHITE))
            labelScore.setAlignment(Align.center)
            labelScore.setFontScale(2.5f)
            labelScore.pack()
            labelScore.setPosition(SCREEN_WIDTH / 2f - labelScore.getWidth() / 2f, 380f)
            labelScore.getColor().a = 0f

            labelScore.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime)))

            tableMenu.getColor().a = 0f

            buttonBack.setDisabled(true)
            buttonTryAgain.setDisabled(true)
            buttonShare.setDisabled(true)

            tableMenu.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime), Actions.run {
                buttonBack.setDisabled(false)
                buttonTryAgain.setDisabled(false)
                buttonShare.setDisabled(false)
            }))

            stage!!.addActor(labelScore)
            stage!!.addActor(tableMenu)
            setNewScore(score)
            Settings.numberOfTimesPlayed++
            save()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
            return true
        }
        return super.keyDown(keycode)
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    companion object {
        var STATE_READY: Int = 0
        var STATE_RUNNING: Int = 1
        var STATE_GAMEOVER: Int = 2
        var MINIMUM_TIME_PER_WORD: Float = .62f
        var INITIAL_TIME_PER_WORD: Float = 5f
    }
}
