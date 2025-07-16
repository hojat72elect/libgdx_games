package com.salvai.centrum.levels

data class BallInfo(
    @JvmField
    var x: Int = 0,
    @JvmField
    var y: Int = 0,
    @JvmField
    var time: Int = 0,
    @JvmField
    var speed: Int = 0,
    @JvmField
    var color: Int = 0 // index of basics color array
)
