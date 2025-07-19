package shaders

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object LightShader {
    @JvmStatic
    fun createLightShader(): ShaderProgram {
        var gamma = ""
        if (RayHandler.getGammaCorrection()) gamma = "sqrt"

        val vertexShader =
            ("attribute vec4 vertex_positions;\n"
                    + "attribute vec4 quad_colors;\n"
                    + "attribute float s;\n"
                    + "uniform mat4 u_projTrans;\n"
                    + "varying vec4 v_color;\n"
                    + "void main()\n"
                    + "{\n"
                    + "   v_color = s * quad_colors;\n"
                    + "   gl_Position =  u_projTrans * vertex_positions;\n"
                    + "}\n")
        val fragmentShader = ("#ifdef GL_ES\n"
                + "precision lowp float;\n"
                + "#define MED mediump\n"
                + "#else\n"
                + "#define MED \n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "void main()\n"
                + "{\n"
                + "  gl_FragColor = " + gamma + "(v_color);\n"
                + "}")

        ShaderProgram.pedantic = false
        var lightShader = ShaderProgram(
            vertexShader,
            fragmentShader
        )
        if (!lightShader.isCompiled) {
            lightShader = ShaderProgram(
                "#version 330 core\n$vertexShader",
                "#version 330 core\n$fragmentShader"
            )
            if (!lightShader.isCompiled) {
                Gdx.app.log("ERROR", lightShader.log)
            }
        }

        return lightShader
    }
}
