package com.mygdx.game.terrains

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable

abstract class Terrain : Disposable {

    // A value we can set to change the actual size of the terrain
    @JvmField
    protected var size = 0

    // Represents the width in vertices, for heightmaps this would be the height map image width
    @JvmField
    protected var width = 0

    // A height scaling factor since heightmap values and noise values are generally between 0.0 - 1.0
    @JvmField
    protected var heightMagnitude = 0F

    @JvmField
    protected var modelInstance: ModelInstance? = null

    fun getModelInstance() = modelInstance

    abstract fun getHeightAtWorldCoord(worldX: Float, worldZ: Float): Float

}