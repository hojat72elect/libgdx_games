package com.nopalsoft.dosmil.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.MainGame;

public class HelpScreen extends Screens {

    Label labelTextHelp1;
    Label labelTextHelp2;
    Image imagePuzzle;

    Button buttonBack;

    public HelpScreen(final MainGame game) {
        super(game);
        labelTextHelp1 = new Label(Assets.languagesBundle.get("helpTop"), Assets.labelStyleSmall);
        labelTextHelp1.setWrap(true);
        labelTextHelp1.setWidth(SCREEN_WIDTH - 20);
        labelTextHelp1.setAlignment(Align.center);
        labelTextHelp1.setPosition(SCREEN_WIDTH / 2f - labelTextHelp1.getWidth() / 2f, 660);
        labelTextHelp1.setScale(1.2f);

        imagePuzzle = new Image(Assets.puzzleSolvedAtlasRegion);
        imagePuzzle.setSize(350, 350);
        imagePuzzle.setPosition(SCREEN_WIDTH / 2F - imagePuzzle.getWidth() / 2F, 290);

        labelTextHelp2 = new Label(Assets.languagesBundle.get("helpBottom"), Assets.labelStyleSmall);
        labelTextHelp2.setWrap(true);
        labelTextHelp2.setWidth(SCREEN_WIDTH - 20);
        labelTextHelp2.setAlignment(Align.center);
        labelTextHelp2.setPosition(SCREEN_WIDTH / 2f - labelTextHelp2.getWidth() / 2f, 200);
        labelTextHelp2.setScale(1.2f);

        buttonBack = new Button(Assets.buttonBack);
        buttonBack.setSize(60, 60);
        buttonBack.setPosition(SCREEN_WIDTH / 2F - 30, 80);
        addPressEffect(buttonBack);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });
        stage.addActor(labelTextHelp1);
        stage.addActor(labelTextHelp2);
        stage.addActor(buttonBack);
        stage.addActor(imagePuzzle);
    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.backgroundAtlasRegion, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
        }
        return super.keyDown(keycode);
    }
}
