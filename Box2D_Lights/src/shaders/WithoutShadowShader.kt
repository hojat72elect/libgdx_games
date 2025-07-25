package shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object WithoutShadowShader {
    @JvmStatic
    fun createShadowShader(): ShaderProgram {
        val vertexShader = ("attribute vec4 a_position;\n"
                + "attribute vec2 a_texCoord;\n"
                + "varying vec2 v_texCoords;\n"
                + "\n"
                + "void main()\n"
                + "{\n"
                + "   v_texCoords = a_texCoord;\n"
                + "   gl_Position = a_position;\n"
                + "}\n")

        val fragmentShader = ("#ifdef GL_ES\n"
                + "precision lowp float;\n"
                + "#define MED mediump\n"
                + "#else\n"
                + "#define MED \n"
                + "#endif\n"
                + "varying MED vec2 v_texCoords;\n"
                + "uniform sampler2D u_texture;\n"
                + "void main()\n"
                + "{\n"
                + "gl_FragColor = texture2D(u_texture, v_texCoords);\n"
                + "}\n")
        ShaderProgram.pedantic = false
        var woShadowShader = ShaderProgram(
            vertexShader,
            fragmentShader
        )
        if (!woShadowShader.isCompiled) {
            woShadowShader = ShaderProgram(
                "#version 330 core\n$vertexShader",
                "#version 330 core\n$fragmentShader"
            )
            if (!woShadowShader.isCompiled) {
                Gdx.app.log("ERROR", woShadowShader.log)
            }
        }

        return woShadowShader
    }
}
