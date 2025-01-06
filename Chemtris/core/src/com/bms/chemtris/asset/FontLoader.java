package com.bms.chemtris.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Class to load fonts.
 */
public class FontLoader {

    //font directory
    private static final String DIR = "fnt/box_font.fnt";

    //method to make bitmap font file
    public static BitmapFont font(float size){
        //setup size
        float screenFont = .85f * size * Gdx.graphics.getDensity();
        //make font
        BitmapFont font =  new BitmapFont(Gdx.files.internal(DIR),false);
        //scale font
        font.getData().setScale(screenFont);
        //return font
        return font;
    }

}
