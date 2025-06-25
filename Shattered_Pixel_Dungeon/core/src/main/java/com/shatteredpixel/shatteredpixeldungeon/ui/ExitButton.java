

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;
import com.watabou.noosa.Game;

public class ExitButton extends IconButton {

    public ExitButton() {
        super(Icons.EXIT.get());

        width = 20;
        height = 20;
    }

    @Override
    protected void onClick() {
        if (Game.scene() instanceof TitleScene) {
            Game.instance.finish();
        } else {
            ShatteredPixelDungeon.switchNoFade(TitleScene.class);
        }
    }

    @Override
    public GameAction keyAction() {
        return GameAction.BACK;
    }

    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "back"));
    }
}
