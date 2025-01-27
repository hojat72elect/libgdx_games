package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.terrains.TerrainMaterial;
import com.mygdx.game.terrains.attributes.TerrainFloatAttribute;
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute;
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute;

/**
 * @author JamesTKhan
 * @version August 15, 2022
 */
public class TerrainShader extends BaseShader {

    public static class TerrainInputs {
        public final static Uniform diffuseUVTransform = new Uniform("u_diffuseUVTransform");
        public final static Uniform diffuseBaseTexture = new Uniform("u_diffuseBaseTexture");
        public final static Uniform diffuseHeightTexture = new Uniform("u_diffuseHeightTexture");
        public final static Uniform diffuseSlopeTexture = new Uniform("u_diffuseSlopeTexture");
        public final static Uniform minSlope = new Uniform("u_minSlope");
    }

    public static class TerrainSetters {
        public final static Setter diffuseUVTransform = new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                TerrainMaterial mat = getTerrainMaterial(renderable);
                TerrainTextureAttribute attr = (TerrainTextureAttribute) mat.get(TerrainTextureAttribute.DiffuseBase);
                shader.set(inputID, attr.offsetU, attr.offsetV, attr.scaleU, attr.scaleV);
            }
        };

        public final static Setter diffuseBaseTexture = new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                TerrainMaterial mat = getTerrainMaterial(renderable);
                TerrainTextureAttribute attr = (TerrainTextureAttribute) mat.get(TerrainTextureAttribute.DiffuseBase);
                int unit = shader.context.textureBinder.bind(attr.textureDescription);
                shader.set(inputID, unit);
            }
        };
        public final static Setter diffuseHeightTexture = new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                TerrainMaterial mat = getTerrainMaterial(renderable);
                TerrainTextureAttribute attr = (TerrainTextureAttribute) mat.get(TerrainTextureAttribute.DiffuseHeight);
                int unit = shader.context.textureBinder.bind(attr.textureDescription);
                shader.set(inputID, unit);
            }
        };
        public final static Setter diffuseSlopeTexture = new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                TerrainMaterial mat = getTerrainMaterial(renderable);
                TerrainTextureAttribute attr = (TerrainTextureAttribute) mat.get(TerrainTextureAttribute.DiffuseSlope);
                int unit = shader.context.textureBinder.bind(attr.textureDescription);
                shader.set(inputID, unit);
            }
        };
        public final static Setter minSlope = new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                TerrainMaterial mat = getTerrainMaterial(renderable);
                TerrainFloatAttribute attr = (TerrainFloatAttribute) mat.get(TerrainFloatAttribute.MinSlope);
                shader.set(inputID, attr.value);
            }
        };

    }

    /**
     * The renderable used to create this shader, invalid after the call to init
     */
    private Renderable renderable;

    // Global uniforms
    public final int u_projViewTrans;

    // Object uniforms
    public final int u_worldTrans;
    public final int u_normalMatrix;

    // Material uniforms
    public final int u_diffuseUVTransform;
    public final int u_diffuseBaseTexture;
    public final int u_diffuseHeightTexture;
    public final int u_diffuseSlopeTexture;
    public final int u_minSlope;

    //Lights
    protected int u_ambientLight;
    protected int u_dirLights0color;
    protected int u_dirLights0direction;

    // Masks
    private final long attributesMask;
    private final long terrainMaterialMask;

    public TerrainShader(Renderable renderable, DefaultShader.Config config) {
        this.renderable = renderable;

        String prefix = DefaultShader.createPrefix(renderable, config);

        TerrainMaterial terrainMaterial = getTerrainMaterial(renderable);

        attributesMask = combineAttributeMasks(renderable);
        terrainMaterialMask = terrainMaterial.getMask();

        // Add defines to our prefix
        TerrainTextureAttribute attribute = terrainMaterial.get(TerrainTextureAttribute.class, TerrainTextureAttribute.DiffuseSlope);
        if (attribute != null) {
            prefix += "#define " + TerrainTextureAttribute.DiffuseSlopeAlias + "Flag\n";
        }

        attribute = terrainMaterial.get(TerrainTextureAttribute.class, TerrainTextureAttribute.DiffuseHeight);
        if (attribute != null) {
            prefix += "#define " + TerrainTextureAttribute.DiffuseHeightAlias + "Flag\n";
        }

        if (renderable.environment.has(ColorAttribute.AmbientLight)) {
            prefix += "#define ambientLightFlag\n";
        }

        // Compile the shaders
        this.program = new ShaderProgram(prefix + getDefaultVertexShader(), prefix + getDefaultFragmentShader());

        u_projViewTrans = register(DefaultShader.Inputs.projViewTrans, DefaultShader.Setters.projViewTrans);
        u_worldTrans = register(DefaultShader.Inputs.worldTrans, DefaultShader.Setters.worldTrans);
        u_normalMatrix = register(DefaultShader.Inputs.normalMatrix, DefaultShader.Setters.normalMatrix);

        u_diffuseUVTransform = register(TerrainInputs.diffuseUVTransform, TerrainSetters.diffuseUVTransform);
        u_diffuseBaseTexture = register(TerrainInputs.diffuseBaseTexture, TerrainSetters.diffuseBaseTexture);
        u_diffuseHeightTexture = register(TerrainInputs.diffuseHeightTexture, TerrainSetters.diffuseHeightTexture);
        u_diffuseSlopeTexture = register(TerrainInputs.diffuseSlopeTexture, TerrainSetters.diffuseSlopeTexture);
        u_minSlope = register(TerrainInputs.minSlope, TerrainSetters.minSlope);
    }

    @Override
    public void init() {
        u_dirLights0color = register(new Uniform("u_dirLights[0].color"));
        u_dirLights0direction = register(new Uniform("u_dirLights[0].direction"));

        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;

        u_ambientLight = program.fetchUniformLocation("u_ambientLight", false);
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);
        context.setDepthTest(GL20.GL_LESS, 0f, 1f);
        context.setCullFace(GL20.GL_BACK);
        context.setDepthMask(true);
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes) {
        bindLights(combinedAttributes);
        super.render(renderable, combinedAttributes);
    }

    private void bindLights(Attributes combinedAttributes) {
        final DirectionalLightsAttribute dla = combinedAttributes.get(DirectionalLightsAttribute.class, DirectionalLightsAttribute.Type);
        final Array<DirectionalLight> dirs = dla == null ? null : dla.lights;

        if (dirs != null) {
            set(u_dirLights0color, dirs.get(0).color.r, dirs.get(0).color.g,
                    dirs.get(0).color.b
            );
            set(u_dirLights0direction, dirs.get(0).direction.x,
                    dirs.get(0).direction.y, dirs.get(0).direction.z
            );
        }

        ColorAttribute ambientLight = combinedAttributes.get(ColorAttribute.class, ColorAttribute.AmbientLight);
        if (ambientLight != null) {
            program.setUniformf(u_ambientLight, ambientLight.color.r, ambientLight.color.g, ambientLight.color.b);
        }
    }

    @Override
    public int compareTo(Shader other) {
        if (other == null) return -1;
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        if (combineAttributeMasks(instance) != attributesMask) {
            return false;
        }

        return terrainMaterialMask == getTerrainMaterial(instance).getMask();
    }

    private static long combineAttributeMasks(final Renderable renderable) {
        long mask = 0;
        if (renderable.environment != null) mask |= renderable.environment.getMask();
        if (renderable.material != null) mask |= renderable.material.getMask();
        return mask;
    }

    public static String getDefaultVertexShader() {
        return Gdx.files.internal("shaders/terrain.vert.glsl").readString();
    }

    public static String getDefaultFragmentShader() {
        return Gdx.files.internal("shaders/terrain.frag.glsl").readString();
    }

    private static TerrainMaterial getTerrainMaterial(Renderable renderable) {
        return renderable.material.get(TerrainMaterialAttribute.class, TerrainMaterialAttribute.TerrainMaterial).terrainMaterial;
    }
}
