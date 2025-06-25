

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Blending {

    public static void useDefault() {
        enable();
        setNormalMode();
    }

    public static void enable() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    public static void disable() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    //in this mode colors overwrite eachother, based on alpha value
    public static void setNormalMode() {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    //in this mode colors add to eachother, eventually reaching pure white
    public static void setLightMode() {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    }
}
