

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;
import java.util.Collections;

public class CursingTrap extends Trap {

    {
        color = VIOLET;
        shape = WAVES;
    }

    @Override
    public void activate() {
        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
            Sample.INSTANCE.play(Assets.Sounds.CURSED);
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            for (Item item : heap.items) {
                if (item.isUpgradable() && !(item instanceof MissileWeapon))
                    curse(item);
            }
        }

        if (Dungeon.hero.pos == pos && !Dungeon.hero.flying) {
            curse(Dungeon.hero);
        }
    }

    public static void curse(Hero hero) {
        //items the trap wants to curse because it will create a more negative effect
        ArrayList<Item> priorityCurse = new ArrayList<>();
        //items the trap can curse if nothing else is available.
        ArrayList<Item> canCurse = new ArrayList<>();

        KindOfWeapon weapon = hero.belongings.weapon();
        if (weapon instanceof Weapon && !(weapon instanceof MagesStaff)) {
            if (((Weapon) weapon).enchantment == null)
                priorityCurse.add(weapon);
            else
                canCurse.add(weapon);
        }

        Armor armor = hero.belongings.armor();
        if (armor != null) {
            if (armor.glyph == null)
                priorityCurse.add(armor);
            else
                canCurse.add(armor);
        }

        Collections.shuffle(priorityCurse);
        Collections.shuffle(canCurse);

        if (!priorityCurse.isEmpty()) {
            curse(priorityCurse.remove(0));
        } else if (!canCurse.isEmpty()) {
            curse(canCurse.remove(0));
        }

        EquipableItem.equipCursed(hero);
        GLog.n(Messages.get(CursingTrap.class, "curse"));
    }

    private static void curse(Item item) {
        item.cursed = item.cursedKnown = true;

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            if (w.enchantment == null) {
                w.enchant(Weapon.Enchantment.randomCurse());
            }
        }
        if (item instanceof Armor) {
            Armor a = (Armor) item;
            if (a.glyph == null) {
                a.inscribe(Armor.Glyph.randomCurse());
            }
        }
    }
}
