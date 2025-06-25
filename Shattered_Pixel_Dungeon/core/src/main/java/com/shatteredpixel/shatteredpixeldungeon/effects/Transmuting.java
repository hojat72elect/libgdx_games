

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class Transmuting extends Component {

    private enum Phase {
        FADE_IN, TRANSMUTING, FADE_OUT
    }

    private static final float FADE_IN_TIME = 0.2f;
    private static final float TRANSMUTING_TIME = 1.4f;
    private static final float FADE_OUT_TIME = 0.4f;

    private static final float ALPHA = 0.6f;

    Image oldSprite;
    Image newSprite;

    private Char target;

    private Phase phase;
    private float duration;
    private float passed;

    public Transmuting(Item oldItem, Item newItem) {
        oldSprite = new ItemSprite(oldItem);
        oldSprite.originToCenter();
        add(oldSprite);
        newSprite = new ItemSprite(newItem);
        newSprite.originToCenter();
        add(newSprite);

        oldSprite.alpha(0f);
        newSprite.alpha(0f);

        phase = Phase.FADE_IN;
        duration = FADE_IN_TIME;
        passed = 0;
    }

    public Transmuting(Talent oldTalent, Talent newTalent) {
        oldSprite = new TalentIcon(oldTalent);
        oldSprite.originToCenter();
        add(oldSprite);
        newSprite = new TalentIcon(newTalent);
        newSprite.originToCenter();
        add(newSprite);

        oldSprite.alpha(0f);
        newSprite.alpha(0f);

        phase = Phase.FADE_IN;
        duration = FADE_IN_TIME;
        passed = 0;
    }

    @Override
    public void update() {
        super.update();

        if (passed == 0) {
            oldSprite.x = target.sprite.center().x - oldSprite.width() / 2;
            oldSprite.y = target.sprite.y - oldSprite.height();

            newSprite.x = target.sprite.center().x - newSprite.width() / 2;
            newSprite.y = target.sprite.y - newSprite.height();
        }

        switch (phase) {
            case FADE_IN:
                oldSprite.alpha(passed / duration * ALPHA);
                oldSprite.scale.set(passed / duration);
                break;
            case TRANSMUTING:
                oldSprite.alpha((TRANSMUTING_TIME - passed) / duration * ALPHA);
                newSprite.alpha(passed / duration * ALPHA);
                break;
            case FADE_OUT:
                newSprite.alpha((1 - passed / duration) * ALPHA);
                newSprite.scale.set(1 + passed / duration);
                break;
        }

        if ((passed += Game.elapsed) > duration) {
            switch (phase) {
                case FADE_IN:
                    phase = Phase.TRANSMUTING;
                    duration = TRANSMUTING_TIME;
                    break;
                case TRANSMUTING:
                    phase = Phase.FADE_OUT;
                    duration = FADE_OUT_TIME;
                    break;
                case FADE_OUT:
                    kill();
                    break;
            }

            passed = 0;
        }
    }

    public static void show(Char ch, Item oldItem, Item newItem) {

        if (!ch.sprite.visible) {
            return;
        }

        Transmuting sprite = new Transmuting(oldItem, newItem);
        sprite.target = ch;
        ch.sprite.parent.add(sprite);
    }

    public static void show(Char ch, Talent oldTalent, Talent newTalent) {

        if (!ch.sprite.visible) {
            return;
        }

        Transmuting sprite = new Transmuting(oldTalent, newTalent);
        sprite.target = ch;
        ch.sprite.parent.add(sprite);
    }
}
