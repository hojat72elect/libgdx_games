package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter

object AssetsManager {
    private val texturesMap = HashMap<String?, TextureRegion?>()
    private val animationsMap = HashMap<String?, Animation?>()
    private var textureAtlas: TextureAtlas? = null
    private var smallFont: BitmapFont? = null
    private var smallestFont: BitmapFont? = null
    private var largeFont: BitmapFont? = null

    fun loadAssets() {
        // Background

        texturesMap.put(
            Constants.BACKGROUND_ASSETS_ID,
            TextureRegion(Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH)))
        )

        // Ground
        texturesMap.put(
            Constants.GROUND_ASSETS_ID,
            TextureRegion(Texture(Gdx.files.internal(Constants.GROUND_IMAGE_PATH)))
        )

        textureAtlas = TextureAtlas(Constants.SPRITES_ATLAS_PATH)

        // Runner
        texturesMap.put(
            Constants.RUNNER_JUMPING_ASSETS_ID,
            textureAtlas!!.findRegion(Constants.RUNNER_JUMPING_REGION_NAME)
        )
        texturesMap.put(
            Constants.RUNNER_DODGING_ASSETS_ID,
            textureAtlas!!.findRegion(Constants.RUNNER_DODGING_REGION_NAME)
        )
        texturesMap.put(
            Constants.RUNNER_HIT_ASSETS_ID,
            textureAtlas!!.findRegion(Constants.RUNNER_HIT_REGION_NAME)
        )
        animationsMap.put(
            Constants.RUNNER_RUNNING_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.RUNNER_RUNNING_REGION_NAMES
            )
        )

        // Enemies
        animationsMap.put(
            Constants.RUNNING_SMALL_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.RUNNING_SMALL_ENEMY_REGION_NAMES
            )
        )
        animationsMap.put(
            Constants.RUNNING_BIG_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.RUNNING_BIG_ENEMY_REGION_NAMES
            )
        )
        animationsMap.put(
            Constants.RUNNING_LONG_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.RUNNING_LONG_ENEMY_REGION_NAMES
            )
        )
        animationsMap.put(
            Constants.RUNNING_WIDE_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.RUNNING_WIDE_ENEMY_REGION_NAMES
            )
        )
        animationsMap.put(
            Constants.FLYING_SMALL_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.FLYING_SMALL_ENEMY_REGION_NAMES
            )
        )
        animationsMap.put(
            Constants.FLYING_WIDE_ENEMY_ASSETS_ID, createAnimation(
                textureAtlas!!,
                Constants.FLYING_WIDE_ENEMY_REGION_NAMES
            )
        )

        // Tutorial
        texturesMap.put(
            Constants.TUTORIAL_LEFT_REGION_NAME,
            textureAtlas!!.findRegion(Constants.TUTORIAL_LEFT_REGION_NAME)
        )
        texturesMap.put(
            Constants.TUTORIAL_RIGHT_REGION_NAME,
            textureAtlas!!.findRegion(Constants.TUTORIAL_RIGHT_REGION_NAME)
        )

        // Fonts
        val generator = FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT_NAME))
        val parameter = FreeTypeFontParameter()
        parameter.size = 36
        smallFont = generator.generateFont(parameter)
        smallFont!!.setColor(.21f, .22f, .21f, 1f)
        parameter.size = 72
        largeFont = generator.generateFont(parameter)
        largeFont!!.setColor(.21f, .22f, .21f, 1f)
        parameter.size = 24
        smallestFont = generator.generateFont(parameter)
        smallestFont!!.setColor(.21f, .22f, .21f, 1f)
        generator.dispose()
    }

    @JvmStatic
    fun getTextureRegion(key: String?): TextureRegion? {
        return texturesMap.get(key)
    }

    @JvmStatic
    fun getAnimation(key: String?): Animation? {
        return animationsMap.get(key)
    }

    private fun createAnimation(textureAtlas: TextureAtlas, regionNames: Array<String>): Animation {
        val runningFrames = arrayOfNulls<TextureRegion>(regionNames.size)

        for (i in regionNames.indices) {
            val path = regionNames[i]
            runningFrames[i] = textureAtlas.findRegion(path)
        }

        return Animation(0.1f, *runningFrames)
    }

    @JvmStatic
    fun getTextureAtlas(): TextureAtlas {
        return textureAtlas!!
    }

    @JvmStatic
    fun getSmallFont(): BitmapFont {
        return smallFont!!
    }

    @JvmStatic
    fun getLargeFont(): BitmapFont {
        return largeFont!!
    }

    @JvmStatic
    fun getSmallestFont(): BitmapFont {
        return smallestFont!!
    }

    fun dispose() {
        textureAtlas!!.dispose()
        smallestFont!!.dispose()
        smallFont!!.dispose()
        largeFont!!.dispose()
        texturesMap.clear()
        animationsMap.clear()
    }
}
