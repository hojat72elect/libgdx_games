package com.esotericsoftware.spine

import com.badlogic.gdx.utils.ObjectMap
import com.esotericsoftware.spine.attachments.Attachment

/**
 * Stores attachments by slot index and attachment name.
 * This is an organized way of managing a collection of attachments for a skeletal animation.
 */
class Skin(val name: String) {

    private val attachments = ObjectMap<Key, Attachment>()

    fun addAttachment(slotIndex: Int, name: String, attachment: Attachment) {
        require(slotIndex >= 0) { "slotIndex must be >= 0." }
        val key = Key()
        key.set(slotIndex, name)
        attachments.put(key, attachment)
    }

    fun getAttachment(slotIndex: Int, name: String): Attachment? {
        require(slotIndex >= 0) { "slotIndex must be >= 0." }
        lookup.set(slotIndex, name)
        return attachments.get(lookup)
    }

    override fun toString() = name

    /**
     * Attach each attachment in this skin if the corresponding attachment in the old skin is currently attached.
     */
    fun attachAll(skeleton: Skeleton, oldSkin: Skin) {
        for (entry in oldSkin.attachments.entries()) {
            val slotIndex = entry.key.slotIndex
            val slot = skeleton.slots.get(slotIndex)
            if (slot.attachment === entry.value) {
                val attachment = getAttachment(slotIndex, entry.key.name)
                if (attachment != null) slot.setAttachment(attachment)
            }
        }
    }

    companion object {
        private val lookup = Key()
    }

    /**
     * This key allows us to efficiently store and retrieve our attachments in an ObjectMap.
     */
    class Key() {
        var slotIndex = 0
        lateinit var name: String
        var hashCode = 0

        fun set(slotName: Int, name: String) {
            this.slotIndex = slotName
            this.name = name
            hashCode = 31 * (31 + name.hashCode()) + slotIndex
        }

        override fun hashCode() = hashCode

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            val other = other as Key
            if (slotIndex != other.slotIndex) return false
            return name == other.name
        }

        override fun toString() = "$slotIndex:$name"

    }
}