

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PrismaticGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfPrismaticImage extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_PRISIMG;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        boolean found = false;
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (m instanceof PrismaticImage) {
                found = true;
                m.HP = m.HT;
                m.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(m.HT), FloatingText.HEALING);
            }
        }

        if (!found) {
            if (Stasis.getStasisAlly() instanceof PrismaticImage) {
                found = true;
                Stasis.getStasisAlly().HP = Stasis.getStasisAlly().HT;
            }
        }

        if (!found) {
            Buff.affect(curUser, PrismaticGuard.class).set(PrismaticGuard.maxHP(curUser));
        }

        identify();

        Sample.INSTANCE.play(Assets.Sounds.READ);

        readAnimation();
    }
}
