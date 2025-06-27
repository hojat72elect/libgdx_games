package io.github.masamune.ui.widget

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Scaling
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.image
import ktx.scene2d.scene2d

class TurnOrderTable(
    skin: Skin,
) : KGroup, Group() {
    private val background: Image
    private val entityImages: MutableList<Pair<Int, Image>> = mutableListOf()
    private val entityDrawables: MutableList<Pair<Int, Drawable>> = mutableListOf()

    init {
        background = image(skin.getDrawable("nine_path_bg")) {
            this.setScaling(Scaling.stretch)
            this.setSize(IMG_SIZE + IMG_PADDING * 2, (IMG_SIZE + IMG_PADDING) * 5 + IMG_PADDING)
        }
    }

    private fun entityImage(entityId: Int, drawable: Drawable, fadeInEffect: Boolean = false) {
        val img = scene2d.image(drawable) {
            val bgd = this@TurnOrderTable.background
            val images = this@TurnOrderTable.entityImages

            setScaling(Scaling.fit)
            setSize(IMG_SIZE, IMG_SIZE)
            setPosition(
                IMG_PADDING,
                bgd.y + bgd.height - IMG_SIZE - IMG_PADDING - (images.size * (IMG_SIZE + IMG_PADDING))
            )
            images.add(entityId to this)

            if (fadeInEffect) {
                this += Actions.alpha(0f) then Actions.delay(1f) then Actions.fadeIn(1f, Interpolation.slowFast)
            }
        }
        this.addActor(img)
    }

    fun drawablesOfRound(entityDrawbles: List<Pair<Int, Drawable>>) {
        while (entityImages.isNotEmpty()) {
            val entityImg = entityImages.removeFirst()
            entityImg.second.remove()
        }

        entityDrawables.clear()
        entityDrawbles.forEachIndexed { idx, (entityId, drawable) ->
            if (idx < 5) {
                entityImage(entityId, drawable)
            }
            entityDrawables.add(entityId to drawable)
        }
    }

    fun removeDrawable(entityId: Int) {
        // fade out image of entity
        // transformed entities are not part of the entityImages list -> do nothing
        entityDrawables.removeIf { it.first == entityId }
        val entityImage = entityImages.firstOrNull { it.first == entityId }?.second ?: return
        entityImage += Actions.fadeOut(1f, Interpolation.bounceOut) then Actions.removeActor()

        // move all images afterward upwards
        val entityImageIdx = entityImages.indexOfFirst { it.first == entityId }
        entityImages.windowed(2) {
            val index = entityImages.indexOfFirst { entityImg -> it[1].first == entityImg.first }
            if (index < entityImageIdx) {
                return@windowed
            }

            val secondImg = it[1].second
            secondImg += Actions.delay(0.5f) then Actions.moveBy(0f, IMG_SIZE + IMG_PADDING, 1f, Interpolation.pow3In)
        }
        entityImages.removeAt(entityImageIdx)

        // add new image if there is any left
        if (entityDrawables.size > 4) {
            val (drawableEntityId, drawable) = entityDrawables[4]
            entityImage(drawableEntityId, drawable, fadeInEffect = true)
        }
    }

    companion object {
        private const val IMG_SIZE = 32f
        private const val IMG_PADDING = 4f
    }
}


@Scene2dDsl
fun <S> KWidget<S>.turnOrderTable(
    skin: Skin,
    init: (@Scene2dDsl TurnOrderTable).(S) -> Unit = {},
): TurnOrderTable = actor(TurnOrderTable(skin), init)
