package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.utils.ShaderLoader;

public final class Combine extends Filter<Combine> {

    private float s1i, s1s, s2i, s2s;
    private Texture inputTexture2 = null;

    public Combine() {
        super(ShaderLoader.fromFile("screenspace", "combine"));
        s1i = 1f;
        s2i = 1f;
        s1s = 1f;
        s2s = 1f;

        rebind();
    }

    public Combine setInput(FrameBuffer buffer1, FrameBuffer buffer2) {
        this.inputTexture = buffer1.getColorBufferTexture();
        this.inputTexture2 = buffer2.getColorBufferTexture();
        return this;
    }

    public Combine setInput(Texture texture1, Texture texture2) {
        this.inputTexture = texture1;
        this.inputTexture2 = texture2;
        return this;
    }

    public void setSource1Intensity(float intensity) {
        s1i = intensity;
        setParam(Combine.Param.Source1Intensity, intensity);
    }

    public void setSource2Intensity(float intensity) {
        s2i = intensity;
        setParam(Combine.Param.Source2Intensity, intensity);
    }

    public void setSource1Saturation(float saturation) {
        s1s = saturation;
        setParam(Combine.Param.Source1Saturation, saturation);
    }

    public void setSource2Saturation(float saturation) {
        s2s = saturation;
        setParam(Combine.Param.Source2Saturation, saturation);
    }

    @Override
    public void rebind() {
        setParams(Param.Texture0, u_texture0);
        setParams(Param.Texture1, u_texture1);
        setParams(Param.Source1Intensity, s1i);
        setParams(Param.Source2Intensity, s2i);
        setParams(Param.Source1Saturation, s1s);
        setParams(Param.Source2Saturation, s2s);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
        inputTexture2.bind(u_texture1);
    }

    public enum Param implements Parameter {

        Texture0("u_texture0", 0), Texture1("u_texture1", 0), Source1Intensity("Src1Intensity", 0), Source1Saturation(
			"Src1Saturation", 0), Source2Intensity("Src2Intensity", 0), Source2Saturation("Src2Saturation", 0);

        private final String mnemonic;
        private final int elementSize;

        Param(String m, int elementSize) {
            this.mnemonic = m;
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
