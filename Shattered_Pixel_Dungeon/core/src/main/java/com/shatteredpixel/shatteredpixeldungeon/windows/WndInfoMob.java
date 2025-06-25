

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {

    public WndInfoMob(Mob mob) {

        super(new MobTitle(mob), mob.info());
    }

    private static class MobTitle extends Component {

        private static final int GAP = 2;

        private final CharSprite image;
        private final RenderedTextBlock name;
        private final HealthBar health;
        private final BuffIndicator buffs;

        public MobTitle(Mob mob) {

            name = PixelScene.renderTextBlock(Messages.titleCase(mob.name()), 9);
            name.hardlight(TITLE_COLOR);
            add(name);

            image = mob.sprite();
            add(image);

            health = new HealthBar();
            health.level(mob);
            add(health);

            buffs = new BuffIndicator(mob, false);
            add(buffs);
        }

        @Override
        protected void layout() {

            image.x = 0;
            image.y = Math.max(0, name.height() + health.height() - image.height());

            float w = width - image.width() - GAP;
            int extraBuffSpace = 0;

            //Tries to make space for up to 11 visible buffs
            do {
                name.maxWidth((int) w - extraBuffSpace);
                buffs.setSize(w - name.width() - 8, 8);
                extraBuffSpace += 8;
            } while (extraBuffSpace <= 40 && !buffs.allBuffsVisible());

            name.setPos(x + image.width() + GAP,
                    image.height() > name.height() ? y + (image.height() - name.height()) / 2 : y);

            health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());

            buffs.setPos(name.right(), name.bottom() - BuffIndicator.SIZE_SMALL - 2);

            height = Math.max(image.y + image.height(), health.bottom());
        }
    }
}
