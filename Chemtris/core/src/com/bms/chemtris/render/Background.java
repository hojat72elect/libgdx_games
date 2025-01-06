package com.bms.chemtris.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Class to render gradient background.
 */
public class Background {
    //member to render background shape
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    //light color
    private static final Color light = new Color(126f/255f,165f/255f,232f/255f,1f);
    //medium color
    private static final Color medium = new Color(223f/255f,96f/255f,213f/255f,1f);
    //normal color
    private static final Color normal = new Color(.94f,.35f,.52f,1f);
    //dark color
    private static final Color dark = new Color(.99f,.78f,.77f,1f);

    //method to render gradient background
    public static void render(){
        //set viewport
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //temporarily clear to normal color
        Gdx.gl.glClearColor(42 / 255f, 66 / 255f, 101 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //start gradient rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //set colors and size
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                dark,normal,medium,light);
        //end gradient rendering
        shapeRenderer.end();

    }

}
