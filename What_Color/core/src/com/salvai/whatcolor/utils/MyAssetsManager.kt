package com.salvai.whatcolor.utils

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.I18NBundle
import com.salvai.whatcolor.actors.PatternData
import com.salvai.whatcolor.global.BLOCK_IMAGE
import com.salvai.whatcolor.global.PATTERN_PREFIX
import com.salvai.whatcolor.global.PATTERN_SIZE
import com.salvai.whatcolor.global.SECTION_SIZE
import com.salvai.whatcolor.global.SKIN_ATLAS_NAME
import com.salvai.whatcolor.global.SKIN_NAME
import com.salvai.whatcolor.global.SPLASH_IMAGE


class MyAssetsManager : Disposable {

    val manager: AssetManager = AssetManager().apply {
        this.setLoader(
            PatternData::class.java,
            PatternDataLoader(
                InternalFileHandleResolver()
            )
        )
    }

    fun loadImages() {
        manager.load(BLOCK_IMAGE, Texture::class.java)
    }

    fun loadBundle() {
        manager.load("i18n/words", I18NBundle::class.java)
    }

    fun loadSkin() {
        manager.load(SKIN_NAME, Skin::class.java, SkinParameter(SKIN_ATLAS_NAME))
    }

    fun loadPatternData() {
        for (i in PATTERN_PREFIX)
            for (j in 1..PATTERN_SIZE / SECTION_SIZE)
                manager.load("levels/${i}x$i/${i}x$i-$j.json", PatternData::class.java)
    }


    fun loadSplashScreen() {
        manager.load(SPLASH_IMAGE, Texture::class.java)
        manager.finishLoading()
    }

    override fun dispose() {
        manager.dispose()
    }

}