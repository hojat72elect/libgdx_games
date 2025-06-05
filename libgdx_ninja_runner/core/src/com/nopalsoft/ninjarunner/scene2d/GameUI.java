package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.game.GameWorld;
import com.nopalsoft.ninjarunner.screens.Screens;

public class GameUI extends Group {
	public static final float ANIMATION_TIME = .35f;

	GameScreen gameScreen;
    GameWorld gameWorld;

    Table tableHeader;
    Label labelScore;

    Button buttonJump, buttonSlide;
	public boolean didJump, didSlide, didDash;

    public GameUI(final GameScreen gameScreen, GameWorld gameWorld) {
		setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
		this.gameScreen = gameScreen;
        this.gameWorld = gameWorld;

		init();
	}

	private void init() {

        buttonJump = new Button(new ButtonStyle(null, null, null));
        buttonJump.setSize(getWidth() / 2f, getHeight());
        buttonJump.setPosition(0, 0);
        buttonJump.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				didJump = true;
				return false;
			}
		});

        buttonSlide = new Button(new ButtonStyle(null, null, null));
        buttonSlide.setSize(getWidth() / 2f, getHeight());
        buttonSlide.setPosition(getWidth() / 2f + 1, 0);
        buttonSlide.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				didSlide = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				didSlide = false;
			}
		});

        tableHeader = new Table();
        tableHeader.setSize(Screens.SCREEN_WIDTH, 50);
        tableHeader.setPosition(0, Screens.SCREEN_HEIGHT - tableHeader.getHeight());

        labelScore = new Label("0", Assets.labelStyleSmall);
        tableHeader.add(labelScore).fill();

        addActor(tableHeader);

        addActor(buttonJump);
        addActor(buttonSlide);
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

    private void addInActions() {
    }

	public void show(Stage stage) {
		addInActions();
		stage.addActor(this);
	}
}
