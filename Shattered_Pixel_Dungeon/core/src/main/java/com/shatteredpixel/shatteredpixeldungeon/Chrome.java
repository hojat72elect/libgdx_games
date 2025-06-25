

package com.shatteredpixel.shatteredpixeldungeon;

import com.watabou.noosa.NinePatch;

public class Chrome {

    public enum Type {
        TOAST,
        TOAST_TR,
        TOAST_TR_HEAVY,
        TOAST_WHITE,
        WINDOW,
        WINDOW_SILVER,
        RED_BUTTON,
        GREY_BUTTON,
        GREY_BUTTON_TR,
        TAG,
        GEM,
        SCROLL,
        TAB_SET,
        TAB_SELECTED,
        TAB_UNSELECTED,
        BLANK
    }

    public static NinePatch get(Type type) {
        String Asset = Assets.Interfaces.CHROME;
        switch (type) {
            case WINDOW:
                return new NinePatch(Asset, 0, 0, 20, 20, 6);
            case WINDOW_SILVER:
                return new NinePatch(Asset, 86, 0, 22, 22, 7);
            case TOAST:
                return new NinePatch(Asset, 20, 0, 9, 9, 4);
            case TOAST_TR:
            case GREY_BUTTON_TR:
                return new NinePatch(Asset, 20, 9, 9, 9, 4);
            case TOAST_TR_HEAVY:
                return new NinePatch(Asset, 29, 9, 9, 9, 4);
            case TOAST_WHITE:
                return new NinePatch(Asset, 29, 0, 9, 9, 4);
            case RED_BUTTON:
                return new NinePatch(Asset, 38, 0, 6, 6, 2);
            case GREY_BUTTON:
                return new NinePatch(Asset, 38, 6, 6, 6, 2);
            case TAG:
                return new NinePatch(Asset, 22, 18, 16, 14, 3);
            case GEM:
                return new NinePatch(Asset, 0, 32, 32, 32, 13);
            case SCROLL:
                return new NinePatch(Asset, 32, 32, 32, 32, 5, 11, 5, 11);
            case TAB_SET:
                return new NinePatch(Asset, 64, 0, 20, 20, 6);
            case TAB_SELECTED:
                return new NinePatch(Asset, 65, 22, 8, 13, 3, 7, 3, 5);
            case TAB_UNSELECTED:
                return new NinePatch(Asset, 75, 22, 8, 13, 3, 7, 3, 5);
            case BLANK:
                return new NinePatch(Asset, 45, 0, 1, 1, 0, 0, 0, 0);
            default:
                return null;
        }
    }
}
