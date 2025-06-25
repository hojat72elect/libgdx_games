

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

public class Effects {

    public enum Type {
        RIPPLE,
        LIGHTNING,
        WOUND,
        EXCLAMATION,
        CHAIN,
        ETHEREAL_CHAIN,
        DEATH_RAY,
        LIGHT_RAY,
        HEALTH_RAY
    }

    public static Image get(Type type) {
        Image icon = new Image(Assets.Effects.EFFECTS);
        switch (type) {
            case RIPPLE:
                icon.frame(icon.texture.uvRect(0, 0, 16, 16));
                break;
            case LIGHTNING:
                icon.frame(icon.texture.uvRect(16, 0, 32, 8));
                break;
            case WOUND:
                icon.frame(icon.texture.uvRect(16, 8, 32, 16));
                break;
            case EXCLAMATION:
                icon.frame(icon.texture.uvRect(0, 16, 6, 25));
                break;
            case CHAIN:
                icon.frame(icon.texture.uvRect(6, 16, 11, 22));
                break;
            case ETHEREAL_CHAIN:
                icon.frame(icon.texture.uvRect(11, 16, 16, 22));
                break;
            case DEATH_RAY:
                icon.frame(icon.texture.uvRect(16, 16, 32, 24));
                break;
            case LIGHT_RAY:
                icon.frame(icon.texture.uvRect(16, 23, 32, 31));
                break;
            case HEALTH_RAY:
                icon.frame(icon.texture.uvRect(16, 30, 32, 38));
                break;
        }
        return icon;
    }
}
