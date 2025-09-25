package com.bitfire.uracer.game.world.models

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.bitfire.uracer.utils.ScaleUtils

class TrackTrees(@JvmField val models: MutableList<TreeStillModel>) {

    private val temporaryVector = Vector3()

    fun count() = models.size

    fun transform(camPersp: PerspectiveCamera, camOrtho: OrthographicCamera) {
        val meshZ = -(camPersp.far - camPersp.position.z) + (camPersp.far * (1 - (camOrtho.zoom)))

        for (m in models) {
            val transf = m.transformed

            temporaryVector.x = (m.positionOffsetPx.x - camPersp.position.x) + (camPersp.viewportWidth / 2) + m.positionPx.x
            temporaryVector.y = (m.positionOffsetPx.y + camPersp.position.y) + (camPersp.viewportHeight / 2) - m.positionPx.y
            temporaryVector.z = 1f

            temporaryVector.x *= ScaleUtils.Scale
            temporaryVector.y *= ScaleUtils.Scale

            temporaryVector.x += ScaleUtils.CropX.toFloat()
            temporaryVector.y += ScaleUtils.CropY.toFloat()

            // transform to world space
            camPersp.unproject(
                temporaryVector,
                ScaleUtils.CropX.toFloat(),
                ScaleUtils.CropY.toFloat(),
                ScaleUtils.PlayWidth.toFloat(),
                ScaleUtils.PlayHeight.toFloat()
            )

            // build model matrix
            val model = m.mtxmodel
            temporaryVector.z = meshZ

            model.idt()
            model.translate(temporaryVector)
            model.rotate(m.iRotationAxis, m.iRotationAngle)
            model.scale(m.scaleAxis.x, m.scaleAxis.y, m.scaleAxis.z)
            transf.set(camPersp.combined).mul(m.mtxmodel)

            // transform the bounding box
            m.treeBoundingBox.inf().set(m.treeLocalBoundingBox)
            m.treeBoundingBox.mul(m.mtxmodel)
        }
    }
}
