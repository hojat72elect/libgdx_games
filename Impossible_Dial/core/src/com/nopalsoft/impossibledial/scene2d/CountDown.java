package com.nopalsoft.impossibledial.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.game.GameScreen;
import com.nopalsoft.impossibledial.screens.Screens;

public class CountDown extends Group {

    Image one, two, three;
    GameScreen gameScreen;

    float tiempoPorNumero = 1.15f;

    public CountDown(GameScreen screen) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        gameScreen = screen;

        float positionY = 350;

        one = new Image(Assets.one);
        one.setPosition(getWidth() / 2f - one.getWidth() / 2f, positionY);

        two = new Image(Assets.two);
        two.setPosition(getWidth() / 2f - two.getWidth() / 2f, positionY);

        three = new Image(Assets.three);
        three.setPosition(getWidth() / 2f - three.getWidth() / 2f, positionY);

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


    }
}
