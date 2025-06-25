

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.PixelParticle;

public class ShopkeeperSprite extends MobSprite {

    private PixelParticle coin;

    public ShopkeeperSprite() {
        super();

        texture(Assets.Sprites.KEEPER);
        TextureFilm film = new TextureFilm(texture, 14, 14);

        idle = new Animation(10, true);
        idle.frames(film, 1, 1, 1, 1, 1, 0, 0, 0, 0);

        die = new Animation(20, false);
        die.frames(film, 0);

        run = idle.clone();

        attack = idle.clone();

        idle();
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);

        if (visible && anim == idle) {
            if (coin == null) {
                coin = new PixelParticle();
                parent.add(coin);
            }
            coin.reset(x + (flipHorizontal ? 0 : 13), y + 7, 0xFFFF00, 1, 0.5f);
            coin.speed.y = -40;
            coin.acc.y = +160;
        }
    }
}
