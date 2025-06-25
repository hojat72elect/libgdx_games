

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class ScrollOfDivination extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_DIVINATE;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        curUser.sprite.parent.add(new Identification(curUser.sprite.center().offset(0, -16)));

        Sample.INSTANCE.play(Assets.Sounds.READ);

        HashSet<Class<? extends Potion>> potions = Potion.getUnknown();
        HashSet<Class<? extends Scroll>> scrolls = Scroll.getUnknown();
        HashSet<Class<? extends Ring>> rings = Ring.getUnknown();

        int total = potions.size() + scrolls.size() + rings.size();

        ArrayList<Item> IDed = new ArrayList<>();
        int left = 4;

        float[] baseProbs = new float[]{3, 3, 3};
        float[] probs = baseProbs.clone();

        while (left > 0 && total > 0) {
            switch (Random.chances(probs)) {
                default:
                    probs = baseProbs.clone();
                    continue;
                case 0:
                    if (potions.isEmpty()) {
                        probs[0] = 0;
                        continue;
                    }
                    probs[0]--;
                    Potion p = Reflection.newInstance(Random.element(potions));
                    p.identify();
                    IDed.add(p);
                    potions.remove(p.getClass());
                    break;
                case 1:
                    if (scrolls.isEmpty()) {
                        probs[1] = 0;
                        continue;
                    }
                    probs[1]--;
                    Scroll s = Reflection.newInstance(Random.element(scrolls));
                    s.identify();
                    IDed.add(s);
                    scrolls.remove(s.getClass());
                    break;
                case 2:
                    if (rings.isEmpty()) {
                        probs[2] = 0;
                        continue;
                    }
                    probs[2]--;
                    Ring r = Reflection.newInstance(Random.element(rings));
                    r.setKnown();
                    IDed.add(r);
                    rings.remove(r.getClass());
                    break;
            }
            left--;
            total--;
        }

        if (left == 4) {
            GLog.n(Messages.get(this, "nothing_left"));
        } else {
            GameScene.show(new WndDivination(IDed));
        }

        readAnimation();
        identify();
    }

    private class WndDivination extends Window {

        private static final int WIDTH = 120;

        WndDivination(ArrayList<Item> IDed) {
            IconTitle cur = new IconTitle(new ItemSprite(ScrollOfDivination.this),
                    Messages.titleCase(Messages.get(ScrollOfDivination.class, "name")));
            cur.setRect(0, 0, WIDTH, 0);
            add(cur);

            RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
            msg.maxWidth(120);
            msg.setPos(0, cur.bottom() + 2);
            add(msg);

            float pos = msg.bottom() + 10;

            for (Item i : IDed) {

                cur = new IconTitle(i);
                cur.setRect(0, pos, WIDTH, 0);
                add(cur);
                pos = cur.bottom() + 2;
            }

            resize(WIDTH, (int) pos);
        }
    }
}
