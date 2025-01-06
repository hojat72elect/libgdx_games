package com.bms.chemtris.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

/**
 * Class to load buttons.
 */
public class ButtonLoader {

    //button texture
    private static final TextureAtlas groupOne = new TextureAtlas(Gdx.files.internal("btn/button.pack"));
    private static final TextureAtlas groupTwo = new TextureAtlas(Gdx.files.internal("btn/button2.pack"));

    //button skins
    private static final Skin skinOne = new Skin(groupOne);
    private static final Skin skinTwo = new Skin(groupTwo);



    //method to make box button style
    public static TextButtonStyle makeBoxStyle(){
        //make text button style
        TextButtonStyle style = new TextButtonStyle();
        //setup button textures
        style.up = skinOne.getDrawable("box");
        style.down = skinOne.getDrawable("boxpress");
        style.over = skinOne.getDrawable("boxpress");
        //setup font color
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.GRAY;
        style.overFontColor = Color.GRAY;

        return style;
    }

    public static TextButtonStyle makeCircleStyle(){
        //make circle style
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinOne.getDrawable("circle");
        style.down = skinOne.getDrawable("cp");
        style.over = skinOne.getDrawable("cp");
        //setup font color
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.GRAY;
        style.overFontColor = Color.GRAY;

        return style;
    }

    //method to make play button
    public static TextButton makePlay(){
        //get button style
        TextButtonStyle style = makeBoxStyle();
        //set button font
        style.font = FontLoader.font(1.5f);
        //make button
        TextButton play = new TextButton("Play", style);
        //set button size and position
        play.setHeight(Gdx.graphics.getHeight()/4.5f);
        play.setWidth(Gdx.graphics.getWidth()/4.5f);
        play.setPosition(Gdx.graphics.getWidth()/2-play.getWidth()/2,
                Gdx.graphics.getHeight()/2-Gdx.graphics.getHeight()/8);
        //return button
        return play;
    }

    public static TextButton makeLevelButton(String text, int level){
        //get button style
        TextButtonStyle style = makeBoxStyle();
        //set button font
        style.font = FontLoader.font(1f);
        //set button size
        TextButton button = new TextButton(text,style);
        button.setWidth(Gdx.graphics.getWidth()/8);
        button.setHeight(Gdx.graphics.getHeight()/8);
        //set button position
        switch (level){
            case 1:
                button.setPosition(Gdx.graphics.getWidth()/4,
                    Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10);
                break;

            case 2:
                button.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/15,
                    Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10);
                break;

            case 3:
                button.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/8,
                        Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10);
                break;

            case 4:
                button.setPosition(Gdx.graphics.getWidth()/4,
                        Gdx.graphics.getHeight()/3);
                break;

            case 5:
                button.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/15,
                        Gdx.graphics.getHeight()/3);
                break;

            default:
                button.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/8,
                        Gdx.graphics.getHeight()/3);
                break;
        }

        return button;
    }


    public static TextButton makeUp(){
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinOne.getDrawable("up");
        style.down = skinOne.getDrawable("upppress");
        style.over = skinOne.getDrawable("upppress");
        style.font = FontLoader.font(1f);

        TextButton up = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        up.setWidth(w);
        up.setHeight(h);
        up.setPosition(w / 2 + w/2 + h/10 + h/2, h + h+ 2*h);

        return up;
    }

    public static TextButton makeDown(){
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinOne.getDrawable("down");
        style.down = skinOne.getDrawable("downpress");
        style.over = skinOne.getDrawable("downpress");
        style.font = FontLoader.font(1f);

        TextButton down = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        down.setWidth(w);
        down.setHeight(h);
        down.setPosition(w / 2 + w/2 + h/10 + h/2, h/2);

        return down;
    }

    public static TextButton makeRight(){
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinOne.getDrawable("right");
        style.down = skinOne.getDrawable("rightpress");
        style.over = skinOne.getDrawable("rightpress");
        style.font = FontLoader.font(1f);

        TextButton right = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        right.setWidth(w);
        right.setHeight(h);
        right.setPosition(w + w / 4 + h + w, h - h / 4 + w / 3 + h);

        return right;
    }

    public static TextButton makeLeft(){
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinOne.getDrawable("left");
        style.down = skinOne.getDrawable("leftpress");
        style.over = skinOne.getDrawable("leftpress");
        style.font = FontLoader.font(1f);

        TextButton left = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        left.setWidth(w);
        left.setHeight(h);
        left.setPosition(0, h - h / 4 + w / 3 + h);

        return left;
    }

    public static TextButton makeDesc(){
        TextButtonStyle style = new TextButtonStyle();
        style.up = skinTwo.getDrawable("descend1");
        style.down = skinTwo.getDrawable("descend2");
        style.over = skinTwo.getDrawable("descend2");
        style.font = FontLoader.font(1f);

        TextButton dsc = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        dsc.setWidth(w);
        dsc.setHeight(h);
        dsc.setPosition(w / 2 + w/2 + h/10 + h/2, h - h / 4 + w / 3 + h);

        return dsc;
    }

    public static TextButton rx(){

        TextButtonStyle style = new TextButtonStyle();
        style.up = skinTwo.getDrawable("rotatex");
        style.down = skinTwo.getDrawable("rotatexpress");
        style.over = skinTwo.getDrawable("rotatexpress");
        style.font = FontLoader.font(1f);

        TextButton b = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;
        b.setHeight(h);
        b.setWidth(w);

        b.setPosition(Gdx.graphics.getWidth()-w - 3f*w, h - h / 4 + w / 3 + h);

        return b;
    }

    public static TextButton ry(){

        TextButtonStyle style = new TextButtonStyle();
        style.up = skinTwo.getDrawable("rotatey");
        style.down = skinTwo.getDrawable("rotateypress");
        style.over = skinTwo.getDrawable("rotateypress");
        style.font = FontLoader.font(1f);

        TextButton b = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        b.setHeight(h);
        b.setWidth(w);
        b.setPosition(Gdx.graphics.getWidth()-w - 1.75f* w, h - h / 4 + w / 3 + h);

        return b;
    }

    public static TextButton rz(){

        TextButtonStyle style = new TextButtonStyle();
        style.up = skinTwo.getDrawable("rotatez");
        style.down = skinTwo.getDrawable("rotatezpress");
        style.over = skinTwo.getDrawable("rotatezpress");
        style.font = FontLoader.font(1f);

        TextButton b = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        b.setHeight(h);
        b.setWidth(w);
        b.setPosition(Gdx.graphics.getWidth()-w - .5f* w, h - h / 4 + w / 3 + h);

        return b;
    }

    public static TextButton cam(){

        TextButtonStyle style = new TextButtonStyle();
        style.up = skinTwo.getDrawable("cam1");
        style.down = skinTwo.getDrawable("cam2");
        style.over = skinTwo.getDrawable("cam2");
        style.font = FontLoader.font(1f);

        TextButton b = new TextButton("",style);

        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        b.setHeight(h);
        b.setWidth(w);
        b.setPosition(Gdx.graphics.getWidth()-w - w,
                h + 7* h + h);

        return b;
    }

    public static TextButton question(){
        TextButtonStyle circle = makeCircleStyle();
        //set button font
        circle.font = FontLoader.font(.65f);
        //make button
        TextButton play = new TextButton("?", circle);
        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        //set button size and position
        play.setHeight(h);
        play.setWidth(w);
        play.setPosition(0, Gdx.graphics.getHeight()-play.getHeight()-play.getHeight()/5);
        //return button
        return play;
    }

    public static TextButton quit(){
        TextButtonStyle circle = makeCircleStyle();
        //set button font
        circle.font = FontLoader.font(.65f);
        //make button
        TextButton play = new TextButton("quit", circle);
        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        //set button size and position
        play.setHeight(h);
        play.setWidth(w);
        play.setPosition(Gdx.graphics.getWidth()-w - w,
                h + 5.5f* h + h);
        //return button
        return play;
    }

    public static TextButton yes(){
        TextButtonStyle circle = makeCircleStyle();
        //set button font
        circle.font = FontLoader.font(.65f);
        //make button
        TextButton play = new TextButton("Y", circle);
        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        //set button size and position
        play.setHeight(h);
        play.setWidth(w);
        play.setPosition(Gdx.graphics.getWidth()-w - w + w/15,
                h + 3.5f* h + h);
        //return button
        return play;
    }

    public static TextButton no(){
        TextButtonStyle circle = makeCircleStyle();
        //set button font
        circle.font = FontLoader.font(.65f);
        //make button
        TextButton play = new TextButton("N", circle);
        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;

        //set button size and position
        play.setHeight(h);
        play.setWidth(w);
        play.setPosition(Gdx.graphics.getWidth()-w - 2.5f* w,
                h + 3.5f* h + h);
        //return button
        return play;
    }

    public static Button pause(){
        Button.ButtonStyle p = new Button.ButtonStyle(skinTwo.getDrawable("pause1"),
                skinTwo.getDrawable("play1"),skinTwo.getDrawable("play1"));

        Button pause = new Button(p);
        float w = Gdx.graphics.getWidth() / 14;
        float h = Gdx.graphics.getHeight() / 12;
        pause.setHeight(h);
        pause.setWidth(w);
        pause.setPosition(Gdx.graphics.getWidth()-w - w,
                h + 8.5f* h + h);

        return pause;
    }

    public static TextButton replay(){
        TextButtonStyle box = makeBoxStyle();
        box.font = FontLoader.font(.8f);

        TextButton replay = new TextButton("Replay",box);
        replay.setWidth(Gdx.graphics.getWidth()/6);
        replay.setHeight(Gdx.graphics.getHeight()/8);
        replay.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);

        return replay;
    }

    public static TextButton levelss(){
        TextButtonStyle box = makeBoxStyle();
        box.font = FontLoader.font(.8f);

        TextButton levels = new TextButton("Levels",box);
        levels.setWidth(Gdx.graphics.getWidth()/7);
        levels.setHeight(Gdx.graphics.getHeight()/8);
        levels.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/4);

        return levels;
    }



    public static void dispose(){
        groupOne.dispose();
        groupTwo.dispose();
        skinOne.dispose();
        skinTwo.dispose();
    }


}
