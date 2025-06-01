package com.nopalsoft.dragracer.scene2D;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.screens.Screens;

public class SwipeVerticalTutorial {

	public SwipeVerticalTutorial(final Stage stage) {

		final Label lblSwipeToMove = new Label("Swipe for turbo!",
				Assets.labelStyleGrande);
		lblSwipeToMove
				.setPosition(
						Screens.SCREEN_WIDTH / 2f - lblSwipeToMove.getWidth()
								/ 2f, 600);
		lblSwipeToMove.getColor().a = 0;

		final Image swipeHand = new Image(Assets.swipeHand);
		swipeHand.setPosition(Screens.SCREEN_WIDTH / 2f, 400);
		swipeHand.setOrigin(swipeHand.getWidth() / 2f,
				swipeHand.getHeight() / 2f);
		swipeHand.setScale(1.2f);
		swipeHand.addAction(//
				Actions.sequence(
						//
						Actions.scaleTo(1, 1, .25f),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								swipeHand.setDrawable(Assets.swipeHandDown);
							}
						}),//
						Actions.moveTo(swipeHand.getX(), 500, .65f), //
						Actions.run(new Runnable() {
							@Override
							public void run() {
								swipeHand.setDrawable(Assets.swipeHand);

							}
						}),//
						Actions.scaleTo(1.1f, 1.1f, .125f),//
						Actions.run(new Runnable() {
							@Override
							public void run() {
								// swipeArrows.remove();
								swipeHand.remove();

							}
						})//
				));

		lblSwipeToMove.addAction(Actions.sequence(Actions.fadeIn(1f),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						lblSwipeToMove.remove();
						stage.addActor(swipeHand);

					}
				})));

		// stage.addActor(swipeArrows);
		stage.addActor(lblSwipeToMove);
		// addActor(swipeArrows);
	}
}
