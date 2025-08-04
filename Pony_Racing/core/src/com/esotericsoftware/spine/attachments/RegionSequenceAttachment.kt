package com.esotericsoftware.spine.attachments

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.esotericsoftware.spine.Slot
import kotlin.math.max
import kotlin.math.min

/**
 * Attachment that displays various texture regions over time.
 */
class RegionSequenceAttachment(name: String) : RegionAttachment(name) {

    var mode = Mode.FORWARD
    var frameTime = 0F // The time in seconds each frame is shown.
    var regions: Array<TextureRegion> = emptyArray()

    override fun updateWorldVertices(slot: Slot, premultipliedAlpha: Boolean) {
        checkNotNull(regions) { "Regions have not been set: $this" }

        var frameIndex = (slot.attachmentTime / frameTime).toInt()
        when (mode) {
            Mode.FORWARD -> frameIndex = min(regions.size - 1, frameIndex)
            Mode.FORWARD_LOOP -> frameIndex = frameIndex % regions.size
            Mode.PING_PONG -> {
                frameIndex = frameIndex % (regions.size * 2)
                if (frameIndex >= regions.size) frameIndex = regions.size - 1 - (frameIndex - regions.size)
            }

            Mode.RANDOM -> frameIndex = MathUtils.random(regions.size - 1)
            Mode.BACKWARD -> frameIndex = max(regions.size - frameIndex - 1, 0)
            Mode.BACKWARD_LOOP -> {
                frameIndex = frameIndex % regions.size
                frameIndex = regions.size - frameIndex - 1
            }
        }
        setRegion(regions[frameIndex])

        super.updateWorldVertices(slot, premultipliedAlpha)
    }

    enum class Mode {
        FORWARD, BACKWARD, FORWARD_LOOP, BACKWARD_LOOP, PING_PONG, RANDOM
    }
}