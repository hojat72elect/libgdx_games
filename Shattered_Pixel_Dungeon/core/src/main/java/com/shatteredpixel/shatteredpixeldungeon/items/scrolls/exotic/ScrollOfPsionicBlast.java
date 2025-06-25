

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ScrollOfPsionicBlast extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_PSIBLAST;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        GameScene.flash(0x80FFFFFF);

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        GLog.i(Messages.get(ScrollOfRetribution.class, "blast"));

        ArrayList<Mob> targets = new ArrayList<>();

        //calculate targets first, in case damaging/blinding a target affects hero vision
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                targets.add(mob);
            }
        }

        for (Mob mob : targets) {
            //always kills non-resistant enemies
            //resistant enemies take 50% current HP at full health, scaling to 75% at 1/2 HP, and 100% at 1/3 hp
            mob.damage(Math.round(mob.HT / 2f + mob.HP / 2f), this);
            if (mob.isAlive()) {
                Buff.prolong(mob, Blindness.class, Blindness.DURATION);
            }
        }

        curUser.damage(Math.max(0, Math.round(curUser.HT * (0.5f * (float) Math.pow(0.9, targets.size())))), this);
        if (curUser.isAlive()) {
            Buff.prolong(curUser, Blindness.class, Blindness.DURATION);
            Buff.prolong(curUser, Weakness.class, Weakness.DURATION * 5f);
            Dungeon.observe();
            readAnimation();
        } else {
            Badges.validateDeathFromFriendlyMagic();
            Dungeon.fail(this);
            GLog.n(Messages.get(this, "ondeath"));
        }

        identify();
    }
}
