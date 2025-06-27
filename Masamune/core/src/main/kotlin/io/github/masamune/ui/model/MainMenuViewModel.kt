package io.github.masamune.ui.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.I18NBundle
import io.github.masamune.Masamune
import io.github.masamune.audio.AudioService
import io.github.masamune.event.Event
import io.github.masamune.screen.CutSceneScreen
import io.github.masamune.screen.DefaultTransitionType
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.screen.GameScreen
import java.util.*

class MainMenuViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val masamune: Masamune,
) : ViewModel(bundle, audioService) {

    var musicVolume: Float
        get() = audioService.musicVolume
        set(value) {
            audioService.musicVolume = value
        }

    var soundVolume: Float
        get() = audioService.soundVolume
        set(value) {
            audioService.soundVolume = value
        }

    override fun onEvent(event: Event) = Unit

    fun startGame() {
        if (masamune.hasScreenTransition()) {
            return
        }

        playSndMenuAccept()
        masamune.getScreen<GameScreen>().clearGameState()
        masamune.transitionScreen<CutSceneScreen>(
            FadeTransitionType(1f, 0f, 3f, Interpolation.fastSlow),
            FadeTransitionType(0f, 1f, 2f, Interpolation.slowFast, delayInSeconds = 1f),
        ) {
            it.startCutScene("intro")
        }
    }

    fun continueGame() {
        if (masamune.hasScreenTransition()) {
            return
        }

        playSndMenuAccept()
        masamune.getScreen<GameScreen>().clearGameState()
        masamune.transitionScreen<GameScreen>(
            FadeTransitionType(1f, 0f, 3f, Interpolation.fastSlow),
            DefaultTransitionType,
        ) {
            it.loadSaveState()
        }
    }

    fun quitGame() {
        Gdx.app.exit()
    }

    fun hasNoSaveState(): Boolean = masamune.save.hasNoSaveState()

    fun hasSaveState(): Boolean = masamune.save.hasSaveState()

    fun language(): String = masamune.save.loadLocale().language

    fun updateLanguage(locale: Locale) {
        masamune.save.saveLocale(locale)
    }

}
