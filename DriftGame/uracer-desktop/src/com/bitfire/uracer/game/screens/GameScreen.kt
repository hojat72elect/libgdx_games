package com.bitfire.uracer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.UserPreferences
import com.bitfire.uracer.configuration.UserPreferences.Preference
import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.Game
import com.bitfire.uracer.game.GameLevels
import com.bitfire.uracer.game.screens.GameScreensFactory.ScreenType
import com.bitfire.uracer.screen.Screen

class GameScreen : Screen() {

    private var game: Game? = null
    private var gameui: GameScreenUI? = null
    private var initialized = false

    override fun init(): Boolean {
        if (GameLevels.levelIdExists(ScreensShared.selectedLevelId)) {
            UserPreferences.string(Preference.LastPlayedTrack, ScreensShared.selectedLevelId)
            val userProfile = UserProfile()
            game = Game(userProfile, ScreensShared.selectedLevelId)
            gameui = GameScreenUI(game)
            game!!.start()
            initialized = true
            return true
        } else {
            // last saved level doesn't exist, so reset it
            ScreensShared.selectedLevelId = ""
            UserPreferences.string(Preference.LastPlayedTrack, "")
            UserPreferences.save()

            Gdx.app.error("GameScreen", "The specified track could not be found.")

            URacer.Game.show(ScreenType.MainScreen)
            initialized = false
            return false
        }
    }

    override fun dispose() {
        if (!initialized) return

        game!!.dispose()
        gameui!!.dispose()

        game = null
        gameui = null
    }

    override fun tick() {
        if (!initialized) return

        gameui!!.tick()
        if (!game!!.isPaused) game!!.tick()
    }

    override fun tickCompleted() {
        if (!initialized) return

        game!!.tickCompleted()
    }

    override fun pause() {
        if (!initialized) return

        game!!.pause()
    }

    override fun resume() {
        if (!initialized) return

        game!!.resume()
    }

    override fun render(destinationBuffer: FrameBuffer?) {
        if (!initialized) return

        game!!.render(destinationBuffer)
        gameui!!.render(destinationBuffer)
    }
}
