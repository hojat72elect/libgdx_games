

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ScrollOfRetribution extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_RETRIB;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        GameScene.flash(0x80FFFFFF);

        //scales from 0x to 1x power, maxing at ~10% HP
        float hpPercent = (curUser.HT - curUser.HP) / (float) (curUser.HT);
        float power = Math.min(4f, 4.45f * hpPercent);

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        GLog.i(Messages.get(this, "blast"));

        ArrayList<Mob> targets = new ArrayList<>();

        //calculate targets first, in case damaging/blinding a target affects hero vision
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                targets.add(mob);
            }
        }

        for (Mob mob : targets) {
            //deals 10%HT, plus 0-90%HP based on scaling
            mob.damage(Math.round(mob.HT / 10f + (mob.HP * power * 0.225f)), this);
            if (mob.isAlive()) {
                Buff.prolong(mob, Blindness.class, Blindness.DURATION);
            }
        }

        Buff.prolong(curUser, Weakness.class, Weakness.DURATION);
        Buff.prolong(curUser, Blindness.class, Blindness.DURATION);
        Dungeon.observe();

        identify();

        readAnimation();
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
