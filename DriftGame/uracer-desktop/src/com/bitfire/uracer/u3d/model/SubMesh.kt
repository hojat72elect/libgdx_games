package com.bitfire.uracer.u3d.model

import com.badlogic.gdx.graphics.Mesh
import com.bitfire.uracer.u3d.materials.Material

abstract class SubMesh @JvmOverloads constructor(@JvmField val name: String, @JvmField val mesh: Mesh, @JvmField val primitiveType: Int, @JvmField var material: Material? = null)
