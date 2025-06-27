package io.github.masamune.ui.model

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Graphic
import io.github.masamune.component.Item
import io.github.masamune.component.Name
import io.github.masamune.component.Transform
import io.github.masamune.event.Event
import io.github.masamune.event.GameResizeEvent
import io.github.masamune.event.PlayerQuestItemBegin
import io.github.masamune.event.PlayerQuestItemEnd
import ktx.math.vec2

class QuestItemViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val gameViewport: Viewport,
    private val uiViewport: Viewport,
) : ViewModel(bundle, audioService) {

    private var playerEntity = Entity.NONE
    var itemDrawable: Drawable = BaseDrawable()
        private set
    var itemName: String = ""
        private set

    var itemPosition: Vector2 by propertyNotify(Vector2.Zero)

    override fun onEvent(event: Event) {
        when (event) {
            is PlayerQuestItemBegin -> with(world) {
                playerEntity = event.player

                val itemRegion = event.item[Graphic].region
                itemDrawable = TextureRegionDrawable(itemRegion)
                val amount = event.item[Item].amount
                itemName = when (amount) {
                    1 -> bundle["item.${event.item[Name].name}.name"]
                    else -> "${amount}x ${bundle["item.${event.item[Name].name}.name"]}"
                }

                onResize()
            }

            is PlayerQuestItemEnd -> {
                itemPosition = Vector2.Zero
                playerEntity = Entity.NONE
            }

            is GameResizeEvent -> onResize()
            else -> Unit
        }
    }

    private fun onResize() = with(world) {
        if (playerEntity == Entity.NONE) {
            return@with
        }

        val (position, size) = playerEntity[Transform]
        itemPosition = vec2(position.x + 0.15f, position.y + size.y + 0.1f)
            .toUiPosition(gameViewport, uiViewport)
    }
}
