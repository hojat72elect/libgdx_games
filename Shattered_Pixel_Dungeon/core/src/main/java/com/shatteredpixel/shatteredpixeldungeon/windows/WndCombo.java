

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndCombo extends Window {

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 180;

    private static final int MARGIN = 2;

    public WndCombo(Combo combo) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos((width - title.width()) / 2, pos);
        title.maxWidth(width - MARGIN * 2);
        add(title);

        pos = title.bottom() + 3 * MARGIN;

        Image icon;
        if (Dungeon.hero.belongings.weapon() != null) {
            icon = new ItemSprite(Dungeon.hero.belongings.weapon().image, null);
        } else {
            icon = new ItemSprite(new Item() {
                {
                    image = ItemSpriteSheet.WEAPON_HOLDER;
                }
            });
        }

        for (Combo.ComboMove move : Combo.ComboMove.values()) {

            String text = "_" + Messages.titleCase(move.title()) + " " + Messages.get(this, "combo_req", move.comboReq) + ":_ " + move.desc(combo.getComboCount());
            RedButton moveBtn = new RedButton(text, 6) {
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    combo.useMove(move);
                }
            };
            moveBtn.leftJustify = true;
            moveBtn.multiline = true;
            moveBtn.setSize(width, moveBtn.reqHeight());
            moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
            moveBtn.enable(combo.canUseMove(move));
            add(moveBtn);
            pos = moveBtn.bottom() + MARGIN;
        }

        resize(width, (int) pos);
    }
}
