package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.Vignetting;

/**
 * Vignette is a very common post-processing technique you can apply to an image for
 * darkening or de-saturating the edges of an image; so the viewer's focus will be
 * drawn to the center.
 */
public final class Vignette extends PostProcessorEffect {
    private final Vignetting vignetting;
    private final float oneOnW;
    private final float oneOnH;

    public Vignette(int viewportWidth, int viewportHeight, boolean controlSaturation) {
        oneOnW = 1f / (float) viewportWidth;
        oneOnH = 1f / (float) viewportHeight;
        vignetting = new Vignetting(controlSaturation);
    }

    @Override
    public void dispose() {
        vignetting.dispose();
    }

    public void setCoords(float x, float y) {
        vignetting.setCoords(x, y);
    }

    public void setX(float x) {
        vignetting.setX(x);
    }

    public void setY(float y) {
        vignetting.setY(y);
    }

    public void setLutTexture(Texture texture) {
        vignetting.setLut(texture);
    }

    public void setLutIndexVal(int index, int value) {
        vignetting.setLutIndexVal(index, value);
    }

    public void setLutIndexOffset(float value) {
        vignetting.setLutIndexOffset(value);
    }

    /**
     * Specify the center, in screen coordinates.
     */
    public void setCenter(float x, float y) {
        vignetting.setCenter(x * oneOnW, 1f - y * oneOnH);
    }

    public float getIntensity() {
        return vignetting.getIntensity();
    }

    public void setIntensity(float intensity) {
        vignetting.setIntensity(intensity);
    }

    public void setLutIntensity(float value) {
        vignetting.setLutIntensity(value);
    }

    @Override
    public void rebind() {
        vignetting.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        restoreViewport(dest);
        vignetting.setInput(src).setOutput(dest).render();
    }
}
