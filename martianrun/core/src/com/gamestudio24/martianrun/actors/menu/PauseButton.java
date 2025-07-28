
package com.gamestudio24.martianrun.actors.menu;

import com.badlogic.gdx.math.Rectangle;
import com.gamestudio24.martianrun.enums.GameState;
import com.gamestudio24.martianrun.utils.Constants;
import com.gamestudio24.martianrun.utils.GameManager;

public class PauseButton extends GameButton {

    public interface PauseButtonListener {
        void onPause();

        void onResume();
    }

    private final PauseButtonListener listener;

    public PauseButton(Rectangle bounds, PauseButtonListener listener) {
        super(bounds);
        this.listener = listener;
    }

    @Override
    protected String getRegionName() {
        return GameManager.getInstance().getGameState() == GameState.PAUSED ? Constants.PLAY_REGION_NAME : Constants.PAUSE_REGION_NAME;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (GameManager.getInstance().getGameState() == GameState.OVER) {
            remove();
        }
    }

    @Override
    public void touched() {
        if (GameManager.getInstance().getGameState() == GameState.PAUSED) {
            listener.onResume();
        } else {
            listener.onPause();
        }
    }

}
