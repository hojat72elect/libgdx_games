package com.salvai.whatcolor.global

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.salvai.whatcolor.actors.Pattern
import com.salvai.whatcolor.actors.PatternData
import com.salvai.whatcolor.actors.SinglePatternPart
import com.salvai.whatcolor.enums.PatternType
import com.salvai.whatcolor.utils.MyAssetsManager


val json: Json = Json()


fun loadPatternData(fileHandle: FileHandle): PatternData {
    return json.fromJson(PatternData::class.java, fileHandle)
}

fun getAllPaterns(myAssetsManager: MyAssetsManager): Array<PatternData> {
    val patternDatas = Array<PatternData>()
    for (i in PATTERN_PREFIX)
        for (j in 1..PATTERN_SIZE / SECTION_SIZE)
            patternDatas.add(myAssetsManager.manager.get("levels/${i}x$i/${i}x$i-$j.json", PatternData::class.java))
    return patternDatas
}


fun createPattern(patternData: PatternData, texture: Texture): Pattern {
    val singlePatternParts: Array<SinglePatternPart> = Array()
    for (color in patternData.colors)
        for (code in color)
            singlePatternParts.add(SinglePatternPart(texture, Color.valueOf(code.toString()), PatternType.valueOf(patternData.patternType)))
    return Pattern(singlePatternParts, PatternType.valueOf(patternData.patternType))
}

fun createAllPatterns(patternDatas: Array<PatternData>, texture: Texture): Array<Pattern> {
    val patterns = Array<Pattern>()
    for (patternData in patternDatas)
        patterns.add(createPattern(patternData, texture))
    return patterns
}


fun getColoredDrawable(width: Int, height: Int, color: Color?): Drawable? {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()
    val drawable = TextureRegionDrawable(TextureRegion(Texture(pixmap)))
    pixmap.dispose()
    return drawable
}
