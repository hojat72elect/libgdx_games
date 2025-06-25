

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.watabou.utils.Random;

public class Snake extends Mob {

    {
        spriteClass = SnakeSprite.class;

        HP = HT = 4;
        defenseSkill = 25;

        EXP = 2;
        maxLvl = 7;

        loot = Generator.Category.SEED;
        lootChance = 0.25f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 4);
    }

    @Override
    public int attackSkill(Char target) {
        return 10;
    }

    private static int dodges = 0;

    @Override
    public String defenseVerb() {
        if (Dungeon.level.heroFOV[pos]) {
            dodges++;
        }
        if ((dodges >= 2 && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_SURPRISE_ATKS))
                || (dodges >= 4 && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1))) {
            GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_SURPRISE_ATKS);
            dodges = 0;
        }
        return super.defenseVerb();
    }
}
