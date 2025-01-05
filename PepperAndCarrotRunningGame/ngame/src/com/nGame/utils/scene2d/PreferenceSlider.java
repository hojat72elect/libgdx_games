package com.nGame.utils.scene2d;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nGame.utils.ng.Assets;

import java.text.NumberFormat;

public abstract class PreferenceSlider extends Table {
    private Slider slider;
    private Label labelTitle;
    private Label labelValue;

    public PreferenceSlider(String labelText, int width, int height, float min, float max, float stepSize, boolean vertical) {
        super(Assets.I.skin);
        defaults();
        //setFillParent(true);
        setWidth(width);
        setHeight(height);

        labelTitle = new Label(labelText, Assets.I.skin);
        labelTitle.setAlignment(Align.left, Align.bottom);

        labelValue = new Label("0", Assets.I.skin);
        labelValue.setAlignment(Align.right, Align.bottom);


        slider = new Slider(min, max, stepSize, vertical, Assets.I.skin);
        add(labelTitle).colspan(2).left().expandX().fillX();
        row();
        add(slider).left().expandX().fillX();
        add(labelValue).right().width(100);


        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange(getValue());
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                System.out.println(nf.format(getValue()));
                labelValue.setText("" + nf.format(getValue()));

            }


        });


    }

    float getValue() {
        return slider.getValue();
    }

    public void setValue(float value) {
        slider.setValue(value);
    }

    public abstract void onChange(float value);
}
