

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class DisplacingDart extends TippedDart {

    {
        image = ItemSpriteSheet.DISPLACING_DART;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //only display enemies when processing charge shot
        if (processingChargedShot && attacker.alignment == defender.alignment) {
            return super.proc(attacker, defender, damage);
        }

        //attempts to teleport the enemy to a position 8-10 cells away from the hero
        //prioritizes the closest visible cell to the defender, or closest non-visible if no visible are present
        //grants vision on the defender if teleport goes to non-visible
        if (!defender.properties().contains(Char.Property.IMMOVABLE)) {

            ArrayList<Integer> visiblePositions = new ArrayList<>();
            ArrayList<Integer> nonVisiblePositions = new ArrayList<>();

            PathFinder.buildDistanceMap(attacker.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

            for (int pos = 0; pos < Dungeon.level.length(); pos++) {
                if (Dungeon.level.passable[pos]
                        && PathFinder.distance[pos] >= 8
                        && PathFinder.distance[pos] <= 10
                        && (!Char.hasProp(defender, Char.Property.LARGE) || Dungeon.level.openSpace[pos])
                        && Actor.findChar(pos) == null) {

                    if (Dungeon.level.heroFOV[pos]) {
                        visiblePositions.add(pos);
                    } else {
                        nonVisiblePositions.add(pos);
                    }
                }
            }

            int chosenPos = -1;

            if (!visiblePositions.isEmpty()) {
                for (int pos : visiblePositions) {
                    if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
                            > Dungeon.level.trueDistance(defender.pos, pos)) {
                        chosenPos = pos;
                    }
                }
            } else {
                for (int pos : nonVisiblePositions) {
                    if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
                            > Dungeon.level.trueDistance(defender.pos, pos)) {
                        chosenPos = pos;
                    }
                }
            }

            if (chosenPos != -1) {
                ScrollOfTeleportation.appear(defender, chosenPos);
                Dungeon.level.occupyCell(defender);
                if (defender == Dungeon.hero) {
                    Dungeon.observe();
                    GameScene.updateFog();
                } else if (!Dungeon.level.heroFOV[chosenPos]) {
                    Buff.append(attacker, TalismanOfForesight.CharAwareness.class, 5f).charID = defender.id();
                }
            }
        }

        return super.proc(attacker, defender, damage);
    }
}
