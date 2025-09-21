package com.bitfire.uracer.game.logic.post;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.LongMap;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.effects.*;
import com.bitfire.postprocessing.filters.CrtScreen.Effect;
import com.bitfire.postprocessing.filters.CrtScreen.RgbMode;
import com.bitfire.postprocessing.filters.RadialBlur;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.configuration.GraphicsUtils;
import com.bitfire.uracer.configuration.PostProcessingUtils;
import com.bitfire.uracer.configuration.UserPreferences;
import com.bitfire.uracer.configuration.UserPreferences.Preference;
import com.bitfire.uracer.game.logic.helpers.TrackProgressData;
import com.bitfire.uracer.game.logic.post.animators.DefaultAnimator;
import com.bitfire.uracer.game.logic.post.effects.LightShafts;
import com.bitfire.uracer.game.logic.post.effects.LightShafts.Quality;
import com.bitfire.uracer.game.logic.post.effects.Ssao;
import com.bitfire.uracer.game.player.PlayerCar;
import com.bitfire.uracer.game.world.GameWorld;
import com.bitfire.uracer.utils.ScaleUtils;
import com.bitfire.utils.Hash;
import com.bitfire.utils.ShaderLoader;

/**
 * Encapsulates a post-processor animator that manages effects such as bloom and zoomblur to compose and enhance the gaming
 * experience.
 */
public final class PostProcessing {

    // public access to stored effects
    public LongMap<PostProcessorEffect> effects = new LongMap<>();
    private boolean hasPostProcessor = false;
    private PostProcessor postProcessor = null;
    private boolean needNormalDepthMap = false;
    // animators
    private PostProcessingAnimator animator = null;
    private boolean hasAnimator = false;

    public PostProcessing(GameWorld gameWorld) {
        if (UserPreferences.bool(Preference.PostProcessing)) {
            postProcessor = new PostProcessor(ScaleUtils.PlayViewport, true /* depth */, true /* alpha */,
                    URacer.Game.isDesktop() /* supports32Bpp */);
            PostProcessor.EnableQueryStates = false;
            postProcessor.setClearBits(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            postProcessor.setClearColor(0, 0, 0, 1);
            postProcessor.setClearDepth(1);
            postProcessor.setEnabled(true);
            postProcessor.setBufferTextureWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
            hasPostProcessor = true;
            createEffects();
            setAnimator(new DefaultAnimator(this, gameWorld));
        }
    }

    public void dispose() {
        if (hasPostProcessor) {
            postProcessor.dispose();
        }
    }

    /**
     * Creates the effects that will be available to the animators/manipulators to use, remember that the ownership of the
     * instantiated objects is transfered to the PostProcessor when adding the effect to it.
     */
    private void createEffects() {
        if (UserPreferences.bool(Preference.Ssao)) {
            addEffect(Effects.Ssao.name, new Ssao(ScaleUtils.PlayWidth, ScaleUtils.PlayHeight,
                    Ssao.Quality.valueOf(UserPreferences.string(Preference.SsaoQuality))));
            needNormalDepthMap = true;
        }

        int refW = GraphicsUtils.REFERENCE_SCREEN_WIDTH;
        int refH = GraphicsUtils.REFERENCE_SCREEN_HEIGHT;

        if (UserPreferences.bool(Preference.ZoomRadialBlur)) {
            RadialBlur.Quality rbq = RadialBlur.Quality.valueOf(UserPreferences.string(Preference.ZoomRadialBlurQuality));
            Zoomer z = new Zoomer(refW, refH, rbq);
            addEffect(Effects.Zoomer.name, z);
        }

        int fboW = (int) ((float) ScaleUtils.PlayWidth * PostProcessingUtils.FboRatio);
        int fboH = (int) ((float) ScaleUtils.PlayHeight * PostProcessingUtils.FboRatio);

        if (UserPreferences.bool(Preference.Bloom)) {
            addEffect(Effects.Bloom.name, new Bloom(fboW, fboH));
        }

        // dbg
        addEffect(Effects.LensFlare.name, new LensFlare((int) (fboW * 0.75f), (int) (fboH * 0.75f)));
        // dbg

        // dbg
        addEffect(Effects.LightShafts.name, new LightShafts((int) (fboW * 0.75f), (int) (fboH * 0.75f), Quality.High));
        // dbg

        if (UserPreferences.bool(Preference.Vignetting)) {
            addEffect(Effects.Vignette.name, new Vignette(ScaleUtils.PlayWidth, ScaleUtils.PlayHeight, false));
        }

        if (UserPreferences.bool(Preference.CrtScreen)) {

            ShaderLoader.INSTANCE.setPedantic(false);
            int effects = Effect.PhosphorVibrance.v | Effect.Scanlines.v | Effect.Tint.v;
            boolean earthCurvature = UserPreferences.bool(Preference.EarthCurvature);

            CrtMonitor crt = new CrtMonitor(ScaleUtils.PlayWidth, ScaleUtils.PlayHeight, earthCurvature, false,
                    RgbMode.ChromaticAberrations, effects);

            addEffect(Effects.Crt.name, crt);
            ShaderLoader.INSTANCE.setPedantic(true);
        }

        Gdx.app.log("PostProcessing", "Post-processing enabled and configured");
    }

    private void setAnimator(PostProcessingAnimator animator) {
        hasAnimator = (animator != null);
        this.animator = animator;
        animator.reset();
    }

    public boolean requiresNormalDepthMap() {
        return needNormalDepthMap;
    }

    public boolean isEnabled() {
        return hasPostProcessor;
    }

    public PostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void addEffect(String name, PostProcessorEffect effect) {
        if (hasPostProcessor) {
            postProcessor.addEffect(effect);
            effects.put(Hash.INSTANCE.apHash(name), effect);
        }
    }

    public PostProcessorEffect getEffect(String name) {
        if (hasPostProcessor) {
            return effects.get(Hash.INSTANCE.apHash(name));
        }

        return null;
    }

    public boolean hasEffect(String name) {
        if (hasPostProcessor) {
            return (effects.get(Hash.INSTANCE.apHash(name)) != null);
        }

        return false;
    }

    public void resetAnimator() {
        if (hasPostProcessor && hasAnimator) {
            animator.reset();
        }
    }

    public void onBeforeRender(Vector2 cameraPos, TrackProgressData progressData, Color ambient, Color trees, float zoom,
                               float warmUpCompletion, float collisionFactor, boolean paused) {
        if (hasPostProcessor && hasAnimator) {
            animator.update(cameraPos, progressData, ambient, trees, zoom, warmUpCompletion, collisionFactor, paused);
        }
    }

    public void setPlayer(PlayerCar player) {
        if (hasPostProcessor && hasAnimator) {
            animator.setPlayer(player);
        }
    }

    public void alertBegins(int milliseconds) {
        if (hasPostProcessor && hasAnimator) {
            animator.alertBegins(milliseconds);
        }
    }

    public void gamePause(int milliseconds) {
        if (hasPostProcessor && hasAnimator) {
            animator.gamePause(milliseconds);
        }
    }

    public void gameResume(int milliseconds) {
        if (hasPostProcessor && hasAnimator) {
            animator.gameResume(milliseconds);
        }
    }

    public enum Effects {
        Zoomer, Bloom, Vignette, Crt, Curvature, Ssao, MotionBlur, LightShafts, LensFlare;

        public final String name;

        Effects() {
            name = this.toString();
        }
    }
}
