package com.bitfire.utils

object Hash {

    fun rsHash(str: String): Long {
        val b = 378_551
        var a = 63_689
        var hash = 0L

        for (i in 0..<str.length) {
            hash = hash * a + str[i].code.toLong()
            a *= b
        }

        return hash
    }

    fun apHash(str: String): Long {
        var hash = 0xAAAAAAAAL

        for (i in 0..<str.length) {
            hash = if ((i and 1) == 0) {
                hash xor ((hash shl 7) xor str[i].code.toLong() * (hash shr 3))
            } else {
                hash xor (((hash shl 11) + str[i].code.toLong() xor (hash shr 5)).inv())
            }
        }

        return hash
    }
}