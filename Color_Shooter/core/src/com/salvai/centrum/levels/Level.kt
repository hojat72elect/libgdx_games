package com.salvai.centrum.levels

import com.badlogic.gdx.utils.Array as GdxArray

class Level {
    @JvmField
    val ballInfos = GdxArray<BallInfo>()

    @JvmField
    var thirdStarScore = 0

    @JvmField
    var secondStarScore = 0
}
