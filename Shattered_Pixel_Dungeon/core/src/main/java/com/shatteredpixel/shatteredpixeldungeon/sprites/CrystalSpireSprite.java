

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonWallsTilemap;
import com.watabou.noosa.TextureFilm;

public abstract class CrystalSpireSprite extends MobSprite {

    {
        perspectiveRaise = 7 / 16f; //7 pixels

        shadowWidth = 1f;
        shadowHeight = 1f;
        shadowOffset = 1f;
    }

    public CrystalSpireSprite() {
        texture(Assets.Sprites.CRYSTAL_SPIRE);

        TextureFilm frames = new TextureFilm(texture, 24, 41);

        int c = texOffset();

        idle = new Animation(1, true);
        idle.frames(frames, c);

        run = idle.clone();
        attack = idle.clone();
        zap = idle.clone();

        die = new Animation(1, false);
        die.frames(frames, 4 + c);

        play(idle);
    }

    public void updateIdle() {
        float hpPercent = 1f;
        if (ch != null) {
            hpPercent = ch.HP / (float) ch.HT;
        }

        TextureFilm frames = new TextureFilm(texture, 24, 41);

        if (hpPercent > 0.9f) {
            idle.frames(frames, texOffset());
        } else if (hpPercent > 0.67f) {
            idle.frames(frames, 1 + texOffset());
        } else if (hpPercent > 0.33f) {
            idle.frames(frames, 2 + texOffset());
        } else {
            idle.frames(frames, 3 + texOffset());
        }
        play(idle, true);
        run = idle.clone();
        attack = idle.clone();
        zap = idle.clone();
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        updateIdle();
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
        Splash.around(this, blood(), 100);
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

    protected abstract int texOffset();

    public static class Blue extends CrystalSpireSprite {
        @Override
        protected int texOffset() {
            return 0;
        }

        @Override
        public int blood() {
            return 0xFF8EE3FF;
        }
    }

    public static class Green extends CrystalSpireSprite {
        @Override
        protected int texOffset() {
            return 5;
        }

        @Override
        public int blood() {
            return 0xFF85FFC8;
        }
    }

    public static class Red extends CrystalSpireSprite {
        @Override
        protected int texOffset() {
            return 10;
        }

        @Override
        public int blood() {
            return 0xFFFFBB33;
        }
    }
}
