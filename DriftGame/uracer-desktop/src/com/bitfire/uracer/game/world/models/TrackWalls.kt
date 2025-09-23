package com.bitfire.uracer.game.world.models

class TrackWalls(@JvmField val models: MutableList<OrthographicAlignedStillModel>) {

    fun count() = models.size
}
