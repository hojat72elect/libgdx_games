package com.salvai.snake

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.salvai.snake.enums.BlockRatio
import com.salvai.snake.enums.GameState
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.levels.Level
import com.salvai.snake.levels.LevelReader
import com.salvai.snake.screens.MenuScreen
import com.salvai.snake.screens.SettingsScreen
import com.salvai.snake.screens.SplashScreen
import com.salvai.snake.screens.background.Background
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.Constants.SCREEN
import com.salvai.snake.utils.MyAssetsManager
import com.salvai.snake.utils.Text
import com.salvai.snake.utils.WorldUtils

class SnakeIt : Game() {
    var gameState: GameState? = null

    var score: Int = 0
    var level: Int = 0
    var selectedLevelTab: Int = 0
    var selectedColor: Int = 0
    var worldTime: Int = 0

    var firstTimeOpen: Boolean = false
    var soundOn: Boolean = false
    var vibrationOn: Boolean = false

    var worldWidth: Float = 0f
    var worldHeight: Float = 0f

    lateinit var highScores: IntArray
    var levels: Array<Level?>? = null
    var camera: OrthographicCamera? = null
    var viewport: FitViewport? = null
    var stage: Stage? = null
    var backgroundStage: Stage? = null
    var topBarStage: Stage? = null
    var worldUtils: WorldUtils? = null
    var assetsManager: MyAssetsManager? = null
    var skin: Skin? = null
    private var background: Background? = null
    private var preferences: Preferences? = null


    override fun create() {
        if (preferences == null) preferences = Gdx.app.getPreferences(Text.GAME_NAME)

        camera = OrthographicCamera()
        viewport = FitViewport(Constants.SCREEN_WIDTH.toFloat(), Constants.SCREEN_HEIGHT.toFloat(), camera)

        setUpGlobalVariables()
        getPreferences()

        setBlockRatio()
        setUpStages()
        setUpAssetsManager()
        setUpBackground()

        //to test
//      firstTimeOpen = true;
        readAllLevels()
        setScreen(SplashScreen(this))
    }

    private fun setUpGlobalVariables() {
        highScores = IntArray(Constants.MAX_LEVEL)
        levels = Array<Level?>()
        worldHeight = viewport!!.worldHeight
        worldWidth = viewport!!.worldWidth
    }

    private fun readAllLevels() {
        val levelReader = LevelReader()
        levels = levelReader.loadAllLevels()
    }

    private fun setUpStages() {
        val backgroundViewport = FillViewport(Constants.SCREEN_WIDTH.toFloat(), Constants.SCREEN_HEIGHT.toFloat(), camera)
        stage = Stage(viewport)
        topBarStage = Stage(viewport)
        backgroundStage = Stage(backgroundViewport)
    }

    private fun setUpAssetsManager() {
        //ASSETS MANAGER
        assetsManager = MyAssetsManager()
        assetsManager!!.loadSplashScreen()
        assetsManager!!.manager.finishLoading()
    }

    private fun setUpBackground() {
        background = Background(
            assetsManager!!.manager.get<Texture?>(Constants.BLOCK_IMAGE_NAME, Texture::class.java),
            backgroundStage,
            assetsManager!!.manager.get<Texture?>(Constants.BACKGROUND_IMAGE, Texture::class.java),
            selectedColor
        )
    }

    private fun setBlockRatio() {
        when (selectedLevelTab) {
            1 -> worldUtils = WorldUtils(BlockRatio.MEDIUM)
            2 -> worldUtils = WorldUtils(BlockRatio.BIG)
            else -> worldUtils = WorldUtils(BlockRatio.SMALL)
        }
    }

    fun drawBackground(delta: Float, direction: MovingDirection?) {
        background!!.draw(delta, direction, worldTime, false)
    }


    //to save space
    fun savePreferences() {
        preferences!!.putInteger("level", level) // to save current level
        preferences!!.putBoolean("sound", soundOn)
        preferences!!.putBoolean("vibration", vibrationOn)
        preferences!!.putInteger("worldTime", worldTime)
        preferences!!.putInteger("selectedColor", selectedColor)
        preferences!!.putInteger("selectedLevelTab", selectedLevelTab)
        preferences!!.putBoolean("firstTimeOpen", firstTimeOpen)
        for (i in 0..<Constants.MAX_LEVEL) preferences!!.putInteger("highScore" + i, highScores[i])
        preferences!!.flush()
    }

    fun getPreferences() {
        firstTimeOpen = preferences!!.getBoolean("firstTimeOpen", true)
        worldTime = preferences!!.getInteger("worldTime", 15)
        level = preferences!!.getInteger("level", 0)
        selectedLevelTab = preferences!!.getInteger("selectedLevelTab", 0)
        selectedColor = preferences!!.getInteger("selectedColor", 0)
        soundOn = preferences!!.getBoolean("sound", true)
        vibrationOn = preferences!!.getBoolean("vibration", true)
        highScores[0] = preferences!!.getInteger("highScore" + 0, 0)
        for (i in 1..<Constants.MAX_LEVEL) highScores[i] = preferences!!.getInteger("highScore" + i, 0)
    }


    val currentLevel: Level?
        get() = levels!!.get(level)


    fun setUpTopBar(screen: SCREEN?) {
        topBarStage!!.clear()
        val backButton = Button(skin, "back")
        backButton.setBounds(worldWidth * 0.05f, worldHeight - worldWidth * 0.15f, worldWidth * 0.1f, worldWidth * 0.1f)
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (getScreen() is MenuScreen) {
                    (getScreen() as MenuScreen).playButton!!.addAction(Actions.fadeOut(Constants.FADE_TIME))
                    (getScreen() as MenuScreen).exitDialog!!.show(stage)
                } else stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        setScreen(MenuScreen(this@SnakeIt))
                    }
                })))
            }
        })

        // hide for ios
        if (Gdx.app.type != Application.ApplicationType.iOS || screen != SCREEN.MENU) {
            topBarStage!!.addActor(backButton)
        }

        if (screen != SCREEN.SETTINGS) {
            val settingsButton = Button(skin, "settings")
            settingsButton.setBounds(worldWidth - (worldWidth * 0.15f), worldHeight - worldWidth * 0.15f, worldWidth * 0.1f, worldWidth * 0.1f)
            settingsButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    settingsButton.addAction(Actions.fadeOut(Constants.FADE_TIME))
                    stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                        override fun run() {
                            setScreen(SettingsScreen(this@SnakeIt))
                        }
                    })))
                }
            })
            topBarStage!!.addActor(settingsButton)
        }
    }

    fun selectColor(index: Int) {
        selectedColor = index
        changeBackground(selectedColor)
        savePreferences()
    }


    private fun changeBackground(index: Int) {
        background!!.changeBackground(index)
    }


    fun draw(delta: Float) {
        background!!.draw(delta, MovingDirection.UP, worldTime, false)

        stage!!.act(delta)
        stage!!.viewport.apply()
        stage!!.draw()

        topBarStage!!.act(delta)
        topBarStage!!.viewport.apply()
        topBarStage!!.draw()
    }


    fun draw(delta: Float, direction: MovingDirection?, newBest: Boolean) {
        background!!.draw(delta, direction, worldTime, newBest)
        stage!!.viewport.apply()
        stage!!.act(delta)
        stage!!.draw()
    }

    fun updateSelectedLevelTab(selectedLevelTab: Int) {
        this.selectedLevelTab = selectedLevelTab
        setBlockRatio()
    }

    override fun dispose() {
        savePreferences()
        stage!!.dispose()
        topBarStage!!.dispose()
        backgroundStage!!.dispose()
        assetsManager!!.manager.dispose()
    }

    override fun resume() {
        super.resume()
    }
}