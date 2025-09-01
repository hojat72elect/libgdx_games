package com.bitfire.uracer.entities

import com.badlogic.gdx.utils.Disposable

abstract class Entity : Disposable {

    // screen coordinates
    protected val stateRender = EntityRenderState()

    // Returns the last interpolated entity state
    fun state() = stateRender
}
