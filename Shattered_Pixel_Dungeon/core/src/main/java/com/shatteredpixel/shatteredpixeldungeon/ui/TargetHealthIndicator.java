

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

public class TargetHealthIndicator extends HealthBar {

    public static TargetHealthIndicator instance;

    private Char target;

    public TargetHealthIndicator() {
        super();

        instance = this;
    }

    @Override
    public void update() {
        super.update();

        if (target != null && target.isAlive() && target.isActive()
                && target.sprite != null && target.sprite.visible) {
            CharSprite sprite = target.sprite;
            width = sprite.width();
            x = sprite.x;
            y = sprite.y - 3;
            level(target);
            visible = true;
        } else {
            visible = false;
        }
    }

    public void target(Char ch) {
        if (ch != null && ch.isAlive() && ch.isActive()) {
            target = ch;
        } else {
            target = null;
        }
    }

    public Char target() {
        return target;
    }
}
