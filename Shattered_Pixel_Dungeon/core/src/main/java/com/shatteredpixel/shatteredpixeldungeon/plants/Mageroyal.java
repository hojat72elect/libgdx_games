

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Mageroyal extends Plant {

    {
        image = 7;
        seedClass = Seed.class;
    }

    @Override
    public void activate(Char ch) {

        if (ch != null) {
            PotionOfHealing.cure(ch);

            if (ch instanceof Hero) {
                GLog.i(Messages.get(this, "refreshed"));

                if (((Hero) ch).subClass == HeroSubClass.WARDEN) {
                    Buff.affect(ch, BlobImmunity.class, BlobImmunity.DURATION / 2f);
                }
            }
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_MAGEROYAL;

            plantClass = Mageroyal.class;
        }
    }
}