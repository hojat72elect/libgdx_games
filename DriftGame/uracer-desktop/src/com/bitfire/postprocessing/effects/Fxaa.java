package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.filters.FxaaFilter;

/**
 * Implements the fast approximate anti-aliasing. Very fast and useful for combining with other post-processing effects.
 */
public final class Fxaa extends Antialiasing {
    private FxaaFilter fxaaFilter = null;

    /**
     * Create a FXAA with the viewport size
     */
    public Fxaa(int viewportWidth, int viewportHeight) {
        setup(viewportWidth, viewportHeight);
    }

    private void setup(int viewportWidth, int viewportHeight) {
        fxaaFilter = new FxaaFilter(viewportWidth, viewportHeight);
    }

    public void setViewportSize(int width, int height) {
        fxaaFilter.setViewportSize(width, height);
    }

    /**
     * Sets the span max parameter. The default value is 8.
     */
    public void setSpanMax(float value) {
        fxaaFilter.setFxaaSpanMax(value);
    }

    @Override
    public void dispose() {
        if (fxaaFilter != null) {
            fxaaFilter.dispose();
            fxaaFilter = null;
        }
    }

    @Override
    public void rebind() {
        fxaaFilter.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        restoreViewport(dest);
        fxaaFilter.setInput(src).setOutput(dest).render();
    }
}
