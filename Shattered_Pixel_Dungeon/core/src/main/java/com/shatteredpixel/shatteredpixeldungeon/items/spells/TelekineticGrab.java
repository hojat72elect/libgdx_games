

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class TelekineticGrab extends TargetedSpell {

    {
        image = ItemSpriteSheet.TELE_GRAB;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.BEACON,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    protected void affectTarget(Ballistica bolt, Hero hero) {
        Char ch = Actor.findChar(bolt.collisionPos);

        //special logic for DK when he is on his throne
        if (ch == null && bolt.path.size() > bolt.dist + 1) {
            ch = Actor.findChar(bolt.path.get(bolt.dist + 1));
            if (!(ch instanceof DwarfKing && Dungeon.level.solid[ch.pos])) {
                ch = null;
            }
        }

        if (ch != null && ch.buff(PinCushion.class) != null) {

            while (ch.buff(PinCushion.class) != null) {
                Item item = ch.buff(PinCushion.class).grabOne();

                if (item.doPickUp(hero, ch.pos)) {
                    hero.spend(-Item.TIME_TO_PICK_UP); //casting the spell already takes a turn
                    GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", item.name())));
                } else {
                    GLog.w(Messages.get(this, "cant_grab"));
                    Dungeon.level.drop(item, ch.pos).sprite.drop();
                    return;
                }
            }
        } else if (Dungeon.level.heaps.get(bolt.collisionPos) != null) {

            Heap h = Dungeon.level.heaps.get(bolt.collisionPos);

            if (h.type != Heap.Type.HEAP) {
                GLog.w(Messages.get(this, "cant_grab"));
                h.sprite.drop();
                return;
            }

            while (!h.isEmpty()) {
                Item item = h.peek();
                if (item.doPickUp(hero, h.pos)) {
                    h.pickUp();
                    hero.spend(-Item.TIME_TO_PICK_UP); //casting the spell already takes a turn
                    GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", item.name())));
                } else {
                    GLog.w(Messages.get(this, "cant_grab"));
                    h.sprite.drop();
                    return;
                }
            }
        } else {
            GLog.w(Messages.get(this, "no_target"));
        }
    }

    @Override
    public int value() {
        return (int) (50 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int) (10 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 8;

        {
            inputs = new Class[]{LiquidMetal.class};
            inQuantity = new int[]{10};

            cost = 10;

            output = TelekineticGrab.class;
            outQuantity = OUT_QUANTITY;
        }
    }
}
