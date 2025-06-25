

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

public class Compass extends Image {

    private static final float RAD_2_G = 180f / 3.1415926f;
    private static final float RADIUS = 12;

    private final int cell;
    private final PointF cellCenter;

    private final PointF lastScroll = new PointF();

    public Compass(int cell) {

        super();
        copy(Icons.COMPASS.get());
        origin.set(width / 2, RADIUS);

        this.cell = cell;
        cellCenter = DungeonTilemap.tileCenterToWorld(cell);
        visible = false;
    }

    @Override
    public void update() {
        super.update();

        if (cell < 0 || cell >= Dungeon.level.length()) {
            visible = false;
            return;
        }

        if (!visible) {
            visible = Dungeon.level.visited[cell] || Dungeon.level.mapped[cell];
        }

        if (visible) {
            PointF scroll = Camera.main.scroll;
            if (!scroll.equals(lastScroll)) {
                lastScroll.set(scroll);
                PointF center = Camera.main.center().offset(scroll);
                angle = (float) Math.atan2(cellCenter.x - center.x, center.y - cellCenter.y) * RAD_2_G;
            }
        }
    }
}
