package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bitfire.utils.ShaderLoader;

/**
 * Lens flare effect as described in John Chapman's article (without lens dirt or diffraction starburst). Lens color image
 * (lenscolor.png) is located in src/main/resources/ folder.
 *
 * @see <a
 * href="http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html">http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html</a>
 **/
public final class Lens2 extends Filter<Lens2> {
    private final Vector2 viewportInverse;
    private int ghosts;
    private float haloWidth;
    private Texture lensColorTexture;

    public Lens2(int width, int height) {
        super(ShaderLoader.fromFile("screenspace", "lensflare2"));
        viewportInverse = new Vector2(1f / width, 1f / height);
        rebind();
    }

    public void setGhosts(int ghosts) {
        this.ghosts = ghosts;
        setParam(Param.Ghosts, ghosts);
    }

    public void setHaloWidth(float haloWidth) {
        this.haloWidth = haloWidth;
        setParam(Param.HaloWidth, haloWidth);
    }

    public void setLensColorTexture(Texture tex) {
        this.lensColorTexture = tex;
        setParam(Param.LensColor, u_texture1);
    }

    @Override
    public void rebind() {
        // Re-implement super to batch every parameter
        setParams(Param.Texture, u_texture0);
        setParams(Param.LensColor, u_texture1);
        setParams(Param.ViewportInverse, viewportInverse);
        setParams(Param.Ghosts, ghosts);
        setParams(Param.HaloWidth, haloWidth);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
        lensColorTexture.bind(u_texture1);
    }

    public enum Param implements Parameter {

        Texture("u_texture0", 0), LensColor("u_texture1", 0), ViewportInverse("u_viewportInverse", 2), Ghosts("u_ghosts", 0), HaloWidth(
			"u_haloWidth", 0);

        private final String mnemonic;
        private final int elementSize;

        Param(String mnemonic, int arrayElementSize) {
            this.mnemonic = mnemonic;
            this.elementSize = arrayElementSize;
        }

        @Override
        public String mnemonic() {
            return this.mnemonic;
        }

        @Override
        public int arrayElementSize() {
            return this.elementSize;
        }
    }
}
