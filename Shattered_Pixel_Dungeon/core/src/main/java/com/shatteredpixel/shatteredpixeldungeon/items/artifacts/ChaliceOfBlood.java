

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ChaliceOfBlood extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_CHALICE1;

        levelCap = 10;
    }

    public static final String AC_PRICK = "PRICK";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)
                && level() < levelCap
                && !cursed
                && !hero.isInvulnerable(getClass())
                && hero.buff(MagicImmune.class) == null)
            actions.add(AC_PRICK);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_PRICK)) {

            int damage = 5 + 3 * (level() * level());

            if (damage > hero.HP * 0.75) {

                GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prick_warn"),
                                Messages.get(this, "yes"),
                                Messages.get(this, "no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0)
                                    prick(Dungeon.hero);
                            }
                        }
                );
            } else {
                prick(hero);
            }
        }
    }

    private void prick(Hero hero) {
        int damage = 5 + 3 * (level() * level());

        Earthroot.Armor armor = hero.buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        WandOfLivingEarth.RockArmor rockArmor = hero.buff(WandOfLivingEarth.RockArmor.class);
        if (rockArmor != null) {
            damage = rockArmor.absorb(damage);
        }

        damage -= hero.drRoll();

        hero.sprite.operate(hero.pos);
        hero.busy();
        hero.spend(3f);
        GLog.w(Messages.get(this, "onprick"));
        if (damage <= 0) {
            damage = 1;
        } else {
            Sample.INSTANCE.play(Assets.Sounds.CURSED);
            hero.sprite.emitter().burst(ShadowParticle.CURSE, 4 + (damage / 10));
        }

        hero.damage(damage, this);

        if (!hero.isAlive()) {
            Badges.validateDeathFromFriendlyMagic();
            Dungeon.fail(this);
            GLog.n(Messages.get(this, "ondeath"));
        } else {
            upgrade();
            Catalog.countUse(getClass());
        }
    }

    @Override
    public Item upgrade() {
        if (level() >= 6)
            image = ItemSpriteSheet.ARTIFACT_CHALICE3;
        else if (level() >= 2)
            image = ItemSpriteSheet.ARTIFACT_CHALICE2;
        return super.upgrade();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (level() >= 7) image = ItemSpriteSheet.ARTIFACT_CHALICE3;
        else if (level() >= 3) image = ItemSpriteSheet.ARTIFACT_CHALICE2;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new chaliceRegen();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;

        //grants 5 turns of healing up-front, if hero isn't starving
        if (target.isStarving()) return;

        float healDelay = 10f - (1.33f + level() * 0.667f);
        healDelay /= amount;
        float heal = 5f / healDelay;
        //effectively 0.5/1/1.5/2/2.5 HP per turn at +0/+6/+8/+9/+10
        if (Random.Float() < heal % 1) {
            heal++;
        }
        if (heal >= 1f && target.HP < target.HT) {
            target.HP = Math.min(target.HT, target.HP + (int) heal);
            target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString((int) heal), FloatingText.HEALING);

            if (target.HP == target.HT && target instanceof Hero) {
                target.resting = false;
            }
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped(Dungeon.hero)) {
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else if (level() == 0)
                desc += Messages.get(this, "desc_1");
            else if (level() < levelCap)
                desc += Messages.get(this, "desc_2");
            else
                desc += Messages.get(this, "desc_3");
        }

        return desc;
    }

    public class chaliceRegen extends ArtifactBuff {
        //see Regeneration.class for effect
    }
}
