package com.nopalsoft.zombiedash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;

public class SettingsScreen extends Screens {

	public static final int DEFAULT_SIZE_BUTTONS = 100;
	public static final Vector2 DEFAULT_POSITION_BUTTON_JUMP = new Vector2(30, 20);
	public static final Vector2 DEFAULT_POSITION_BUTTON_FIRE = new Vector2(670, 20);

	Image btJump, btFire;
	Vector3 dragPoint;

	Slider sliderButtonSize;

	TextButton btDefaults;
	TextButton btFacebookLogin;

	Button btMenu;

	public SettingsScreen(final MainZombieDash game) {
		super(game);
		dragPoint = new Vector3();

		Table tbSizes = new Table();
		tbSizes.setPosition(25, 210);
		// tbSizes.debug();

		// Size buttons
		Label lbButtonsSize = new Label(game.idiomas.get("button_size"), Assets.labelStyleChico);
		sliderButtonSize = new Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle);
		sliderButtonSize.setValue(1);// LA mitad es 1
		sliderButtonSize.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float size = DEFAULT_SIZE_BUTTONS * sliderButtonSize.getValue();
				btJump.setSize(size, size);
				btFire.setSize(size, size);
				Settings.buttonSize = size;
			}

		});

		btDefaults = new TextButton(game.idiomas.get("defaults"), Assets.styleTextButtonBuy);
		addEfectoPress(btDefaults);
		btDefaults.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				btFire.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);
				btJump.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);

				sliderButtonSize.setValue(1);

				btFire.setPosition(DEFAULT_POSITION_BUTTON_FIRE.x, DEFAULT_POSITION_BUTTON_FIRE.y);
				btJump.setPosition(DEFAULT_POSITION_BUTTON_JUMP.x, DEFAULT_POSITION_BUTTON_JUMP.y);

				Settings.saveNewButtonFireSettings(btFire.getX(), btFire.getY(), btFire.getWidth());
				Settings.saveNewButtonJumpSettings(btJump.getX(), btJump.getY(), btJump.getWidth());
			}
		});

		tbSizes.defaults().left();

		tbSizes.row().padTop(20);
		tbSizes.add(lbButtonsSize);
		tbSizes.row();
		tbSizes.add(sliderButtonSize).width(200);

		tbSizes.row().padTop(15);
		tbSizes.add(btDefaults).height(50);

		tbSizes.pack();

		btJump = new Image(Assets.btJump);
		btJump.setSize(Settings.buttonSize, Settings.buttonSize);
		btJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY);
		btJump.getColor().a = .5f;
		addEfectoPress(btJump);
		btJump.addListener(new DragListener() {
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				btJump.setPosition(dragPoint.x - btJump.getWidth() / 2f, dragPoint.y - btJump.getHeight() / 2f);
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				Settings.saveNewButtonJumpSettings(btJump.getX(), btJump.getY(), btJump.getWidth());
			}
		});

		btFire = new Image(Assets.btFire);
		btFire.setSize(Settings.buttonSize, Settings.buttonSize);
		btFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY);
		btFire.getColor().a = .5f;
		addEfectoPress(btFire);
		btFire.addListener(new DragListener() {
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				btFire.setPosition(dragPoint.x - btFire.getWidth() / 2f, dragPoint.y - btFire.getHeight() / 2f);
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				Settings.saveNewButtonFireSettings(btFire.getX(), btFire.getY(), btFire.getWidth());
			}
		});

		btMenu = new Button(Assets.btMenu);
		btMenu.setSize(45, 45);
		btMenu.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
		addEfectoPress(btMenu);
		btMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(MainMenuScreen.class, game);
			}
		});

		// Facebook
		btFacebookLogin = new TextButton("", Assets.styleTextButtonFacebook);
		btFacebookLogin.getLabel().setWrap(true);
		btFacebookLogin.setSize(160, 64);

		addEfectoPress(btFacebookLogin);
		btFacebookLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.facebookHandler.facebookIsSignedIn())
					game.facebookHandler.facebookSignOut();
				else
					game.facebookHandler.facebookSignIn();
			}
		});

		Table tbFacebookStuff = new Table();
		tbFacebookStuff.setPosition(25, 350);
		// tbFacebookStuff.debug();

		Label lbLoginFacebook = new Label(game.idiomas.get("facebook_login_description"), Assets.labelStyleChico);
		lbLoginFacebook.setWrap(true);

		tbFacebookStuff.add(lbLoginFacebook).width(600).expandX().left();

		tbFacebookStuff.row();
		tbFacebookStuff.add(btFacebookLogin).left();

		tbFacebookStuff.pack();

		stage.addActor(btJump);
		stage.addActor(btFire);
		stage.addActor(tbSizes);
		stage.addActor(btMenu);
		stage.addActor(tbFacebookStuff);

	}

	@Override
	public void update(float delta) {

		if (game.facebookHandler.facebookIsSignedIn())
			btFacebookLogin.setText(game.idiomas.get("logout"));
		else
			btFacebookLogin.setText(game.idiomas.get("login"));
	}

	@Override
	public void draw(float delta) {
		Assets.parallaxBackground.render(delta);

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			changeScreenWithFadeOut(MainMenuScreen.class, game);
			return true;
		}
		return false;
	}

}
