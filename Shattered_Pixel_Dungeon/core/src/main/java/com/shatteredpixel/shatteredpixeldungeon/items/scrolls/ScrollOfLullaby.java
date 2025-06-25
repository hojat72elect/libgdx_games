

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLullaby extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_LULLABY;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        curUser.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
        Sample.INSTANCE.play(Assets.Sounds.LULLABY);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                Buff.affect(mob, Drowsy.class, Drowsy.DURATION);
                mob.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
            }
        }

        Buff.affect(curUser, Drowsy.class, Drowsy.DURATION);

        GLog.i(Messages.get(this, "sooth"));

        identify();
        readAnimation();
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
