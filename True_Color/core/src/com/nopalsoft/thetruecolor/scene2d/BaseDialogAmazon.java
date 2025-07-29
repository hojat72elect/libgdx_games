package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.screens.Screens;

public class BaseDialogAmazon extends BaseDialog {
    static final float WIDTH = 440;
    static final float HEIGHT = 250;

    Label lbText;
    TextButton btAmazonLogin;

    public BaseDialogAmazon(Screens currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 300);

        setCloseButton(400, 210, 50);

        lbText = new Label(idiomas.get("loginToGoogle").replace("Google", "Amazon"), new LabelStyle(Assets.fontSmall, Color.BLACK));
        lbText.setWidth(getWidth() - 20);
        lbText.setFontScale(.75f);
        lbText.setWrap(true);
        lbText.setPosition(getWidth() / 2f - lbText.getWidth() / 2f, 165);

        btAmazonLogin = new TextButton("", new TextButtonStyle(Assets.buttonPlayDrawable, null, null, Assets.fontSmall));
        screen.addPressEffect(btAmazonLogin);
        btAmazonLogin.getLabel().setFontScale(.75f);

        btAmazonLogin.addListener(new ClickListener() {

        });

        addActor(lbText);
        addActor(btAmazonLogin);
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);

        String textButton = idiomas.get("login");

        btAmazonLogin.setText(textButton);
        btAmazonLogin.pack();
        btAmazonLogin.setPosition(getWidth() / 2f - btAmazonLogin.getWidth() / 2f, 35);
    }
}