package com.bitfire.postprocessing.filters

import com.bitfire.utils.ShaderLoader.fromFile

class RadialBlur(quality: Quality) : Filter<RadialBlur>(
    fromFile("radial-blur", "radial-blur", ("#define BLUR_LENGTH " + quality.length
                + "\n#define ONE_ON_BLUR_LENGTH " + 1F / quality.length))) {

    private val blur_len = quality.length
    private var strength = 0F
    private var x = 0F
    private var y = 0F
    private var zoom = 0F

    init {
        rebind()
        setOrigin(0.5F, 0.5F)
        setStrength(0.5F)
        setZoom(1F)
    }

    fun setOrigin(x: Float, y: Float) {
        this.x = x
        this.y = y
        setParams(Param.OffsetX, x)
        setParams(Param.OffsetY, y)
        endParams()
    }

    fun getZoom() = zoom

    fun setZoom(zoom: Float) {
        this.zoom = zoom
        setParam(Param.Zoom, this.zoom)
    }

    fun setStrength(strength: Float) {
        this.strength = strength
        setParam(Param.BlurDiv, strength / blur_len)
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
    }

    override fun rebind() {
        setParams(Param.Texture, u_texture0)
        setParams(Param.BlurDiv, this.strength / blur_len.toFloat())
        setParams(Param.OffsetX, x)
        setParams(Param.OffsetY, y)
        setParams(Param.Zoom, zoom)
        endParams()
    }

    enum class Quality(val length: Int) {
        VeryHigh(16),
        High(8),
        Normal(5),
        Medium(4),
        Low(2);
    }

    enum class Param(private val mnemonic: String, private val arrayElementSize: Int) : Parameter {
        Texture("u_texture0", 0),
        BlurDiv("blur_div", 0),
        OffsetX("offset_x", 0),
        OffsetY("offset_y", 0),
        Zoom("zoom", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.arrayElementSize
    }
}
