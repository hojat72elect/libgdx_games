package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.math.Vector2;
import com.bitfire.utils.ShaderLoader;

/**
 * Normal filtered anti-aliasing filter.
 */
public final class NfaaFilter extends Filter<NfaaFilter> {
    private final Vector2 viewportInverse;

    public NfaaFilter(int viewportWidth, int viewportHeight) {
        this(new Vector2(viewportWidth, viewportHeight));
    }

    public NfaaFilter(Vector2 viewportSize) {
        super(ShaderLoader.fromFile("screenspace", "nfaa"));
        this.viewportInverse = viewportSize;
        this.viewportInverse.x = 1f / this.viewportInverse.x;
        this.viewportInverse.y = 1f / this.viewportInverse.y;

        rebind();
    }

    public void setViewportSize(float width, float height) {
        this.viewportInverse.set(1f / width, 1f / height);
        setParam(Param.ViewportInverse, this.viewportInverse);
    }

    public Vector2 getViewportSize() {
        return viewportInverse;
    }

    @Override
    public void rebind() {
        // Re-implement super to batch every parameter
        setParams(Param.Texture, u_texture0);
        setParams(Param.ViewportInverse, viewportInverse);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
    }

    public enum Param implements Parameter {

        Texture("u_texture0", 0), ViewportInverse("u_viewportInverse", 2);
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
