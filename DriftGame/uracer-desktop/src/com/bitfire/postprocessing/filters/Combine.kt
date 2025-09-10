package com.bitfire.postprocessing.filters

import com.badlogic.gdx.graphics.Texture
import com.bitfire.utils.ShaderLoader

/**
 * This filter takes 2 input textures and combines then based on the specified intensity and saturation values
 * for each source.
 */
class Combine : Filter<Combine>(ShaderLoader.fromFile("screenspace", "combine")) {

    private var s1i = 1F
    private var s1s = 1F
    private var s2i = 1F
    private var s2s = 1F
    private var inputTexture2: Texture? = null

    init {
        rebind()
    }

    fun setInput(texture1: Texture, texture2: Texture): Combine {
        this.inputTexture = texture1
        this.inputTexture2 = texture2
        return this
    }

    fun setSource1Intensity(intensity: Float) {
        s1i = intensity
        setParam(Param.Source1Intensity, intensity)
    }

    fun setSource2Intensity(intensity: Float) {
        s2i = intensity
        setParam(Param.Source2Intensity, intensity)
    }

    fun setSource1Saturation(saturation: Float) {
        s1s = saturation
        setParam(Param.Source1Saturation, saturation)
    }

    fun setSource2Saturation(saturation: Float) {
        s2s = saturation
        setParam(Param.Source2Saturation, saturation)
    }

    override fun rebind() {
        setParams(Param.Texture0, u_texture0)
        setParams(Param.Texture1, u_texture1)
        setParams(Param.Source1Intensity, s1i)
        setParams(Param.Source2Intensity, s2i)
        setParams(Param.Source1Saturation, s1s)
        setParams(Param.Source2Saturation, s2s)
        endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
        inputTexture2!!.bind(u_texture1)
    }

    enum class Param(private val mnemonic: String, private val elementSize: Int) : Parameter {

        Texture0("u_texture0", 0),
        Texture1("u_texture1", 0),
        Source1Intensity("Src1Intensity", 0),
        Source1Saturation("Src1Saturation", 0),
        Source2Intensity("Src2Intensity", 0),
        Source2Saturation("Src2Saturation", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.elementSize
    }
}
