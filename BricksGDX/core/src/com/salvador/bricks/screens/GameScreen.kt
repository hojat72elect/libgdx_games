package com.salvador.bricks.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Timer
import com.salvador.bricks.BrickBreaker
import com.salvador.bricks.game_objects.Background
import com.salvador.bricks.game_objects.Ball
import com.salvador.bricks.game_objects.Brick
import com.salvador.bricks.game_objects.Constants.BALL_SPEED
import com.salvador.bricks.game_objects.Constants.BALL_WIDTH
import com.salvador.bricks.game_objects.Constants.POWER_UP_ADD_ONE_BALL
import com.salvador.bricks.game_objects.Constants.POWER_UP_FIREBALL
import com.salvador.bricks.game_objects.Constants.POWER_UP_PADDLE_SIZE
import com.salvador.bricks.game_objects.Constants.POWER_UP_SPEED_LOW
import com.salvador.bricks.game_objects.Constants.POWER_UP_SPEED_UP
import com.salvador.bricks.game_objects.Constants.SCREEN_WIDTH
import com.salvador.bricks.game_objects.Paddle
import com.salvador.bricks.game_objects.PowerUp
import com.salvador.bricks.game_objects.Score
import com.salvador.bricks.game_objects.SoundManager.loadSounds
import com.salvador.bricks.game_objects.SoundManager.playBrickSound
import com.salvador.bricks.game_objects.SoundManager.playMusicBackground
import com.salvador.bricks.game_objects.SoundManager.stopMusicBackground
import kotlin.math.max
import kotlin.math.min

class GameScreen(brickBreaker: BrickBreaker) : GameClass(brickBreaker) {

    private val stage = Stage()
    private val paddle: Paddle
    private val score = Score(770f)
    private val bricks = ArrayList<Brick>()
    private val powerUps = ArrayList<PowerUp>()
    private val balls = ArrayList<Ball>()
    var level: Array<IntArray>
    var level1: Array<IntArray>
    var level2: Array<IntArray>
    var brickN = 0
    private var selectPosition: Boolean = true

    init {
        Gdx.input.inputProcessor = stage
        Gdx.input.isCatchBackKey = true
        loadSounds()
        val camera = OrthographicCamera()
        stage.viewport.camera = camera
        camera.setToOrtho(false, SCREEN_WIDTH.toFloat(), 800F)
        val background = Background(0F, 0F)
        paddle = Paddle(SCREEN_WIDTH / 2F - 130F / 2, 50F)
        stage.addActor(background)
        stage.addActor(paddle)
        stage.addActor(score)

        playMusicBackground()
        stage.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = true

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                selectPosition = false
                balls[0].pause = false
            }

            override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)

                val screenW = Gdx.graphics.width.toFloat()

                val xx = Gdx.input.x.toFloat()

                val x1 = (xx) * (SCREEN_WIDTH / screenW)

                paddle.setPosition(x1 - 130F / 2, paddle.getY())

                if (selectPosition) balls[0].setPosition(paddle.position.x + 130F / 2 - 30F / 2, balls[0].position.y)
            }
        })

        level1 = arrayOf<IntArray>(
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 1, 1, 11, 1),
            intArrayOf(2, 2, 12, 2, 2, 2),
            intArrayOf(3, 3, 3, 13, 3, 3),
            intArrayOf(4, 4, 4, 4, 14, 4),
            intArrayOf(5, 5, 15, 5, 5, 5),
            intArrayOf(6, 6, 6, 6, 6, 6),
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0),
        )
        level2 = arrayOf<IntArray>(
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0),
            intArrayOf(2, 2, 2, 2, 2, 2),
            intArrayOf(2, 5, 12, 2, 5, 2),
            intArrayOf(2, 2, 2, 2, 2, 2),
            intArrayOf(2, 2, 2, 2, 12, 2),
            intArrayOf(2, 5, 2, 12, 5, 2),
            intArrayOf(2, 5, 15, 5, 5, 2),
            intArrayOf(12, 2, 2, 2, 2, 2),
        )

        level = level1
        loadLevel()
    }

    fun clearLevel() {
        clearBricks()
        clearBalls()
        clearPowerUps()
    }

    fun resetPosition() {
        balls.add(Ball(SCREEN_WIDTH / 2F - 30F / 2, 100F))
        val ballx = balls[balls.size - 1]
        ballx.pause = true
        stage.addActor(ballx)
        selectPosition = true
        paddle.setPosition(SCREEN_WIDTH / 2F - 130F / 2, paddle.getY())
    }

    fun loadLevel() {
        addNewBall(true)
        selectPosition = true
        paddle.setPosition(SCREEN_WIDTH / 2F - 130F / 2, paddle.getY())

        for (y in 0..9) {
            for (x in 0..5) {
                if (level[y][x] != 0) {
                    bricks.add(Brick(level[y][x], x * 75, 800 - (y * 30) - 30))
                    level[y][x] = 0
                }
            }
        }

        Timer.schedule(object : Timer.Task() {
            override fun run() {
                if (brickN < bricks.size) {
                    stage.addActor(bricks[brickN])
                    brickN++
                }
            }
        }, 0F, 0.03F)
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        stage.draw()
        stage.act()

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            stopGame()
            game.setScreen(MenuScreen(game))
        }

        val ballIterator = balls.iterator()
        while (ballIterator.hasNext()) {
            val ball = ballIterator.next()

            if (bricks.isEmpty()) {
                clearLevel()
                selectPosition = true
                paddle.setPosition(SCREEN_WIDTH / 2F - 130F / 2, paddle.getY())
                level = level2
                score.level = score.level + 1
                brickN = 0
                loadLevel()
            }

            if (ball.position.x <= 0) {
                ball.position.x = 0F
                ball.speedX = -ball.speedX
            }

            if ((ball.position.x + BALL_WIDTH) >= SCREEN_WIDTH) {
                ball.position.x = (SCREEN_WIDTH - BALL_WIDTH).toFloat()
                ball.speedX = -ball.speedX
            }

            if ((ball.position.y + BALL_WIDTH) >= 800) {
                ball.position.y = (800 - BALL_WIDTH).toFloat()
                ball.speedY = -ball.speedY
            }

            if (ball.position.y < 0) {
                ball.remove()
                ballIterator.remove()
                if (score.lives > 0) {
                    if (balls.isEmpty()) {
                        clearBalls()
                        clearPowerUps()
                        resetPosition()
                        score.lives = score.lives - 1
                    }
                } else {
                    stopGame()
                    game.setScreen(GameOverScreen(game, score.score.toFloat()))
                    return
                }
                continue
            }

            if (paddle.getBounds().overlaps(ball.getBounds())) {
                if (ball.speedX > 0)
                    ball.speedX = BALL_SPEED
                else
                    ball.speedX = -BALL_SPEED

                ball.position.y = paddle.position.y + 40
                ball.speedY = -ball.speedY
            }

            val brickIterator = bricks.iterator()
            while (brickIterator.hasNext()) {
                val brick = brickIterator.next()
                if (brick.getBounds().overlaps(ball.getBounds()) && brick.live) {
                    val insect = intersect(brick.getBounds(), ball.getBounds())

                    var vertical = false
                    var horizontal = false

                    if (insect.x == brick.positionX.toFloat()) {
                        horizontal = true
                    } else if (insect.x + insect.width == brick.positionX + brick.brickWidth) {
                        horizontal = true
                    }
                    if (insect.y == brick.positionY.toFloat()) {
                        vertical = true
                    } else if (insect.y + insect.height == brick.positionY + brick.brickHeight) {
                        vertical = true
                    }

                    if (horizontal && vertical) {
                        if (insect.width > insect.height)
                            horizontal = false
                        else
                            vertical = false
                    }

                    if (!ball.isFireballActive) {
                        if (horizontal) {
                            ball.speedX = -ball.speedX
                        } else if (vertical) {
                            ball.speedY = -ball.speedY
                        }
                    }
                    playBrickSound()
                    brick.remove()
                    brickIterator.remove()
                    brick.live = false
                    score.score = score.score + 100
                    if (brick.type >= 11) {
                        powerUps.add(PowerUp(brick.type, brick.positionX + brick.brickWidth / 2 - 15, brick.positionY.toFloat()))
                        stage.addActor(powerUps[powerUps.size - 1])
                    }
                    break
                }
            }
        }


        val powerUpIterator = powerUps.iterator()
        while (powerUpIterator.hasNext()) {
            val powerUp = powerUpIterator.next()
            if (paddle.getBounds().overlaps(powerUp.getBounds())) {
                if (powerUp.live) {
                    var collected = true
                    when (powerUp.type) {
                        POWER_UP_ADD_ONE_BALL -> {
                            addNewBall(false)
                        }

                        POWER_UP_PADDLE_SIZE -> {
                            setPaddlesize(80F, 30F)
                        }

                        POWER_UP_FIREBALL -> {
                            setFireBall()
                        }

                        POWER_UP_SPEED_LOW -> {
                            setVelocityBall(500F)
                        }

                        POWER_UP_SPEED_UP -> {
                            setVelocityBall(100F)
                        }

                        else -> {
                            collected = false
                        }
                    }

                    if (collected) {
                        powerUp.remove()
                        powerUp.live = false
                        powerUpIterator.remove()
                    }
                }
            }
        }

    }

    fun stopGame() {
        stopMusicBackground()
    }

    fun clearBalls() {
        for (ball in balls) ball.remove()
        balls.clear()
    }

    fun clearBricks() {
        for (brick in bricks) brick.remove()
        bricks.clear()
    }

    fun clearPowerUps() {
        for (powerUp in powerUps) powerUp.remove()
        powerUps.clear()
    }

    fun setFireBall() {
        for (ball in balls) ball.setFireBall()
    }

    fun setPaddlesize(w: Float, h: Float) {
        paddle.setSize(w, h)
    }

    fun setVelocityBall(speed: Float) {
        for (ball in balls) ball.setVelocity(speed)
    }

    fun addNewBall(paused: Boolean) {
        balls.add(Ball(SCREEN_WIDTH / 2F - BALL_WIDTH / 2F, 100F))
        val ball = balls[balls.size - 1]
        if (paused) ball.pause = true
        stage.addActor(ball)
    }

    fun intersect(rectangle1: Rectangle, rectangle2: Rectangle): Rectangle {
        val intersection = Rectangle()
        intersection.x = max(rectangle1.x, rectangle2.x)
        intersection.width = min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x
        intersection.y = max(rectangle1.y, rectangle2.y)
        intersection.height = min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y

        return intersection
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
