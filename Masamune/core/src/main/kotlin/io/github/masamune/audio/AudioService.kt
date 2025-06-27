package io.github.masamune.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.SoundAsset
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.MapChangeEvent
import ktx.app.gdxError
import ktx.log.logger
import ktx.tiled.propertyOrNull

class AudioService(
    val assetService: AssetService,
) : EventListener {

    private var prevMusic: MusicAsset? = null
    private var lastMusic: Pair<Music, MusicAsset>? = null
    private val soundCache = mutableMapOf<SoundAsset, Sound>()

    val currentMusic: Music
        get() = lastMusic?.first ?: gdxError("There is no music currently playing")

    var musicVolume: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            lastMusic?.first?.volume = field
        }

    var soundVolume: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
        }

    fun play(musicAsset: MusicAsset, loop: Boolean = true, keepPrevious: Boolean = false) {
        // stop previous music instance if there is any and unload it
        lastMusic?.let { (prevMusic, prevMusicAsset) ->
            if (prevMusicAsset == musicAsset) {
                // music is currently playing -> ignore this call
                return
            }

            log.debug { "Unloading previous music $prevMusicAsset" }
            prevMusic.stop()
            assetService.unload(prevMusicAsset)
        }
        if (!keepPrevious) {
            prevMusic = lastMusic?.second
        }

        // load new music asset and finish unloading/loading process
        assetService.load(musicAsset)
        assetService.finishLoading()

        // play new music
        log.debug { "Playing new music $musicAsset" }
        with(assetService[musicAsset]) {
            play()
            isLooping = loop
            volume = musicVolume
            // remember music asset and music instance for unloading/stopping later on
            lastMusic = this to musicAsset
        }
    }

    fun playPrevMusic() {
        prevMusic?.let { play(it) }
    }

    fun stopMusic() {
        lastMusic?.let { (prevMusic, prevMusicAsset) ->
            log.debug { "Unloading previous music $prevMusicAsset" }
            prevMusic.stop()
            assetService.unload(prevMusicAsset)
            lastMusic = null
        }
    }

    fun play(soundAsset: SoundAsset, pitch: Float = 1f) {
        if (soundCache.size > 100) {
            // cache is to big -> unload current sound instances
            log.info { "Sound cache exceeds size of 100 -> clearing it" }
            soundCache.keys.forEach(assetService::unload)
            assetService.finishLoading()
            soundCache.clear()
        }

        if (soundAsset !in soundCache) {
            // load sound instance into sound cache
            log.debug { "Adding $soundAsset to sound cache" }
            assetService.load(soundAsset)
            assetService.finishLoading()
            soundCache[soundAsset] = assetService[soundAsset]
            log.debug { "${soundCache.size} sound entries in sound cache" }
        }

        soundCache[soundAsset]?.play(soundVolume, pitch.coerceIn(0.5f, 2f), 0f)
    }

    fun pause() {
        soundCache.values.forEach { it.pause() }
        lastMusic?.first?.pause()
    }

    fun resume() {
        soundCache.values.forEach { it.resume() }
        lastMusic?.first?.play()
    }

    override fun onEvent(event: Event) {
        when {
            event is MapChangeEvent && !event.ignoreTrigger -> {
                event.tiledMap.propertyOrNull<String>("music")?.let { musicAssetStr ->
                    val musicAsset = MusicAsset.valueOf(musicAssetStr)
                    play(musicAsset, true)
                }
            }

            else -> Unit
        }
    }

    override fun toString(): String {
        return "AudioService(musicVolume=$musicVolume, soundVolume=$soundVolume, lastMusic=$lastMusic)"
    }

    companion object {
        private val log = logger<AudioService>()
    }

}
