package com.bitfire.uracer.u3d.model;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.bitfire.uracer.u3d.materials.Material;

public abstract class SubMesh {
    public String name;
    public Material material;
    public int primitiveType;
    public Mesh mesh;


    public SubMesh(String name, Mesh mesh, int primitiveType, Material material) {
        this.name = name;
        this.setMesh(mesh);
        this.primitiveType = primitiveType;
        this.material = material;
    }

    public SubMesh(String name, Mesh mesh, int primitiveType) {
        this(name, mesh, primitiveType, null);
    }

    /**
     * Obtain the {@link BoundingBox} of this {@link SubMesh}.
     *
     * @param bbox This {@link BoundingBox} will be modified so that its contain values that are the bounding box for this SubMesh.
     */
    public abstract void getBoundingBox(BoundingBox bbox);

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
