package com.nopalsoft.ninjarunner.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.nopalsoft.ninjarunner.game.GameScreen;


public class SettingsScreen extends Screens {

    public SettingsScreen(Game game) {
        super(game);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return super.keyUp(keycode);
    }


}
