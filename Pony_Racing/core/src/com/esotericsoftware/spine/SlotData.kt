package com.esotericsoftware.spine

import com.badlogic.gdx.graphics.Color

class SlotData(val name: String? = null, val boneData: BoneData? = null) {

    val color = Color(1f, 1f, 1f, 1f)
    var attachmentName: String? = null
    var additiveBlending = false

    override fun toString() = name ?: ""
}