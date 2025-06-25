

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTerror extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_TERROR;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        new Flare(5, 32).color(0xFF0000, true).show(curUser.sprite, 2f);
        Sample.INSTANCE.play(Assets.Sounds.READ);

        int count = 0;
        Mob affected = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                Buff.affect(mob, Terror.class, Terror.DURATION).object = curUser.id();

                if (mob.buff(Terror.class) != null) {
                    count++;
                    affected = mob;
                }
            }
        }

        switch (count) {
            case 0:
                GLog.i(Messages.get(this, "none"));
                break;
            case 1:
                GLog.i(Messages.get(this, "one", affected.name()));
                break;
            default:
                GLog.i(Messages.get(this, "many"));
        }
        identify();

        readAnimation();
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
