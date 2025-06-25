

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PoisonDartTrap extends Trap {

    {
        color = GREEN;
        shape = CROSSHAIR;

        canBeHidden = false;
        avoidsHallways = true;
    }

    protected int poisonAmount() {
        return 8 + Math.round(2 * scalingDepth() / 3f);
    }

    protected boolean canTarget(Char ch) {
        return true;
    }

    @Override
    public void activate() {

        //we handle this inside of a separate actor as the trap may produce a visual effect we need to pause for
        Actor.add(new Actor() {

            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                Actor.remove(this);
                Char target = Actor.findChar(pos);

                if (target != null && !canTarget(target)) {
                    target = null;
                }

                //find the closest char that can be aimed at
                //can't target beyond view distance, with a min of 6 (torch range)
                //add 0.5 for better consistency with vision radius shape
                float range = Math.max(6, Dungeon.level.viewDistance) + 0.5f;
                if (target == null) {
                    float closestDist = Float.MAX_VALUE;
                    for (Char ch : Actor.chars()) {
                        if (!ch.isAlive()) continue;
                        float curDist = Dungeon.level.trueDistance(pos, ch.pos);
                        //invis targets are considered to be at max range
                        if (ch.invisible > 0) curDist = Math.max(curDist, range);
                        Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
                        if (canTarget(ch) && bolt.collisionPos == ch.pos
                                && (curDist < closestDist || (curDist == closestDist && target instanceof Hero))) {
                            target = ch;
                            closestDist = curDist;
                        }
                    }
                    if (closestDist > range) {
                        target = null;
                    }
                }

                if (target != null) {
                    if (target instanceof Mob) {
                        Buff.prolong(target, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                    }
                    final Char finalTarget = target;
                    if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
                        ((MissileSprite) ShatteredPixelDungeon.scene().recycle(MissileSprite.class)).
                                reset(pos, finalTarget.sprite, new PoisonDart(), new Callback() {
                                    @Override
                                    public void call() {
                                        int dmg = Random.NormalIntRange(4, 8) - finalTarget.drRoll();
                                        finalTarget.damage(dmg, PoisonDartTrap.this);
                                        if (finalTarget == Dungeon.hero) {
                                            //for the poison dart traps in the Tengu fight
                                            if (Dungeon.depth == 10) {
                                                Statistics.qualifiedForBossChallengeBadge = false;
                                                Statistics.bossScores[1] -= 100;
                                            }
                                            if (!finalTarget.isAlive()) {
                                                Dungeon.fail(PoisonDartTrap.this);
                                                GLog.n(Messages.get(PoisonDartTrap.class, "ondeath"));
                                                if (reclaimed) Badges.validateDeathFromFriendlyMagic();
                                            }
                                        }
                                        Buff.affect(finalTarget, Poison.class).set(poisonAmount());
                                        Sample.INSTANCE.play(Assets.Sounds.HIT, 1, 1, Random.Float(0.8f, 1.25f));
                                        finalTarget.sprite.bloodBurstA(finalTarget.sprite.center(), dmg);
                                        finalTarget.sprite.flash();
                                        next();
                                    }
                                });
                        return false;
                    } else {
                        finalTarget.damage(Random.NormalIntRange(4, 8) - finalTarget.drRoll(), PoisonDartTrap.this);
                        Buff.affect(finalTarget, Poison.class).set(poisonAmount());
                        return true;
                    }
                } else {
                    return true;
                }
            }
        });
    }
}
