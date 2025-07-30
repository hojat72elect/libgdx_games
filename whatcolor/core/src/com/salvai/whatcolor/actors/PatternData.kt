package com.salvai.whatcolor.actors

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.salvai.whatcolor.global.loadPatternData


class PatternData() {
    lateinit var patternType: String
    lateinit var colors: Array<Array<String>>

    constructor(file: FileHandle) : this() {
        val loadPatternData = loadPatternData(file)
        this.patternType = loadPatternData.patternType
        this.colors = loadPatternData.colors
    }
}
