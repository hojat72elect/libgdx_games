package com.bitfire.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShaderLoader {
    public static String BasePath = "";
    public static boolean Pedantic = true;

    public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName) {
        return ShaderLoader.fromFile(vertexFileName, fragmentFileName, "");
    }

    public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName, String defines) {
        String log = "\"" + vertexFileName + "/" + fragmentFileName + "\"";
        if (!defines.isEmpty()) {
            log += " w/ (" + defines.replace("\n", ", ") + ")";
        }
        log += "...";
        Gdx.app.log("ShaderLoader", "Compiling " + log);

        String vpSrc = Gdx.files.internal(BasePath + vertexFileName + ".vertex").readString();
        String fpSrc = Gdx.files.internal(BasePath + fragmentFileName + ".fragment").readString();

        return ShaderLoader.fromString(vpSrc, fpSrc, defines);
    }

    public static ShaderProgram fromString(String vertex, String fragment) {
        return ShaderLoader.fromString(vertex, fragment, "");
    }

    public static ShaderProgram fromString(String vertex, String fragment, String defines) {
        ShaderProgram.pedantic = ShaderLoader.Pedantic;
        ShaderProgram shader = new ShaderProgram(defines + "\n" + vertex, defines + "\n" + fragment);

        if (!shader.isCompiled()) {
            Gdx.app.error("ShaderLoader", shader.getLog());
            System.exit(-1);
        }

        return shader;
    }

    private ShaderLoader() {
    }
}
