package com.nopalsoft.impossibledial.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.scene2d.VentanaDificultad;

public class MainMenuScreen extends Screens {

    Image titulo;
    Image circulo;
    Image arrow;


    ImageButton btJugar;

    Table menuUI;
    Button btRate, btLeaderboard, btAchievement, btFacebook;

    VentanaDificultad ventanaDificultad;


    public MainMenuScreen(final MainGame game) {
        super(game);

        ventanaDificultad = new VentanaDificultad(this);

        titulo = new Image(Assets.titulo);
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 610);

        circulo = new Image(Assets.circle);
        circulo.setSize(325, 325);
        circulo.setPosition(SCREEN_WIDTH / 2f - circulo.getWidth() / 2f, 240);

        arrow = new Image(Assets.arrowBlue);
        arrow.setSize(8, 150);
        arrow.setPosition(SCREEN_WIDTH / 2f - arrow.getWidth() / 2f, 240 + circulo.getHeight() / 2f);
        arrow.setOrigin(4, 0);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                float rotation = MathUtils.random(180, 360);
                arrow.addAction(Actions.sequence(Actions.rotateBy(rotation, 3), Actions.rotateBy(-rotation, 3)));
            }
        };
        arrow.addAction(Actions.forever(Actions.sequence(Actions.run(run), Actions.delay(6))));


        btJugar = new ImageButton(new ImageButtonStyle(Assets.btJugar, null, null, Assets.play, null, null));
        addEfectoPress(btJugar);
        btJugar.getImageCell().padLeft(10).size(47, 54);// Centro la imagen play con el pad, y le pongo el tamano
        btJugar.setSize(288, 72);
        btJugar.setPosition(SCREEN_WIDTH / 2f - btJugar.getWidth() / 2f, 120);
        btJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ventanaDificultad.show(stage);
            }
        });

        btRate = new Button(Assets.btRate);
        addEfectoPress(btRate);
        btRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showRater();
            }
        });

        btLeaderboard = new Button(Assets.btLeaderboard);
        addEfectoPress(btLeaderboard);
        btLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getLeaderboard();
                }
                else {
                    game.gameServiceHandler.signIn();
                }
            }
        });

        btAchievement = new Button(Assets.btAchievement);
        addEfectoPress(btAchievement);
        btAchievement.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getAchievements();
                }
                else {
                    game.gameServiceHandler.signIn();
                }

            }
        });

        btFacebook = new Button(Assets.btFacebook);
        addEfectoPress(btFacebook);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.facebookHandler.showFacebook();
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
        menuUI.add(btFacebook);

        stage.addActor(titulo);
        stage.addActor(circulo);
        stage.addActor(arrow);
        stage.addActor(btJugar);
        stage.addActor(menuUI);


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


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return super.keyDown(keycode);
    }

}
