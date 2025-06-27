package io.github.masamune.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.GdxRuntimeException
import io.github.masamune.component.Animation.Companion.DEFAULT_FRAME_DURATION
import io.github.masamune.component.FacingDirection
import io.github.masamune.component.GdxAnimation
import io.github.masamune.tiledmap.AnimationType
import ktx.app.gdxError
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.logger
import com.badlogic.gdx.utils.StringBuilder as GdxStringBuilder

/**
 * A [TextureAtlas] implementation that caches the regions of [findRegions][TextureAtlas.findRegions] calls.
 * Also, supports the creation of [GdxAnimation] instances that are cached as well.
 * This atlas can be identified by its unique [type][AtlasAsset].
 */
data class CachingAtlas(
    // type to identify the atlas (mainly used for logging)
    val type: AtlasAsset,
    // the underlying texture atlas instance
    private val textureAtlas: TextureAtlas,
) : Disposable {

    private val regionCache = mutableMapOf<String, GdxArray<TextureAtlas.AtlasRegion>>()
    private val animationCache = mutableMapOf<GdxStringBuilder, GdxAnimation>()
    private val stringBuilder = GdxStringBuilder()

    /**
     * Returns all regions with the specified [name]. If the regions
     * are not part of the cache, then they are retrieved from the underlying
     * [TextureAtlas] first.
     *
     * @throws [GdxRuntimeException] if there are no regions for the given [name].
     */
    fun findRegions(name: String, errorOnMissingRegions: Boolean = true): GdxArray<TextureAtlas.AtlasRegion> {
        return regionCache.getOrPut(name) {
            val regions = textureAtlas.findRegions(name)
            if (regions.isEmpty && errorOnMissingRegions) {
                gdxError("There are no regions with name '$name' in atlas '$type'")
            }
            regions
        }
    }

    /**
     * Returns a single region with the specified [name]. If the region
     * is not part of the cache, then it is retrieved from the underlying
     * [TextureAtlas] first.
     *
     * @throws [GdxRuntimeException] if there is no region for the given [name].
     */
    fun findRegion(name: String): TextureAtlas.AtlasRegion {
        return regionCache.getOrPut(name) {
            val region = textureAtlas.findRegion(name)
                ?: gdxError("There is no region with name '$name' in atlas '$type'")
            gdxArrayOf(region)
        }.single()
    }

    /**
     * Returns a [GdxAnimation] with the specified [atlasMainKey], [animationType] and optional [direction].
     * The [TextureAtlas] must contain regions in the format `MAIN_KEY/TYPE` or `MAIN_KEY/TYPE_DIRECTION`.
     * If the animation is not part of the cache, then it is created and cached first.
     *
     * @throws [GdxRuntimeException] if there are no regions with the special animation format.
     */
    fun gdxAnimation(
        atlasMainKey: String,
        animationType: AnimationType,
        direction: FacingDirection = FacingDirection.UNDEFINED
    ): GdxAnimation {
        // create animation texture regions key in format "MAIN_KEY/TYPE_DIRECTION".
        stringBuilder.clear()
        stringBuilder.append(atlasMainKey)
            .append("/")
            .append(animationType.atlasKey)
        if (direction != FacingDirection.UNDEFINED) {
            stringBuilder.append("_")
                .append(direction.atlasKey)
        }

        return animationCache.getOrPut(stringBuilder) {
            val regionKey = stringBuilder.toString()
            var texRegions = findRegions(regionKey, errorOnMissingRegions = false)
            if (texRegions.isEmpty) {
                // try one last time without facing direction
                texRegions = findRegions(regionKey.substringBeforeLast("_"))
                if (texRegions.isEmpty) {
                    gdxError("No regions in atlas $type for key $stringBuilder")
                }
            }

            GdxAnimation(DEFAULT_FRAME_DURATION, texRegions, atlasMainKey, animationType, direction)
        }
    }

    /**
     * Disposes the underlying [TextureAtlas].
     */
    override fun dispose() {
        log.debug { "Disposing caching atlas $type with ${regionCache.size} cached regions and ${animationCache.size} cached animations" }
        textureAtlas.dispose()
    }

    companion object {
        private val log = logger<CachingAtlas>()
    }
}

/**
 * [AssetLoaderParameters] for a [CachingAtlasLoader]. Has a mandatory [type][AtlasAsset] parameter
 * that is passed to the [CachingAtlas] instance for unique identification.
 */
class CachingAtlasParameter(
    val type: AtlasAsset,
    val flip: Boolean = false,
) : AssetLoaderParameters<CachingAtlas>()

/**
 * A [TextureAtlasLoader] for a [CachingAtlas]. It has a mandatory [type][AtlasAsset] parameter.
 */
class CachingAtlasLoader(
    fileHandleResolver: FileHandleResolver
) : SynchronousAssetLoader<CachingAtlas, CachingAtlasParameter>(fileHandleResolver) {

    private val textureAtlasLoader = TextureAtlasLoader(fileHandleResolver)

    override fun getDependencies(
        fileName: String,
        file: FileHandle,
        parameter: CachingAtlasParameter
    ): Array<AssetDescriptor<Any>> {
        return textureAtlasLoader.getDependencies(fileName, file, TextureAtlasParameter(parameter.flip))
    }

    override fun load(
        assetManager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: CachingAtlasParameter
    ): CachingAtlas {
        val atlas = textureAtlasLoader.load(assetManager, fileName, file, TextureAtlasParameter(parameter.flip))
        return CachingAtlas(parameter.type, atlas)
    }
}
