package com.salvai.snake.screens.helper;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.utils.Constants;

public class SpeedChooser {

    private final SnakeIt game;
    public Slider slider;
    public Label label;
    private int speedValue;

    public SpeedChooser(SnakeIt gameClass) {
        game = gameClass;

        speedValue = Constants.WORLD_TIME_MAX - game.worldTime;

        label = new Label("speed", game.skin);


        slider = new Slider(0, 20, 5, false, game.skin, "difficulty");
        slider.setValue(speedValue);
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedValue = (int) slider.getValue();
                game.worldTime = Constants.WORLD_TIME_MAX - speedValue;
            }
        });
    }
}
