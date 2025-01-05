package com.nGame.utils.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by n0iz on 03.02.2015.
 */
public class WindowNG extends Window {
    public WindowNG(String title, Skin skin) {
        super(title, skin);
    }

    public WindowNG(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
    }

    public WindowNG(String title, WindowStyle style) {
        super(title, style);
    }


}
