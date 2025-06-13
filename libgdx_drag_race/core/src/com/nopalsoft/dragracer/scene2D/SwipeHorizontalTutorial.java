package com.nopalsoft.dragracer.scene2D;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.screens.Screens;

public class SwipeHorizontalTutorial extends Group {

    public SwipeHorizontalTutorial() {
        setSize(Screens.SCREEN_WIDTH, 195);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 0);

        final Image swipeArrows = new Image(Assets.swipeArrows);
        swipeArrows.setPosition(getWidth() / 2f - swipeArrows.getWidth() / 2f,
                160);

        final Label lblSwipeToMove = new Label("Swipe to move",
                Assets.labelStyleLarge);
        lblSwipeToMove.setPosition(getWidth() / 2f - lblSwipeToMove.getWidth()
                / 2f, 100);
        lblSwipeToMove.getColor().a = 0;
        lblSwipeToMove.addAction(Actions.fadeIn(1f));

        final Image swipeHand = new Image(Assets.swipeHand);
        swipeHand.setPosition(180, 10);
        swipeHand.setOrigin(swipeHand.getWidth() / 2f,
                swipeHand.getHeight() / 2f);
        swipeHand.setScale(1.2f);
        swipeHand.addAction(
                Actions.sequence(
                        Actions.scaleTo(1, 1, .25f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                swipeHand.setDrawable(Assets.swipeHandDown);
                            }
                        }),
                        Actions.moveTo(250, 10, .5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                swipeHand.setDrawable(Assets.swipeHand);
                            }
                        }),
                        Actions.scaleTo(1.1f, 1.1f, .15f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {

                                swipeHand.remove();
                                addActor(lblSwipeToMove);
                            }
                        })
                ));

        addActor(swipeHand);
        addActor(swipeArrows);
    }
}
