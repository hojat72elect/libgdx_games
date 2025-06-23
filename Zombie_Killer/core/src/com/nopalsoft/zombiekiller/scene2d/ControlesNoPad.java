package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.game.GameScreen;

public class ControlesNoPad extends Table {

	GameScreen gameScreen;

	Button btUp, btDown, btLeft, btRight;
	public boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
	public float widthButtons;

	public ControlesNoPad() {
		init();
	}

	private void init() {
		btUp = new Button(Assets.btUp, Assets.btUpPress, Assets.btUpPress);
		btUp.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				isMovingUp = true;
				btUp.setChecked(true);

			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				isMovingUp = false;
				btUp.setChecked(false);
			};
		});

		btDown = new Button(Assets.btDown, Assets.btDownPress, Assets.btDownPress);
		btDown.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				isMovingDown = true;
				btDown.setChecked(true);

			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				isMovingDown = false;
				btDown.setChecked(false);
			};
		});

		btLeft = new Button(Assets.btLeft, Assets.btLeftPress, Assets.btLeftPress);
		btLeft.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				isMovingLeft = true;
				btLeft.setChecked(true);

			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				isMovingLeft = false;
				btLeft.setChecked(false);
			};
		});

		btRight = new Button(Assets.btRight, Assets.btRightPress, Assets.btRightPress);
		btRight.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				isMovingRight = true;
				btRight.setChecked(true);

			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				isMovingRight = false;
				btRight.setChecked(false);
			};
		});

		setNewSize(Settings.padSize);
	}

	public void setNewSize(float width) {
		this.widthButtons = width;
		clearChildren();

		defaults().size(widthButtons / 3f);

		add(btUp).colspan(2).center();
		row();
		add(btLeft).left();
		add(btRight).right().padLeft(widthButtons / 3.5f);
		row();
		add(btDown).colspan(2).center();
		pack();

	}
}
