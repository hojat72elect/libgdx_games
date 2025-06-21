package com.mygdx.game.shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider

class CustomShaderProvider : PBRShaderProvider(createDefaultConfig()) {
    override fun createShader(renderable: Renderable): Shader {
        if (renderable.material.has(TerrainMaterialAttribute.TerrainMaterial)) {
            return createTerrainShader(renderable)
        }
        return super.createShader(renderable)
    }

    private fun createTerrainShader(renderable: Renderable): Shader {
        val shader: Shader = TerrainShader(renderable, config)
        Gdx.app.log(TAG, "Terrain Shader Compiled")
        return shader
    }

    companion object {
        val TAG: String = CustomShaderProvider::class.java.simpleName
    }
}
