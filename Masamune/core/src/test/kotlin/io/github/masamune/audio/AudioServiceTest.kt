package io.github.masamune.audio

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.SoundAsset
import io.github.masamune.gdxTest
import ktx.app.KtxApplicationAdapter

/**
 * Test for [AudioService].
 * Press '1' and '2' to play different music.
 * Press '3', '4' and '5' to play the same sound effect with different pitch.
 * Press '6' to pause any audio.
 * Press '7' to resume any audio.
 */

fun main() = gdxTest("Audio Test, 1-7 to play/stop music/sound", AudioTest())

private class AudioTest : KtxApplicationAdapter {
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val audioService by lazy { AudioService(assetService) }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        audioService.musicVolume = 0.5f
        audioService.soundVolume = 0.5f
    }

    override fun render() {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) -> audioService.play(MusicAsset.VILLAGE, true)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) -> audioService.play(MusicAsset.ROAD, false)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) -> audioService.play(SoundAsset.MENU_CLICK)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) -> audioService.play(SoundAsset.MENU_CLICK, 0.5f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) -> audioService.play(SoundAsset.MENU_CLICK, 2f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) -> audioService.pause()
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_7) -> audioService.resume()
        }
    }

    override fun dispose() {
        assetService.dispose()
    }
}

