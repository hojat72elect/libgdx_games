package io.github.masamune.screen

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import io.github.masamune.Masamune
import io.github.masamune.asset.*
import ktx.app.KtxScreen
import ktx.scene2d.*

class LoadingScreen(
    private val masamune: Masamune,
    private val assetService: AssetService = masamune.asset,
    private val shaderService: ShaderService = masamune.shader,
    batch: Batch = masamune.batch,
) : KtxScreen {

    private val uiViewport = FitViewport(928f, 522f)
    private val stage = Stage(uiViewport, batch)
    private val progressBar by lazy { stage.root.findActor<ProgressBar>("progressBar") }
    private val i18n by lazy { assetService[I18NAsset.MESSAGES] }

    override fun show() {
        val locale = masamune.save.loadLocale()

        // load default skin to be able to render basic UI with loading bar
        assetService.load(SkinAsset.DEFAULT)
        assetService.load(I18NAsset.MESSAGES, locale)
        assetService.finishLoading()
        val skin = assetService[SkinAsset.DEFAULT]
        skin.getAll(BitmapFont::class.java).values().forEach { (it as BitmapFont).data.markupEnabled = true }

        setupUI(skin)

        // queue remaining assets to load
        AtlasAsset.entries
            .filterNot { it == AtlasAsset.SKIN }
            .forEach(assetService::load)
    }

    private fun setupUI(skin: Skin) {
        stage.clear()
        stage.actors {
            table(skin) {
                setFillParent(true)
                label(i18n["loading.assets"], defaultStyle, skin) { cell ->
                    this.setAlignment(Align.center)
                    this.name = "infoLabel"
                    cell.padBottom(10f).row()
                }
                progressBar(0f, 1f, 0.01f, false, "default-horizontal", skin) { cell ->
                    cell.width(350f).center()
                    this.name = "progressBar"
                }
            }
        }
    }

    override fun render(delta: Float) {
        if (assetService.update()) {
            shaderService.loadAllShader()
            onFinishLoading()
            return
        }

        stage.act(delta)
        stage.draw()
        progressBar.value = assetService.progress()
    }

    private fun onFinishLoading() {
        masamune.addScreen(GameScreen(masamune))
        masamune.addScreen(CombatScreen(masamune))
        masamune.addScreen(MainMenuScreen(masamune))
        masamune.addScreen(CutSceneScreen(masamune))
        masamune.addScreen(ControlsScreen(masamune))

        masamune.removeScreen<LoadingScreen>()
        dispose()
        masamune.setScreen<ControlsScreen>()
    }

    override fun dispose() {
        stage.dispose()
    }
}
