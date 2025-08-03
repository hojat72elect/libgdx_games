package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.esotericsoftware.spine.SkeletonRenderer
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.GameScreen

abstract class BaseScreen(game: PonyRacingGame) : InputAdapter(), Screen {
    @JvmField
    var game: PonyRacingGame?

    @JvmField
    var stage: Stage

    @JvmField
    var batch: SpriteBatch?

    @JvmField
    var camera: OrthographicCamera?

    @JvmField
    var skeletonRenderer: SkeletonRenderer?

    @JvmField
    protected var glyphLayout: GlyphLayout?

    @JvmField
    protected var assetsHandler: AssetsHandler

    @JvmField
    protected var screenLastStateTime: Float

    @JvmField
    protected var ScreenStateTime: Float

    init {
        assetsHandler = game.assetsHandler
        stage = game.stage
        stage.clear()
        batch = game.batch
        glyphLayout = GlyphLayout()
        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
        this.game = game
        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera!!.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        skeletonRenderer = SkeletonRenderer()

        ScreenStateTime = 0f
        screenLastStateTime = ScreenStateTime
        if (this is MainMenuScreen) {
            assetsHandler.fontGde.getData().setScale(1f)
            assetsHandler.fontChco.getData().setScale(.65f)
        } else if (this is GameScreen) {
            assetsHandler.fontGde.getData().setScale(.625f)
            assetsHandler.fontChco.getData().setScale(.55f)
        } else if (this is WorldMapTiledScreen) {
            assetsHandler.fontGde.getData().setScale(.8f)
            assetsHandler.fontChco.getData().setScale(.6f)
        } else if (this is LeaderboardChooseScreen) {
            assetsHandler.fontGde.getData().setScale(.8f)
            assetsHandler.fontChco.getData().setScale(.65f)
        } else if (this is ShopScreen) {
            assetsHandler.fontGde.getData().setScale(.68f)
            assetsHandler.fontChco.getData().setScale(.45f)
        }
    }

    override fun render(delta: Float) {
        screenLastStateTime = ScreenStateTime
        ScreenStateTime += delta

        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(delta)
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    abstract override fun show()

    override fun hide() {
        Settings.guardar()
    }

    override fun pause() {
        assetsHandler.pauseMusic()
    }

    override fun resume() {
        if (this is GameScreen) assetsHandler.platMusicInGame()
        else assetsHandler.playMusicMenus()
    }

    override fun dispose() {
    }

    companion object {
        const val SCREEN_WIDTH: Int = 800
        const val SCREEN_HEIGHT: Int = 480

        const val WORLD_WIDTH: Int = 80
        const val WORLD_HEIGHT: Int = 48
    }
}
