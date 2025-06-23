package com.nopalsoft.zombiekiller.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.scene2d.ControlesNoPad;

public class SettingsScreen extends Screens {

	public static final int DEFAULT_SIZE_PAD = 170;
	public static final Vector2 DEFAULT_POSITION_PAD = new Vector2(10, 10);

	public static final int DEFAULT_SIZE_BUTTONS = 80;
	public static final Vector2 DEFAULT_POSITION_BUTTON_JUMP = new Vector2(560, 20);
	public static final Vector2 DEFAULT_POSITION_BUTTON_FIRE = new Vector2(680, 20);

	ControlesNoPad controlesNoPad;
	Touchpad pad;
	Image btJump, btFire;
	Vector3 dragPoint;

	Button btEnablePad;
	Slider sliderPadSize;
	Slider sliderButtonSize;

	TextButton btDefaults;

	Button btMenu;

	public SettingsScreen(final MainZombie game) {
		super(game);
		dragPoint = new Vector3();

		Table tbSizes = new Table();
		tbSizes.setPosition(25, 275);
		// tbSizes.debug();

		Table tbEnablePad = new Table();

		Label lbEnablePad = new Label("Enable Pad", Assets.labelStyleChico);
		btEnablePad = new Button(Assets.upgradeOff, new TextureRegionDrawable(Assets.itemSkull), new TextureRegionDrawable(Assets.itemSkull));
		btEnablePad.setChecked(Settings.isPadEnabled);

		ClickListener clickEnablePad = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.isPadEnabled = !Settings.isPadEnabled;
				btEnablePad.setChecked(Settings.isPadEnabled);

				pad.remove();
				controlesNoPad.remove();

				if (Settings.isPadEnabled) {
					pad.setPosition(Settings.padPositionX, Settings.padPositionY);
					stage.addActor(pad);
				}
				else {
					controlesNoPad.setPosition(Settings.padPositionX, Settings.padPositionY);
					stage.addActor(controlesNoPad);
				}
			}
		};

		btEnablePad.addListener(clickEnablePad);
		lbEnablePad.addListener(clickEnablePad);

		tbEnablePad.add(lbEnablePad);
		tbEnablePad.add(btEnablePad).size(30).padLeft(10);

		// Size pad
		Label lbPadSize = new Label("Pad size:", Assets.labelStyleChico);
		sliderPadSize = new Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle);
		sliderPadSize.setValue(1);// LA mitad es 1
		sliderPadSize.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float size = DEFAULT_SIZE_PAD * sliderPadSize.getValue();
				pad.setSize(size, size);
				Settings.padSize = size;
				controlesNoPad.setNewSize(size);
			}

		});

		// Size buttons
		Label lbButtonsSize = new Label("Buttons size:", Assets.labelStyleChico);
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

		btDefaults = new TextButton("Defaults", Assets.styleTextButtonBuy);
		addEfectoPress(btDefaults);
		btDefaults.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				btFire.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);
				btJump.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);
				pad.setSize(DEFAULT_SIZE_PAD, DEFAULT_SIZE_PAD);
				controlesNoPad.setNewSize(DEFAULT_SIZE_PAD);
				sliderButtonSize.setValue(1);
				sliderPadSize.setValue(1);

				pad.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y);
				controlesNoPad.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y);
				btFire.setPosition(DEFAULT_POSITION_BUTTON_FIRE.x, DEFAULT_POSITION_BUTTON_FIRE.y);
				btJump.setPosition(DEFAULT_POSITION_BUTTON_JUMP.x, DEFAULT_POSITION_BUTTON_JUMP.y);

				Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth());
				Settings.saveNewButtonFireSettings(btFire.getX(), btFire.getY(), btFire.getWidth());
				Settings.saveNewButtonJumpSettings(btJump.getX(), btJump.getY(), btJump.getWidth());
			}
		});

		tbSizes.defaults().left();

		tbSizes.add(tbEnablePad).colspan(2);
		tbSizes.row().padTop(20);

		tbSizes.add(lbPadSize);
		tbSizes.add(lbButtonsSize).padLeft(100);

		tbSizes.row().padTop(20);
		tbSizes.add(sliderPadSize).width(200);
		tbSizes.add(sliderButtonSize).width(200).padLeft(100);

		tbSizes.row().colspan(2).padTop(20);
		tbSizes.add(btDefaults).height(50);

		tbSizes.pack();

		controlesNoPad = new ControlesNoPad();
		controlesNoPad.setPosition(Settings.padPositionX, Settings.padPositionY);
		controlesNoPad.getColor().a = .5f;
		controlesNoPad.addListener(new DragListener() {
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				controlesNoPad.setPosition(dragPoint.x - controlesNoPad.getWidth() / 2f, dragPoint.y - controlesNoPad.getHeight() / 2f);
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				Settings.saveNewPadSettings(controlesNoPad.getX(), controlesNoPad.getY(), controlesNoPad.widthButtons);
			}
		});

		pad = new Touchpad(1000, Assets.touchPadStyle);
		pad.setPosition(Settings.padPositionX, Settings.padPositionY);
		pad.setSize(Settings.padSize, Settings.padSize);
		pad.getColor().a = .5f;
		pad.addListener(new DragListener() {
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				pad.setPosition(dragPoint.x - pad.getWidth() / 2f, dragPoint.y - pad.getHeight() / 2f);
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth());
			}
		});

		btJump = new Image(Assets.btUp);
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

		if (Settings.isPadEnabled)
			stage.addActor(pad);
		else
			stage.addActor(controlesNoPad);
		stage.addActor(btJump);
		stage.addActor(btFire);
		stage.addActor(tbSizes);
		stage.addActor(btMenu);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.draw(Assets.moon, 450, 220, 350, 255);

		batcher.end();

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
