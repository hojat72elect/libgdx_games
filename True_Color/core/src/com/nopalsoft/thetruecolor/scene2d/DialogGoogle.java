package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.screens.BaseScreen;

public class DialogGoogle extends BaseDialog {
    static final float WIDTH = 440;
    static final float HEIGHT = 250;

    Label labelText;
    TextButton buttonGoogleLogin;

    public DialogGoogle(BaseScreen currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 300);

        setCloseButton(210);

        labelText = new Label(languages.get("loginToGoogle"), new LabelStyle(Assets.fontSmall, Color.BLACK));
        labelText.setWidth(getWidth() - 20);
        labelText.setFontScale(.75f);
        labelText.setWrap(true);
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() / 2f, 140);

        buttonGoogleLogin = new TextButton("", new TextButtonStyle(Assets.buttonGoogleTextDrawable, null, null, Assets.fontSmall));
        screen.addPressEffect(buttonGoogleLogin);
        buttonGoogleLogin.getLabel().setFontScale(.75f);

        addActor(labelText);
        addActor(buttonGoogleLogin);
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);

        String textButton = languages.get("login");

        buttonGoogleLogin.setText(textButton);
        buttonGoogleLogin.pack();
        buttonGoogleLogin.setPosition(getWidth() / 2f - buttonGoogleLogin.getWidth() / 2f, 35);
    }
}