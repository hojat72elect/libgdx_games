

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndInfoArmorAbility extends WndTitledMessage {

    public WndInfoArmorAbility(HeroClass cls, ArmorAbility ability) {
        super(new HeroIcon(ability), Messages.titleCase(ability.name()), ability.desc());

        ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
        Talent.initArmorTalents(ability, talentList);

        Ratmogrify.useRatroicEnergy = ability instanceof Ratmogrify;
        TalentsPane.TalentTierPane talentPane = new TalentsPane.TalentTierPane(talentList.get(3), 4, TalentButton.Mode.INFO);
        talentPane.title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")));
        talentPane.setRect(0, height + 5, width, talentPane.height());
        add(talentPane);
        resize(width, (int) talentPane.bottom());
    }

    @Override
    protected float targetHeight() {
        return super.targetHeight() - 40;
    }
}
