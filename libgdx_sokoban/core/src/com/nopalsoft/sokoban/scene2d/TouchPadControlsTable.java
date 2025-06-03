package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game.GameScreen;

public class TouchPadControlsTable extends Table {

    GameScreen gameScreen;

    Button buttonUp, buttonDown, buttonLeft, buttonRight;

    public TouchPadControlsTable(GameScreen oScreen) {
        gameScreen = oScreen;

        getColor().a = .4f;
        init();

        int buttonSize = 75;
        defaults().size(buttonSize);

        add(buttonUp).colspan(2).center();
        row();
        add(buttonLeft).left();
        add(buttonRight).right().padLeft(buttonSize / 1.15f);
        row();
        add(buttonDown).colspan(2).center();
        pack();
    }

    private void init() {
        buttonUp = new Button(Assets.buttonUpDrawable, Assets.buttonUpPressedDrawable);
        buttonUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.up();
            }
        });

        buttonDown = new Button(Assets.buttonDownDrawable, Assets.buttonDownPressedDrawable);
        buttonDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.down();
            }
        });

        buttonLeft = new Button(Assets.buttonLeftDrawable, Assets.buttonLeftPressedDrawable);
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.left();
            }
        });

        buttonRight = new Button(Assets.buttonRightDrawable, Assets.buttonRightPressedDrawable);
        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.right();
            }
        });
    }
}
