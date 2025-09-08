package com.bitfire.postprocessing.filters;

import com.bitfire.postprocessing.utils.PingPongBuffer;

/**
 * Implements a separable 2D convolution filter.
 * This filter is often used for effects like blurring, sharpening, or edge detection.
 * <p>
 * In here we have a two-pass rendering process; first a horizontal convolution is applied to the input image,
 * then the result of the horizontal pass will be passed to the vertical convolution.
 */
public final class Convolve2D extends MultipassFilter {
    public final int radius;
    public final int length;

    public final float[] weights, offsetsHor, offsetsVert;

    private final Convolve1D horizontalPass;
    private final Convolve1D verticalPass;

    public Convolve2D(int radius) {
        this.radius = radius;
        length = (radius * 2) + 1;

        horizontalPass = new Convolve1D(length);
        verticalPass = new Convolve1D(length, horizontalPass.weights);

        weights = horizontalPass.weights;
        offsetsHor = horizontalPass.offsets;
        offsetsVert = verticalPass.offsets;
    }

    public void dispose() {
        horizontalPass.dispose();
        verticalPass.dispose();
    }

    public void upload() {
        rebind();
    }

    @Override
    public void rebind() {
        horizontalPass.rebind();
        verticalPass.rebind();
    }

    @Override
    public void render(PingPongBuffer buffer) {
        horizontalPass.setInput(buffer.capture()).render();
        verticalPass.setInput(buffer.capture()).render();
    }
}
