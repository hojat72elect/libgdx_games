

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

public class CharHealthIndicator extends HealthBar {

    private static final int HEIGHT = 1;

    private Char target;

    public CharHealthIndicator(Char c) {
        target = c;
        GameScene.add(this);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        height = HEIGHT;
    }

    @Override
    public void update() {
        super.update();

        if (target != null && target.isAlive() && target.isActive() && target.sprite.visible) {
            CharSprite sprite = target.sprite;
            width = sprite.width() * (4 / 6f);
            x = sprite.x + sprite.width() / 6f;
            y = sprite.y - 2;
            level(target);
            visible = target.HP < target.HT || target.shielding() > 0;
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
