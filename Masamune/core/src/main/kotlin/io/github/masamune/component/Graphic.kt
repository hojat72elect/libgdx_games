package io.github.masamune.component

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.Masamune.Companion.UNIT_SCALE
import ktx.graphics.color
import ktx.math.vec2

/**
 * Component for graphical data of an entity to render it to the screen.
 * The data consists of:
 * - [region]: the [TextureRegion]
 * - [color]: the tinting [Color]. Default value is white
 */
class Graphic(
    region: TextureRegion,
    val color: Color = color(1f, 1f, 1f, 1f),
) : Component<Graphic> {

    val regionSize: Vector2 = vec2(region.regionWidth * UNIT_SCALE, region.regionHeight * UNIT_SCALE)
    var region: TextureRegion = region
        set(value) {
            regionSize.x = value.regionWidth * UNIT_SCALE
            regionSize.y = value.regionHeight * UNIT_SCALE
            field = value
        }

    operator fun component1() = region

    operator fun component2() = color

    override fun type() = Graphic

    companion object : ComponentType<Graphic>()
}
