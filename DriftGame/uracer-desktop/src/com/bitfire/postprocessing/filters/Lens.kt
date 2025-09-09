package com.bitfire.postprocessing.filters

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.bitfire.utils.ShaderLoader

/**
 * Lens flare is a graphical effect that simulates the way that light scatters and reflects within a camera lens, creating visual
 * artifacts like glows, streaks, and rings around bright light sources.
 *
 * This class implements the lens flare effect as described in John Chapman's article (without lens dirt or diffraction starburst). Lens color image
 * (lenscolor.png) is located in src/main/resources/ folder.
 *
 * @see [http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html](http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html)
 */
class Lens(width: Int, height: Int) : Filter<Lens>(ShaderLoader.fromFile("screenspace", "lensflare2")) {

    private val viewportInverse = Vector2(1F / width, 1F / height)
    private var ghosts = 0
    private var haloWidth = 0F
    private var lensColorTexture: Texture? = null

    init {
        rebind()
    }

    fun setGhosts(ghosts: Int) {
        this.ghosts = ghosts
        setParam(Param.Ghosts, ghosts)
    }

    fun setHaloWidth(haloWidth: Float) {
        this.haloWidth = haloWidth
        setParam(Param.HaloWidth, haloWidth)
    }

    fun setLensColorTexture(tex: Texture) {
        this.lensColorTexture = tex
        setParam(Param.LensColor, u_texture1)
    }

    override fun rebind() {
        // Re-implement super to batch every parameter
        setParams(Param.Texture, u_texture0)
        setParams(Param.LensColor, u_texture1)
        setParams(Param.ViewportInverse, viewportInverse)
        setParams(Param.Ghosts, ghosts)
        setParams(Param.HaloWidth, haloWidth)
        endParams()
    }

    override fun onBeforeRender() {
        inputTexture.bind(u_texture0)
        lensColorTexture!!.bind(u_texture1)
    }

    enum class Param(private val mnemonic: String, private val arrayElementSize: Int) : Parameter {
        Texture("u_texture0", 0),
        LensColor("u_texture1", 0),
        ViewportInverse("u_viewportInverse", 2),
        Ghosts("u_ghosts", 0),
        HaloWidth("u_haloWidth", 0);

        override fun mnemonic() = this.mnemonic
        override fun arrayElementSize() = this.arrayElementSize
    }
}
