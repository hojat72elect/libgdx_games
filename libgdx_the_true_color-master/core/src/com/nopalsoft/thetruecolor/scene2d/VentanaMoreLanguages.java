package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.screens.Screens;

public class VentanaMoreLanguages extends Ventana {
    static final float WIDTH = 440;
    static final float HEIGHT = 250;

    Label lbText;
    TextButton btTransalate;

    public VentanaMoreLanguages(Screens currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 300);

        setCloseButton(400, 210, 50);

        lbText = new Label(Assets.idiomas.get("translateDescription"), new Label.LabelStyle(Assets.fontChico, Color.BLACK));
        lbText.setWidth(getWidth() - 20);
        lbText.setFontScale(.75f);
        lbText.setWrap(true);
        lbText.setPosition(getWidth() / 2f - lbText.getWidth() / 2f, getHeight() / 2f - lbText.getHeight() / 2f + 30);

        btTransalate = new TextButton(Assets.idiomas.get("translate"), Assets.txtButtonStyle);
        screen.addEfectoPress(btTransalate);
        btTransalate.getLabel().setFontScale(.75f);

        btTransalate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btTransalate.setChecked(false);
                Gdx.net.openURI("https://webtranslateit.com/en/projects/10553-The-true-color/invitation_request");
                hide();
            }
        });


        btTransalate.pack();
        btTransalate.setPosition(getWidth() / 2f - btTransalate.getWidth() / 2f, 35);

        addActor(lbText);
        addActor(btTransalate);

    }
}
