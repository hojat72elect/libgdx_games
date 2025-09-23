package com.bitfire.uracer.game.world.models

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.collision.BoundingBox
import com.bitfire.uracer.u3d.materials.Material
import com.bitfire.uracer.u3d.still.StillModel

class TreeStillModel(aModel: StillModel, material: Material, meshName: String) : OrthographicAlignedStillModel(aModel, material) {

    @JvmField
    var smLeaves = model.getSubMesh("${meshName}leaves")

    @JvmField
    var smTrunk = model.getSubMesh("${meshName}trunk")

    @JvmField
    var trunk = smTrunk?.mesh

    @JvmField
    var leaves = smLeaves?.mesh

    @JvmField
    var transformed = Matrix4()

    @JvmField
    var mtxmodel = Matrix4()

    @JvmField
    var treeBoundingBox = BoundingBox()

    @JvmField
    var treeLocalBoundingBox = BoundingBox()

    init {
        model.getBoundingBox(treeLocalBoundingBox)
        treeBoundingBox.set(treeLocalBoundingBox)
    }
}
