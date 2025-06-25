

package com.watabou.noosa.tweeners;

import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

public class PosTweener extends Tweener {

    public Visual visual;

    public PointF start;
    public PointF end;

    public PosTweener(Visual visual, PointF pos, float time) {
        super(visual, time);

        this.visual = visual;
        start = visual.point();
        end = pos;
    }

    @Override
    protected void updateValues(float progress) {
        visual.point(PointF.inter(start, end, progress));
    }
}
