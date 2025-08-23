package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.Lens;

/**
 * Lens flare effect.
 *
 * @deprecated Please use the better {@link com.bitfire.postprocessing.effects.LensFlare2}.
 */
@Deprecated
public final class LensFlare extends PostProcessorEffect {
    private Lens lens = null;

    public LensFlare(int viewportWidth, int viewportHeight) {
        setup(viewportWidth, viewportHeight);
    }

    private void setup(int viewportWidth, int viewportHeight) {
        lens = new Lens(viewportWidth, viewportHeight);
    }

    public float getIntensity() {
        return lens.getIntensity();
    }

    public void setIntensity(float intensity) {
        lens.setIntensity(intensity);
    }

    public void setColor(float r, float g, float b) {
        lens.setColor(r, g, b);
    }

    @Override
    public void dispose() {
        if (lens != null) {
            lens.dispose();
            lens = null;
        }
    }

    @Override
    public void rebind() {
        lens.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        restoreViewport(dest);
        lens.setInput(src).setOutput(dest).render();
    }
}
