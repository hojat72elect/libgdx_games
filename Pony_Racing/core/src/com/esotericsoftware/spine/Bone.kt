package com.esotericsoftware.spine

import com.badlogic.gdx.math.MathUtils

class Bone {
    @JvmField
    val data: BoneData

    @JvmField
    val parent: Bone?

    @JvmField
    var x: Float = 0f

    @JvmField
    var y: Float = 0f

    @JvmField
    var rotation: Float = 0f

    @JvmField
    var scaleX: Float = 0f

    @JvmField
    var scaleY: Float = 0f

    @JvmField
    var m00: Float = 0f

    @JvmField
    var m01: Float = 0f

    @JvmField
    var worldX: Float = 0f // a b x

    @JvmField
    var m10: Float = 0f

    @JvmField
    var m11: Float = 0f

    @JvmField
    var worldY: Float = 0f // c d y

    @JvmField
    var worldRotation: Float = 0f

    @JvmField
    var worldScaleX: Float = 0f

    @JvmField
    var worldScaleY: Float = 0f

    /**
     * @param parent May be null.
     */
    constructor(data: BoneData, parent: Bone?) {
        requireNotNull(data) { "data cannot be null." }
        this.data = data
        this.parent = parent
        setToSetupPose()
    }

    /**
     * Copy constructor.
     *
     * @param parent May be null.
     */
    constructor(bone: Bone, parent: Bone?) {
        requireNotNull(bone) { "bone cannot be null." }
        this.parent = parent
        data = bone.data
        x = bone.x
        y = bone.y
        rotation = bone.rotation
        scaleX = bone.scaleX
        scaleY = bone.scaleY
    }

    /**
     * Computes the world SRT using the parent bone and the local SRT.
     */
    fun updateWorldTransform(flipX: Boolean, flipY: Boolean) {
        val parent = this.parent
        if (parent != null) {
            worldX = x * parent.m00 + y * parent.m01 + parent.worldX
            worldY = x * parent.m10 + y * parent.m11 + parent.worldY
            if (data.inheritScale) {
                worldScaleX = parent.worldScaleX * scaleX
                worldScaleY = parent.worldScaleY * scaleY
            } else {
                worldScaleX = scaleX
                worldScaleY = scaleY
            }
            worldRotation = if (data.inheritRotation) parent.worldRotation + rotation else rotation
        } else {
            worldX = if (flipX) -x else x
            worldY = if (flipY) -y else y
            worldScaleX = scaleX
            worldScaleY = scaleY
            worldRotation = rotation
        }
        val cos = MathUtils.cosDeg(worldRotation)
        val sin = MathUtils.sinDeg(worldRotation)
        m00 = cos * worldScaleX
        m10 = sin * worldScaleX
        m01 = -sin * worldScaleY
        m11 = cos * worldScaleY
        if (flipX) {
            m00 = -m00
            m01 = -m01
        }
        if (flipY) {
            m10 = -m10
            m11 = -m11
        }
    }

    fun setToSetupPose() {
        val data = this.data
        x = data.x
        y = data.y
        rotation = data.rotation
        scaleX = data.scaleX
        scaleY = data.scaleY
    }

    override fun toString(): String {
        return data.name
    }
}
