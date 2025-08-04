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

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            val other = obj as Key
            if (a1 == null) {
                if (other.a1 != null) return false
            } else if (a1 != other.a1) return false
            if (a2 == null) {
                return other.a2 == null
            } else return a2 == other.a2
        }
    }
}