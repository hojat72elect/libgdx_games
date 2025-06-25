

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfInvisibility extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_INVIS;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        Buff.prolong(hero, Invisibility.class, Invisibility.DURATION);
        GLog.i(Messages.get(this, "invisible"));
        Sample.INSTANCE.play(Assets.Sounds.MELD);
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
