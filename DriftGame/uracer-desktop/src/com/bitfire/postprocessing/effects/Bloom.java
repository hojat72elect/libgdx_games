package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.Blur;
import com.bitfire.postprocessing.filters.Blur.BlurType;
import com.bitfire.postprocessing.filters.Combine;
import com.bitfire.postprocessing.filters.Threshold;
import com.bitfire.postprocessing.utils.PingPongBuffer;

public final class Bloom extends PostProcessorEffect {
    private final PingPongBuffer pingPongBuffer;
    private final Blur blur;
    private final Threshold threshold;
    private final Combine combine;

    public Bloom(int fboWidth, int fboHeight) {
        pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.getFramebufferFormat(), false);

        blur = new Blur(fboWidth, fboHeight);
        threshold = new Threshold();
        combine = new Combine();

        setSettings(new Settings("default", 2, 0.277f, 1f, .85f, 1.1f, .85f));
    }

    @Override
    public void dispose() {
        combine.dispose();
        threshold.dispose();
        blur.dispose();
        pingPongBuffer.dispose();
    }

    public void setBaseIntensity(float intensity) {
        combine.setSource1Intensity(intensity);
    }

    public void setBloomIntensity(float intensity) {
        combine.setSource2Intensity(intensity);
    }

    public void setThreshold(float gamma) {
        threshold.setTreshold(gamma);
    }

    public void setBaseSaturation(float saturation) {
        combine.setSource1Saturation(saturation);
    }

    public void setBloomSaturation(float saturation) {
        combine.setSource2Saturation(saturation);
    }

    public void setBlurType(BlurType type) {
        blur.setType(type);
    }

    public void setSettings(Settings settings) {

        // setup threshold filter
        setThreshold(settings.bloomThreshold);

        // setup combine filter
        setBaseIntensity(settings.baseIntensity);
        setBaseSaturation(settings.baseSaturation);
        setBloomIntensity(settings.bloomIntensity);
        setBloomSaturation(settings.bloomSaturation);

        // setup blur filter
        setBlurPasses(settings.blurPasses);
        setBlurAmount(settings.blurAmount);
        setBlurType(settings.blurType);
    }

    public void setBlurPasses(int passes) {
        blur.setPasses(passes);
    }

    public void setBlurAmount(float amount) {
        blur.setAmount(amount);
    }

    @Override
    public void render(final FrameBuffer src, final FrameBuffer dest) {
        Texture texsrc = src.getColorBufferTexture();

        boolean blendingWasEnabled = PostProcessor.isStateEnabled(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        pingPongBuffer.begin();
        {
            // threshold / high-pass filter
            // only areas with pixels >= threshold are blit to smaller fbo
            threshold.setInput(texsrc).setOutput(pingPongBuffer.getSourceBuffer()).render();

            // blur pass
            blur.render(pingPongBuffer);
        }
        pingPongBuffer.end();

        if (blendingWasEnabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
        }

        restoreViewport(dest);

        // mix original scene and blurred threshold, modulate via
        // set(Base|Bloom)(Saturation|Intensity)
        combine.setOutput(dest).setInput(texsrc, pingPongBuffer.getResultTexture()).render();
    }

    @Override
    public void rebind() {
        blur.rebind();
        threshold.rebind();
        combine.rebind();
        pingPongBuffer.rebind();
    }

    public static class Settings {
        public final String name;

        public final BlurType blurType;
        public final int blurPasses; // simple blur
        public final float blurAmount; // normal blur (1 pass)
        public final float bloomThreshold;

        public final float bloomIntensity;
        public final float bloomSaturation;
        public final float baseIntensity;
        public final float baseSaturation;

        public Settings(String name, BlurType blurType, int blurPasses, float blurAmount, float bloomThreshold,
                        float baseIntensity, float baseSaturation, float bloomIntensity, float bloomSaturation) {
            this.name = name;
            this.blurType = blurType;
            this.blurPasses = blurPasses;
            this.blurAmount = blurAmount;

            this.bloomThreshold = bloomThreshold;
            this.baseIntensity = baseIntensity;
            this.baseSaturation = baseSaturation;
            this.bloomIntensity = bloomIntensity;
            this.bloomSaturation = bloomSaturation;
        }

        // simple blur
        public Settings(String name, int blurPasses, float bloomThreshold, float baseIntensity, float baseSaturation,
                        float bloomIntensity, float bloomSaturation) {
            this(name, BlurType.Gaussian5x5b, blurPasses, 0, bloomThreshold, baseIntensity, baseSaturation, bloomIntensity,
                    bloomSaturation);
        }
    }
}
