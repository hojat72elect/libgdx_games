package com.mygdx.game.terrains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.terrains.attributes.TerrainFloatAttribute;
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute;
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute;


public class HeightMapTerrain extends Terrain {
    private static final Vector3 c00 = new Vector3();
    private static final Vector3 c01 = new Vector3();
    private static final Vector3 c10 = new Vector3();
    private static final Vector3 c11 = new Vector3();

    private final HeightField field;

    public HeightMapTerrain(Pixmap data, float magnitude) {
        this.size = 400;
        this.width = data.getWidth();
        this.heightMagnitude = magnitude;

        field = new HeightField(true, data, true, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        data.dispose();
        field.corner00.set(0, 0, 0);
        field.corner10.set(size, 0, 0);
        field.corner01.set(0, 0, size);
        field.corner11.set(size, 0, size);
        field.magnitude.set(0f, magnitude, 0f);
        field.update();

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.part("terrain", field.mesh, GL20.GL_TRIANGLES, new Material());
        modelInstance = new ModelInstance(mb.end());

        // Setting the material attributes before model creation was resulting in strange issues
        Material material = modelInstance.materials.get(0);

        TerrainTextureAttribute baseAttribute = TerrainTextureAttribute.createDiffuseBase(getMipMapTexture("textures/Vol_19_4_Base_Color.png"));
        TerrainTextureAttribute terrainSlopeTexture = TerrainTextureAttribute.createDiffuseSlope(getMipMapTexture("textures/Vol_27_4_Base_Color.png"));
        TerrainTextureAttribute terrainHeightTexture = TerrainTextureAttribute.createDiffuseHeight(getMipMapTexture("textures/Vol_16_2_Base_Color.png"));

        baseAttribute.scaleU = 40f;
        baseAttribute.scaleV = 40f;

        TerrainFloatAttribute slope = TerrainFloatAttribute.createMinSlope(0.85f);

        TerrainMaterial terrainMaterial = new TerrainMaterial();
        terrainMaterial.set(baseAttribute);
        terrainMaterial.set(terrainSlopeTexture);
        terrainMaterial.set(terrainHeightTexture);
        terrainMaterial.set(slope);

        material.set(TerrainMaterialAttribute.createTerrainMaterialAttribute(terrainMaterial));
    }

    private Texture getMipMapTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return texture;
    }

    @Override
    public void dispose() {
        field.dispose();
    }

    @Override
    public float getHeightAtWorldCoord(float worldX, float worldZ) {
        // Convert world coordinates to a position relative to the terrain
        modelInstance.transform.getTranslation(c00);
        float terrainX = worldX - c00.x;
        float terrainZ = worldZ - c00.z;

        // The size between the vertices
        float gridSquareSize = size / ((float) width - 1);

        // Determine which grid square the coordinates are in
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        // Validates the grid square
        if (gridX >= width - 1 || gridZ >= width - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        // Determine where on the grid square the coordinates are
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        // Determine the triangle we are on and apply barrycentric.
        float height;
        if (xCoord <= (1 - zCoord)) { // Upper left triangle
            height = barryCentric(
                    c00.set(0, field.data[gridZ * width + gridX], 0),
                    c10.set(1, field.data[gridZ * width + (gridX + 1)], 0),
                    c01.set(0, field.data[(gridZ + 1) * width + gridX], 1),
                    new Vector2(xCoord, zCoord)
            );
        } else {
            height = barryCentric(
                    c10.set(1, field.data[gridZ * width + (gridX + 1)], 0),
                    c11.set(1, field.data[(gridZ + 1) * width + (gridX + 1)], 1),
                    c01.set(0, field.data[(gridZ + 1) * width + gridX], 1),
                    new Vector2(xCoord, zCoord)
            );
        }

        return height * heightMagnitude;
    }

    public static float barryCentric(Vector3 p1, Vector3 p2, Vector3 p3, Vector2 pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
