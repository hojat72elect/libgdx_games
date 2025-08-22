package com.bitfire.uracer.game.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.bitfire.uracer.u3d.materials.Material;
import com.bitfire.uracer.u3d.model.UStillModel;
import com.bitfire.uracer.u3d.still.StillModel;
import com.bitfire.utils.ShaderLoader;

/**
 * The model is expected to follow the z-up convention.
 */
public class OrthographicAlignedStillModel {
    public static final float World3DScalingFactor = 1.42222f;
    private static final float Adjustment = 1.05f; // 1280x720
    private static final float To256 = 224f / 256f;
    public static final float BlenderToURacer = Adjustment * World3DScalingFactor * To256 * 5f;
    public static ShaderProgram shader = null;
    public static ShaderProgram shaderNight = null;
    public UStillModel model;
    public Material material;
    public BoundingBox localBoundingBox = new BoundingBox();
    public BoundingBox boundingBox = new BoundingBox();
    public Vector3 scaleAxis = new Vector3();
    // position
    public Vector2 positionOffsetPx = new Vector2(0, 0);
    public Vector2 positionPx = new Vector2();
    public float iRotationAngle;
    public Vector3 iRotationAxis = new Vector3();
    // scale
    private float scale, scalingFactor;
    private float alpha = 1;

    public OrthographicAlignedStillModel(StillModel aModel, Material material) {
        loadShaders();

        try {
            model = new UStillModel(aModel.subMeshes);

            this.material = material;
            model.setMaterial(this.material);

            model.getBoundingBox(localBoundingBox);
            boundingBox.set(localBoundingBox);

            setScalingFactor(BlenderToURacer);
            setPosition(0, 0);
            setRotation(0, 0, 0, 0);
            setAlpha(1);
        } catch (Exception e) {
            Gdx.app.log("OrthographicAlignedStillModel", e.getMessage());
            Gdx.app.exit();
        }
    }

    public static void disposeShader() {
        if (shader != null) {
            shader.dispose();
            shader = null;
        }

        if (shaderNight != null) {
            shaderNight.dispose();
            shaderNight = null;
        }
    }

    // explicitle initialize the static iShader member
    // (Android: statics need to be re-initialized!)
    private void loadShaders() {
        // @off
        String vertexShader =
                "uniform mat4 u_projTrans;							\n" +
                        "attribute vec4 a_position;						\n" +
                        "attribute vec2 a_texCoord0;						\n" +
                        "varying vec2 v_TexCoord;							\n" +
                        "void main()											\n" +
                        "{\n" +
                        "	gl_Position = u_projTrans * a_position;	\n" +
                        "	v_TexCoord = a_texCoord0;						\n" +
                        "}\n";

        String fragmentShader =
                "#ifdef GL_ES											\n" +
                        "	precision mediump float;						\n" +
                        "#endif													\n" +
                        "uniform float alpha;								\n" +
                        "uniform sampler2D u_texture;						\n" +
                        "varying vec2 v_TexCoord;							\n" +
                        "void main()											\n" +
                        "{\n" +
                        "	vec4 texel = texture2D( u_texture, v_TexCoord );	\n" +
                        "	if(texel.a < 0.25) discard;								\n" +
                        "	gl_FragColor = vec4(texel.rgb,texel.a*alpha);		\n" +
                        "}\n";

        String fragmentShaderNight =
                "#ifdef GL_ES											\n" +
                        "	precision mediump float;						\n" +
                        "#endif													\n" +
                        "uniform float alpha;								\n" +
                        "uniform sampler2D u_texture;						\n" +
                        "uniform vec4 u_ambient;							\n" +
                        "varying vec2 v_TexCoord;							\n" +
                        "void main()											\n" +
                        "{\n" +
                        "	vec4 texel = texture2D( u_texture, v_TexCoord );	\n" +
                        "	if(texel.a < 0.25) discard;																\n" +
                        "	vec4 c = vec4((u_ambient.rgb + texel.rgb*texel.a)*u_ambient.a, texel.a);	\n" +
                        "	gl_FragColor = vec4(c.rgb,c.a*alpha);													\n" +
                        "}\n";
        // @on

        if (shader == null) {
            shader = ShaderLoader.fromString(vertexShader, fragmentShader);
        }

        if (shaderNight == null) {
            shaderNight = ShaderLoader.fromString(vertexShader, fragmentShaderNight);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * Sets the world position in pixels, top-left origin.
     */
    public final void setPosition(float posPxX, float posPxY) {
        positionPx.set(posPxX, posPxY);
    }

    public final void setRotation(float angle, float x_axis, float y_axis, float z_axis) {
        iRotationAngle = angle;
        iRotationAxis.set(x_axis, y_axis, z_axis);
    }

    public void setScalingFactor(float factor) {
        scalingFactor = factor;
        scaleAxis.set(scale, scale, scale);
    }

    public final void setScale(float scale) {
        this.scale = scalingFactor * scale;
        scaleAxis.set(this.scale, this.scale, this.scale);
    }
}
