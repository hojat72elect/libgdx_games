package com.nGame.utils.scene2d;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nGame.utils.ng.Assets;

abstract class PreferenceCheckBox extends Table {
    private CheckBox box;

    PreferenceCheckBox(String labelText, int width, int height) {
        super(Assets.I.skin);
        defaults();
        //setFillParent(true);
        setWidth(width);
        setHeight(height);

        Label label = new Label(labelText, Assets.I.skin);
        label.setAlignment(Align.left, Align.bottom);
        add(label).expandX().fillX();


        box = new CheckBox("", Assets.I.skin);
        add(box);


        box.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange(box.isChecked());
            }


        });


    }

    boolean isChecked() {
        return box.isChecked();
    }

    public void setChecked(boolean checked) {
        box.setChecked(checked);
    }

    protected abstract void onChange(boolean checked);
}
