

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class TargetedSpell extends Spell {

    protected int collisionProperties = Ballistica.PROJECTILE;

    @Override
    protected void onCast(Hero hero) {
        GameScene.selectCell(targeter);
    }

    protected abstract void affectTarget(Ballistica bolt, Hero hero);

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.MAGIC_MISSILE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    private static final CellSelector.Listener targeter = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                //FIXME this safety check shouldn't be necessary
                //it would be better to eliminate the curItem static variable.
                final TargetedSpell curSpell;
                if (curItem instanceof TargetedSpell) {
                    curSpell = (TargetedSpell) curItem;
                } else {
                    return;
                }

                final Ballistica shot = new Ballistica(curUser.pos, target, curSpell.collisionProperties);
                int cell = shot.collisionPos;

                curUser.sprite.zap(cell);

                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                curUser.busy();

                curSpell.fx(shot, new Callback() {
                    public void call() {
                        curSpell.affectTarget(shot, curUser);
                        curSpell.detach(curUser.belongings.backpack);
                        Invisibility.dispel();
                        updateQuickslot();
                        curUser.spendAndNext(1f);
                        Catalog.countUse(curSpell.getClass());
                        if (Random.Float() < curSpell.talentChance) {
                            Talent.onScrollUsed(curUser, curUser.pos, curSpell.talentFactor, curSpell.getClass());
                        }
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(TargetedSpell.class, "prompt");
        }
    };
}
