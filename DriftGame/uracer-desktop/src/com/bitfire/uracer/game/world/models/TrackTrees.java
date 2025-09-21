package com.bitfire.uracer.game.world.models;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.uracer.utils.ScaleUtils;

import java.util.List;

public class TrackTrees {
    public final List<TreeStillModel> models;
    private final Vector3 tmpvec = new Vector3();

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

            // compute position
            tmpvec.x = (m.positionOffsetPx.x - camPersp.position.x) + (camPersp.viewportWidth / 2) + m.positionPx.x;
            tmpvec.y = (m.positionOffsetPx.y + camPersp.position.y) + (camPersp.viewportHeight / 2) - m.positionPx.y;
            tmpvec.z = 1;

            tmpvec.x *= ScaleUtils.Scale;
            tmpvec.y *= ScaleUtils.Scale;

            tmpvec.x += ScaleUtils.CropX;
            tmpvec.y += ScaleUtils.CropY;

            // transform to world space
            camPersp.unproject(tmpvec, ScaleUtils.CropX, ScaleUtils.CropY, ScaleUtils.PlayWidth, ScaleUtils.PlayHeight);

            // build model matrix
            Matrix4 model = m.mtxmodel;
            tmpvec.z = meshZ;

            model.idt();
            model.translate(tmpvec);
            model.rotate(m.iRotationAxis, m.iRotationAngle);
            model.scale(m.scaleAxis.x, m.scaleAxis.y, m.scaleAxis.z);
            transf.set(camPersp.combined).mul(m.mtxmodel);

            // transform the bounding box
            m.boundingBox.inf().set(m.localBoundingBox);
            m.boundingBox.mul(m.mtxmodel);

        }
    }
}
