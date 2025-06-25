

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfDread extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_DREAD;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        new Flare(5, 32).color(0xFF0000, true).show(curUser.sprite, 2f);
        Sample.INSTANCE.play(Assets.Sounds.READ);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                if (!mob.isImmune(Dread.class)) {
                    Buff.affect(mob, Dread.class).object = curUser.id();
                } else {
                    Buff.affect(mob, Terror.class, Terror.DURATION).object = curUser.id();
                }
            }
        }

        identify();

        readAnimation();
    }
}
