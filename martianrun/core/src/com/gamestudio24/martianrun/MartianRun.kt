package com.gamestudio24.martianrun

import com.badlogic.gdx.Game
import com.gamestudio24.martianrun.screens.GameScreen
import com.gamestudio24.martianrun.utils.AssetsManager
import com.gamestudio24.martianrun.utils.AudioUtils

class MartianRun : Game() {
    override fun create() {
        AssetsManager.loadAssets()
        setScreen(GameScreen())
    }

    override fun dispose() {
        super.dispose()
        AudioUtils.dispose()
        AssetsManager.dispose()
    }
}
