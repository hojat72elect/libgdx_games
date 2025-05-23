package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game.GameScreen;

public class TouchPadControlsTable extends Table {

    GameScreen gameScreen;

    Button btUp, btDown, btLeft, btRight;

    public TouchPadControlsTable(GameScreen oScreen) {
        gameScreen = oScreen;

        getColor().a = .4f;
        init();

        int buttonSize = 75;
        defaults().size(buttonSize);

        add(btUp).colspan(2).center();
        row();
        add(btLeft).left();
        add(btRight).right().padLeft(buttonSize / 1.15f);
        row();
        add(btDown).colspan(2).center();
        pack();
    }

    private void init() {
        btUp = new Button(Assets.buttonUpDrawable, Assets.buttonUpPressedDrawable);
        btUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.up();
            }
        });

        btDown = new Button(Assets.buttonDownDrawable, Assets.buttonDownPressedDrawable);
        btDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.down();
            }
        });

        btLeft = new Button(Assets.buttonLeftDrawable, Assets.buttonLeftPressedDrawable);
        btLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.left();
            }
        });

        btRight = new Button(Assets.buttonRightDrawable, Assets.buttonRightPressedDrawable);
        btRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.right();
            }
        });
    }
}
