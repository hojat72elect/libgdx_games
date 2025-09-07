package com.bitfire.uracer.game.debug

class RankInfo : Comparable<RankInfo> {

    @JvmField
    var completion = 0F

    @JvmField
    var uid = ""

    @JvmField
    var ticks = 0

    @JvmField
    var valid = false

    @JvmField
    var player = false

    @JvmField
    var isNextTarget = false

    fun reset() {
        completion = 0F
        uid = ""
        ticks = 0
        valid = false
        player = false
        isNextTarget = false
    }

    override fun compareTo(other: RankInfo): Int {
        if (valid.not()) return 1
        if (other.valid.not()) return -1
        if (completion == 0F && player) return 1 // player at bottom if in warmup
        if (other.completion == 0F && other.player) return -1 // player at bottom if in warmup

        if (completion == 0F || other.completion == 0F) {
            // order by time
            if (ticks < other.ticks) return -1
            if (ticks > other.ticks) return 1
        } else {
            if (completion > other.completion) return -1
            if (completion < other.completion) return 1
        }

        return 0
    }
}
