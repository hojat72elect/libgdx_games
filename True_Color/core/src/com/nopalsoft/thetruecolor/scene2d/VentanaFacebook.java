package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.screens.Screens;

public class VentanaFacebook extends Ventana {
    static final float WIDTH = 440;
    static final float HEIGHT = 250;

    Label lbText;
    TextButton btFacebookLogin;

    public VentanaFacebook(Screens currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 300);

        setCloseButton(400, 210, 50);

        lbText = new Label(idiomas.get("loginToFacebook"), new LabelStyle(Assets.fontChico, Color.BLACK));
        lbText.setWidth(getWidth() - 20);
        lbText.setFontScale(.75f);
        lbText.setWrap(true);
        lbText.setPosition(getWidth() / 2f - lbText.getWidth() / 2f, 140);

        btFacebookLogin = new TextButton("", new TextButtonStyle(Assets.btFacebookText, null, null, Assets.fontChico));
        screen.addEfectoPress(btFacebookLogin);
        btFacebookLogin.getLabel().setFontScale(.75f);



        addActor(lbText);
        addActor(btFacebookLogin);
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);

        String textButton = idiomas.get("login");

        btFacebookLogin.setText(textButton);
        btFacebookLogin.pack();
        btFacebookLogin.setPosition(getWidth() / 2f - btFacebookLogin.getWidth() / 2f, 35);
    }
}