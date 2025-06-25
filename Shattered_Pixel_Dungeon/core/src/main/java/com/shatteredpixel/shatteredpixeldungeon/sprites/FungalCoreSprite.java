

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonWallsTilemap;
import com.watabou.noosa.TextureFilm;

public class FungalCoreSprite extends MobSprite {

    public FungalCoreSprite() {
        super();

        texture(Assets.Sprites.FUNGAL_CORE);

        TextureFilm frames = new TextureFilm(texture, 27, 27);

        idle = new Animation(0, true);
        idle.frames(frames, 0);

        run = new Animation(0, true);
        run.frames(frames, 0);

        attack = new Animation(24, false);
        attack.frames(frames, 0);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 0);

        play(idle);
    }

    boolean wasVisible = false;

    @Override
    public void update() {
        super.update();
        if (curAnim != die && ch != null && visible != wasVisible) {
            if (visible) {
                DungeonWallsTilemap.skipCells.add(ch.pos - 2 * Dungeon.level.width());
                DungeonWallsTilemap.skipCells.add(ch.pos - Dungeon.level.width());
            } else {
                DungeonWallsTilemap.skipCells.remove(ch.pos - 2 * Dungeon.level.width());
                DungeonWallsTilemap.skipCells.remove(ch.pos - Dungeon.level.width());
            }
            GameScene.updateMap(ch.pos - 2 * Dungeon.level.width());
            GameScene.updateMap(ch.pos - Dungeon.level.width());
            wasVisible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (ch != null && visible) {
            DungeonWallsTilemap.skipCells.remove(ch.pos - 2 * Dungeon.level.width());
            DungeonWallsTilemap.skipCells.remove(ch.pos - Dungeon.level.width());
            GameScene.updateMap(ch.pos - 2 * Dungeon.level.width());
            GameScene.updateMap(ch.pos - Dungeon.level.width());
        }
    }

    @Override
    public void turnTo(int from, int to) {
        //do nothing
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }
}
