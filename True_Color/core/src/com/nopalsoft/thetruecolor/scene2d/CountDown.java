package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.game.GameScreen;
import com.nopalsoft.thetruecolor.screens.Screens;

public class CountDown extends Group {

    Image one, two, three;
    GameScreen gameScreen;
    Label lbText;

    float tiempoPorNumero = 1.25f;

    public CountDown(GameScreen screen) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        gameScreen = screen;

        lbText = new Label(Assets.languagesBundle.get("verdaderoFalse"), new LabelStyle(Assets.fontSmall, Color.BLACK));
        lbText.setFontScale(1.2f);
        lbText.setPosition(getWidth() / 2f - lbText.getWidth() * lbText.getFontScaleX() / 2f, 300);

        one = new Image(Assets.oneDrawable);
        one.setPosition(getWidth() / 2f - one.getWidth() / 2f, 500);

        two = new Image(Assets.twoDrawable);
        two.setPosition(getWidth() / 2f - two.getWidth() / 2f, 500);

        three = new Image(Assets.threeDrawable);
        three.setPosition(getWidth() / 2f - three.getWidth() / 2f, 500);

        Runnable runAfterThree = new Runnable() {

            @Override
            public void run() {
                three.remove();
                addActor(two);
            }
        };
        three.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterThree)));

        Runnable runAfterTwo = new Runnable() {

            @Override
            public void run() {
                two.remove();
                addActor(one);
            }
        };
        two.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterTwo)));

        Runnable runAfterOne = new Runnable() {

            @Override
            public void run() {
                one.remove();
                gameScreen.setRunning();
                remove();
            }
        };
        one.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterOne)));

        addActor(three);
        addActor(lbText);
    }
}
