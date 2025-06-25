

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;

public class DangerIndicator extends Tag {

    public static final int COLOR = 0xC03838;

    private BitmapText number;
    private Image icon;

    private int enemyIndex = 0;

    private int lastNumber = -1;

    public static int HEIGHT = 16;

    public DangerIndicator() {
        super(COLOR);

        setSize(SIZE, HEIGHT);

        visible = false;
    }

    @Override
    public GameAction keyAction() {
        return SPDAction.CYCLE;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        number = new BitmapText(PixelScene.pixelFont);
        add(number);

        icon = Icons.SKULL.get();
        add(icon);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = right() - 10;
        icon.y = y + (height - icon.height) / 2;

        placeNumber();
    }

    private void placeNumber() {
        number.x = right() - 11 - number.width();
        number.y = y + (height - number.baseLine()) / 2f;
        PixelScene.align(number);
    }

    @Override
    public void update() {

        if (Dungeon.hero.isAlive()) {
            int v = Dungeon.hero.visibleEnemies();
            if (v != lastNumber) {
                lastNumber = v;
                if (visible = lastNumber > 0) {
                    number.text(Integer.toString(lastNumber));
                    number.measure();
                    placeNumber();

                    flash();
                }
            }
        } else {
            visible = false;
        }

        super.update();
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (Dungeon.hero.visibleEnemies() > 0) {

            Mob target = Dungeon.hero.visibleEnemy(++enemyIndex);

            QuickSlotButton.target(target);
            if (Dungeon.hero.canAttack(target)) AttackIndicator.target(target);

            if (Dungeon.hero.curAction == null && target.sprite != null) {
                Camera.main.panFollow(target.sprite, 5f);
            }
        }
    }

    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "tag_danger"));
    }
}
