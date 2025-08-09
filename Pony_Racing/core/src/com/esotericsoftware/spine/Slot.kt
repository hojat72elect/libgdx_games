package com.esotericsoftware.spine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.FloatArray
import com.esotericsoftware.spine.attachments.Attachment

class Slot {
    @JvmField
    val data: SlotData

    @JvmField
    val bone: Bone

    @JvmField
    val color: Color

    @JvmField
    val skeleton: Skeleton

    @JvmField
    val attachmentVertices: FloatArray = FloatArray()

    @JvmField
    var attachment: Attachment? = null
    private var attachmentTime = 0f

    constructor(data: SlotData, skeleton: Skeleton, bone: Bone) {
        this.data = data
        this.skeleton = skeleton
        this.bone = bone
        color = Color()
        setToSetupPose()
    }

    constructor(slot: Slot, skeleton: Skeleton, bone: Bone) {
        data = slot.data
        this.skeleton = skeleton
        this.bone = bone
        color = Color(slot.color)
        attachment = slot.attachment
        attachmentTime = slot.attachmentTime
    }


    fun getAttachment(): Attachment? {
        return attachment
    }

    fun setAttachment(attachment: Attachment?) {
        if (this.attachment === attachment) return
        this.attachment = attachment
        attachmentTime = skeleton.time
        attachmentVertices.clear()
    }

    fun setToSetupPose(slotIndex: Int) {
        color.set(data.color)
        setAttachment(if (data.attachmentName == null) null else skeleton.getAttachment(slotIndex, data.attachmentName))
    }

    fun setToSetupPose() {
        setToSetupPose(skeleton.data.slots.indexOf(data, true))
    }

    override fun toString(): String {
        return data.name!!
    }
}
