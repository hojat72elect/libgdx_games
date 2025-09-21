package com.bitfire.uracer.game.logic.post.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.Blur;
import com.bitfire.postprocessing.filters.Blur.BlurType;
import com.bitfire.postprocessing.filters.Combine;
import com.bitfire.postprocessing.filters.Threshold;
import com.bitfire.postprocessing.utils.FullscreenQuad;
import com.bitfire.postprocessing.utils.PingPongBuffer;
import com.bitfire.uracer.configuration.GraphicsUtils;
import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.events.GameRendererEvent;
import com.bitfire.uracer.utils.ScaleUtils;
import com.bitfire.utils.ShaderLoader;

public class LightShafts extends PostProcessorEffect {
    private final PingPongBuffer occlusionMap;
    private final ShaderProgram shShafts;
    private final FullscreenQuad quad = new FullscreenQuad();
    private final Blur blur;
    private final Combine combine;
    private final Threshold threshold;
    private final float oneOnW;
    private final float oneOnH;
    private final GameRendererEvent.Listener gameRendererEvent = (source, type, order) -> debug(GameEvents.gameRenderer.batch);
    private final float[] vLightPos = new float[2];

    public LightShafts(int fboWidth, int fboHeight, Quality quality) {
        Gdx.app.log("LightShafts", "Quality profile = " + quality.toString());
        float oscale = quality.scale;

        oneOnW = 1f / (float) GraphicsUtils.REFERENCE_SCREEN_WIDTH;
        oneOnH = 1f / (float) GraphicsUtils.REFERENCE_SCREEN_HEIGHT;

        // maps
        occlusionMap = new PingPongBuffer((int) ((float) fboWidth * oscale), (int) ((float) fboHeight * oscale), Format.RGBA8888, false);

        // shaders
        shShafts = ShaderLoader.INSTANCE.fromFile("screenspace", "lightshafts/lightshafts");
        combine = new Combine();
        threshold = new Threshold();

        // blur
        blur = new Blur(occlusionMap.width, occlusionMap.height);
        blur.setType(BlurType.Gaussian5x5b);

        blur.setPasses(2);
        int w = Gdx.graphics.getWidth();
        if (w >= 1920)
            blur.setPasses(4);
        else if (w >= 1680)
            blur.setPasses(3);
        else if (w >= 1280) blur.setPasses(2);

        setParams(16, 0.0034f, 1f, 0.84f, 5.65f, 1f, (float) GraphicsUtils.REFERENCE_SCREEN_WIDTH / 2,
                (float) GraphicsUtils.REFERENCE_SCREEN_HEIGHT / 2);
    }

    @Override
    public void dispose() {
        disableDebug();
        blur.dispose();
        shShafts.dispose();
        combine.dispose();
        threshold.dispose();
        occlusionMap.dispose();
    }

    public void disableDebug() {
        GameEvents.gameRenderer.removeListener(gameRendererEvent, GameRendererEvent.Type.BatchDebug,
                GameRendererEvent.Order.DEFAULT);
    }

    private void dbgTextureW(SpriteBatch batch, Texture tex) {
        if (tex == null) return;

        float h = (float) 360 / ScaleUtils.RefAspect;
        float x = GraphicsUtils.REFERENCE_SCREEN_WIDTH - (float) 360 - 10;
        float y = 50 * 10;
        batch.draw(tex, x, y, (float) 360, h);
    }

    private void debug(SpriteBatch batch) {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        batch.disableBlending();
        dbgTextureW(batch, occlusionMap.getResultTexture());
    }

    public void setParams(int samples, float exposure, float decay, float density, float weight, float illuminationDecay,
                          float lightScreenPosX, float lightScreenPosY) {
        shShafts.begin();
        shShafts.setUniformi("samples", samples); // 16
        shShafts.setUniformf("exposure", exposure); // 0.0034
        shShafts.setUniformf("decay", decay); // 1
        shShafts.setUniformf("density", density); // 0.84
        shShafts.setUniformf("weight", weight); // 5.65
        shShafts.setUniformf("illuminationDecay", illuminationDecay); // 1
        // normalized position
        vLightPos[0] = lightScreenPosX * oneOnW;
        vLightPos[1] = 1 - lightScreenPosY * oneOnH;
        shShafts.setUniform2fv("lightPositionOnScreen", vLightPos, 0, 2);
        shShafts.end();
    }

    public void setLightScreenPositionN(float x, float y) {
        vLightPos[0] = x;
        vLightPos[1] = 1 - y;
        shShafts.begin();
        shShafts.setUniform2fv("lightPositionOnScreen", vLightPos, 0, 2);
        shShafts.end();
    }

    public void setExposure(float exposure) {
        shShafts.begin();
        shShafts.setUniformf("exposure", exposure); // 0.0034
        shShafts.end();
    }

    public void setDecay(float decay) {
        shShafts.begin();
        shShafts.setUniformf("decay", decay); // 1
        shShafts.end();
    }

    public void setDensity(float density) {
        shShafts.begin();
        shShafts.setUniformf("density", density); // 0.84
        shShafts.end();
    }

    public void setWeight(float weight) {
        shShafts.begin();
        shShafts.setUniformf("weight", weight); // 5.65
        shShafts.end();
    }

    public void setThreshold(float gamma) {
        this.threshold.setTreshold(gamma);
    }

    public Combine getCombinePass() {
        return combine;
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        Texture tsrc = src.getColorBufferTexture();

        occlusionMap.begin();
        {
            threshold.setInput(tsrc).setOutput(occlusionMap.getSourceBuffer()).render();
            blur.render(occlusionMap);
        }
        occlusionMap.end();
        Texture result = occlusionMap.getResultTexture();
        occlusionMap.capture();
        {
            shShafts.begin();
            {
                result.bind(0);
                shShafts.setUniformi("u_texture", 0);
                quad.render(shShafts);
            }
            shShafts.end();
        }
        occlusionMap.end();

        restoreViewport(dest);

        combine.setOutput(dest).setInput(tsrc, occlusionMap.getResultTexture()).render();
    }

    @Override
    public void rebind() {
    }

    public enum Quality {
        High(1), Medium(0.75f), Low(0.5f);
        public final float scale;

        Quality(float scale) {
            this.scale = scale;
        }
    }
}
