package com.bitfire.uracer.game.events

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.bitfire.postprocessing.PostProcessor

class GameRendererEvent() : Event<GameRendererEvent.Type, GameRendererEvent.Order>(Type::class.java, Order::class.java) {

    @JvmField
    var batch: SpriteBatch? = null

    @JvmField
    var mtxOrthographicMvpMt: Matrix4? = null

    @JvmField
    var camPersp: PerspectiveCamera? = null

    @JvmField
    var camOrtho: OrthographicCamera? = null

    @JvmField
    var timeAliasingFactor = 0F

    @JvmField
    var camZoom = 0F

    @JvmField
    var postProcessor: PostProcessor? = null

    /**
     * defines the type of render queue
     */
    enum class Type {
        SubframeInterpolate,  // interpolate positions and orientations
        BeforeRender,  // update rendering data such as camera position and zoom
        BatchBeforeCars,  // draw *before* cars are drawn
        BatchAfterCars,  // draw *after* cars are drawn
        BatchBeforePostProcessing,  // draw before the post-processing passes
        BatchAfterPostProcessing,  // draw after all the post-processing passes
        BatchDebug,  // debug draw (via SpriteBatch)
        Debug // debug draw (via default GL calls)
    }

    /**
     * defines the position in the render queue specified by the Type parameter
     */
    enum class Order {
        MINUS_4, MINUS_3, MINUS_2, MINUS_1, DEFAULT, PLUS_1, PLUS_2, PLUS_3, PLUS_4
    }

    fun interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
