package com.nopalsoft.thetruecolor.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.MainTheTrueColor;
import com.nopalsoft.thetruecolor.Settings;
import com.nopalsoft.thetruecolor.game.GameScreen;
import com.nopalsoft.thetruecolor.leaderboard.DialogRanking;
import com.nopalsoft.thetruecolor.leaderboard.Person;
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings;
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages;

public class MainMenuScreen extends BaseScreen {

    Image titleImage;
    DialogRanking dialogRanking;

    ImageButton startButton;

    Table menuUITable;
    Button buttonRate, buttonLeaderboard, buttonAchievement, buttonHelp;

    DialogHelpSettings helpDialog;

    public MainMenuScreen(final MainTheTrueColor game) {
        super(game);

        titleImage = new Image(Assets.titleDrawable);
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 610);

        helpDialog = new DialogHelpSettings(this);
        dialogRanking = new DialogRanking(this);

        startButton = new ImageButton(new ImageButtonStyle(Assets.buttonPlayDrawable, null, null, Assets.playDrawable, null, null));
        addPressEffect(startButton);
        startButton.getImageCell().padLeft(10).size(47, 54);// Centro la imagen play con el pad, y le pongo el tamano
        startButton.setSize(288, 72);
        startButton.setPosition(SCREEN_WIDTH / 2f - startButton.getWidth() / 2f, 120);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        buttonRate = new Button(Assets.buttonRateDrawable);
        addPressEffect(buttonRate);

        buttonLeaderboard = new Button(Assets.buttonLeaderBoardDrawable);
        addPressEffect(buttonLeaderboard);

        buttonAchievement = new Button(Assets.buttonAchievementDrawable);
        addPressEffect(buttonAchievement);

        buttonHelp = new Button(Assets.buttonHelpDrawable);
        addPressEffect(buttonHelp);
        buttonHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                helpDialog.show(stage);
            }
        });

        menuUITable = new Table();
        menuUITable.setSize(SCREEN_WIDTH, 70);
        menuUITable.setPosition(0, 35);
        menuUITable.defaults().size(70).expand();

        if (Gdx.app.getType() != ApplicationType.WebGL) {
            menuUITable.add(buttonRate);
            menuUITable.add(buttonLeaderboard);
            menuUITable.add(buttonAchievement);
        }
        menuUITable.add(buttonHelp);

        stage.addActor(titleImage);
        stage.addActor(dialogRanking);
        stage.addActor(startButton);
        stage.addActor(menuUITable);

        if (game.persons != null)
            updateLeaderboard();

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.header, 0, 780, 480, 20);
        batch.draw(Assets.header, 0, 0, 480, 20);
        batch.end();
    }

    public void updateLeaderboard() {
        dialogRanking.clearLeaderboard();
        game.persons.sort();// Arrange from largest to smallest
        for (Person obj : game.persons) {
            dialogRanking.addPerson(obj);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        } else if (keycode == Keys.E) {
            Settings.selectedLanguage = Languages.ENGLISH;
            Settings.save();
            Assets.loadAssetsWithSettings();
            game.setScreen(new MainMenuScreen(game));
        } else if (keycode == Keys.R) {
            Settings.selectedLanguage = Languages.SPANISH;
            Settings.save();
            Assets.loadAssetsWithSettings();
            game.setScreen(new MainMenuScreen(game));
        } else if (keycode == Keys.T) {
            Settings.selectedLanguage = Languages.CHINESE;
            Settings.save();
            Assets.loadAssetsWithSettings();
            game.setScreen(new MainMenuScreen(game));
        } else if (keycode == Keys.Y) {
            Settings.selectedLanguage = Languages.DEFAULT;
            Settings.save();
            Assets.loadAssetsWithSettings();
            game.setScreen(new MainMenuScreen(game));
        }

        return super.keyDown(keycode);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
