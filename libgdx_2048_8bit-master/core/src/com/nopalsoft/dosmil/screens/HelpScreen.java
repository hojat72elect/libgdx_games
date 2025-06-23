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

    Label lbTextHelp1;
    Label lbTextHelp2;
    Image imgPuzzle;

    Button btBack;

    public HelpScreen(final MainGame game) {
        super(game);
        lbTextHelp1 = new Label(Assets.idiomas.get("helpTop"),
                Assets.labelStyleChico);
        lbTextHelp1.setWrap(true);
        lbTextHelp1.setWidth(SCREEN_WIDTH - 20);
        lbTextHelp1.setAlignment(Align.center);
        lbTextHelp1.setPosition(
                SCREEN_WIDTH / 2f - lbTextHelp1.getWidth() / 2f, 660);
        lbTextHelp1.setScale(1.2f);

        imgPuzzle = new Image(Assets.puzzleSolved);
        imgPuzzle.setSize(350, 350);
        imgPuzzle
                .setPosition(SCREEN_WIDTH / 2 - imgPuzzle.getWidth() / 2f, 290);

        lbTextHelp2 = new Label(
                Assets.idiomas.get("helpBottom"),
                Assets.labelStyleChico);
        lbTextHelp2.setWrap(true);
        lbTextHelp2.setWidth(SCREEN_WIDTH - 20);
        lbTextHelp2.setAlignment(Align.center);
        lbTextHelp2.setPosition(
                SCREEN_WIDTH / 2f - lbTextHelp2.getWidth() / 2f, 200);
        lbTextHelp2.setScale(1.2f);

        btBack = new Button(Assets.btAtras);
        btBack.setSize(60, 60);
        btBack.setPosition(SCREEN_WIDTH / 2 - 30, 80);
        addEfectoPress(btBack);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });
        stage.addActor(lbTextHelp1);
        stage.addActor(lbTextHelp2);
        stage.addActor(btBack);
        stage.addActor(imgPuzzle);
    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();
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
