

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

public class Fadeleaf extends Plant {

    {
        image = 10;
        seedClass = Seed.class;
    }

    @Override
    public void activate(final Char ch) {

        if (ch instanceof Hero) {

            ((Hero) ch).curAction = null;

            if (((Hero) ch).subClass == HeroSubClass.WARDEN && Dungeon.interfloorTeleportAllowed()) {

                Level.beforeTransition();
                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1));
                InterlevelScene.returnBranch = 0;
                InterlevelScene.returnPos = -2;
                Game.switchScene(InterlevelScene.class);
            } else {
                ScrollOfTeleportation.teleportChar(ch, Fadeleaf.class);
            }
        } else if (ch instanceof Mob && !ch.properties().contains(Char.Property.IMMOVABLE)) {

            Buff.prolong(ch, Trap.HazardAssistTracker.class, Trap.HazardAssistTracker.DURATION);
            ScrollOfTeleportation.teleportChar(ch, Fadeleaf.class);
        }

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_FADELEAF;

            plantClass = Fadeleaf.class;
        }
    }
}
