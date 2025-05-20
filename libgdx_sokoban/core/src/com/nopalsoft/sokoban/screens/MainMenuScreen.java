package com.nopalsoft.sokoban.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.MainSokoban;
import com.nopalsoft.sokoban.scene2d.LevelSelector;

public class MainMenuScreen extends Screens {

	LevelSelector lvlSelector;

	Table tbMenu;
	Button btLeaderboard, btAchievements, btFacebook, btSettings, btMore;
	Button btNextPage, btPreviousPage;

	public MainMenuScreen(final MainSokoban game) {
		super(game);

		lvlSelector = new LevelSelector(this);

		btPreviousPage = new Button(Assets.btIzq, Assets.btIzqPress);
		btPreviousPage.setSize(75, 75);
		btPreviousPage.setPosition(65, 220);
		btPreviousPage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				right();
			}

		});
		btNextPage = new Button(Assets.btDer, Assets.btDerPress);
		btNextPage.setSize(75, 75);
		btNextPage.setPosition(660, 220);
		btNextPage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				left();
			}
		});

		btLeaderboard = new Button(Assets.btLeaderboard, Assets.btLeaderboardPress);
		btLeaderboard.addListener(new ClickListener() {

		});

		btAchievements = new Button(Assets.btAchievement, Assets.btAchievementPress);
		btAchievements.addListener(new ClickListener() {

		});

		btFacebook = new Button(Assets.btFacebook, Assets.btFacebookPress);
		btFacebook.addListener(new ClickListener() {

		});

		btSettings = new Button(Assets.btSettings, Assets.btSettingsPress);
		btSettings.addListener(new ClickListener() {

		});

		btMore = new Button(Assets.btMas, Assets.btMasPress);
		btMore.addListener(new ClickListener() {

		});

		tbMenu = new Table();
		tbMenu.defaults().size(80).pad(7.5f);

		// tbMenu.add(btLeaderboard);
		tbMenu.add(btAchievements);
		tbMenu.add(btFacebook);
		tbMenu.add(btSettings);
		tbMenu.add(btMore);

		tbMenu.pack();
		tbMenu.setPosition(SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, 20);

		stage.addActor(lvlSelector);
		stage.addActor(tbMenu);
		stage.addActor(btPreviousPage);
		stage.addActor(btNextPage);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		Assets.background.render(delta);
	}

	@Override
	public void right() {
		lvlSelector.previousPage();
	}

	@Override
	public void left() {
		lvlSelector.nextPage();

	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.LEFT || keycode == Keys.A) {
			right();
		}
		else if (keycode == Keys.RIGHT || keycode == Keys.D) {
			left();
		}
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}

		return true;
	}


}
