

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;

public class CellEmitter {

    public static Emitter floor(int cell) {

        PointF p = DungeonTilemap.tileToWorld(cell);

        Emitter emitter = GameScene.floorEmitter();
        emitter.pos(p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE);

        return emitter;
    }

    public static Emitter get(int cell) {

        PointF p = DungeonTilemap.tileToWorld(cell);

        Emitter emitter = GameScene.emitter();
        emitter.pos(p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE);

        return emitter;
    }

    public static Emitter center(int cell) {

        PointF p = DungeonTilemap.tileToWorld(cell);

        Emitter emitter = GameScene.emitter();
        emitter.pos(p.x + DungeonTilemap.SIZE / 2, p.y + DungeonTilemap.SIZE / 2);

        return emitter;
    }

    public static Emitter bottom(int cell) {

        PointF p = DungeonTilemap.tileToWorld(cell);

        Emitter emitter = GameScene.emitter();
        emitter.pos(p.x, p.y + DungeonTilemap.SIZE, DungeonTilemap.SIZE, 0);

        return emitter;
    }
}
