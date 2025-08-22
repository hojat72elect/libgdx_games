package com.bitfire.uracer.u3d.still;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.bitfire.uracer.u3d.materials.Material;
import com.bitfire.uracer.u3d.model.Model;
import com.bitfire.uracer.u3d.model.SubMesh;

public class StillModel implements Model {
    private final static BoundingBox tmpBox = new BoundingBox();
    final public StillSubMesh[] subMeshes;

    public StillModel(SubMesh... subMeshes) {
        this.subMeshes = new StillSubMesh[subMeshes.length];
        for (int i = 0; i < subMeshes.length; ++i) {
            this.subMeshes[i] = (StillSubMesh) subMeshes[i];
        }
    }

    @Override
    public void render(ShaderProgram program) {
        int len = subMeshes.length;
        for (int i = 0; i < len; i++) {
            StillSubMesh subMesh = subMeshes[i];
            if (i == 0) {
                subMesh.material.bind(program);
            } else if (!subMeshes[i - 1].material.equals(subMesh.material)) {
                subMesh.material.bind(program);
            }
            subMesh.mesh.render(program, subMesh.primitiveType);
        }
    }

    @Override
    public StillSubMesh getSubMesh(String name) {
        for (StillSubMesh subMesh : subMeshes) {
            if (subMesh.name.equals(name)) return subMesh;
        }
        return null;
    }

    @Override
    public void setMaterials(Material... materials) {
        if (materials.length != subMeshes.length)
            throw new UnsupportedOperationException("number of materials must equal number of sub-meshes");
        int len = materials.length;
        for (int i = 0; i < len; i++) {
            subMeshes[i].material = materials[i];
        }
    }

    @Override
    public void setMaterial(Material material) {
        for (StillSubMesh subMesh : subMeshes) {
            subMesh.material = material;
        }
    }

    @Override
    public void getBoundingBox(BoundingBox bbox) {
        bbox.inf();
        for (StillSubMesh subMesh : subMeshes) {
            subMesh.mesh.calculateBoundingBox(tmpBox);
            bbox.ext(tmpBox);
        }
    }

    @Override
    public void dispose() {
        for (StillSubMesh subMesh : subMeshes) {
            subMesh.mesh.dispose();
        }
    }
}
