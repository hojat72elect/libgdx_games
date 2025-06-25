

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class ScrollOfRecharging extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_RECHARGE;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        Buff.affect(curUser, Recharging.class, Recharging.DURATION);
        charge(curUser);

        Sample.INSTANCE.play(Assets.Sounds.READ);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        GLog.i(Messages.get(this, "surge"));
        SpellSprite.show(curUser, SpellSprite.CHARGE);
        identify();

        readAnimation();
    }

    public static void charge(Char user) {
        if (user.sprite != null) {
            Emitter e = user.sprite.centerEmitter();
            if (e != null) e.burst(EnergyParticle.FACTORY, 15);
        }
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
