package com.bitfire.uracer.u3d.still

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.collision.BoundingBox
import com.bitfire.uracer.u3d.materials.Material
import com.bitfire.uracer.u3d.model.Model
import com.bitfire.uracer.u3d.model.SubMesh

open class StillModel(vararg subMeshes: SubMesh) : Model {

    @JvmField
    val subMeshes = subMeshes.map { it as StillSubMesh }.toTypedArray()

    override fun render(program: ShaderProgram) {
        val len = subMeshes.size
        for (i in 0..<len) {
            val subMesh = subMeshes[i]
            if (i == 0) {
                subMesh.material!!.bind(program)
            } else if (subMeshes[i - 1].material != subMesh.material) {
                subMesh.material!!.bind(program)
            }
            subMesh.mesh.render(program, subMesh.primitiveType)
        }
    }

    override fun getSubMesh(name: String): StillSubMesh? {
        for (subMesh in subMeshes) {
            if (subMesh.name == name) return subMesh
        }
        return null
    }

    override fun setMaterials(vararg materials: Material) {
        if (materials.size != subMeshes.size) throw UnsupportedOperationException("number of materials must equal number of sub-meshes")
        for (i in 0..<materials.size) {
            subMeshes[i].material = materials[i]
        }
    }

    override fun setMaterial(material: Material) {
        for (subMesh in subMeshes) {
            subMesh.material = material
        }
    }

    override fun getBoundingBox(bbox: BoundingBox) {
        bbox.inf()
        for (subMesh in subMeshes) {
            subMesh.mesh.calculateBoundingBox(tmpBox)
            bbox.ext(tmpBox)
        }
    }

    override fun dispose() {
        for (subMesh in subMeshes) {
            subMesh.mesh.dispose()
        }
    }

    companion object {
        private val tmpBox = BoundingBox()
    }
}
