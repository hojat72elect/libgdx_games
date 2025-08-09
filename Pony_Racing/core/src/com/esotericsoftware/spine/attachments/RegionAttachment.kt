package com.esotericsoftware.spine.attachments

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.NumberUtils
import com.esotericsoftware.spine.Slot

/**
 * Attachment that displays a texture region.
 */
class RegionAttachment(name: String) : Attachment(name) {
    val worldVertices = FloatArray(20)
    private val offset = FloatArray(8)

    @JvmField
    val color = Color(1f, 1f, 1f, 1f)
    private var region: TextureRegion? = null

    @JvmField
    var x = 0f

    @JvmField
    var y = 0f

    @JvmField
    var scaleX = 1f

    @JvmField
    var scaleY = 1f

    @JvmField
    var rotation = 0f

    @JvmField
    var width = 0f

    @JvmField
    var height = 0f

    fun updateOffset() {
        val width = this.width
        val height = this.height
        var localX2 = width / 2
        var localY2 = height / 2
        var localX = -localX2
        var localY = -localY2
        if (region is AtlasRegion) {
            val region = this.region as AtlasRegion
            if (region.rotate) {
                localX += region.offsetX / region.originalWidth * width
                localY += region.offsetY / region.originalHeight * height
                localX2 -= (region.originalWidth - region.offsetX - region.packedHeight) / region.originalWidth * width
                localY2 -= (region.originalHeight - region.offsetY - region.packedWidth) / region.originalHeight * height
            } else {
                localX += region.offsetX / region.originalWidth * width
                localY += region.offsetY / region.originalHeight * height
                localX2 -= (region.originalWidth - region.offsetX - region.packedWidth) / region.originalWidth * width
                localY2 -= (region.originalHeight - region.offsetY - region.packedHeight) / region.originalHeight * height
            }
        }
        val scaleX = this.scaleX
        val scaleY = this.scaleY
        localX *= scaleX
        localY *= scaleY
        localX2 *= scaleX
        localY2 *= scaleY
        val rotation = this.rotation
        val cos = MathUtils.cosDeg(rotation)
        val sin = MathUtils.sinDeg(rotation)
        val x = this.x
        val y = this.y
        val localXCos = localX * cos + x
        val localXSin = localX * sin
        val localYCos = localY * cos + y
        val localYSin = localY * sin
        val localX2Cos = localX2 * cos + x
        val localX2Sin = localX2 * sin
        val localY2Cos = localY2 * cos + y
        val localY2Sin = localY2 * sin
        val offset = this.offset
        offset[BLX] = localXCos - localYSin
        offset[BLY] = localYCos + localXSin
        offset[ULX] = localXCos - localY2Sin
        offset[ULY] = localY2Cos + localXSin
        offset[URX] = localX2Cos - localY2Sin
        offset[URY] = localY2Cos + localX2Sin
        offset[BRX] = localX2Cos - localYSin
        offset[BRY] = localYCos + localX2Sin
    }

    fun getRegion() = region!!

    fun setRegion(region: TextureRegion) {
        this.region = region
        val vertices = this.worldVertices
        if (region is AtlasRegion && region.rotate) {
            vertices[Batch.U3] = region.getU()
            vertices[Batch.V3] = region.getV2()
            vertices[Batch.U4] = region.getU()
            vertices[Batch.V4] = region.getV()
            vertices[Batch.U1] = region.getU2()
            vertices[Batch.V1] = region.getV()
            vertices[Batch.U2] = region.getU2()
            vertices[Batch.V2] = region.getV2()
        } else {
            vertices[Batch.U2] = region.getU()
            vertices[Batch.V2] = region.getV2()
            vertices[Batch.U3] = region.getU()
            vertices[Batch.V3] = region.getV()
            vertices[Batch.U4] = region.getU2()
            vertices[Batch.V4] = region.getV()
            vertices[Batch.U1] = region.getU2()
            vertices[Batch.V1] = region.getV2()
        }
        updateOffset()
    }

    fun updateWorldVertices(slot: Slot, premultipliedAlpha: Boolean) {
        val skeleton = slot.skeleton
        val skeletonColor = skeleton.getColor()
        val slotColor = slot.color
        val regionColor = color
        val a = skeletonColor.a * slotColor.a * regionColor.a * 255
        val multiplier = if (premultipliedAlpha) a else 255f
        val color = NumberUtils.intToFloatColor(
            ((a.toInt() shl 24)
                    or ((skeletonColor.b * slotColor.b * regionColor.b * multiplier).toInt() shl 16)
                    or ((skeletonColor.g * slotColor.g * regionColor.g * multiplier).toInt() shl 8)
                    or (skeletonColor.r * slotColor.r * regionColor.r * multiplier).toInt())
        )

        val vertices = this.worldVertices
        val offset = this.offset
        val bone = slot.bone
        val x = skeleton.getX() + bone.getWorldX()
        val y = skeleton.getY() + bone.getWorldY()
        val m00 = bone.getM00()
        val m01 = bone.getM01()
        val m10 = bone.getM10()
        val m11 = bone.getM11()

        var offsetX = offset[BRX]
        var offsetY = offset[BRY]
        vertices[Batch.X1] = offsetX * m00 + offsetY * m01 + x // br
        vertices[Batch.Y1] = offsetX * m10 + offsetY * m11 + y
        vertices[Batch.C1] = color

        offsetX = offset[BLX]
        offsetY = offset[BLY]
        vertices[Batch.X2] = offsetX * m00 + offsetY * m01 + x // bl
        vertices[Batch.Y2] = offsetX * m10 + offsetY * m11 + y
        vertices[Batch.C2] = color

        offsetX = offset[ULX]
        offsetY = offset[ULY]
        vertices[Batch.X3] = offsetX * m00 + offsetY * m01 + x // ul
        vertices[Batch.Y3] = offsetX * m10 + offsetY * m11 + y
        vertices[Batch.C3] = color

        offsetX = offset[URX]
        offsetY = offset[URY]
        vertices[Batch.X4] = offsetX * m00 + offsetY * m01 + x // ur
        vertices[Batch.Y4] = offsetX * m10 + offsetY * m11 + y
        vertices[Batch.C4] = color
    }

    companion object {
        const val BLX = 0
        const val BLY = 1
        const val ULX = 2
        const val ULY = 3
        const val URX = 4
        const val URY = 5
        const val BRX = 6
        const val BRY = 7
    }
}
