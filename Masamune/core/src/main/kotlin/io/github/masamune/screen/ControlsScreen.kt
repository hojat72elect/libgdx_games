package io.github.masamune.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import io.github.masamune.Masamune
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.I18NAsset
import io.github.masamune.asset.SkinAsset
import io.github.masamune.isAnyKeyPressed
import io.github.masamune.ui.view.controlView
import ktx.actors.alpha
import ktx.app.KtxScreen
import ktx.scene2d.actors

class ControlsScreen(
    private val masamune: Masamune,
    private val batch: Batch = masamune.batch,
    assetService: AssetService = masamune.asset,
) : KtxScreen {
    // viewports and stage
    private val uiViewport = uiViewport()
    private val stage = Stage(uiViewport, batch)
    private val skin = assetService[SkinAsset.DEFAULT]

    // other stuff
    private val bundle: I18NBundle = assetService[I18NAsset.MESSAGES]

    override fun show() {
        // setup UI views
        stage.actors {
            controlView(bundle, skin)
        }
    }

    override fun hide() {
        stage.clear()
    }

    override fun resize(width: Int, height: Int) {
        uiViewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        uiViewport.apply()
        stage.alpha = batch.color.a
        stage.act(delta)
        stage.draw()
        batch.setColor(1f, 1f, 1f, 1f)

        if (Gdx.input.isAnyKeyPressed()) {
            masamune.removeScreen<ControlsScreen>()
            dispose()
            masamune.setScreen<MainMenuScreen>()
        }
    }

    override fun dispose() {
        stage.dispose()
    }
}
