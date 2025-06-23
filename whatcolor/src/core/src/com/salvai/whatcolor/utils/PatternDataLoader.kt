package com.salvai.whatcolor.utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.salvai.whatcolor.actors.PatternData

class PatternDataLoader(resolver: FileHandleResolver?) : AsynchronousAssetLoader<PatternData?, PatternDataLoader.PatternDataParameter?>(resolver) {
    var patternData: PatternData? = null
    override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: PatternDataParameter?) {
        patternData = null
        patternData = file?.let { PatternData(it) }
    }

    override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: PatternDataParameter?): PatternData? {
        val patternData: PatternData? = this.patternData
        this.patternData = null
        return patternData
    }


    class PatternDataParameter : AssetLoaderParameters<PatternData?>()

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: PatternDataParameter?): Array<AssetDescriptor<Any>>? {
        return null
    }


}