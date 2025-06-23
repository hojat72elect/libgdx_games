package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.game.GameScreen;
import com.nopalsoft.zombiedash.game.WorldGame;
import com.nopalsoft.zombiedash.screens.MainMenuScreen;
import com.nopalsoft.zombiedash.screens.Screens;
import com.nopalsoft.zombiedash.shop.VentanaShop;

public class VentanaGameover extends Ventana {

    Button btMenu, btShop, btTryAgain;
    TextButton btShare;

    int buttonSize = 55;

    WorldGame oWorld;

    VentanaShop ventanaShop;

    public VentanaGameover(Screens currentScreen) {
        super(currentScreen, 450, 410, 30, Assets.backgroundSmallWindow);
        oWorld = ((GameScreen) currentScreen).oWorld;

        ventanaShop = new VentanaShop(screen);

        Label lbDistance = new Label(idiomas.format("distance_num", (int) oWorld.distance), Assets.labelStyleGrande);
        lbDistance.setFontScale(1.5f);
        lbDistance.setAlignment(Align.center);
        lbDistance.setPosition(getWidth() / 2f - lbDistance.getWidth() / 2f, 310);
        addActor(lbDistance);

        Label lbBestDistance = new Label(idiomas.format("best_distance", Settings.bestScore), Assets.labelStyleChico);
        lbBestDistance.setAlignment(Align.center);
        lbBestDistance.setPosition(getWidth() / 2f - lbBestDistance.getWidth() / 2f, 270);
        addActor(lbBestDistance);

        initButtons();

        Table tbButtons = new Table();
        tbButtons.setSize(250, 90);
        tbButtons.setPosition(getWidth() / 2f - tbButtons.getWidth() / 2f, 180);
        // content.debug();

        tbButtons.defaults().expandX().uniform();

        tbButtons.add(btMenu);
        tbButtons.add(btShop);
        tbButtons.add(btTryAgain);

        addActor(tbButtons);

        Label lbShare = new Label(idiomas.format("share_on_Facebook", Settings.NUM_GEMS_SHARE_FACEBOOK), Assets.labelStyleChico);
        lbShare.setSize(getWidth() - 100, 120);
        lbShare.setWrap(true);
        lbShare.setAlignment(Align.center);
        lbShare.setPosition(getWidth() / 2f - lbShare.getWidth() / 2f, 90);

        btShare = new TextButton(idiomas.get("share"), Assets.styleTextButtonShare);
        screen.addEfectoPress(btShare);
        btShare.setPosition(getWidth() / 2f - btShare.getWidth() / 2f, 50);
        btShare.getStyle().fontColor = Color.WHITE;


        btShare.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (game.facebookHandler.facebookIsSignedIn()) {
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            btShare.setTouchable(Touchable.disabled);
                            btShare.getStyle().fontColor = Color.GRAY;
                            game.facebookHandler.facebookShareFeed(text);
                        }

                        @Override
                        public void canceled() {

                        }
                    }, idiomas.get("type_your_message"), "", "");
                } else
                    game.facebookHandler.facebookSignIn();
            }

            ;
        });

        addActor(lbShare);
        addActor(btShare);
    }

    private void initButtons() {
        btMenu = new Button(Assets.btMenu);
        btMenu.setSize(buttonSize, buttonSize);

        screen.addEfectoPress(btMenu);
        btMenu.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
            }

            ;
        });

        btShop = new Button(Assets.btShop);
        btShop.setSize(buttonSize, buttonSize);
        screen.addEfectoPress(btShop);
        btShop.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                ventanaShop.show(screen.stage);
            }

            ;
        });

        btTryAgain = new Button(Assets.btTryAgain);
        btTryAgain.setSize(buttonSize, buttonSize);
        screen.addEfectoPress(btTryAgain);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(GameScreen.class, game);
            }

            ;
        });

    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
        game.reqHandler.showAdBanner();
    }

}
