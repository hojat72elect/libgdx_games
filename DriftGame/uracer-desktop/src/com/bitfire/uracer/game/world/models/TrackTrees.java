package com.bitfire.uracer.game.world.models;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.uracer.utils.ScaleUtils;

import java.util.List;

public class TrackTrees {
    public final List<TreeStillModel> models;
    private final Vector3 temporaryVector = new Vector3();

    public TrackTrees(List<TreeStillModel> models) {
        this.models = models;
    }

    public int count() {
        return (models != null ? models.size() : 0);
    }

    public void transform(PerspectiveCamera camPersp, OrthographicCamera camOrtho) {
        float meshZ = -(camPersp.far - camPersp.position.z) + (camPersp.far * (1 - (camOrtho.zoom)));

        for (TreeStillModel m : models) {
            Matrix4 transf = m.transformed;

            temporaryVector.x = (m.positionOffsetPx.x - camPersp.position.x) + (camPersp.viewportWidth / 2) + m.positionPx.x;
            temporaryVector.y = (m.positionOffsetPx.y + camPersp.position.y) + (camPersp.viewportHeight / 2) - m.positionPx.y;
            temporaryVector.z = 1;

            temporaryVector.x *= ScaleUtils.Scale;
            temporaryVector.y *= ScaleUtils.Scale;

            temporaryVector.x += ScaleUtils.CropX;
            temporaryVector.y += ScaleUtils.CropY;

            // transform to world space
            camPersp.unproject(temporaryVector, ScaleUtils.CropX, ScaleUtils.CropY, ScaleUtils.PlayWidth, ScaleUtils.PlayHeight);

            // build model matrix
            Matrix4 model = m.mtxmodel;
            temporaryVector.z = meshZ;

            model.idt();
            model.translate(temporaryVector);
            model.rotate(m.iRotationAxis, m.iRotationAngle);
            model.scale(m.scaleAxis.x, m.scaleAxis.y, m.scaleAxis.z);
            transf.set(camPersp.combined).mul(m.mtxmodel);

            // transform the bounding box
            m.treeBoundingBox.inf().set(m.treeLocalBoundingBox);
            m.treeBoundingBox.mul(m.mtxmodel);

        }
    }
}
