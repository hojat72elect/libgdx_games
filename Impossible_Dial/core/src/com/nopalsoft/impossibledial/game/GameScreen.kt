package com.nopalsoft.impossibledial.game

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.impossibledial.Achievements.unlockScoreAchievementsEasy
import com.nopalsoft.impossibledial.Achievements.unlockScoreAchievementsHard
import com.nopalsoft.impossibledial.Achievements.unlockTimesPlayedAchievements
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.MainGame
import com.nopalsoft.impossibledial.Settings
import com.nopalsoft.impossibledial.Settings.save
import com.nopalsoft.impossibledial.Settings.setNewScoreEasy
import com.nopalsoft.impossibledial.Settings.setNewScoreHard
import com.nopalsoft.impossibledial.game_objects.Arrow
import com.nopalsoft.impossibledial.game_objects.ArrowEasy
import com.nopalsoft.impossibledial.game_objects.ArrowHard
import com.nopalsoft.impossibledial.scene2d.CountDown
import com.nopalsoft.impossibledial.screens.MainMenuScreen
import com.nopalsoft.impossibledial.screens.Screens

class GameScreen(game: MainGame, var dificultad: Int) : Screens(game) {
    var state: Int = 0


    var tbMenu: Table
    var btBack: Button
    var btTryAgain: Button
    var btShare: Button

    var lbScore: Label

    var score: Int = 0
    var scoreAnterior: Int = 0

    var circulo: Image = Image()
    var oArrow: Arrow? = null

    /**
     * Esta variable indica si la flecha esta apuntando al cuadrante de su color, si no se preciona y se sale hacia otro cuadrante es gameOver
     */
    var entroASuCuadrante: Boolean = false


    init {
        circulo.setSize(385f, 385f)
        circulo.setPosition(SCREEN_WIDTH / 2f - circulo.getWidth() / 2f, 200f)

        if (dificultad == DIFICULTAD_EASY) {
            circulo.setDrawable(Assets.circle)
            oArrow = ArrowEasy(SCREEN_WIDTH / 2f - Arrow.WIDTH / 2f, circulo.getY() + circulo.getHeight() / 2f)
        } else {
            circulo.setDrawable(Assets.circleHard)
            oArrow = ArrowHard(SCREEN_WIDTH / 2f - Arrow.WIDTH / 2f, circulo.getY() + circulo.getHeight() / 2f)
        }

        oArrow!!.init()

        stage!!.addActor(circulo)

        lbScore = Label("0", LabelStyle(Assets.fontChico, Color.WHITE))
        lbScore.setColor(Color.RED)
        lbScore.setPosition(10f, 735f)

        btBack = Button(Assets.btBack)
        addEfectoPress(btBack)
        btBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!btBack.isDisabled()) {
                    changeScreenWithFadeOut(MainMenuScreen::class.java, game)
                }
            }
        })

        btTryAgain = Button(Assets.btTryAgain)
        addEfectoPress(btTryAgain)
        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!btTryAgain.isDisabled()) {
                    changeScreenWithFadeOut(GameScreen::class.java, game, dificultad)
                }
            }
        })

        btShare = Button(Assets.btShare)
        addEfectoPress(btShare)


        tbMenu = Table()
        tbMenu.setSize(SCREEN_WIDTH.toFloat(), 90f)
        tbMenu.setPosition(0f, 110f)
        tbMenu.defaults().expandX().size(90f)
        tbMenu.add<Button?>(btBack)
        tbMenu.add<Button?>(btTryAgain)
        tbMenu.add<Button?>(btShare)

        stage!!.addActor(lbScore)
        setReady()
    }

    override fun update(delta: Float) {
        if (Gdx.input.justTouched()) {
            if (oArrow!!.flechaApuntandoAlCuadrante == oArrow!!.colorActual) {
                entroASuCuadrante = false


                oArrow!!.didScore()
                score++

                if (dificultad == DIFICULTAD_EASY) {
                    unlockScoreAchievementsEasy()
                } else {
                    unlockScoreAchievementsHard()
                }
            } else {
                setGameover()
            }
        }


        if (oArrow!!.flechaApuntandoAlCuadrante == oArrow!!.colorActual) {
            entroASuCuadrante = true
        }
        if (entroASuCuadrante) {
            if (oArrow!!.flechaApuntandoAlCuadrante != oArrow!!.colorActual) {
                setGameover()
            }
        }

        run {
            if (score > scoreAnterior) {
                scoreAnterior = score

                lbScore.setColor(Arrow.randomColor)
                lbScore.setText(scoreAnterior.toString() + "")
            }
        }
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.header!!, 0f, 780f, 480f, 20f)
        batcher!!.draw(Assets.header!!, 0f, 0f, 480f, 20f)

        batcher!!.end()
    }

    private fun setReady() {
        state = STATE_READY
        stage!!.addActor(CountDown(this))
    }

    fun setRunning() {
        if (state == STATE_READY) {
            state = STATE_RUNNING
            stage!!.addActor(oArrow)
        }
    }

    private fun setGameover() {
        if (state == STATE_RUNNING) {
            state = STATE_GAMEOVER

            val bestScore: Int

            if (dificultad == DIFICULTAD_EASY) {
                setNewScoreEasy(score)
                bestScore = Settings.bestScoreEasy
            } else {
                setNewScoreHard(score)
                bestScore = Settings.bestScoreHard
            }


            val animationTime = .8f

            oArrow!!.setGameOver()
            oArrow!!.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))
            circulo.addAction(Actions.sequence(Actions.alpha(0f, animationTime), Actions.removeActor()))


            var scoreText = Assets.idiomas!!.get("score")

            scoreText += "\n$score\n Best\n$bestScore"

            val scoreTextColor = StringBuilder()

            // HOT FIX PARA PONER ENTRE LAS LETRAS COLORES OBVIAMENTE ESTA MAL PERO nO SE ME OCURRIO OTRA COSA
            val apend = arrayOf<String?>(
                "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]"
            )
            for (i in 0..<scoreText.length) {
                scoreTextColor.append(apend[i])
                scoreTextColor.append(scoreText[i])
            }
            scoreTextColor.append(apend[scoreText.length])

            val lblScore = Label(scoreTextColor.toString(), LabelStyle(Assets.fontChico, Color.WHITE))
            lblScore.setAlignment(Align.center)
            lblScore.setFontScale(2.5f)
            lblScore.pack()
            lblScore.setPosition(SCREEN_WIDTH / 2f - lblScore.getWidth() / 2f, 300f)
            lblScore.getColor().a = 0f

            lblScore.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime)))

            tbMenu.getColor().a = 0f

            btBack.setDisabled(true)
            btTryAgain.setDisabled(true)
            btShare.setDisabled(true)

            tbMenu.addAction(Actions.sequence(Actions.delay(1f), Actions.alpha(1f, animationTime), Actions.run {
                btBack.setDisabled(false)
                btTryAgain.setDisabled(false)
                btShare.setDisabled(false)
            }))

            stage!!.addActor(lblScore)
            stage!!.addActor(tbMenu)

            Settings.numVecesJugadas++

            unlockTimesPlayedAchievements()
            save()
        }
    }

    override fun hide() {
        super.hide()
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
            return true
        }
        return super.keyDown(keycode)
    }

    companion object {
        private const val STATE_READY = 0
        private const val STATE_RUNNING = 1
        private const val STATE_GAMEOVER = 2
        var DIFICULTAD_EASY: Int = 0
        var DIFICULTAD_HARD: Int = 1
    }
}
