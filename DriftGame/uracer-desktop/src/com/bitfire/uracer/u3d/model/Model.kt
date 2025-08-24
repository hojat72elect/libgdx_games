package com.bitfire.uracer.u3d.model

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.collision.BoundingBox
import com.bitfire.uracer.u3d.materials.Material

interface Model {

    /**
     * Renders this model using the [GL20] shader pipeline.
     *
     * **IMPORTANT:** This model must have materials set before you can use this render function. Do that by using
     * [Model.setMaterials].
     *
     * @param program The shader program that you will use to draw this object to the screen. It must be non-null.
     */
    fun render(program: ShaderProgram)

    /**
     * @param name The name of the [SubMesh] to be acquired.
     * @return The [SubMesh] that matches that name; or null, if one does not exist.
     */
    fun getSubMesh(name: String): SubMesh?

    /**
     * Generates the bounding box for the Model.
     *
     * For every finite 3D object there exists a box that can enclose the object. This function sets the given [BoundingBox]
     * to be one such enclosing box.
     * Bounding boxes are useful for very basic collision detection amongst other tasks.
     *
     * @param bbox The provided [BoundingBox] will have its internal values correctly set. (To allow Java Object reuse)
     */
    fun getBoundingBox(bbox: BoundingBox)

    /**
     * Sets every [Material] of every [SubMesh] in this [Model] to be the materials provided.
     *
     * @param materials A list of the materials to set the submeshes to, for this model. (The length of the list of materials must
     * be the same as the number of SubMeshes in this Model. Failure to do so will result in an
     * [UnsupportedOperationException])
     */
    fun setMaterials(vararg materials: Material)

    /**
     * Sets the [Material] of every [SubMesh] in this Model to be the material provided.
     *
     * @param material The Material that you wish the whole object to be rendered with.
     */
    fun setMaterial(material: Material)

    /**
     * This function releases memory once you are done with the Model. Once you are finished with the Model you MUST call this
     * function or else you will suffer memory leaks.
     */
    fun dispose()
}
