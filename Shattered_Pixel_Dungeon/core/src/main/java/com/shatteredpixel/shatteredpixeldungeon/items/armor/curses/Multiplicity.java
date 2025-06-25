

package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Multiplicity extends Armor.Glyph {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        float procChance = 1 / 20f * procChanceMultiplier(defender);
        if (Random.Float() < procChance) {
            ArrayList<Integer> spawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = defender.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                    spawnPoints.add(p);
                }
            }

            if (spawnPoints.size() > 0) {

                Mob m = null;
                if (Random.Int(2) == 0 && defender instanceof Hero) {
                    m = new MirrorImage();
                    ((MirrorImage) m).duplicate((Hero) defender);
                } else {
                    Char toDuplicate = attacker;

                    if (toDuplicate instanceof Ratmogrify.TransmogRat) {
                        toDuplicate = ((Ratmogrify.TransmogRat) attacker).getOriginal();
                    }

                    //FIXME should probably have a mob property for this
                    if (!(toDuplicate instanceof Mob)
                            || toDuplicate.properties().contains(Char.Property.BOSS) || toDuplicate.properties().contains(Char.Property.MINIBOSS)
                            || toDuplicate instanceof Mimic || toDuplicate instanceof Statue || toDuplicate instanceof NPC) {
                        m = Dungeon.level.createMob();
                    } else {
                        Actor.fixTime();

                        m = (Mob) Reflection.newInstance(toDuplicate.getClass());

                        if (m != null) {

                            Bundle store = new Bundle();
                            attacker.storeInBundle(store);
                            m.restoreFromBundle(store);
                            m.pos = 0;
                            m.HP = m.HT;

                            //don't duplicate stuck projectiles
                            m.remove(PinCushion.class);
                            //don't duplicate pending damage to dwarf king
                            m.remove(DwarfKing.KingDamager.class);
                            //don't duplicate downed ghouls
                            m.remove(Ghoul.GhoulLifeLink.class);

                            //If a thief has stolen an item, that item is not duplicated.
                            if (m instanceof Thief) {
                                ((Thief) m).item = null;
                            }
                        }
                    }
                }

                if (m != null) {

                    if (Char.hasProp(m, Char.Property.LARGE)) {
                        for (int i : spawnPoints.toArray(new Integer[0])) {
                            if (!Dungeon.level.openSpace[i]) {
                                //remove the value, not at the index
                                spawnPoints.remove((Integer) i);
                            }
                        }
                    }

                    if (!spawnPoints.isEmpty()) {
                        m.pos = Random.element(spawnPoints);
                        GameScene.add(m);
                        ScrollOfTeleportation.appear(m, m.pos);
                    }
                }
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public boolean curse() {
        return true;
    }
}
