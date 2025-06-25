

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class StoneOfDeepSleep extends Runestone {

    {
        image = ItemSpriteSheet.STONE_SLEEP;
    }

    @Override
    protected void activate(int cell) {

        if (Actor.findChar(cell) != null) {

            Char c = Actor.findChar(cell);

            if (c instanceof Mob) {

                Buff.affect(c, MagicalSleep.class);
                c.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.LULLABY);
    }
}
