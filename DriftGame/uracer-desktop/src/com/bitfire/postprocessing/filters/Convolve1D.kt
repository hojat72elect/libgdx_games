package com.bitfire.postprocessing.filters

import com.bitfire.utils.ShaderLoader

class Convolve1D @JvmOverloads constructor(
    var length: Int = 0,
    var weights: FloatArray? = FloatArray(length),
    var offsets: FloatArray? = FloatArray(length * 2)
) : Filter<Convolve1D>(ShaderLoader.fromFile("screenspace", "convolve-1d", "#define LENGTH $length")) {

    init {
        rebind()
    }

    override fun dispose() {
        super.dispose()
        weights = null
        offsets = null
        length = 0
    }

    override fun rebind() {
        setParams(Param.Texture, u_texture0)
        setParamsv(Param.SampleWeights, weights, length)
        setParamsv(Param.SampleOffsets, offsets, length * 2)
        endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
    }

    enum class Param(private val mnemonic: String, val arrayElementSize: Int) : Parameter {

        Texture("u_texture0", 0), SampleWeights("SampleWeights", 1), SampleOffsets("SampleOffsets", 2);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.arrayElementSize
    }
}
