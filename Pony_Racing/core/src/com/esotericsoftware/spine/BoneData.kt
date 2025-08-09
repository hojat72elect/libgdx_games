package com.esotericsoftware.spine

import com.badlogic.gdx.graphics.Color

/**
 * Properties of a single bone in skeletal animations.
 * Bones in a skeletal animation system are usually hierarchical, which means bones can have parent bones.
 * This class only works for 2D skeletal animations (Spine app).
 */
class BoneData(val name: String, val parent: BoneData?) {
    val color = Color(1f, 1f, 1f, 1f)
    var length = 0f
    var x = 0f
    var y = 0f
    var rotation = 0f
    var scaleX = 1f
    var scaleY = 1f
    var inheritScale = true
    var inheritRotation = true

    override fun toString() = name

}