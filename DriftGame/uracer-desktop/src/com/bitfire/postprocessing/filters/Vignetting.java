package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.graphics.Texture;
import com.bitfire.utils.ShaderLoader;

public final class Vignetting extends Filter<Vignetting> {

    private float x, y;
    private float intensity;

    private Texture texLut;
    private boolean dolut;
    private final boolean dosat;
    private float lutintensity;
    private final int[] lutindex;
    private float lutStep, lutStepOffset, lutIndexOffset;
    private float centerX, centerY;

    public Vignetting(boolean controlSaturation) {
        super(ShaderLoader.INSTANCE.fromFile("screenspace", "vignetting",
                (controlSaturation ? "#define CONTROL_SATURATION\n#define ENABLE_GRADIENT_MAPPING" : "#define ENABLE_GRADIENT_MAPPING")));
        dolut = false;
        dosat = controlSaturation;

        texLut = null;
        lutindex = new int[2];
        lutindex[0] = -1;
        lutindex[1] = -1;

        lutintensity = 1f;
        lutIndexOffset = 0;
        rebind();
        setCoords(0.8f, 0.25f);
        setCenter(0.5f, 0.5f);
        setIntensity(1f);
    }

    public void setCoords(float x, float y) {
        this.x = x;
        this.y = y;
        setParams(Param.VignetteX, x);
        setParams(Param.VignetteY, y);
        endParams();
    }

    public void setLutIndexVal(int index, int value) {
        lutindex[index] = value;

        switch (index) {
            case 0:
                setParam(Param.LutIndex, lutindex[0]);
                break;
            case 1:
                setParam(Param.LutIndex2, lutindex[1]);
                break;
        }
    }

    public void setLutIndexOffset(float value) {
        lutIndexOffset = value;
        setParam(Param.LutIndexOffset, lutIndexOffset);
    }

    /**
     * Specify the center, in normalized screen coordinates.
     */
    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        setParams(Param.CenterX, centerX);
        setParams(Param.CenterY, centerY).endParams();
    }

    public void setLutIntensity(float value) {
        lutintensity = value;
        setParam(Param.LutIntensity, lutintensity);
    }

    /**
     * Sets the texture with which gradient mapping will be performed.
     */
    public void setLut(Texture texture) {
        texLut = texture;
        dolut = (texLut != null);

        if (dolut) {
            lutStep = 1f / (float) texture.getHeight();
            lutStepOffset = lutStep / 2f; // center texel
            setParams(Param.TexLUT, u_texture1);
            setParams(Param.LutStep, lutStep);
            setParams(Param.LutStepOffset, lutStepOffset).endParams();
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        setParam(Param.VignetteX, x);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        setParam(Param.VignetteY, y);
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        setParam(Param.VignetteIntensity, intensity);
    }

    @Override
    public void rebind() {
        setParams(Param.Texture0, u_texture0);

        setParams(Param.LutIndex, lutindex[0]);
        setParams(Param.LutIndex2, lutindex[1]);
        setParams(Param.LutIndexOffset, lutIndexOffset);

        setParams(Param.TexLUT, u_texture1);
        setParams(Param.LutIntensity, lutintensity);
        setParams(Param.LutStep, lutStep);
        setParams(Param.LutStepOffset, lutStepOffset);

        if (dosat) {
            setParams(Param.Saturation, 0F);
            setParams(Param.SaturationMul, 0F);
        }

        setParams(Param.VignetteIntensity, intensity);
        setParams(Param.VignetteX, x);
        setParams(Param.VignetteY, y);
        setParams(Param.CenterX, centerX);
        setParams(Param.CenterY, centerY);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
        if (dolut) {
            texLut.bind(u_texture1);
        }
    }

    public enum Param implements Parameter {
        Texture0("u_texture0", 0), TexLUT("u_texture1", 0), VignetteIntensity("VignetteIntensity", 0), VignetteX("VignetteX", 0), VignetteY(
                "VignetteY", 0), Saturation("Saturation", 0), SaturationMul("SaturationMul", 0), LutIntensity("LutIntensity", 0), LutIndex(
                "LutIndex", 0), LutIndex2("LutIndex2", 0), LutIndexOffset("LutIndexOffset", 0), LutStep("LutStep", 0), LutStepOffset(
                "LutStepOffset", 0), CenterX("CenterX", 0), CenterY("CenterY", 0);

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
