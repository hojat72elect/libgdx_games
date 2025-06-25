

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class GuardianTrap extends Trap {

    {
        color = RED;
        shape = STARS;
    }

    @Override
    public void activate() {

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon(pos);
        }

        if (Dungeon.level.heroFOV[pos]) {
            GLog.w(Messages.get(this, "alarm"));
            CellEmitter.center(pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        }

        Sample.INSTANCE.play(Assets.Sounds.ALERT);

        for (int i = 0; i < (scalingDepth() - 5) / 5; i++) {
            Guardian guardian = new Guardian();
            guardian.createWeapon(false);
            guardian.state = guardian.WANDERING;
            guardian.pos = Dungeon.level.randomRespawnCell(guardian);
            if (guardian.pos != -1) {
                GameScene.add(guardian);
                guardian.beckon(Dungeon.hero.pos);
            }
        }
    }

    public static class Guardian extends Statue {

        {
            spriteClass = GuardianSprite.class;

            EXP = 0;
            state = WANDERING;

            levelGenStatue = false;
        }

        @Override
        public void createWeapon(boolean useDecks) {
            weapon = (MeleeWeapon) Generator.randomUsingDefaults(Generator.Category.WEAPON);
            weapon.cursed = false;
            weapon.enchant(null);
            weapon.level(0);
        }

        @Override
        public void beckon(int cell) {
            //Beckon works on these ones, unlike their superclass.
            notice();

            if (state != HUNTING) {
                state = WANDERING;
            }
            target = cell;
        }
    }

    public static class GuardianSprite extends StatueSprite {

        public GuardianSprite() {
            super();
            tint(0, 0, 1, 0.2f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(0, 0, 1, 0.2f);
        }
    }
}
