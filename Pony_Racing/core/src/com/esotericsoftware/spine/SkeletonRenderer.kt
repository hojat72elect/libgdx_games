package com.esotericsoftware.spine

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.esotericsoftware.spine.attachments.MeshAttachment
import com.esotericsoftware.spine.attachments.RegionAttachment
import com.esotericsoftware.spine.attachments.SkeletonAttachment

class SkeletonRenderer {

    private val quadTriangle = shortArrayOf(0, 1, 2, 2, 3, 0)

    fun draw(batch: PolygonSpriteBatch, skeleton: Skeleton) {
        val premultipliedAlpha = false
        val srcFunc = GL20.GL_SRC_ALPHA
        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA)

        var additive = false

        var vertices: FloatArray
        var triangles: ShortArray
        var texture: Texture?
        val drawOrder = skeleton.drawOrder
        var i = 0
        val n = drawOrder.size
        while (i < n) {
            val slot = drawOrder.get(i)
            val attachment = slot.attachment
            if (attachment is RegionAttachment) {
                val region = attachment
                region.updateWorldVertices(slot, premultipliedAlpha)
                vertices = region.worldVertices
                triangles = quadTriangle
                texture = region.getRegion().getTexture()

                if (slot.data.additiveBlending != additive) {
                    additive = !additive
                    if (additive) batch.setBlendFunction(srcFunc, GL20.GL_ONE)
                    else batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA)
                }

                batch.draw(texture, vertices, 0, vertices.size, triangles, 0, triangles.size)
            } else if (attachment is MeshAttachment) {
                val mesh = attachment
                mesh.updateWorldVertices(slot, true)
                vertices = mesh.worldVertices
                triangles = mesh.triangles
                texture = mesh.getRegion().getTexture()
                batch.draw(texture, vertices, 0, vertices.size, triangles, 0, triangles.size)
            } else if (attachment is SkeletonAttachment) {
                val attachmentSkeleton = attachment.skeleton
                if (attachmentSkeleton == null) {
                    i++
                    continue
                }
                val bone = slot.bone
                val rootBone = attachmentSkeleton.getRootBone()
                val oldScaleX = rootBone.scaleX
                val oldScaleY = rootBone.scaleY
                val oldRotation = rootBone.rotation
                attachmentSkeleton.setX(skeleton.getX() + bone.worldX)
                attachmentSkeleton.setY(skeleton.getY() + bone.worldY)
                rootBone.scaleX = 1 + bone.worldScaleX - oldScaleX
                rootBone.scaleY = 1 + bone.worldScaleY - oldScaleY
                rootBone.rotation = oldRotation + bone.worldRotation
                attachmentSkeleton.updateWorldTransform()

                draw(batch, attachmentSkeleton)

                attachmentSkeleton.setX(0f)
                attachmentSkeleton.setY(0f)
                rootBone.scaleX = oldScaleX
                rootBone.scaleY = oldScaleY
                rootBone.rotation = oldRotation
            }
            i++
        }
    }

    fun draw(batch: Batch, skeleton: Skeleton) {
        val premultipliedAlpha = false
        val srcFunc = GL20.GL_SRC_ALPHA
        batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA)

        var additive = false

        val drawOrder = skeleton.drawOrder
        var i = 0
        val n = drawOrder.size
        while (i < n) {
            val slot = drawOrder.get(i)
            val attachment = slot.attachment
            if (attachment is RegionAttachment) {
                val regionAttachment = attachment
                regionAttachment.updateWorldVertices(slot, premultipliedAlpha)
                val vertices = regionAttachment.worldVertices
                if (slot.data.additiveBlending != additive) {
                    additive = !additive
                    if (additive) batch.setBlendFunction(srcFunc, GL20.GL_ONE)
                    else batch.setBlendFunction(srcFunc, GL20.GL_ONE_MINUS_SRC_ALPHA)
                }
                batch.draw(regionAttachment.getRegion().getTexture(), vertices, 0, 20)
            } else if (attachment is SkeletonAttachment) {
                val attachmentSkeleton = attachment.skeleton
                if (attachmentSkeleton == null) {
                    i++
                    continue
                }
                val bone = slot.bone
                val rootBone = attachmentSkeleton.getRootBone()
                val oldScaleX = rootBone.scaleX
                val oldScaleY = rootBone.scaleY
                val oldRotation = rootBone.rotation
                attachmentSkeleton.setX(skeleton.getX() + bone.worldX)
                attachmentSkeleton.setY(skeleton.getY() + bone.worldY)
                rootBone.scaleX = 1 + bone.worldScaleX - oldScaleX
                rootBone.scaleY = 1 + bone.worldScaleY - oldScaleY
                rootBone.rotation = oldRotation + bone.worldRotation
                attachmentSkeleton.updateWorldTransform()

                draw(batch, attachmentSkeleton)

                attachmentSkeleton.setX(0f)
                attachmentSkeleton.setY(0f)
                rootBone.scaleX = oldScaleX
                rootBone.scaleY = oldScaleY
                rootBone.rotation = oldRotation
            }
            i++
        }
    }

}
