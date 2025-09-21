package com.bitfire.uracer.utils

import java.util.*

class SortedProperties : Properties() {
    /**
     * Overrides, called by the store method.
     */
    @Synchronized
    override fun keys(): Enumeration<Any> {
        val keysEnum = super.keys()
        val keys = ArrayList<Any>()
        while (keysEnum.hasMoreElements()) {
            keys.add(keysEnum.nextElement())
        }
        keys.sortWith { o1: Any, o2: Any ->
            val s1 = o1 as String
            val s2 = o2 as String
            s1.compareTo(s2, ignoreCase = true)
        }
        return Collections.enumeration(keys)
    }
}
