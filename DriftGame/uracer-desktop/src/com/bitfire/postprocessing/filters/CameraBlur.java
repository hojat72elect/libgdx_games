package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitfire.utils.ShaderLoader;

/**
 * FIXME this effect is INCOMPLETE!
 */
public final class CameraBlur extends Filter<CameraBlur> {

    private Texture normaldepth = null;
    private final Vector2 viewport = new Vector2();

    public CameraBlur() {
        super(ShaderLoader.fromFile("screenspace", "camerablur"));
        rebind();
        // dolut = false;
    }

    public void setNormalDepthMap(Texture texture) {
        this.normaldepth = texture;
    }

    public void setCurrentToPrevious(Matrix4 ctp) {
        setParams(Param.CurrentToPrevious, ctp);
        endParams();
    }

    public void setInverseProj(Matrix4 invProj) {
        setParams(Param.InvProj, invProj);
        endParams();
    }

    public void setBlurPasses(int passes) {
        setParams(Param.BlurPasses, passes);
        endParams();
    }

    public void setBlurScale(float blurScale) {
        setParams(Param.BlurScale, blurScale);
        endParams();
    }

    public void setNearFarPlanes(float near, float far) {
        setParams(Param.Near, near);
        setParams(Param.Far, far);
        endParams();
    }

    public void setViewport(float width, float height) {
        viewport.set(width, height);
        setParams(Param.Viewport, viewport);
    }

    public void setDepthScale(float scale) {
        setParams(Param.DepthScale, scale);
        endParams();
    }

    @Override
    public void rebind() {
        setParams(Param.InputScene, u_texture0);
        setParams(Param.DepthMap, u_texture1);
        endParams();
    }

    @Override
    protected void onBeforeRender() {
        rebind();
        inputTexture.bind(u_texture0);
        normaldepth.bind(u_texture1);
    }

    public enum Param implements Parameter {
        // @formatter:off
		InputScene("u_texture0", 0), DepthMap("u_texture1", 0), CurrentToPrevious("ctp", 0), Near("near", 0), Far("far", 0), BlurPasses(
			"blur_passes", 0), BlurScale("blur_scale", 0), DepthScale("depth_scale", 0), InvProj("inv_proj", 0), Viewport(
			"viewport", 0);
		// @formatter:on

        private final String mnemonic;
        private final int elementSize;

        Param(String m, int elementSize) {
            this.mnemonic = m;
            this.elementSize = elementSize;
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
