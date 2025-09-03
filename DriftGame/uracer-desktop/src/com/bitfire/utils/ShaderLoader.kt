package com.bitfire.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import kotlin.system.exitProcess

object ShaderLoader {
    var BasePath = ""
    var Pedantic = true

    fun fromFile(vertexFileName: String, fragmentFileName: String) = fromFile(vertexFileName, fragmentFileName, "")

    fun fromFile(vertexFileName: String, fragmentFileName: String, defines: String): ShaderProgram {
        var log = "\"$vertexFileName/$fragmentFileName\""
        if (defines.isNotEmpty()) log += " w/ (" + defines.replace("\n", ", ") + ")"
        log += "..."
        Gdx.app.log("ShaderLoader", "Compiling $log")

        val vpSrc = Gdx.files.internal("$BasePath$vertexFileName.vertex").readString()
        val fpSrc = Gdx.files.internal("$BasePath$fragmentFileName.fragment").readString()

        return fromString(vpSrc, fpSrc, defines)
    }

    fun fromString(vertex: String, fragment: String) = fromString(vertex, fragment, "")

    fun fromString(vertex: String, fragment: String, defines: String): ShaderProgram {
        ShaderProgram.pedantic = Pedantic
        val shader = ShaderProgram(defines + "\n" + vertex, defines + "\n" + fragment)

        if (shader.isCompiled.not()) {
            Gdx.app.error("ShaderLoader", shader.log)
            exitProcess(-1)
        }

        return shader
    }
}
