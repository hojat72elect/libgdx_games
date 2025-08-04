package com.esotericsoftware.spine.attachments

import com.esotericsoftware.spine.Bone

class BoundingBoxAttachment(name: String) : Attachment(name) {
    lateinit var vertices: FloatArray

    fun computeWorldVertices(x: Float, y: Float, bone: Bone, worldVertices: FloatArray) {
        var tempX = x
        var tempY = y
        tempX += bone.worldX
        tempY += bone.worldY
        val m00 = bone.m00
        val m01 = bone.m01
        val m10 = bone.m10
        val m11 = bone.m11
        var i = 0

        while (i < vertices.size) {
            val px = vertices[i]
            val py = vertices[i + 1]
            worldVertices[i] = px * m00 + py * m01 + tempX
            worldVertices[i + 1] = px * m10 + py * m11 + tempY
            i += 2
        }
    }
}