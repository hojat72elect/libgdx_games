package com.salvai.centrum.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.salvai.centrum.actors.player.Missile;
import com.salvai.centrum.enums.GameState;
import com.salvai.centrum.screens.GameScreen;
import com.salvai.centrum.screens.MenuScreen;
import com.salvai.centrum.utils.Constants;


public class GameInputProcessor extends InputAdapter {

    private GameScreen gameScreen;
    private Vector3 touchPos;

    public GameInputProcessor(GameScreen gameScreen) {
        super();
        this.gameScreen = gameScreen;
        touchPos = new Vector3();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (gameScreen.game.gameState == GameState.RUNNING)
                gameScreen.game.gameState = GameState.PAUSED;
            else {
                gameScreen.dispose();
                gameScreen.game.setScreen(new MenuScreen(gameScreen.game));
            }
        }
        return true;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        gameScreen.game.camera.unproject(touchPos, gameScreen.game.viewport.getScreenX(), gameScreen.game.viewport.getScreenY(), gameScreen.game.viewport.getScreenWidth(), gameScreen.game.viewport.getScreenHeight());
        if ( gameScreen.countdownTime == 0 && gameScreen.game.gameState == GameState.RUNNING && touchPos.x != Constants.SCREEN_WIDTH * 0.5f && touchPos.y != Constants.SCREEN_HEIGHT * 0.5f)
            gameScreen.gameFlowManager.missiles.add(new Missile(new Vector2(touchPos.x, touchPos.y), gameScreen.gameFlowManager.ballTexture));
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.game.gameState == GameState.PAUSED)
            gameScreen.game.gameState = GameState.RUNNING;
        return true;
    }
}
