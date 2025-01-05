package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nGame.utils.ng.Assets;


public abstract class PreferenceCheckBoxSlider extends PreferenceCheckBox {

    private Slider slider;

    public PreferenceCheckBoxSlider(String label, int width, int height, float min, float max, float stepSize) {
        super(label, width, height);

        row();

        slider = new Slider(min, max, stepSize, false, Assets.I.skin);
        add(slider).fillX().expandX().colspan(2);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSliderChange(slider.getValue());
            }
        });


        if (isChecked()) {
            slider.setDisabled(false);
            slider.addAction(Actions.color(Color.WHITE, .3f, Interpolation.exp5In));
        } else {

            slider.setDisabled(true);
            slider.addAction(Actions.color(Color.LIGHT_GRAY, .3f, Interpolation.exp5Out));
        }
    }

    @Override
    public void onChange(boolean checked) {
        if (checked) {
            slider.setDisabled(false);
            slider.addAction(Actions.color(Color.WHITE, .3f, Interpolation.exp5In));
        } else {

            slider.setDisabled(true);
            slider.addAction(Actions.color(Color.LIGHT_GRAY, .3f, Interpolation.exp5Out));
        }
        onCheckBoxChange(checked);
    }

    public abstract void onSliderChange(float value);

    public void setValue(float music_vol) {
        slider.setValue(music_vol * 100);
    }

    public abstract void onCheckBoxChange(boolean checked);

    public float getCurrentValue() {
        return slider.getValue();
    }


}
