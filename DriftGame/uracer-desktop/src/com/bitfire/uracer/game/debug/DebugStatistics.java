package com.bitfire.uracer.game.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.configuration.PhysicsUtils;
import com.bitfire.uracer.utils.AlgebraMath;

public final class DebugStatistics {

    private final Pixmap pixels;
    private final Texture texture;
    private final TextureRegion region;
    private final int PanelWidth;
    private final int PanelHeight;
    private final float ratio_rtime;
    private final float ratio_ptime;
    private final float ratio_fps;
    private final long intervalNs;
    private final float[] dataRenderTime;
    private final float[] dataFps;
    private final float[] dataPhysicsTime;
    private final float[] dataTimeAliasing;
    public WindowedMean meanPhysics = new WindowedMean(16);
    public WindowedMean meanRender = new WindowedMean(16);
    public WindowedMean meanTickCount = new WindowedMean(16);
    private long startTime;

    public DebugStatistics(int width, int height, float updateHz) {

        final float oneOnUpdHz = 1f / updateHz;

        PanelWidth = width;
        PanelHeight = height;
        intervalNs = (long) (1000000000L * oneOnUpdHz);
        pixels = new Pixmap(PanelWidth, PanelHeight, Format.RGBA8888);
        texture = new Texture(width, height, Format.RGBA8888);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        region = new TextureRegion(texture, 0, 0, pixels.getWidth(), pixels.getHeight());
        dataRenderTime = new float[PanelWidth];
        dataFps = new float[PanelWidth];
        dataPhysicsTime = new float[PanelWidth];
        dataTimeAliasing = new float[PanelWidth];
        ratio_rtime = ((float) PanelHeight / 2f) * PhysicsUtils.TimestepHz;
        ratio_ptime = ((float) PanelHeight / 2f) * PhysicsUtils.TimestepHz;
        ratio_fps = ((float) PanelHeight / 2f) * oneOnUpdHz;

        for (int i = 0; i < PanelWidth; i++) {
            dataRenderTime[i] = 0;
            dataPhysicsTime[i] = 0;
            dataFps[i] = 0;
            dataTimeAliasing[i] = 0;
        }

        plot();
        startTime = TimeUtils.nanoTime();
    }

    public void dispose() {
        pixels.dispose();
        texture.dispose();
    }

    public TextureRegion getRegion() {
        return region;
    }

    public int getWidth() {
        return PanelWidth;
    }

    public int getHeight() {
        return PanelHeight;
    }

    public void update() {
        if (collect()) {
            plot();
        }
    }

    private void plot() {
        pixels.setColor(0, 0, 0, 0.8f);
        pixels.fill();

        float alpha = 0.5f;
        for (int x = 0; x < PanelWidth; x++) {
            int xc = PanelWidth - x - 1;
            int value;

            value = (int) (dataRenderTime[x] * ratio_rtime);
            if (value > 0) {
                pixels.setColor(0, 0.5f, 1f, alpha);
                pixels.drawLine(xc, 0, xc, value);
            }

            value = (int) (dataPhysicsTime[x] * ratio_ptime);
            pixels.setColor(1, 1, 1, alpha);
            pixels.drawLine(xc, 0, xc, value);

            value = (int) (dataFps[x] * ratio_fps);
            if (value > 0) {
                pixels.setColor(0, 1, 1, .8f);
                pixels.drawPixel(xc, value);
            }

            value = (int) (AlgebraMath.clamp(dataTimeAliasing[x] * PanelHeight, 0, PanelHeight));
            if (value > 0) {
                pixels.setColor(1, 0, 1, .8f);
                pixels.drawPixel(xc, value);
            }
        }

        texture.draw(pixels, 0, 0);
    }

    private boolean collect() {
        long time = TimeUtils.nanoTime();

        if (time - startTime > intervalNs) {
            for (int i = PanelWidth - 1; i > 0; i--) {
                dataRenderTime[i] = dataRenderTime[i - 1];
                dataPhysicsTime[i] = dataPhysicsTime[i - 1];
                dataFps[i] = dataFps[i - 1];
                dataTimeAliasing[i] = dataTimeAliasing[i - 1];
            }

            meanPhysics.addValue(URacer.Game.getPhysicsTime());
            meanRender.addValue(URacer.Game.getRenderTime());
            meanTickCount.addValue(URacer.Game.getLastTicksCount());

            dataPhysicsTime[0] = meanPhysics.getMean();
            dataRenderTime[0] = meanRender.getMean();
            dataFps[0] = Gdx.graphics.getFramesPerSecond();
            dataTimeAliasing[0] = URacer.Game.getTemporalAliasing();

            startTime = time;
            return true;
        }

        return false;
    }
}
