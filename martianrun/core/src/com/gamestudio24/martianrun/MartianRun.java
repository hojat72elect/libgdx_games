package com.gamestudio24.martianrun;

import com.badlogic.gdx.Game;
import com.gamestudio24.martianrun.screens.GameScreen;
import com.gamestudio24.martianrun.utils.AssetsManager;
import com.gamestudio24.martianrun.utils.AudioUtils;

public class MartianRun extends Game {

    public MartianRun() {

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
