package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.RadialBlur;
import com.bitfire.postprocessing.filters.Zoom;

/**
 * Implements a zooming effect: either a radial blur filter or a zoom filter is used.
 */
public final class Zoomer extends PostProcessorEffect {
    private boolean doRadial = false;
    private RadialBlur radialBlur = null;
    private Zoom zoom = null;
    private float oneOnW, oneOnH;

    /**
     * Creating a Zoomer specifying the radial blur quality will enable radial blur
     */
    public Zoomer(int viewportWidth, int viewportHeight, RadialBlur.Quality quality) {
        setup(viewportWidth, viewportHeight, new RadialBlur(quality));
    }

    private void setup(int viewportWidth, int viewportHeight, RadialBlur radialBlurFilter) {
        radialBlur = radialBlurFilter;
        if (radialBlur != null) {
            doRadial = true;
            zoom = null;
        } else {
            doRadial = false;
            zoom = new Zoom();
        }

        oneOnW = 1f / (float) viewportWidth;
        oneOnH = 1f / (float) viewportHeight;
    }

    /**
     * Specify the zoom origin, in screen coordinates.
     */
    public void setOrigin(Vector2 o) {
        setOrigin(o.x, o.y);
    }

    /**
     * Specify the zoom origin, in screen coordinates.
     */
    public void setOrigin(float x, float y) {

        if (doRadial) {
            radialBlur.setOrigin(x * oneOnW, 1f - y * oneOnH);
        } else {
            zoom.setOrigin(x * oneOnW, 1f - y * oneOnH);
        }
    }

    public float getZoom() {
        if (doRadial) {
            return 1f / radialBlur.getZoom();
        } else {
            return 1f / zoom.getZoom();
        }
    }

    public void setZoom(float zoom) {
        if (doRadial) {
            radialBlur.setZoom(1f / zoom);
        } else {
            this.zoom.setZoom(1f / zoom);
        }
    }

    public void setBlurStrength(float strength) {
        if (doRadial) {
            radialBlur.setStrength(strength);
        }
    }

    @Override
    public void dispose() {
        if (radialBlur != null) {
            radialBlur.dispose();
            radialBlur = null;
        }

        if (zoom != null) {
            zoom.dispose();
            zoom = null;
        }
    }

    @Override
    public void rebind() {
        radialBlur.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        restoreViewport(dest);
        if (doRadial) {
            radialBlur.setInput(src).setOutput(dest).render();
        } else {
            zoom.setInput(src).setOutput(dest).render();
        }
    }
}
