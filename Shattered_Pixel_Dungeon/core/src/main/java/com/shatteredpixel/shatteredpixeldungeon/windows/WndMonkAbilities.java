

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndMonkAbilities extends Window {

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 180;

    private static final int MARGIN = 2;

    public WndMonkAbilities(MonkEnergy energyBuff) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos((width - title.width()) / 2, pos);
        title.maxWidth(width - MARGIN * 2);
        add(title);

        pos = title.bottom() + 3 * MARGIN;

        for (MonkEnergy.MonkAbility abil : MonkEnergy.MonkAbility.abilities) {
            String text = "_" + Messages.titleCase(abil.name()) + " " + Messages.get(this, "energycost", abil.energyCost()) + ":_ " + abil.desc();
            RedButton moveBtn = new RedButton(text, 6) {
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    if (abil.targetingPrompt() != null) {
                        abilityBeingUsed = abil;
                        GameScene.selectCell(listener);
                    } else {
                        abil.doAbility(Dungeon.hero, null);
                    }
                }
            };
            moveBtn.leftJustify = true;
            moveBtn.multiline = true;
            moveBtn.setSize(width, moveBtn.reqHeight());
            moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
            moveBtn.enable(abil.usable(energyBuff));
            add(moveBtn);
            pos = moveBtn.bottom() + MARGIN;
        }

        resize(width, (int) pos);
    }

    MonkEnergy.MonkAbility abilityBeingUsed;

    private final CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            abilityBeingUsed.doAbility(Dungeon.hero, cell);
        }

        @Override
        public String prompt() {
            return abilityBeingUsed.targetingPrompt();
        }
    };
}
