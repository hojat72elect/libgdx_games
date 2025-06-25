

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;

public class ResumeIndicator extends Tag {

    private Image icon;

    public ResumeIndicator() {
        super(0xA3A695);

        setSize(SIZE, SIZE);

        visible = false;
    }

    @Override
    public GameAction keyAction() {
        return SPDAction.TAG_RESUME;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        icon = Icons.get(Icons.ARROW);
        add(icon);
    }

    @Override
    protected void layout() {
        super.layout();

        if (!flipped) icon.x = x + (SIZE - icon.width()) / 2f + 1;
        else icon.x = x + width - (SIZE + icon.width()) / 2f - 1;
        icon.y = y + (height - icon.height) / 2f;
        PixelScene.align(icon);
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (Dungeon.hero.ready) {
            Dungeon.hero.resume();
        }
    }

    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "tag_resume"));
    }

    @Override
    public void update() {
        if (!Dungeon.hero.isAlive())
            visible = false;
        else if (visible == (Dungeon.hero.lastAction == null)) {
            visible = Dungeon.hero.lastAction != null;
            if (visible)
                flash();
        }
        super.update();
    }
}
