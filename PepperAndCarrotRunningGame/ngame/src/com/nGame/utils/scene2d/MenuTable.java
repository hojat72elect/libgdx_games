package com.nGame.utils.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.ng.Assets;

public class MenuTable extends Table {

    //private float fontScale;
    private int align = Align.center;

    public MenuTable(float padding, int align) {
        super(Assets.I.skin);
        //this.fontScale = fontScale;
        defaults();
        setFillParent(false);
        columnDefaults(0).pad(padding);
        this.align=align;

    }

    public void addEntry(MenuEntry entry) {
        //entry.setFontScale(fontScale);
        add(entry).align(align);
        row();
    }

    public void addEntry(MenuEntry entry,int align) {
        //entry.setFontScale(fontScale);
        add(entry).align(align);
        row();
    }
}
