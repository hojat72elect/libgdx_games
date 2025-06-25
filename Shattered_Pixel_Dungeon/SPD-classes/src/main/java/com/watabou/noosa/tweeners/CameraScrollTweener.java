

package com.watabou.noosa.tweeners;

import com.watabou.noosa.Camera;
import com.watabou.utils.PointF;

public class CameraScrollTweener extends Tweener {

    public Camera camera;

    public PointF start;
    public PointF end;

    public CameraScrollTweener(Camera camera, PointF pos, float time) {
        super(camera, time);

        this.camera = camera;
        start = camera.scroll;
        end = pos;
    }

    @Override
    protected void updateValues(float progress) {
        camera.scroll = PointF.inter(start, end, progress);
    }
}
