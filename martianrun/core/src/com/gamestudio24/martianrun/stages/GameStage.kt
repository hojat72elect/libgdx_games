package com.gamestudio24.martianrun.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.gamestudio24.martianrun.actors.Background
import com.gamestudio24.martianrun.actors.Enemy
import com.gamestudio24.martianrun.actors.Ground
import com.gamestudio24.martianrun.actors.Runner
import com.gamestudio24.martianrun.actors.Score
import com.gamestudio24.martianrun.actors.menu.AboutButton
import com.gamestudio24.martianrun.actors.menu.AboutButton.AboutButtonListener
import com.gamestudio24.martianrun.actors.menu.AboutLabel
import com.gamestudio24.martianrun.actors.menu.AchievementsButton
import com.gamestudio24.martianrun.actors.menu.AchievementsButton.AchievementsButtonListener
import com.gamestudio24.martianrun.actors.menu.GameLabel
import com.gamestudio24.martianrun.actors.menu.LeaderboardButton
import com.gamestudio24.martianrun.actors.menu.LeaderboardButton.LeaderboardButtonListener
import com.gamestudio24.martianrun.actors.menu.MusicButton
import com.gamestudio24.martianrun.actors.menu.PauseButton
import com.gamestudio24.martianrun.actors.menu.PauseButton.PauseButtonListener
import com.gamestudio24.martianrun.actors.menu.PausedLabel
import com.gamestudio24.martianrun.actors.menu.ShareButton
import com.gamestudio24.martianrun.actors.menu.ShareButton.ShareButtonListener
import com.gamestudio24.martianrun.actors.menu.SoundButton
import com.gamestudio24.martianrun.actors.menu.StartButton
import com.gamestudio24.martianrun.actors.menu.StartButton.StartButtonListener
import com.gamestudio24.martianrun.actors.menu.Tutorial
import com.gamestudio24.martianrun.enums.Difficulty
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AudioUtils
import com.gamestudio24.martianrun.utils.BodyUtils
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager
import com.gamestudio24.martianrun.utils.WorldUtils

class GameStage : Stage(
    ScalingViewport(
        Scaling.stretch, VIEWPORT_WIDTH.toFloat(), VIEWPORT_HEIGHT.toFloat(),
        OrthographicCamera(VIEWPORT_WIDTH.toFloat(), VIEWPORT_HEIGHT.toFloat())
    )
), ContactListener {
    private var world: World? = null
    private var runner: Runner? = null
    private var accumulator = 0f

    private var screenLeftSide: Rectangle? = null
    private var screenRightSide: Rectangle? = null

    private var soundButton: SoundButton? = null
    private var musicButton: MusicButton? = null
    private var pauseButton: PauseButton? = null
    private var startButton: StartButton? = null
    private var leaderboardButton: LeaderboardButton? = null
    private var aboutButton: AboutButton? = null

    private var score: Score? = null
    private var totalTimePassed = 0f
    private var tutorialShown = false

    private var touchPoint: Vector3? = null

    init {
        setUpCamera()
        setUpStageBase()
        setUpGameLabel()
        setUpMainMenu()
        setUpTouchControlAreas()
        Gdx.input.inputProcessor = this
        AudioUtils.instance.init()
        onGameOver()
    }

    private fun setUpStageBase() {
        setUpWorld()
        setUpFixedMenu()
    }

    private fun setUpGameLabel() {
        val gameLabelBounds = Rectangle(
            0f, camera.viewportHeight * 7 / 8,
            camera.viewportWidth, camera.viewportHeight / 4
        )
        addActor(GameLabel(gameLabelBounds))
    }

    private fun setUpAboutText() {
        val gameLabelBounds = Rectangle(
            0f, camera.viewportHeight * 5 / 8,
            camera.viewportWidth, camera.viewportHeight / 4
        )
        addActor(AboutLabel(gameLabelBounds))
    }

    /**
     * These menu buttons are always displayed
     */
    private fun setUpFixedMenu() {
        setUpSound()
        setUpMusic()
        setUpScore()
    }

    private fun setUpSound() {
        val soundButtonBounds = Rectangle(
            camera.viewportWidth / 64,
            camera.viewportHeight * 13 / 20, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        soundButton = SoundButton(soundButtonBounds)
        addActor(soundButton)
    }

    private fun setUpMusic() {
        val musicButtonBounds = Rectangle(
            camera.viewportWidth / 64,
            camera.viewportHeight * 4 / 5, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        musicButton = MusicButton(musicButtonBounds)
        addActor(musicButton)
    }

    private fun setUpScore() {
        val scoreBounds = Rectangle(
            camera.viewportWidth * 47 / 64,
            camera.viewportHeight * 57 / 64, camera.viewportWidth / 4,
            camera.viewportHeight / 8
        )
        score = Score(scoreBounds)
        addActor(score)
    }

    private fun setUpPause() {
        val pauseButtonBounds = Rectangle(
            camera.viewportWidth / 64,
            camera.viewportHeight * 1 / 2, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        pauseButton = PauseButton(pauseButtonBounds, GamePauseButtonListener())
        addActor(pauseButton)
    }

    /**
     * These menu buttons are only displayed when the game is over
     */
    private fun setUpMainMenu() {
        setUpStart()
        setUpLeaderboard()
        setUpAbout()
        setUpShare()
        setUpAchievements()
    }

    private fun setUpStart() {
        val startButtonBounds = Rectangle(
            camera.viewportWidth * 3 / 16,
            camera.viewportHeight / 4, camera.viewportWidth / 4,
            camera.viewportWidth / 4
        )
        startButton = StartButton(startButtonBounds, GameStartButtonListener())
        addActor(startButton)
    }

    private fun setUpLeaderboard() {
        val leaderboardButtonBounds = Rectangle(
            camera.viewportWidth * 9 / 16,
            camera.viewportHeight / 4, camera.viewportWidth / 4,
            camera.viewportWidth / 4
        )
        leaderboardButton = LeaderboardButton(
            leaderboardButtonBounds,
            GameLeaderboardButtonListener()
        )
        addActor(leaderboardButton)
    }

    private fun setUpAbout() {
        val aboutButtonBounds = Rectangle(
            camera.viewportWidth * 23 / 25,
            camera.viewportHeight * 13 / 20, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        aboutButton = AboutButton(aboutButtonBounds, GameAboutButtonListener())
        addActor(aboutButton)
    }

    private fun setUpShare() {
        val shareButtonBounds = Rectangle(
            camera.viewportWidth / 64,
            camera.viewportHeight / 2, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        val shareButton = ShareButton(shareButtonBounds, GameShareButtonListener())
        addActor(shareButton)
    }

    private fun setUpAchievements() {
        val achievementsButtonBounds = Rectangle(
            camera.viewportWidth * 23 / 25,
            camera.viewportHeight / 2, camera.viewportHeight / 10,
            camera.viewportHeight / 10
        )
        val achievementsButton = AchievementsButton(
            achievementsButtonBounds,
            GameAchievementsButtonListener()
        )
        addActor(achievementsButton)
    }

    private fun setUpWorld() {
        world = WorldUtils.createWorld()
        world!!.setContactListener(this)
        setUpBackground()
        setUpGround()
    }

    private fun setUpBackground() {
        addActor(Background())
    }

    private fun setUpGround() {
        val ground = Ground(WorldUtils.createGround(world!!))
        addActor(ground)
    }

    private fun setUpCharacters() {
        setUpRunner()
        setUpPauseLabel()
        createEnemy()
    }

    private fun setUpRunner() {
        if (runner != null) {
            runner!!.remove()
        }
        runner = Runner(WorldUtils.createRunner(world!!))
        addActor(runner)
    }

    private fun setUpCamera() {
        val camera = OrthographicCamera(VIEWPORT_WIDTH.toFloat(), VIEWPORT_HEIGHT.toFloat())
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f)
        camera.update()
    }

    private fun setUpTouchControlAreas() {
        touchPoint = Vector3()
        screenLeftSide = Rectangle(
            0f, 0f, camera.viewportWidth / 2,
            camera.viewportHeight
        )
        screenRightSide = Rectangle(
            camera.viewportWidth / 2, 0f,
            camera.viewportWidth / 2, camera.viewportHeight
        )
    }

    private fun setUpPauseLabel() {
        val pauseLabelBounds = Rectangle(
            0f, camera.viewportHeight * 7 / 8,
            camera.viewportWidth, camera.viewportHeight / 4
        )
        addActor(PausedLabel(pauseLabelBounds))
    }

    private fun setUpTutorial() {
        if (tutorialShown) {
            return
        }
        setUpLeftTutorial()
        setUpRightTutorial()
        tutorialShown = true
    }

    private fun setUpLeftTutorial() {
        val width = camera.viewportHeight / 4
        val x = camera.viewportWidth / 4 - width / 2
        val leftTutorialBounds = Rectangle(
            x, camera.viewportHeight * 9 / 20, width,
            width
        )
        addActor(
            Tutorial(
                leftTutorialBounds, Constants.TUTORIAL_LEFT_REGION_NAME,
                Constants.TUTORIAL_LEFT_TEXT
            )
        )
    }

    private fun setUpRightTutorial() {
        val width = camera.viewportHeight / 4
        val x = camera.viewportWidth * 3 / 4 - width / 2
        val rightTutorialBounds = Rectangle(
            x, camera.viewportHeight * 9 / 20, width,
            width
        )
        addActor(
            Tutorial(
                rightTutorialBounds, Constants.TUTORIAL_RIGHT_REGION_NAME,
                Constants.TUTORIAL_RIGHT_TEXT
            )
        )
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (GameManager.instance.gameState == GameState.PAUSED) return

        if (GameManager.instance.gameState == GameState.RUNNING) {
            totalTimePassed += delta
            updateDifficulty()
        }

        val bodies = Array<Body>(world!!.bodyCount)
        world!!.getBodies(bodies)

        for (body in bodies) {
            update(body)
        }

        // Fixed timestep
        accumulator += delta

        while (accumulator >= delta) {
            val timeStep = 1 / 300f
            world!!.step(timeStep, 6, 2)
            accumulator -= timeStep
        }
    }

    private fun update(body: Body) {
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner!!.isHit) {
                createEnemy()
            }
            world!!.destroyBody(body)
        }
    }

    private fun createEnemy() {
        val enemy = Enemy(WorldUtils.createEnemy(world!!))
        enemy.getUserData().linearVelocity = GameManager.instance.difficulty?.enemyLinearVelocity
        addActor(enemy)
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // Need to get the actual coordinates

        translateScreenToWorldCoordinates(x, y)

        // If a menu control was touched ignore the rest
        if (menuControlTouched(touchPoint!!.x, touchPoint!!.y)) {
            return super.touchDown(x, y, pointer, button)
        }

        if (GameManager.instance.gameState != GameState.RUNNING) {
            return super.touchDown(x, y, pointer, button)
        }

        if (rightSideTouched(touchPoint!!.x, touchPoint!!.y)) {
            runner!!.jump()
        } else if (leftSideTouched(touchPoint!!.x, touchPoint!!.y)) {
            runner!!.dodge()
        }

        return super.touchDown(x, y, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (GameManager.instance.gameState != GameState.RUNNING) {
            return super.touchUp(screenX, screenY, pointer, button)
        }

        if (runner!!.isDodging) {
            runner!!.stopDodge()
        }

        return super.touchUp(screenX, screenY, pointer, button)
    }

    private fun menuControlTouched(x: Float, y: Float): Boolean {
        val touched = when (GameManager.instance.gameState) {
            GameState.OVER -> (startButton!!.bounds.contains(x, y)
                    || leaderboardButton!!.bounds.contains(x, y)
                    || aboutButton!!.bounds.contains(x, y))

            GameState.RUNNING, GameState.PAUSED -> pauseButton!!.bounds.contains(x, y)
            else -> false
        }

        return touched || soundButton!!.bounds.contains(x, y)
                || musicButton!!.bounds.contains(x, y)
    }

    private fun rightSideTouched(x: Float, y: Float): Boolean {
        return screenRightSide!!.contains(x, y)
    }

    private fun leftSideTouched(x: Float, y: Float): Boolean {
        return screenLeftSide!!.contains(x, y)
    }

    /**
     * Helper function to get the actual coordinates in my world
     */
    private fun translateScreenToWorldCoordinates(x: Int, y: Int) {
        camera.unproject(touchPoint!!.set(x.toFloat(), y.toFloat(), 0f))
    }

    override fun beginContact(contact: Contact) {
        val a = contact.fixtureA.body
        val b = contact.fixtureB.body

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
            (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))
        ) {
            if (runner!!.isHit) {
                return
            }
            runner!!.hit()
            onGameOver()
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
            (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))
        ) {
            runner!!.landed()
        }
    }

    private fun updateDifficulty() {
        if (GameManager.instance.isMaxDifficulty) {
            return
        }

        val currentDifficulty = GameManager.instance.difficulty

        if (totalTimePassed > GameManager.instance.difficulty!!.level * 5) {
            val nextDifficulty = currentDifficulty!!.level + 1
            val difficultyName = "DIFFICULTY_$nextDifficulty"
            GameManager.instance.difficulty = Difficulty.valueOf(difficultyName)

            runner!!.onDifficultyChange(GameManager.instance.difficulty!!)
            score!!.setMultiplier(GameManager.instance.difficulty!!.scoreMultiplier)
        }
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

    private fun onGamePaused() {
        GameManager.instance.gameState = GameState.PAUSED
    }

    private fun onGameResumed() {
        GameManager.instance.gameState = GameState.RUNNING
    }

    private fun onGameOver() {
        GameManager.instance.gameState = GameState.OVER
        GameManager.instance.resetDifficulty()
        totalTimePassed = 0f
        setUpMainMenu()
    }

    private fun onGameAbout() {
        GameManager.instance.gameState = GameState.ABOUT
        clear()
        setUpStageBase()
        setUpGameLabel()
        setUpAboutText()
        setUpAbout()
    }

    private inner class GamePauseButtonListener : PauseButtonListener {
        override fun onPause() {
            onGamePaused()
        }

        override fun onResume() {
            onGameResumed()
        }
    }

    private inner class GameStartButtonListener : StartButtonListener {
        override fun onStart() {
            clear()
            setUpStageBase()
            setUpCharacters()
            setUpPause()
            setUpTutorial()
            onGameResumed()
        }
    }

    private class GameLeaderboardButtonListener : LeaderboardButtonListener {
        override fun onLeaderboard() {
        }
    }

    private inner class GameAboutButtonListener : AboutButtonListener {
        override fun onAbout() {
            if (GameManager.instance.gameState == GameState.OVER) {
                onGameAbout()
            } else {
                clear()
                setUpStageBase()
                setUpGameLabel()
                onGameOver()
            }
        }
    }

    private class GameShareButtonListener : ShareButtonListener {
        override fun onShare() {
        }
    }

    private class GameAchievementsButtonListener : AchievementsButtonListener {
        override fun onAchievements() {
        }
    }

    companion object {
        private const val VIEWPORT_WIDTH = Constants.APP_WIDTH
        private const val VIEWPORT_HEIGHT = Constants.APP_HEIGHT
    }
}