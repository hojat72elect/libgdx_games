package shaders

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object GaussianBlur {
    @JvmStatic
    fun createShader(width: Int, height: Int): ShaderProgram {

        val rgb = if (RayHandler.isDiffuseLight()) ".rgb" else ""
        val vertexShader = ("attribute vec4 a_position;\n"
                + "uniform vec2  dir;\n"
                + "attribute vec2 a_texCoord;\n"
                + "varying vec2 v_texCoords0;\n"
                + "varying vec2 v_texCoords1;\n"
                + "varying vec2 v_texCoords2;\n"
                + "varying vec2 v_texCoords3;\n"
                + "varying vec2 v_texCoords4;\n"
                + "#define FBO_W "
                + width.toString()
                + ".0\n"
                + "#define FBO_H "
                + height.toString()
                + ".0\n"
                + "const vec2 futher = vec2(3.2307692308 / FBO_W, 3.2307692308 / FBO_H );\n" //
                + "const vec2 closer = vec2(1.3846153846 / FBO_W, 1.3846153846 / FBO_H );\n" //
                + "void main()\n"
                + "{\n"
                + "vec2 f = futher * dir;\n"
                + "vec2 c = closer * dir;\n"
                + "v_texCoords0 = a_texCoord - f;\n"
                + "v_texCoords1 = a_texCoord - c;\n"
                + "v_texCoords2 = a_texCoord;\n"
                + "v_texCoords3 = a_texCoord + c;\n"
                + "v_texCoords4 = a_texCoord + f;\n"
                + "gl_Position = a_position;\n"
                + "}\n")
        val fragmentShader = ("#ifdef GL_ES\n"
                + "precision lowp float;\n"
                + "#define MED mediump\n"
                + "#else\n"
                + "#define MED \n"
                + "#endif\n"
                + "uniform sampler2D u_texture;\n"
                + "varying MED vec2 v_texCoords0;\n"
                + "varying MED vec2 v_texCoords1;\n"
                + "varying MED vec2 v_texCoords2;\n"
                + "varying MED vec2 v_texCoords3;\n"
                + "varying MED vec2 v_texCoords4;\n"
                + "const float center = 0.2270270270;\n"
                + "const float close  = 0.3162162162;\n"
                + "const float far    = 0.0702702703;\n"
                + "void main()\n"
                + "{	 \n"
                + "gl_FragColor" + rgb + " = far    * texture2D(u_texture, v_texCoords0)" + rgb + "\n"
                + "	      		+ close  * texture2D(u_texture, v_texCoords1)" + rgb + "\n"
                + "				+ center * texture2D(u_texture, v_texCoords2)" + rgb + "\n"
                + "				+ close  * texture2D(u_texture, v_texCoords3)" + rgb + "\n"
                + "				+ far    * texture2D(u_texture, v_texCoords4)" + rgb + ";\n"
                + "}\n")
        ShaderProgram.pedantic = false
        var blurShader = ShaderProgram(
            vertexShader,
            fragmentShader
        )
        if (!blurShader.isCompiled) {
            blurShader = ShaderProgram(
                "#version 330 core\n$vertexShader",
                "#version 330 core\n$fragmentShader"
            )
            if (!blurShader.isCompiled) {
                Gdx.app.log("ERROR", blurShader.log)
            }
        }

        return blurShader
    }
}