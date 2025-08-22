package com.bitfire.uracer.u3d.still;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.bitfire.uracer.u3d.materials.Material;
import com.bitfire.uracer.u3d.model.SubMesh;

public class StillSubMesh extends SubMesh {

    public StillSubMesh(String name, Mesh mesh, int primitiveType, Material material) {
        super(name, mesh, primitiveType, material);
    }

    public StillSubMesh(String name, Mesh mesh, int primitiveType) {
        super(name, mesh, primitiveType);
    }

    @Override
    public void getBoundingBox(BoundingBox bbox) {
        mesh.calculateBoundingBox(bbox);
    }
}
