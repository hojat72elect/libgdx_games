package com.gamestudio24.martianrun;

import com.badlogic.gdx.Game;
import com.gamestudio24.martianrun.screens.GameScreen;
import com.gamestudio24.martianrun.utils.AssetsManager;
import com.gamestudio24.martianrun.utils.AudioUtils;
import com.gamestudio24.martianrun.utils.GameEventListener;
import com.gamestudio24.martianrun.utils.GameManager;

public class MartianRun extends Game {

    public MartianRun(GameEventListener listener) {
        GameManager.getInstance().setGameEventListener(listener);
    }

    @Override
    public void create() {
        AssetsManager.loadAssets();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        AudioUtils.dispose();
        AssetsManager.dispose();
    }
}
