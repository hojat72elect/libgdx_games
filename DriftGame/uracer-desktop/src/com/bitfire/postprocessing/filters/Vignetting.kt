package com.bitfire.postprocessing.filters

import com.badlogic.gdx.graphics.Texture
import com.bitfire.utils.ShaderLoader.fromFile

class Vignetting(private val dosat: Boolean) : Filter<Vignetting>(
    fromFile("screenspace", "vignetting", (if (dosat) "#define CONTROL_SATURATION\n#define ENABLE_GRADIENT_MAPPING" else "#define ENABLE_GRADIENT_MAPPING"))
) {

    private var x = 0F
    private var y = 0F
    private var intensity = 0F
    private var texLut: Texture? = null
    private var dolut = false
    private var lutintensity: Float = 1f
    private val lutindex = intArrayOf(-1, -1)
    private var lutStep = 0F
    private var lutStepOffset = 0F
    private var lutIndexOffset = 0F
    private var centerX = 0F
    private var centerY = 0F

    init {
        rebind()
        setCoords(0.8F, 0.25F)
        setCenter(0.5F, 0.5F)
        setIntensity(1F)
    }

    fun setCoords(x: Float, y: Float) {
        this.x = x
        this.y = y
        setParams(Param.VignetteX, x)
        setParams(Param.VignetteY, y)
        endParams()
    }

    fun setLutIndexVal(index: Int, value: Int) {
        lutindex[index] = value

        when (index) {
            0 -> setParam(Param.LutIndex, lutindex[0])
            1 -> setParam(Param.LutIndex2, lutindex[1])
        }
    }

    fun setLutIndexOffset(value: Float) {
        lutIndexOffset = value
        setParam(Param.LutIndexOffset, lutIndexOffset)
    }

    /**
     * Specify the center, in normalized screen coordinates.
     */
    fun setCenter(x: Float, y: Float) {
        this.centerX = x
        this.centerY = y
        setParams(Param.CenterX, centerX)
        setParams(Param.CenterY, centerY).endParams()
    }

    fun setLutIntensity(value: Float) {
        lutintensity = value
        setParam(Param.LutIntensity, lutintensity)
    }

    /**
     * Sets the texture with which gradient mapping will be performed.
     */
    fun setLut(texture: Texture?) {
        texLut = texture
        dolut = (texLut != null)

        if (dolut) {
            lutStep = 1F / texture!!.height
            lutStepOffset = lutStep / 2F // center texel
            setParams(Param.TexLUT, u_texture1)
            setParams(Param.LutStep, lutStep)
            setParams(Param.LutStepOffset, lutStepOffset).endParams()
        }
    }

    fun getX() = x

    fun getY() = y

    fun setIntensity(intensity: Float) {
        this.intensity = intensity
        setParam(Param.VignetteIntensity, intensity)
    }

    override fun rebind() {

        setParams(Param.Texture0, u_texture0)
        setParams(Param.LutIndex, lutindex[0])
        setParams(Param.LutIndex2, lutindex[1])
        setParams(Param.LutIndexOffset, lutIndexOffset)
        setParams(Param.TexLUT, u_texture1)
        setParams(Param.LutIntensity, lutintensity)
        setParams(Param.LutStep, lutStep)
        setParams(Param.LutStepOffset, lutStepOffset)

        if (dosat) {
            setParams(Param.Saturation, 0f)
            setParams(Param.SaturationMul, 0f)
        }

        setParams(Param.VignetteIntensity, intensity)
        setParams(Param.VignetteX, x)
        setParams(Param.VignetteY, y)
        setParams(Param.CenterX, centerX)
        setParams(Param.CenterY, centerY)
        endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
        if (dolut) texLut?.bind(u_texture1)
    }

    enum class Param(private val mnemonic: String, private val elementSize: Int) : Parameter {
        Texture0("u_texture0", 0),
        TexLUT("u_texture1", 0),
        VignetteIntensity("VignetteIntensity", 0),
        VignetteX("VignetteX", 0),
        VignetteY("VignetteY", 0),
        Saturation("Saturation", 0),
        SaturationMul("SaturationMul", 0),
        LutIntensity("LutIntensity", 0),
        LutIndex("LutIndex", 0),
        LutIndex2("LutIndex2", 0),
        LutIndexOffset("LutIndexOffset", 0),
        LutStep("LutStep", 0),
        LutStepOffset("LutStepOffset", 0),
        CenterX("CenterX", 0),
        CenterY("CenterY", 0);

        override fun mnemonic() = mnemonic
        override fun arrayElementSize()=elementSize
    }
}
