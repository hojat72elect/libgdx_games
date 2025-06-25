

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class EbonyMimic extends Mimic {

    {
        spriteClass = MimicSprite.Ebony.class;
    }

    @Override
    public String name() {
        if (alignment == Alignment.NEUTRAL) {
            return Messages.get(this, "hidden_name");
        } else {
            return super.name();
        }
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL) {
            return Messages.get(this, "hidden_desc");
        } else {
            return super.description();
        }
    }

    @Override
    public boolean stealthy() {
        return true;
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
        if (Actor.chars().contains(this) && Dungeon.level.map[pos] == Terrain.DOOR) {
            Door.enter(pos);
        }
    }

    @Override
    public int damageRoll() {
        if (alignment == Alignment.NEUTRAL) {
            return Math.round(super.damageRoll() * 2f); //BIG damage on surprise
        } else {
            return super.damageRoll();
        }
    }

    @Override
    protected void generatePrize(boolean useDecks) {
        super.generatePrize(useDecks);
        //add one extra random loot item, on top of the one granted by mimic tooth
        items.add(Generator.randomUsingDefaults());

        //all existing prize items are guaranteed uncursed, and are always at least +1
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
                if (!(i instanceof MissileWeapon || i instanceof Artifact) && i.level() == 0) {
                    i.upgrade();
                }
            }
        }
    }
}
