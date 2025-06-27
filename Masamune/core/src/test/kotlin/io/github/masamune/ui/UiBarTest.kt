package io.github.masamune.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.gdxTest
import io.github.masamune.testSkin
import io.github.masamune.ui.widget.Bar
import io.github.masamune.ui.widget.bar
import ktx.actors.txt
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.graphics.use
import ktx.math.vec2
import ktx.scene2d.actors
import ktx.scene2d.label
import ktx.scene2d.stack

/**
 * UI test for the custom [bar] widget.
 * It shows a tinted bar version when moving the bar to the left/right.
 */

fun main() = gdxTest("UI Bar Test; 1,2,3,4 to adjust bar value", UiBarTest())

private class UiBarTest : KtxApplicationAdapter {
    private val gameViewport: Viewport = ExtendViewport(8f, 8f)
    private val uiViewport = uiViewport()
    private val batch by lazy { SpriteBatch() }
    private val stage by lazy { Stage(uiViewport, batch) }
    private val charAtlas by lazy { TextureAtlas("graphics/chars_and_props.atlas".toClasspathFile()) }
    private val skin by lazy { testSkin() }
    private lateinit var bar: Bar
    private lateinit var label: Label
    private val butterflyTexture by lazy { charAtlas.findRegions("butterfly/idle_down").first() }

    override fun create() {
        setupStage()
    }

    fun setupStage() {
        stage.clear()
        stage.actors {
            stack {
                this@UiBarTest.bar = bar(skin, 50f, 0f, 100f, 1f, skin.getColor("red"))
                this@UiBarTest.label = label("50", "bar_content", skin) {
                    this.setAlignment(Align.top, Align.center)
                }

                val realPosition = vec2(2f, 2f)
                gameViewport.project(realPosition)
                uiViewport.unproject(realPosition)
                realPosition.y = uiViewport.worldHeight - realPosition.y

                val realSize = vec2(1f, 0f)
                gameViewport.project(realSize)
                uiViewport.unproject(realSize)

                this.setPosition(realPosition.x, realPosition.y - 25f)
                this.setSize(realSize.x, 30f)
                this.name = "bar"
            }
        }
    }

    private fun updateBar(value: Float) {
        val stack = stage.root.findActor<Stack>("bar")
        val bar = stack.children.first() as Bar
        val label = stack.children.last() as Label

        bar.value = value
        label.txt = "${value.toInt()}"
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        stage.viewport.update(width, height, true)
        setupStage()
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        gameViewport.apply()
        batch.color = Color.WHITE
        batch.use(gameViewport.camera) {
            it.draw(butterflyTexture, 2f, 2f, 1f, 1f)
        }

        uiViewport.apply()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) -> updateBar(0f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) -> updateBar(25f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) -> updateBar(75f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) -> updateBar(100f)
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> setupStage()
        }
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
        skin.dispose()
        charAtlas.dispose()
    }
}
