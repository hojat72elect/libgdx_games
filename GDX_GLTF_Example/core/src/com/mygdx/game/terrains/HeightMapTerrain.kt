package com.mygdx.game.terrains

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.terrains.attributes.TerrainFloatAttribute.Companion.createMinSlope
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute.Companion.createTerrainMaterialAttribute
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute.Companion.createDiffuseBase
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute.Companion.createDiffuseHeight
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute.Companion.createDiffuseSlope
import kotlin.math.floor

class HeightMapTerrain(data: Pixmap, magnitude: Float) : Terrain() {
    private val field: HeightField

    init {
        this.size = 400
        this.width = data.width
        this.heightMagnitude = magnitude

        field = HeightField(true, data, true, VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates)
        data.dispose()
        field.corner00[0f, 0f] = 0f
        field.corner10[size.toFloat(), 0f] = 0f
        field.corner01[0f, 0f] = size.toFloat()
        field.corner11[size.toFloat(), 0f] = size.toFloat()
        field.magnitude[0f, magnitude] = 0f
        field.update()

        val mb = ModelBuilder()
        mb.begin()
        mb.part("terrain", field.mesh, GL20.GL_TRIANGLES, Material())
        modelInstance = ModelInstance(mb.end())

        // Setting the material attributes before model creation was resulting in strange issues
        val material = modelInstance!!.materials[0]

        val baseAttribute = createDiffuseBase(getMipMapTexture("textures/Vol_19_4_Base_Color.png"))
        val terrainSlopeTexture = createDiffuseSlope(getMipMapTexture("textures/Vol_27_4_Base_Color.png"))
        val terrainHeightTexture = createDiffuseHeight(getMipMapTexture("textures/Vol_16_2_Base_Color.png"))

        baseAttribute.scaleU = 40f
        baseAttribute.scaleV = 40f

        val slope = createMinSlope(0.85f)

        val terrainMaterial = TerrainMaterial()
        terrainMaterial.set(baseAttribute)
        terrainMaterial.set(terrainSlopeTexture)
        terrainMaterial.set(terrainHeightTexture)
        terrainMaterial.set(slope)

        material.set(createTerrainMaterialAttribute(terrainMaterial))
    }

    private fun getMipMapTexture(path: String): Texture {
        val texture = Texture(Gdx.files.internal(path), true)
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear)
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
        return texture
    }

    override fun dispose() {
        field.dispose()
    }

    override fun getHeightAtWorldCoord(worldX: Float, worldZ: Float): Float {
        // Convert world coordinates to a position relative to the terrain
        modelInstance!!.transform.getTranslation(c00)
        val terrainX = worldX - c00.x
        val terrainZ = worldZ - c00.z

        // The size between the vertices
        val gridSquareSize = size / (width.toFloat() - 1)

        // Determine which grid square the coordinates are in
        val gridX = floor((terrainX / gridSquareSize).toDouble()).toInt()
        val gridZ = floor((terrainZ / gridSquareSize).toDouble()).toInt()

        // Validates the grid square
        if (gridX >= width - 1 || gridZ >= width - 1 || gridX < 0 || gridZ < 0) {
            return 0f
        }

        // Determine where on the grid square the coordinates are
        val xCoord = (terrainX % gridSquareSize) / gridSquareSize
        val zCoord = (terrainZ % gridSquareSize) / gridSquareSize

        // Determine the triangle we are on and apply barrycentric.
        val height = if (xCoord <= (1 - zCoord)) { // Upper left triangle
            barryCentric(
                c00.set(0f, field.data[gridZ * width + gridX], 0f),
                c10.set(1f, field.data[gridZ * width + (gridX + 1)], 0f),
                c01.set(0f, field.data[(gridZ + 1) * width + gridX], 1f),
                Vector2(xCoord, zCoord)
            )
        } else {
            barryCentric(
                c10.set(1f, field.data[gridZ * width + (gridX + 1)], 0f),
                c11.set(1f, field.data[(gridZ + 1) * width + (gridX + 1)], 1f),
                c01.set(0f, field.data[(gridZ + 1) * width + gridX], 1f),
                Vector2(xCoord, zCoord)
            )
        }

        return height * heightMagnitude
    }

    companion object {
        private val c00 = Vector3()
        private val c01 = Vector3()
        private val c10 = Vector3()
        private val c11 = Vector3()

        fun barryCentric(p1: Vector3, p2: Vector3, p3: Vector3, pos: Vector2): Float {
            val det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z)
            val l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det
            val l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det
            val l3 = 1.0f - l1 - l2
            return l1 * p1.y + l2 * p2.y + l3 * p3.y
        }
    }
}
