package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.utils.ShaderLoader;

/**
 * Lens flare effect.
 **/
public final class Lens extends Filter<Lens> {
    private final Vector2 lightPosition = new Vector2();
    private float intensity;
    private final Vector3 color = new Vector3();
    private final Vector2 viewport = new Vector2();

    public Lens(float width, float height) {
        super(ShaderLoader.INSTANCE.fromFile("screenspace", "lensflare"));
        viewport.set(width, height);
        rebind();
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        setParam(Param.Intensity, intensity);
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        color.set(r, g, b);
        setParam(Param.Color, color);
    }

    @Override
    public void rebind() {
        // Re-implement super to batch every parameter
        setParams(Param.Texture, u_texture0);
        setParams(Param.LightPosition, lightPosition);
        setParams(Param.Intensity, intensity);
        setParams(Param.Color, color);
        setParams(Param.Viewport, viewport);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
    }

    public enum Param implements Parameter {

        Texture("u_texture0", 0), LightPosition("u_lightPosition", 2), Intensity("u_intensity", 0), Color("u_color", 3), Viewport(
			"u_viewport", 2);


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
