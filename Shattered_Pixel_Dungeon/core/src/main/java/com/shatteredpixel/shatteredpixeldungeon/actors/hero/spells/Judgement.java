

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Judgement extends ClericSpell {

    public static Judgement INSTANCE = new Judgement();

    @Override
    public int icon() {
        return HeroIcon.JUDGEMENT;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 3;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero)
                && hero.hasTalent(Talent.JUDGEMENT)
                && hero.buff(AscendedForm.AscendBuff.class) != null;
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {

        hero.sprite.attack(hero.pos, new Callback() {
            @Override
            public void call() {
                GameScene.flash(0x80FFFFFF);
                Sample.INSTANCE.play(Assets.Sounds.BLAST);

                int damageBase = 5 + 5 * hero.pointsInTalent(Talent.JUDGEMENT);
                damageBase += Math.round(damageBase * hero.buff(AscendedForm.AscendBuff.class).spellCasts / 3f);

                for (Char ch : Actor.chars()) {
                    if (ch.alignment != hero.alignment && Dungeon.level.heroFOV[ch.pos]) {
                        ch.damage(Random.NormalIntRange(damageBase, 2 * damageBase), Judgement.this);
                    }
                }

                hero.spendAndNext(1f);
                onSpellCast(tome, hero);

                hero.buff(AscendedForm.AscendBuff.class).spellCasts = 0;
            }
        });
        hero.busy();
    }

    @Override
    public String desc() {
        int baseDmg = 5 + 5 * Dungeon.hero.pointsInTalent(Talent.JUDGEMENT);
        int totalBaseDmg = baseDmg;
        if (Dungeon.hero.buff(AscendedForm.AscendBuff.class) != null) {
            totalBaseDmg += Math.round(baseDmg * Dungeon.hero.buff(AscendedForm.AscendBuff.class).spellCasts / 3f);
        }

        return Messages.get(this, "desc", baseDmg, 2 * baseDmg, totalBaseDmg, 2 * totalBaseDmg) + "\n\n" + Messages.get(this, "charge_cost", (int) chargeUse(Dungeon.hero));
    }
}
