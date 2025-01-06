package com.bms.chemtris.asset;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class to load textures.
 */
public class TextureLoader {

    //splash image texture
    private static Texture splash = new Texture("img/splash.png");
    //tutorial image texture
    private static Texture tuts = new Texture("img/tutorial.png");
    private static Texture h2o = new Texture("img/wat.png");
    private static Texture ch4 = new Texture("img/ch4.png");

    //method to load splash texture
    public static Texture getSplash(){
        //set a linear filter to clean image
        splash.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //return image
        return splash;
    }

    //method to load tutorial texture
    public static Texture getTut(){
        //set a linear filter to clean image
        tuts.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //return image
        return tuts;
    }

    //method to load tutorial texture
    public static Texture getWat(){
        //set a linear filter to clean image
        h2o.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //return image
        return h2o;
    }

    //method to load tutorial texture
    public static Texture getCh4(){
        //set a linear filter to clean image
        ch4.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //return image
        return ch4;
    }

    //method to dispose splash texture
    public static void disposeSplash(){
        splash.dispose();
    }

    public static void disposeTuts(){
        tuts.dispose();
    }

    public static void disposeWat(){
        h2o.dispose();
    }

    public static void disposeCh4(){
        ch4.dispose();
    }
}
