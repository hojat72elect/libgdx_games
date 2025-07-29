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
import com.nopalsoft.thetruecolor.scene2d.VentanaHelpSettings;
import com.nopalsoft.thetruecolor.scene2d.VentanaHelpSettings.Languages;

public class MainMenuScreen extends Screens {

    Image titulo;
    DialogRanking dialogRanking;

    ImageButton btJugar;

    Table menuUI;
    Button btRate, btLeaderboard, btAchievement, btHelp;

    VentanaHelpSettings ventanaHelp;

    public MainMenuScreen(final MainTheTrueColor game) {
        super(game);

        titulo = new Image(Assets.titulo);
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 610);

        ventanaHelp = new VentanaHelpSettings(this);
        dialogRanking = new DialogRanking(this);

        btJugar = new ImageButton(new ImageButtonStyle(Assets.btJugar, null, null, Assets.play, null, null));
        addEfectoPress(btJugar);
        btJugar.getImageCell().padLeft(10).size(47, 54);// Centro la imagen play con el pad, y le pongo el tamano
        btJugar.setSize(288, 72);
        btJugar.setPosition(SCREEN_WIDTH / 2f - btJugar.getWidth() / 2f, 120);
        btJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        btRate = new Button(Assets.btRate);
        addEfectoPress(btRate);


        btLeaderboard = new Button(Assets.btLeaderboard);
        addEfectoPress(btLeaderboard);

        btAchievement = new Button(Assets.btAchievement);
        addEfectoPress(btAchievement);


        btHelp = new Button(Assets.btHelp);
        addEfectoPress(btHelp);
        btHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ventanaHelp.show(stage);
            }
        });

        menuUI = new Table();
        menuUI.setSize(SCREEN_WIDTH, 70);
        menuUI.setPosition(0, 35);
        menuUI.defaults().size(70).expand();

        if (Gdx.app.getType() != ApplicationType.WebGL) {
            menuUI.add(btRate);
            menuUI.add(btLeaderboard);
            menuUI.add(btAchievement);
        }
        menuUI.add(btHelp);

        stage.addActor(titulo);
        stage.addActor(dialogRanking);
        stage.addActor(btJugar);
        stage.addActor(menuUI);

        if (game.arrPerson != null)
            updateLeaderboard();

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.header, 0, 780, 480, 20);
        batcher.draw(Assets.header, 0, 0, 480, 20);
        batcher.end();
    }

    public void updateLeaderboard() {
        dialogRanking.clearLeaderboard();
        game.arrPerson.sort();// Acomoda de mayor a menor
        for (Person obj : game.arrPerson) {
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
            Settings.selectedLanguage = Languages.CHINESE_TAIWAN;
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
}
