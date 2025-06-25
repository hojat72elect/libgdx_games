

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Earthroot extends Plant {

    {
        image = 8;
        seedClass = Seed.class;
    }

    @Override
    public void activate(Char ch) {

        if (ch != null) {
            if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
                Barkskin.conditionallyAppend(Dungeon.hero, Dungeon.hero.lvl + 5, 5);
            } else {
                Buff.affect(ch, Armor.class).level(ch.HT);
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8);
            PixelScene.shake(1, 0.4f);
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_EARTHROOT;

            plantClass = Earthroot.class;

            bones = true;
        }
    }

    public static class Armor extends Buff {

        private static final float STEP = 1f;

        private int pos;
        private int level;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public boolean act() {
            if (target.pos != pos) {
                detach();
            }
            spend(STEP);
            return true;
        }

        private static int blocking() {
            return (Dungeon.scalingDepth() + 5) / 2;
        }

        public int absorb(int damage) {
            if (pos != target.pos) {
                detach();
                return damage;
            }
            int block = Math.min(damage, blocking());
            if (level <= block) {
                detach();
                return damage - block;
            } else {
                level -= block;
                return damage - block;
            }
        }

        public void level(int value) {
            if (target != null) {
                if (level < value) {
                    level = value;
                }
                pos = target.pos;
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (target.HT - level) / (float) target.HT);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", blocking(), level);
        }

        private static final String POS = "pos";
        private static final String LEVEL = "level";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
            level = bundle.getInt(LEVEL);
        }
    }
}
