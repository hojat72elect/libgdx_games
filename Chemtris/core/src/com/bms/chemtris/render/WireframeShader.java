package com.bms.chemtris.render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Class to manage wireframe rendering of elements.
 */
public class WireframeShader extends DefaultShader {

    public static final int PRIMITIVE_TYPE = GL20.GL_LINE_STRIP;
    private int mSavedPrimitiveType;

    public WireframeShader(Renderable renderable) {
        super(renderable);
    }

    public WireframeShader(Renderable renderable, Config config) {
        super(renderable, config);
    }

    public WireframeShader(Renderable renderable, Config config, String prefix) {
        super(renderable, config, prefix);
    }

    public WireframeShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
        super(renderable, config, prefix, vertexShader, fragmentShader);
    }

    public WireframeShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
        super(renderable, config, shaderProgram);
    }

    @Override
    public void render(Renderable renderable) {
        setPrimitiveType(renderable);
        super.render(renderable);
        restorePrimitiveType(renderable);
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes) {
        setPrimitiveType(renderable);
        super.render(renderable, combinedAttributes);
        restorePrimitiveType(renderable);
    }

    private void restorePrimitiveType(Renderable renderable) {
        renderable.meshPart.primitiveType = mSavedPrimitiveType;
    }

    private void setPrimitiveType(Renderable renderable) {
        mSavedPrimitiveType = renderable.meshPart.primitiveType;
        renderable.meshPart.primitiveType = PRIMITIVE_TYPE;
    }
}