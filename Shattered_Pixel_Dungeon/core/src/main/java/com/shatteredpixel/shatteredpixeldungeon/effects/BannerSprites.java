

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

public class BannerSprites {

    public enum Type {
        TITLE_PORT,
        TITLE_GLOW_PORT,
        TITLE_LAND,
        TITLE_GLOW_LAND,
        BOSS_SLAIN,
        GAME_OVER,
        SELECT_YOUR_HERO
    }

    public static Image get(Type type) {
        Image icon = new Image(Assets.Interfaces.BANNERS);
        switch (type) {
            case TITLE_PORT:
                icon.frame(icon.texture.uvRect(0, 0, 139, 100));
                break;
            case TITLE_GLOW_PORT:
                icon.frame(icon.texture.uvRect(139, 0, 278, 100));
                break;
            case TITLE_LAND:
                icon.frame(icon.texture.uvRect(0, 100, 240, 157));
                break;
            case TITLE_GLOW_LAND:
                icon.frame(icon.texture.uvRect(240, 100, 480, 157));
                break;
            case BOSS_SLAIN:
                icon.frame(icon.texture.uvRect(0, 157, 128, 192));
                break;
            case GAME_OVER:
                icon.frame(icon.texture.uvRect(0, 192, 128, 227));
                break;
            case SELECT_YOUR_HERO:
                icon.frame(icon.texture.uvRect(0, 227, 128, 248));
                break;
        }
        return icon;
    }
}
