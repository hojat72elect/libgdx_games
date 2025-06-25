

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class Chains extends Group {

    private static final double A = 180 / Math.PI;

    private float spent = 0f;
    private final float duration;

    private final Callback callback;

    private final Image[] chains;
    private final int numChains;
    private final float distance;
    private float rotation = 0;

    private final PointF from;
    private final PointF to;

    public Chains(int from, int to, Effects.Type type, Callback callback) {
        this(DungeonTilemap.tileCenterToWorld(from),
                DungeonTilemap.tileCenterToWorld(to),
                type,
                callback);
    }

    public Chains(PointF from, PointF to, Effects.Type type, Callback callback) {
        super();

        this.callback = callback;

        this.from = from;
        this.to = to;

        float dx = to.x - from.x;
        float dy = to.y - from.y;
        distance = (float) Math.hypot(dx, dy);

        //base of 200ms, plus 50ms per tile travelled
        duration = distance / 320f + 0.2f;

        rotation = (float) (Math.atan2(dy, dx) * A) + 90f;

        numChains = Math.round(distance / 6f) + 1;

        chains = new Image[numChains];
        for (int i = 0; i < chains.length; i++) {
            chains[i] = new Image(Effects.get(type));
            chains[i].angle = rotation;
            chains[i].origin.set(chains[i].width() / 2, chains[i].height());
            add(chains[i]);
        }
    }

    @Override
    public void update() {
        if ((spent += Game.elapsed) > duration) {

            killAndErase();
            if (callback != null) {
                callback.call();
            }
        } else {
            float dx = to.x - from.x;
            float dy = to.y - from.y;
            for (int i = 0; i < chains.length; i++) {
                chains[i].center(new PointF(
                        from.x + ((dx * (i / (float) chains.length)) * (spent / duration)),
                        from.y + ((dy * (i / (float) chains.length)) * (spent / duration))
                ));
            }
        }
    }
}
