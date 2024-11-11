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

    LevelSelector levelSelector;

    Table tableMenu;
    Button buttonLeaderboard, buttonAchievements, buttonFacebook, buttonSettings, buttonMore;
    Button buttonNextPage, buttonPreviousPage;

    public MainMenuScreen(final MainSokoban game) {
        super(game);

        levelSelector = new LevelSelector(this);

        buttonPreviousPage = new Button(Assets.buttonIzq, Assets.btIzqPress);
        buttonPreviousPage.setSize(75, 75);
        buttonPreviousPage.setPosition(65, 220);
        buttonPreviousPage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                right();
            }

        });
        buttonNextPage = new Button(Assets.buttonDer, Assets.buttonDerPress);
        buttonNextPage.setSize(75, 75);
        buttonNextPage.setPosition(660, 220);
        buttonNextPage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                left();
            }
        });

        buttonLeaderboard = new Button(Assets.btLeaderboard, Assets.btLeaderboardPress);
        buttonLeaderboard.addListener(new ClickListener() {

        });

        buttonAchievements = new Button(Assets.btAchievement, Assets.btAchievementPress);
        buttonAchievements.addListener(new ClickListener() {

        });

        buttonFacebook = new Button(Assets.btFacebook, Assets.btFacebookPress);
        buttonFacebook.addListener(new ClickListener() {

        });

        buttonSettings = new Button(Assets.btSettings, Assets.btSettingsPress);
        buttonSettings.addListener(new ClickListener() {

        });

        buttonMore = new Button(Assets.btMas, Assets.btMasPress);
        buttonMore.addListener(new ClickListener() {

        });

        tableMenu = new Table();
        tableMenu.defaults().size(80).pad(7.5f);
        
        tableMenu.add(buttonAchievements);
        tableMenu.add(buttonFacebook);
        tableMenu.add(buttonSettings);
        tableMenu.add(buttonMore);

        tableMenu.pack();
        tableMenu.setPosition(SCREEN_WIDTH / 2f - tableMenu.getWidth() / 2f, 20);

        stage.addActor(levelSelector);
        stage.addActor(tableMenu);
        stage.addActor(buttonPreviousPage);
        stage.addActor(buttonNextPage);
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
        levelSelector.previousPage();
    }

    @Override
    public void left() {
        levelSelector.nextPage();

    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Keys.LEFT || keycode == Keys.A) {
            right();
        } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
            left();
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            Gdx.app.exit();
        }

        return true;
    }


}
