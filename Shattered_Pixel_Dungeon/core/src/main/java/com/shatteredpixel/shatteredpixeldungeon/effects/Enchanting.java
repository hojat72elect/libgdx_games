

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.Game;

public class Enchanting extends ItemSprite {

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

    private static final float FADE_IN_TIME = 0.2f;
    private static final float STATIC_TIME = 1.0f;
    private static final float FADE_OUT_TIME = 0.4f;

    private static final float ALPHA = 0.6f;

    private final int color;

    private Char target;

    private Phase phase;
    private float duration;
    private float passed;

    public Enchanting(Item item) {
        super(item.image(), null);
        originToCenter();

        if (item.glowing() != null) {
            color = item.glowing().color;
        } else {
            color = -1;
        }

        phase = Phase.FADE_IN;
        duration = FADE_IN_TIME;
        passed = 0;
    }

    @Override
    public void update() {
        super.update();

        if (passed == 0) {
            x = target.sprite.center().x - width() / 2;
            y = target.sprite.y - height();
        }

        switch (phase) {
            case FADE_IN:
                alpha(passed / duration * ALPHA);
                scale.set(passed / duration);
                break;
            case STATIC:
                if (color != -1) {
                    tint(color, passed / duration * 0.8f);
                }
                break;
            case FADE_OUT:
                alpha((1 - passed / duration) * ALPHA);
                scale.set(1 + passed / duration);
                break;
        }

        if ((passed += Game.elapsed) > duration) {
            switch (phase) {
                case FADE_IN:
                    phase = Phase.STATIC;
                    duration = STATIC_TIME;
                    break;
                case STATIC:
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

    public static void show(Char ch, Item item) {

        if (!ch.sprite.visible) {
            return;
        }

        Enchanting sprite = new Enchanting(item);
        sprite.target = ch;
        ch.sprite.parent.add(sprite);
    }
}