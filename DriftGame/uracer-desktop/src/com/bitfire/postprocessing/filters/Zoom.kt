package com.bitfire.postprocessing.filters

import com.bitfire.utils.ShaderLoader

class Zoom : Filter<Zoom>(ShaderLoader.fromFile("zoom", "zoom")) {

    private var x = 0F
    private var y = 0F
    private var zoom = 0F

    init {
        rebind()
        setOrigin(0.5F, 0.5F)
        setZoom(1F)
    }

    /**
     * Specify the zoom origin, in normalized screen coordinates.
     */
    fun setOrigin(x: Float, y: Float) {
        this.x = x
        this.y = y
        setParams(Param.OffsetX, this.x)
        setParams(Param.OffsetY, this.y)
        endParams()
    }

    fun getZoom() = zoom

    fun setZoom(zoom: Float) {
        this.zoom = zoom
        setParam(Param.Zoom, this.zoom)
    }

    override fun rebind() {
        // reimplement super to batch every parameter
        setParams(Param.Texture, u_texture0)
        setParams(Param.OffsetX, x)
        setParams(Param.OffsetY, y)
        setParams(Param.Zoom, zoom)
        endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
    }

    enum class Param(private val mnemonic: String, private val elementSize: Int) : Parameter {
        Texture("u_texture0", 0),
        OffsetX("offset_x", 0),
        OffsetY("offset_y", 0),
        Zoom("zoom", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.elementSize
    }
}
