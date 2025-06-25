

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MimicTooth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class GoldenMimic extends Mimic {

    {
        spriteClass = MimicSprite.Golden.class;
    }

    @Override
    public String name() {
        if (alignment == Alignment.NEUTRAL) {
            return Messages.get(Heap.class, "locked_chest");
        } else {
            return super.name();
        }
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL) {
            if (MimicTooth.stealthyMimics()) {
                return Messages.get(Heap.class, "locked_chest_desc");
            } else {
                return Messages.get(Heap.class, "locked_chest_desc") + "\n\n" + Messages.get(this, "hidden_hint");
            }
        } else {
            return super.description();
        }
    }

    public void stopHiding() {
        state = HUNTING;
        if (sprite != null) sprite.idle();
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            GLog.w(Messages.get(this, "reveal"));
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
        }
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(Math.round(level * 1.33f));
    }

    @Override
    protected void generatePrize(boolean useDecks) {
        super.generatePrize(useDecks);
        //all existing prize items are guaranteed uncursed, and have a 50% chance to be +1 if they were +0
        for (Item i : items) {
            if (i instanceof EquipableItem || i instanceof Wand) {
                i.cursed = false;
                i.cursedKnown = true;
                if (i instanceof Weapon && ((Weapon) i).hasCurseEnchant()) {
                    ((Weapon) i).enchant(null);
                }
                if (i instanceof Armor && ((Armor) i).hasCurseGlyph()) {
                    ((Armor) i).inscribe(null);
                }
                if (!(i instanceof MissileWeapon || i instanceof Artifact) && i.level() == 0 && Random.Int(2) == 0) {
                    i.upgrade();
                }
            }
        }
    }
}
