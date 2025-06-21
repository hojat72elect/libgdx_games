package com.nopalsoft.invaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.getTextWidth
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.GalaxyInvadersGame
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.Settings.addScore
import com.nopalsoft.invaders.screens.MainMenuScreen
import com.nopalsoft.invaders.screens.Screens

class GameScreen(game: GalaxyInvadersGame) : Screens(game) {


    private var tutorialScreenIndex: Int = 0 // if it is on screen 1 or 2 of the tutorial
    var world: World
    private var renderer: WorldRenderer
    var isFiring: Boolean = false
    var isMissileFired: Boolean = false
    private var touchPoint: Vector3

    private var leftButton: Rectangle
    private var rightButton: Rectangle

    var dialogPause: Dialog
    var dialogGameOver: Dialog

    private var scoresBarTable: Table
    private var labelLevel: Label
    private var labelScore: Label
    private var labelLivesCount: Label
    private var buttonPause: ImageButton

    private var buttonLeft: ImageButton
    private var buttonRight: ImageButton
    private var buttonFire: ImageButton
    private var buttonMissile: TextButton

    private var groupTutorial: Group? = null
    private var labelTiltYourDevice: Label? = null

    var acceleration: Float

    private var gameLevel: Int

    private fun setUpTutorial() {
        labelTiltYourDevice = Label(Assets.languagesBundle!!["tilt_your_device_to_move_horizontally"], LabelStyle(Assets.font45, Color.GREEN))
        labelTiltYourDevice!!.wrap = true
        labelTiltYourDevice!!.setAlignment(Align.center)
        labelTiltYourDevice!!.setPosition(0f, 120f)
        labelTiltYourDevice!!.width = SCREEN_WIDTH.toFloat()
        stage?.addActor(labelTiltYourDevice)

        groupTutorial = Group()

        val boostTable = Table()
        groupTutorial!!.addActor(boostTable)

        val livesImage = Image(Assets.upgLife)
        val boostBombImage = Image(Assets.boost2)
        val boostShieldImage = Image(Assets.boost3)
        val boostUpgradeWeaponImage = Image(Assets.boost1)

        val labelLives = Label(Assets.languagesBundle!!["get_one_extra_life"], Assets.styleLabel)
        val labelBomb = Label(Assets.languagesBundle!!["get_one_extra_missil"], Assets.styleLabel)
        val labelShield = Label(Assets.languagesBundle!!["get_a_shield"], Assets.styleLabel)
        val labelUpgradeWeapon = Label(Assets.languagesBundle!!["upgrade_your_weapon"], Assets.styleLabel)

        boostTable.setPosition(0f, 340f)
        boostTable.width = SCREEN_WIDTH.toFloat()

        val iconSize = 40
        boostTable.add(livesImage).size(iconSize.toFloat())
        boostTable.add(labelLives).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostBombImage).size(iconSize.toFloat())
        boostTable.add(labelBomb).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostShieldImage).size(iconSize.toFloat())
        boostTable.add(labelShield).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostUpgradeWeaponImage).size(iconSize.toFloat())
        boostTable.add(labelUpgradeWeapon).padLeft(15f).left()
        val touchLeft = Label(Assets.languagesBundle!!["touch_left_side_to_fire_missils"], Assets.styleLabel)
        touchLeft.wrap = true
        touchLeft.width = 160f
        touchLeft.setAlignment(Align.center)
        touchLeft.setPosition(0f, 50f)

        val touchRight = Label(Assets.languagesBundle!!["touch_right_side_to_fire"], Assets.styleLabel)
        touchRight.wrap = true
        touchRight.width = 160f
        touchRight.setAlignment(Align.center)
        touchRight.setPosition(165f, 50f)

        groupTutorial!!.addActor(touchRight)
        groupTutorial!!.addActor(touchLeft)
    }

    override fun update(deltaTime: Float) {
        when (state) {
            GAME_TUTORIAL -> updateTutorial()
            GAME_READY -> updateReady()
            GAME_RUNNING -> updateRunning(deltaTime)
        }
    }

    private fun updateTutorial() {
        if (Gdx.input.justTouched()) {
            if (tutorialScreenIndex == 0) {
                tutorialScreenIndex++
                labelTiltYourDevice!!.remove()
                stage?.addActor(groupTutorial)
            } else {
                state = GAME_READY
                groupTutorial!!.remove()
            }
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched() && !game.dialog!!.isDialogShown) {
            state = GAME_RUNNING

            if (!Settings.isTiltControl) {
                stage?.addActor(buttonLeft)
                stage?.addActor(buttonRight)
                stage?.addActor(buttonMissile)
                stage?.addActor(buttonFire)
            }
        }
    }

    private fun updateRunning(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            acceleration = 0f
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) acceleration = 5f
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) acceleration = -5f

            world.update(deltaTime, acceleration, isFiring, isMissileFired)
        } else if (Settings.isTiltControl) {
            if (Gdx.input.justTouched()) {
                camera.unproject(touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))

                if (leftButton.contains(touchPoint.x, touchPoint.y)) {
                    isMissileFired = true
                }
                if (rightButton.contains(touchPoint.x, touchPoint.y)) {
                    isFiring = true
                }
            }
            world.update(deltaTime, Gdx.input.accelerometerX, isFiring, isMissileFired)
        } else {
            world.update(deltaTime, acceleration, isFiring, isMissileFired)
        }

        if (gameLevel != world.currentLevel) {
            gameLevel = world.currentLevel
            labelLevel.setText(Assets.languagesBundle!!["level"] + " " + gameLevel)
        }

        labelScore.setText(Assets.languagesBundle!!["score"] + " " + world.score)
        labelLivesCount.setText("x" + world.oSpaceShip.lives)

        if (world.state == World.STATE_GAME_OVER) {
            state = GAME_OVER
            dialogGameOver.show(stage)
        }

        buttonMissile.setText(world.missileCount.toString() + "")

        isFiring = false
        isMissileFired = false
    }

    private fun setPaused() {
        playSound(Assets.clickSound!!)
        state = GAME_PAUSE
        world.state = World.STATE_PAUSED
        dialogPause.show(stage)
    }

    override fun draw(delta: Float) {
        if (state != GAME_TUTORIAL) renderer.render(delta)
        else Assets.backgroundLayer!!.render(delta)
        camera.update()
        batch?.projectionMatrix = camera.combined
        batch?.enableBlending()
        batch?.begin()

        when (state) {
            GAME_TUTORIAL -> displayTutorial()
            GAME_READY -> drawTouchToStart()
            GAME_RUNNING -> showMissileCount()
        }
        batch?.end()
    }

    private var rotationAngle: Float = 0f
    private var rotationStep: Float = .3f

    init {
        Settings.numberOfTimesPlayed++
        state = GAME_READY
        if (Settings.numberOfTimesPlayed < 3) { // Se mostrara 2 veces, la vez cero y la vez 1
            state = GAME_TUTORIAL
            tutorialScreenIndex = 0
            setUpTutorial()
        }
        touchPoint = Vector3()

        world = World()
        renderer = WorldRenderer(batch!!, world)
        leftButton = Rectangle(0f, 0f, 160f, 480f)
        rightButton = Rectangle(161f, 0f, 160f, 480f)

        // OnScreen Controls
        acceleration = 0f
        gameLevel = world.currentLevel
        buttonLeft = ImageButton(Assets.buttonLeft)
        buttonLeft.setSize(65f, 50f)
        buttonLeft.setPosition(10f, 5f)
        buttonLeft.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                acceleration = 5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                acceleration = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })
        buttonRight = ImageButton(Assets.buttonRight)
        buttonRight.setSize(65f, 50f)
        buttonRight.setPosition(85f, 5f)
        buttonRight.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                acceleration = -5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                acceleration = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })

        buttonMissile = TextButton(world.missileCount.toString() + "", TextButtonStyle(Assets.buttonMissile, Assets.buttonMissilePressed, null, Assets.font10))
        buttonMissile.label.color = Color.GREEN
        buttonMissile.setSize(60f, 60f)
        buttonMissile.setPosition((SCREEN_WIDTH - 5 - 60 - 20 - 60).toFloat(), 5f)
        buttonMissile.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                isMissileFired = true
            }
        })
        buttonFire = ImageButton(Assets.buttonFire, Assets.buttonFirePressed)
        buttonFire.setSize(60f, 60f)
        buttonFire.setPosition((SCREEN_WIDTH - 60 - 5).toFloat(), 5f)
        buttonFire.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                isFiring = true
            }
        })

        // End OnScreen Controls

        // Start dialog Pause
        dialogPause = Dialog(Assets.languagesBundle!!["game_paused"], Assets.styleDialogPause)

        val btContinue = TextButton(Assets.languagesBundle!!["continue"], Assets.styleTextButton)
        val btMenu = TextButton(Assets.languagesBundle!!["main_menu"], Assets.styleTextButton)

        btContinue.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                state = GAME_RUNNING
                world.state = World.STATE_RUNNING
                dialogPause.hide()
            }
        })

        btMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = MainMenuScreen(game)
                dialogPause.hide()
            }
        })

        dialogPause.buttonTable.pad(15f)
        dialogPause.buttonTable.add(btContinue).minWidth(160f).minHeight(40f).expand().padBottom(20f)
        dialogPause.buttonTable.row()
        dialogPause.buttonTable.add(btMenu).minWidth(160f).minHeight(40f).expand()

        // Start GameOver dialog
        dialogGameOver = Dialog("Game Over", Assets.styleDialogPause)

        val buttonTryAgain = TextButton(Assets.languagesBundle!!["try_again"], Assets.styleTextButton)
        val buttonMenu2 = TextButton(Assets.languagesBundle!!["main_menu"], Assets.styleTextButton)
        val buttonShare = TextButton(Assets.languagesBundle!!["share"], Assets.styleTextButtonFacebook)

        buttonTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = GameScreen(game)
                dialogGameOver.hide()
            }
        })

        buttonMenu2.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = MainMenuScreen(game)
                dialogGameOver.hide()
            }
        })
        buttonShare.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val text = Assets.languagesBundle!!.format("i_just_score_n_points_playing_droid_invaders_can_you_beat_me", world.score)
                Gdx.app.log("Share text", text)
                playSound(Assets.clickSound!!)
            }
        })

        dialogGameOver.buttonTable.pad(15f)
        dialogGameOver.buttonTable.add(buttonTryAgain).minWidth(160f).minHeight(40f).expand().padBottom(20f)
        dialogGameOver.buttonTable.row()
        dialogGameOver.buttonTable.add(buttonMenu2).minWidth(160f).minHeight(40f).expand()
        dialogGameOver.buttonTable.row()


        val labelShare = Label(Assets.languagesBundle!!["share_your_score_on_facebook"], Assets.styleLabelDialog)
        labelShare.setAlignment(Align.center)
        labelShare.wrap = true
        dialogGameOver.buttonTable.add(labelShare).width(200f).expand()
        dialogGameOver.buttonTable.row()
        dialogGameOver.buttonTable.add(buttonShare).expand()


        if (Settings.numberOfTimesPlayed % 5 == 0) {
            game.dialog!!.showDialogRate()
        }

        buttonPause = ImageButton(Assets.styleImageButtonPause)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        labelLevel = Label(Assets.languagesBundle!!["level"] + " " + world.currentLevel, Assets.styleLabel)
        labelScore = Label(Assets.languagesBundle!!["score"] + " " + world.score, Assets.styleLabel)
        labelLivesCount = Label("x" + world.oSpaceShip.lives, Assets.styleLabel)
        val livesImage = Image(Assets.spaceShip)

        scoresBarTable = Table()
        scoresBarTable.background = Assets.inGameStatusDrawable
        scoresBarTable.width = SCREEN_WIDTH.toFloat()
        scoresBarTable.height = 30f
        scoresBarTable.setPosition(0f, (SCREEN_HEIGHT - 30).toFloat())

        scoresBarTable.add(labelLevel).left()

        scoresBarTable.add(labelScore).center().expandX()

        scoresBarTable.add(livesImage).size(20f).right()
        scoresBarTable.add(labelLivesCount).right()
        scoresBarTable.add(buttonPause).size(26f).right().padLeft(8f)

        stage?.addActor(scoresBarTable)
    }

    private fun displayTutorial() {
        if (tutorialScreenIndex == 0 && Settings.isTiltControl) {
            if (rotationAngle < -20 || rotationAngle > 20) rotationStep *= -1f
            rotationAngle += rotationStep
            batch?.draw(Assets.help1, SCREEN_WIDTH / 2f - 51, 190f, 51f, 0f, 102f, 200f, 1f, 1f, rotationAngle)
        } else {
            batch?.draw(Assets.helpClick, 155f, 0f, 10f, 125f)
        }
    }

    private fun drawTouchToStart() {
        val touchToStart = Assets.languagesBundle!!["touch_to_start"]
        val textWidth = getTextWidth(Assets.font45, touchToStart)
        Assets.font45!!.draw(batch, touchToStart, (SCREEN_WIDTH / 2f) - (textWidth / 2f), 220f)
    }

    private fun showMissileCount() {
        if (world.missileCount > 0 && Settings.isTiltControl) {
            batch?.draw(Assets.missileAnimation!!.getKeyFrame(0f), 1f, 1f, 8f, 28f)
            Assets.font15!!.draw(batch, "X" + world.missileCount, 10f, 25f)
        }
    }

    override fun hide() {
        addScore(world.score)
        super.hide()
    }

    override fun pause() {
        setPaused()
        super.pause()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            playSound(Assets.clickSound!!)
            if (state == GAME_RUNNING) {
                setPaused()
                return true
            } else if (state == GAME_PAUSE) {
                game.screen = MainMenuScreen(game)
                return true
            }
        } else if (keycode == Input.Keys.MENU) {
            setPaused()
            return true
        } else if (keycode == Input.Keys.SPACE) {
            isFiring = true

            return true
        } else if (keycode == Input.Keys.ALT_LEFT) {
            isMissileFired = true
            return true
        }
        return false
    }

    companion object {
        private const val GAME_TUTORIAL = 4
        const val GAME_READY = 0
        const val GAME_RUNNING = 1
        const val GAME_OVER = 2
        const val GAME_PAUSE = 3
        var state = 0
    }
}
