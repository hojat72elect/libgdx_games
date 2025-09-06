package com.bitfire.postprocessing.filters

import com.bitfire.utils.ShaderLoader

/**
 * Bias filter. Adapted for LensFlare effect. For more info look [here](http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html)
 */
class Bias : Filter<Bias>(ShaderLoader.fromFile("screenspace", "bias")) {
    private var bias = 0F

    init {
        rebind()
    }

    fun setBias(bias: Float) {
        this.bias = bias
        setParam(Param.Bias, this.bias)
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
    }

    override fun rebind() {
        setParams(Param.Texture, u_texture0)
        setBias(this.bias)
    }

    enum class Param(private val mnemonic: String, private val elementSize: Int) : Parameter {
        Texture("u_texture0", 0), Bias("u_bias", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.elementSize
    }
}
