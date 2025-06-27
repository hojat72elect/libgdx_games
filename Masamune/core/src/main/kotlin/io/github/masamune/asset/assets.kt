package io.github.masamune.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.I18NBundle
import com.ray3k.stripe.FreeTypeSkinLoader
import ktx.assets.getAsset
import ktx.assets.load
import ktx.log.logger
import java.util.*

/**
 * Asset type for [TextureAtlas] instances. Used by [AssetService].
 */
enum class AtlasAsset(folder: String = "graphics") {
    CHARS_AND_PROPS,
    SFX,
    SKIN("ui");

    val path = "$folder/${name.lowercase()}.atlas"
}

/**
 * Asset type for [TiledMap] instances. Used by [AssetService].
 */
enum class TiledMapAsset {
    VILLAGE,
    PATH_TO_FOREST,
    FOREST_ENTRANCE,
    FOREST_MASAMUNE,
    MENU,
    ;

    val path = "maps/${name.lowercase()}.tmx"
}

/**
 * Asset type for [Skin] instances. Used by [AssetService].
 */
enum class SkinAsset(skinName: String, atlas: AtlasAsset) {
    DEFAULT("skin", AtlasAsset.SKIN);

    val path = "ui/$skinName.json"
    val atlasPath = atlas.path
}

/**
 * Asset type for [I18NBundle] instances. Used by [AssetService].
 */
enum class I18NAsset {
    MESSAGES;

    val path = "ui/${name.lowercase()}"
}

/**
 * Asset type for [Music] instances. Used by [AssetService].
 */
enum class MusicAsset(format: String) {
    VILLAGE("mp3"),
    ROAD("mp3"),
    COMBAT1("mp3"),
    COMBAT2("mp3"),
    COMBAT_VICTORY("mp3"),
    COMBAT_DEFEAT("mp3"),
    FOREST("mp3"),
    INTRO("mp3"),
    ;

    val path = "music/${name.lowercase()}.$format"
}

/**
 * Asset type for [Sound] instances. Used by [AssetService].
 */
enum class SoundAsset {
    MENU_ACCEPT,
    MENU_CLICK,
    MENU_ABORT,
    ATTACK_SWIPE,
    SWORD_SWIPE,
    EXPLOSION1,
    HEAL1,
    ATTACK_MISS,
    QUEST_ITEM,
    DEMON_TELEPORT,
    SLOW,
    DEMI1,
    CONSUME,
    POISON1,
    ;

    val path = "sound/${name.lowercase()}.wav"
}

/**
 * Service class for asset management. It supports loading and unloading of:
 * - [AtlasAsset]
 * - [TiledMapAsset]
 * - [SkinAsset]
 * - [I18NAsset]
 * - [MusicAsset]
 * - [SoundAsset]
 *
 * Loading is done asynchronously. Call [finishLoading] to immediately load any queued assets.
 */
class AssetService(fileHandleResolver: FileHandleResolver = InternalFileHandleResolver()) : Disposable {

    private val manager = AssetManager(fileHandleResolver).apply {
        setLoader(TiledMap::class.java, TmxMapLoader(this.fileHandleResolver))
        setLoader(CachingAtlas::class.java, CachingAtlasLoader(this.fileHandleResolver))
        setLoader(Skin::class.java, FreeTypeSkinLoader(this.fileHandleResolver))
    }

    /**
     * Queues an [AtlasAsset] for loading.
     */
    fun load(asset: AtlasAsset) {
        manager.load<CachingAtlas>(asset.path, CachingAtlasParameter(asset))
    }

    /**
     * Gets a loaded [AtlasAsset] as [TextureAtlas].
     */
    operator fun get(asset: AtlasAsset): CachingAtlas = manager.getAsset<CachingAtlas>(asset.path)

    /**
     * Queues a [TiledMapAsset] for loading.
     */
    fun load(asset: TiledMapAsset) {
        manager.load<TiledMap>(asset.path, TmxMapLoader.Parameters().apply {
            projectFilePath = "maps/masamune-tiled.tiled-project"
        })
    }

    /**
     * Queues a [TiledMapAsset] for unloading.
     */
    fun unload(asset: TiledMapAsset) {
        manager.unload(asset.path)
    }

    /**
     * Gets a loaded [TiledMapAsset] as [TiledMap].
     */
    operator fun get(asset: TiledMapAsset): TiledMap = manager.getAsset<TiledMap>(asset.path)

    /**
     * Queues a [SkinAsset] for loading.
     */
    fun load(asset: SkinAsset) {
        manager.load<Skin>(asset.path, SkinParameter(asset.atlasPath))
    }

    /**
     * Gets a loaded [SkinAsset] as [Skin].
     */
    operator fun get(asset: SkinAsset): Skin = manager.getAsset<Skin>(asset.path)

    /**
     * Queues a [I18NAsset] for loading.
     */
    fun load(asset: I18NAsset, locale: Locale) {
        manager.load<I18NBundle>(asset.path, I18NBundleParameter(locale, Charsets.ISO_8859_1.name()))
    }

    /**
     * Gets a loaded [I18NAsset] as [I18NBundle].
     */
    operator fun get(asset: I18NAsset): I18NBundle = manager.getAsset<I18NBundle>(asset.path)

    /**
     * Queues a [MusicAsset] for loading.
     */
    fun load(asset: MusicAsset) {
        manager.load<Music>(asset.path)
    }

    /**
     * Queues a [MusicAsset] for unloading.
     */
    fun unload(asset: MusicAsset) {
        manager.unload(asset.path)
    }

    /**
     * Gets a loaded [MusicAsset] as [Music].
     */
    operator fun get(asset: MusicAsset): Music = manager.getAsset<Music>(asset.path)

    /**
     * Queues a [SoundAsset] for loading.
     */
    fun load(asset: SoundAsset) {
        manager.load<Sound>(asset.path)
    }

    /**
     * Queues a [SoundAsset] for unloading.
     */
    fun unload(asset: SoundAsset) {
        manager.unload(asset.path)
    }

    /**
     * Gets a loaded [SoundAsset] as [Sound].
     */
    operator fun get(asset: SoundAsset): Sound = manager.getAsset<Sound>(asset.path)

    /**
     * Updates the loading process of any queued asset.
     */
    fun update(): Boolean = manager.update(1 / 60 * 1000)

    /**
     * Returns the loading progress in percent of completion.
     */
    fun progress(): Float = manager.progress

    /**
     * Immediately finishes loading of any queued asset.
     */
    fun finishLoading() = manager.finishLoading()

    /**
     * Disposes all assets and stop all loading processes.
     */
    override fun dispose() {
        log.debug { "Disposing AssetService:\n${manager.diagnostics}" }
        manager.dispose()
    }

    companion object {
        private val log = logger<AssetService>()
    }
}
