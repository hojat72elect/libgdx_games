package com.esotericsoftware.spine

import com.badlogic.gdx.utils.Array as GdxArray

/**
 * This is a container for all the defining information of a single animated skeleton. It's a
 * recipe from which animated instances are created and controlled.
 */
class SkeletonData {
    val bones = GdxArray<BoneData>() // Ordered parents first.
    val slots = GdxArray<SlotData>() // Setup pose draw order.
    val skins = GdxArray<Skin>()
    val events = GdxArray<EventData>()
    val animations = GdxArray<Animation>()
    var name: String? = null
    var defaultSkin: Skin? = null

    fun addBone(bone: BoneData) {
        bones.add(bone)
    }

    fun findBone(boneName: String): BoneData? {
        val bones = this.bones
        var i = 0
        val n = bones.size
        while (i < n) {
            val bone = bones.get(i)
            if (bone.name == boneName) return bone
            i++
        }
        return null
    }

    /**
     * @return -1 if the bone was not found.
     */
    fun findBoneIndex(boneName: String): Int {
        val bones = this.bones
        var i = 0
        val n = bones.size
        while (i < n) {
            if (bones.get(i).name == boneName) return i
            i++
        }
        return -1
    }

    fun addSlot(slot: SlotData) {
        slots.add(slot)
    }

    /**
     * @return -1 if the bone was not found.
     */
    fun findSlotIndex(slotName: String): Int {
        val slots = this.slots
        var i = 0
        val n = slots.size
        while (i < n) {
            if (slots.get(i).name == slotName) return i
            i++
        }
        return -1
    }

    fun addSkin(skin: Skin) {
        skins.add(skin)
    }

    fun findSkin(skinName: String): Skin? {
        for (skin in skins) {
            if (skin.name == skinName) {
                return skin
            }
        }
        return null
    }

    fun addEvent(eventData: EventData) {
        events.add(eventData)
    }

    fun findEvent(eventDataName: String): EventData? {
        for (eventData in events) if (eventData.name == eventDataName) return eventData
        return null
    }

    fun addAnimation(animation: Animation) {
        animations.add(animation)
    }

    fun findAnimation(animationName: String): Animation? {
        val animations = this.animations
        var i = 0
        val n = animations.size
        while (i < n) {
            val animation = animations.get(i)
            if (animation.name == animationName) return animation
            i++
        }
        return null
    }

    override fun toString(): String {
        return name ?: super.toString()
    }

}