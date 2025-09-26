package com.bitfire.uracer.game.world.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.LongMap
import com.bitfire.uracer.game.GameLogic
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.actors.CarPreset
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.u3d.loaders.G3dtLoader
import com.bitfire.uracer.u3d.materials.Material
import com.bitfire.uracer.u3d.materials.TextureAttribute
import com.bitfire.uracer.u3d.still.StillModel
import com.bitfire.uracer.utils.URacerRuntimeException
import com.bitfire.utils.Hash.rsHash
import java.io.IOException

object ModelFactory {

    private var cachedMaterials: LongMap<Material>? = null
    private var cachedStillModels: LongMap<StillModel>? = null

    @JvmStatic
    fun dispose() {
        // finally, delete cached shared still models
        for (m in cachedStillModels!!.values()) m.dispose()

        cachedStillModels?.clear()
        cachedMaterials?.clear()
    }

    private fun fromString(mesh: String?): ModelMesh {
        if (mesh == null) {
            return ModelMesh.Missing
        } else if (mesh.equals("tree-1", ignoreCase = true)) {
            return ModelMesh.Tree_1
        } else if (mesh.equals("tree-2", ignoreCase = true)) {
            return ModelMesh.Tree_2
        } else if (mesh.equals("tree-3", ignoreCase = true)) {
            return ModelMesh.Tree_3
        } else if (mesh.equals("tree-4", ignoreCase = true)) {
            return ModelMesh.Tree_4
        } else if (mesh.equals("tree-5", ignoreCase = true)) {
            return ModelMesh.Tree_5
        } else if (mesh.equals("tree-6", ignoreCase = true)) {
            return ModelMesh.Tree_6
        } else if (mesh.equals("tree-7", ignoreCase = true)) {
            return ModelMesh.Tree_7
        } else if (mesh.equals("tree-8", ignoreCase = true)) {
            return ModelMesh.Tree_8
        } else if (mesh.equals("tree-9", ignoreCase = true)) {
            return ModelMesh.Tree_9
        } else if (mesh.equals("car", ignoreCase = true)) {
            return ModelMesh.Car
        }

        return ModelMesh.Missing
    }

    @JvmStatic
    fun create(meshType: String, posPxX: Float, posPxY: Float, scale: Float): OrthographicAlignedStillModel {
        val type = fromString(meshType)
        return create(type, posPxX, posPxY, scale)
    }

    fun create(modelMesh: ModelMesh, posPxX: Float, posPxY: Float, scale: Float): OrthographicAlignedStillModel {

        val stillModel = OrthographicAlignedStillModel(getStillModel("data/3d/models/missing-mesh.g3dt"), getMaterial(modelMesh, Art.meshMissing, ""))

        stillModel.setPosition(posPxX, posPxY)
        if (modelMesh != ModelMesh.Missing) {
            stillModel.setScale(scale)
        } else {
            stillModel.setScale(1F)
        }

        return stillModel
    }

    @JvmStatic
    fun createCarStillModel(gameLogic: GameLogic?, car: Car?, presetType: CarPreset.Type): CarStillModel {
        return CarStillModel(
            gameLogic, getStillModel("data/3d/models/car-low-01.g3dt"), getMaterial(
                ModelMesh.Car, Art.meshCar.get(presetType.regionName), presetType.regionName
            ), car
        )
    }

    @JvmStatic
    fun createTree(meshType: String, posPxX: Float, posPxY: Float, scale: Float): TreeStillModel {
        val type = fromString(meshType)
        return createTree(type, posPxX, posPxY, scale)
    }

    fun createTree(modelMesh: ModelMesh, posPxX: Float, posPxY: Float, scale: Float): TreeStillModel {
        val stillModel: TreeStillModel

        val treeModelName: String?
        val treeMeshName: String?
        val leavesTexture: Texture?

        when (modelMesh) {
            ModelMesh.Tree_1 -> {
                treeModelName = "tree-1.g3dt"
                treeMeshName = "tree_1_"
                leavesTexture = Art.meshTreeLeavesSpring[2]
            }

            ModelMesh.Tree_2 -> {
                treeModelName = "tree-2.g3dt"
                treeMeshName = "tree_2_"
                leavesTexture = Art.meshTreeLeavesSpring[0]
            }

            ModelMesh.Tree_3 -> {
                treeModelName = "tree-3.g3dt"
                treeMeshName = "tree_3_"
                leavesTexture = Art.meshTreeLeavesSpring[0]
            }

            ModelMesh.Tree_4 -> {
                treeModelName = "tree-4.g3dt"
                treeMeshName = "tree_4_"
                leavesTexture = Art.meshTreeLeavesSpring[1]
            }

            ModelMesh.Tree_5 -> {
                treeModelName = "tree-5.g3dt"
                treeMeshName = "tree_5_"
                leavesTexture = Art.meshTreeLeavesSpring[4]
            }

            ModelMesh.Tree_6 -> {
                treeModelName = "tree-6.g3dt"
                treeMeshName = "tree_6_"
                leavesTexture = Art.meshTreeLeavesSpring[5]
            }

            ModelMesh.Tree_7 -> {
                treeModelName = "tree-7.g3dt"
                treeMeshName = "tree_7_"
                leavesTexture = Art.meshTreeLeavesSpring[4]
            }

            ModelMesh.Tree_8 -> {
                treeModelName = "tree-8.g3dt"
                treeMeshName = "tree_8_"
                leavesTexture = Art.meshTreeLeavesSpring[3]
            }

            ModelMesh.Tree_9 -> {
                treeModelName = "tree-9.g3dt"
                treeMeshName = "tree_9_"
                leavesTexture = Art.meshTreeLeavesSpring[6]
            }

            ModelMesh.Car, ModelMesh.Missing -> throw URacerRuntimeException("The specified model is not a tree")
        }

        stillModel = TreeStillModel(getStillModel("data/3d/models/$treeModelName")!!, getMaterial(modelMesh, leavesTexture, "")!!, treeMeshName)

        stillModel.setPosition(posPxX, posPxY)
        stillModel.setScale(scale)

        return stillModel
    }

    private fun getMaterial(modelMesh: ModelMesh, texture: Texture?, textureName: String?): Material? {
        val m: Material?

        val materialHash = rsHash(modelMesh.toString() + textureName)
        if (cachedMaterials == null) {
            cachedMaterials = LongMap<Material>()
        }

        if (cachedMaterials!!.containsKey(materialHash)) {
            return cachedMaterials!!.get(materialHash)
        } else {
            val ta = TextureAttribute(texture!!, 0, "u_texture")
            m = Material("default", ta)
            cachedMaterials!!.put(materialHash, m)
        }

        return m
    }

    private fun getStillModel(model: String): StillModel? {
        var m: StillModel? = null
        val modelHash = rsHash(model)

        if (cachedStillModels == null) {
            cachedStillModels = LongMap<StillModel>()
        }

        if (cachedStillModels!!.containsKey(modelHash)) {
            return cachedStillModels!!.get(modelHash)
        } else {
            try {
                val ext: Array<String?> = model.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if (ext[1] == "g3dt") {
                    // NO opengl coords, NO invert v
                    val `in` = Gdx.files.internal(model).read()
                    m = G3dtLoader.loadStillModel(`in`, true)
                    `in`.close()
                } else if (ext[1] == "obj") {
                    Gdx.app.log("ModelFactory", "Attention, ignoring deprecated OBJ model!")
                }

                cachedStillModels!!.put(modelHash, m)
            } catch (ioex: IOException) {
                Gdx.app.log("ModelFactory", ioex.message)
            }
        }

        return m
    }

    enum class ModelMesh {
        Missing, Tree_1, Tree_2, Tree_3, Tree_4, Tree_5, Tree_6, Tree_7, Tree_8, Tree_9, Car
    }
}
