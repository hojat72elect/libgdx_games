package com.bitfire.postprocessing.filters;

import com.bitfire.utils.ShaderLoader;

/**
 * Bias filter. Adapted for LensFlare effect.
 *
 * @see <a
 * href="http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html">http://john-chapman-graphics.blogspot.co.uk/2013/02/pseudo-lens-flare.html</a>
 */
public final class Bias extends Filter<Bias> {
    private float bias;

    public Bias() {
        super(ShaderLoader.INSTANCE.fromFile("screenspace", "bias"));
        rebind();
    }

    public void setBias(float bias) {
        this.bias = bias;
        setParam(Param.Bias, this.bias);
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
    }

    @Override
    public void rebind() {
        setParams(Param.Texture, u_texture0);
        setBias(this.bias);
    }

    public enum Param implements Parameter {

        Texture("u_texture0", 0), Bias("u_bias", 0);

        private final String mnemonic;
        private final int elementSize;

        Param(String mnemonic, int elementSize) {
            this.mnemonic = mnemonic;
            this.elementSize = elementSize;
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
