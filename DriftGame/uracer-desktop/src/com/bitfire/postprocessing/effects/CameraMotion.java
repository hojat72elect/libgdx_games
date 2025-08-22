package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.CameraBlur;

/**
 * FIXME this effect is INCOMPLETE!
 */
public final class CameraMotion extends PostProcessorEffect {
    private final CameraBlur camblur;
    private final Matrix4 ctp = new Matrix4();
    private final float width;
    private final float height;

    public CameraMotion(int width, int height) {
        this.width = width;
        this.height = height;
        camblur = new CameraBlur();
        camblur.setNormalDepthMap(null);
    }

    @Override
    public void dispose() {
        camblur.dispose();
    }

    public void setNormalDepthMap(Texture normalDepthMap) {
        camblur.setNormalDepthMap(normalDepthMap);
    }

    public void setMatrices(Matrix4 inv_view, Matrix4 prevViewProj, Matrix4 inv_proj) {
        ctp.set(prevViewProj).mul(inv_view);
        camblur.setCurrentToPrevious(ctp);
        camblur.setInverseProj(inv_proj);
    }

    public void setBlurPasses(int passes) {
        camblur.setBlurPasses(passes);
    }

    public void setBlurScale(float scale) {
        camblur.setBlurScale(scale);
    }

    public void setNearFar(float near, float far) {
        camblur.setNearFarPlanes(near, far);
    }

    public void setDepthScale(float scale) {
        camblur.setDepthScale(scale);
    }

    @Override
    public void rebind() {
        camblur.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        if (dest != null) {
            camblur.setViewport(dest.getWidth(), dest.getHeight());
        } else {
            camblur.setViewport(width, height);
        }

        restoreViewport(dest);
        camblur.setInput(src).setOutput(dest).render();
    }
}
