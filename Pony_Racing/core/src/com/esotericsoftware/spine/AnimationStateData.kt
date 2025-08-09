package com.esotericsoftware.spine

import com.badlogic.gdx.utils.ObjectFloatMap

/**
 * Stores mixing times between animations.
 */
class AnimationStateData(val skeletonData: SkeletonData) {
    val animationToMixTime = ObjectFloatMap<Key>()
    val tempKey = Key()
    var defaultMix = 0F

    fun setMix(fromName: String, toName: String, duration: Float) {
        val from = skeletonData.findAnimation(fromName)
        requireNotNull(from) { "Animation not found: $fromName" }
        val to = skeletonData.findAnimation(toName)
        requireNotNull(to) { "Animation not found: $toName" }
        setMix(from, to, duration)
    }

    fun setMix(from: Animation, to: Animation, duration: Float) {
        val key = Key()
        key.a1 = from
        key.a2 = to
        animationToMixTime.put(key, duration)
    }

    fun getMix(from: Animation, to: Animation): Float {
        tempKey.a1 = from
        tempKey.a2 = to
        val time = animationToMixTime.get(tempKey, Float.Companion.MIN_VALUE)
        if (time == Float.Companion.MIN_VALUE) return defaultMix
        return time
    }

    inner class Key {
        var a1: Animation? = null
        var a2: Animation? = null

        override fun hashCode(): Int {
            return 31 * (31 + a1.hashCode()) + a2.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null) return false
            val otherKey = other as Key
            if (a1 == null) {
                if (otherKey.a1 != null) return false
            } else if (a1 != otherKey.a1) return false
            return if (a2 == null) {
                otherKey.a2 == null
            } else a2 == otherKey.a2
        }
    }
}