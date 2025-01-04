package com.nopalsoft.impossibledial.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.game.GameScreen;
import com.nopalsoft.impossibledial.screens.MainMenuScreen;


public class VentanaDificultad extends Ventana {
    static final float WIDTH = 300;
    static final float HEIGHT = 225;

    TextButton btHard, btEasy;

    public VentanaDificultad(final MainMenuScreen currentScreen) {
        super(currentScreen, WIDTH, HEIGHT, 300);
        setCloseButton(270, 190, 50);


        btEasy = new TextButton(Assets.idiomas.get("easy"), new TextButtonStyle(Assets.btEnabled, null, null, Assets.fontChico));
        btEasy.setSize(200, 50);
        screen.addEfectoPress(btEasy);
        btEasy.setPosition(getWidth() / 2f - btEasy.getWidth() / 2f, 125);
        btEasy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentScreen.changeScreenWithFadeOut(GameScreen.class, game, GameScreen.DIFICULTAD_EASY);
                hide();
            }
        });

        btHard = new TextButton(Assets.idiomas.get("hard"), new TextButtonStyle(Assets.btEnabled, null, null, Assets.fontChico));
        btHard.setSize(200, 50);
        screen.addEfectoPress(btHard);
        btHard.setPosition(getWidth() / 2f - btHard.getWidth() / 2f, 40);
        btHard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentScreen.changeScreenWithFadeOut(GameScreen.class, game, GameScreen.DIFICULTAD_HARD);
                hide();
            }
        });

        addActor(btHard);
        addActor(btEasy);

    }


}