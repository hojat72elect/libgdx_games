package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute;

import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;


public class CustomShaderProvider extends PBRShaderProvider {

    public static final String TAG = CustomShaderProvider.class.getSimpleName();

    public CustomShaderProvider() {
        super(createDefaultConfig());
    }

    @Override
    protected Shader createShader(Renderable renderable) {
        if (renderable.material.has(TerrainMaterialAttribute.TerrainMaterial)) {
            return createTerrainShader(renderable);
        }
        return super.createShader(renderable);
    }

    private Shader createTerrainShader(Renderable renderable) {
        Shader shader = new TerrainShader(renderable, config);
        Gdx.app.log(TAG, "Terrain Shader Compiled");
        return shader;
    }
}
