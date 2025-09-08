package com.bitfire.postprocessing.filters

import com.bitfire.utils.ShaderLoader

class Threshold : Filter<Threshold>(ShaderLoader.fromFile("screenspace", "threshold")) {

    private var gamma = 0F

    init {
        rebind()
    }

    fun setTreshold(gamma: Float) {
        this.gamma = gamma
        setParams(Param.Threshold, gamma)
        setParams(Param.ThresholdInvTx, 1F / (1 - gamma)).endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
    }

    override fun rebind() {
        setParams(Param.Texture, u_texture0)
        setTreshold(this.gamma)
    }

    enum class Param(private val mnemonic: String, private val elementSize: Int) : Parameter {
        Texture("u_texture0", 0), Threshold("treshold", 0), ThresholdInvTx("tresholdInvTx", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.elementSize
    }
}
