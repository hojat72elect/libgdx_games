package com.tetris.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class ButtonLoader {
    AssetLoader assets;
    public Button music,sound, clock,play;
    public TextButton menuPlay,menuTut, back,forward;
    public TextButton up,down,left,right,a,rx,ry,rz,cameraB, quit;

    public ButtonLoader(AssetLoader assets){
        this.assets = assets;
    }

    public Button getMusic(){
        //Music Button
        music = new Button(assets.miscS.getDrawable("music"), assets.miscS.getDrawable("musicOff"),assets.miscS.getDrawable("musicOff"));
        music.setHeight(Gdx.graphics.getHeight() / 17);
        music.setWidth(Gdx.graphics.getWidth() / 13);
        music.setPosition(Gdx.graphics.getWidth() - music.getWidth(), Gdx.graphics.getHeight() - music.getHeight());

        return music;
    }

    public Button getSound(){
        //Sound Button
        sound = new Button(assets.miscS.getDrawable("sound"), assets.miscS.getDrawable("soundOff"),assets.miscS.getDrawable("soundOff"));
        sound.setHeight(Gdx.graphics.getHeight() / 17);
        sound.setWidth(Gdx.graphics.getWidth() / 13);
        sound.setPosition(Gdx.graphics.getWidth()-music.getWidth()-sound.getWidth()*5/4, Gdx.graphics.getHeight()-sound.getHeight());

        return sound;
    }

    public void generateGameButtons(){
        //Game Buttons
        up = new TextButton("",assets.up);
        down = new TextButton("",assets.down);
        left = new TextButton("",assets.left);
        right = new TextButton("",assets.right);
        a = new TextButton("",assets.a);

        left.setWidth(Gdx.graphics.getWidth() / 7);
        left.setHeight(Gdx.graphics.getHeight() / 7);

        a.setWidth(Gdx.graphics.getWidth() / 7);
        a.setHeight(Gdx.graphics.getHeight() / 7);

        up.setWidth(Gdx.graphics.getWidth() / 7);
        up.setHeight(Gdx.graphics.getHeight() / 6);
        up.setPosition(left.getWidth() / 2 + a.getWidth() / 6, up.getHeight() + up.getHeight() / 2 + a.getHeight() / 6);

        right.setWidth(Gdx.graphics.getWidth() / 8);
        right.setHeight(Gdx.graphics.getHeight() / 7);
        right.setPosition(a.getWidth() + a.getWidth() / 4 + a.getHeight() / 6 + a.getWidth() / 6, left.getHeight() - left.getHeight() / 4 + a.getWidth() / 10);

        left.setPosition(-20, left.getHeight() - left.getHeight() / 4 + a.getWidth() / 9);
        down.setWidth(Gdx.graphics.getWidth() / 7);
        down.setHeight(Gdx.graphics.getHeight() / 6);
        down.setPosition(left.getWidth() / 2 + a.getWidth() / 6, 0 - down.getHeight() / 8);

        rx = new TextButton("",assets.rx);
        ry = new TextButton("",assets.ry);
        rz = new TextButton("",assets.rz);

        rz.setWidth(Gdx.graphics.getWidth() / 7);
        rz.setHeight(Gdx.graphics.getHeight() / 7 + 2f);
        rz.setPosition(Gdx.graphics.getWidth() - rz.getWidth() + 2f, 0 + rz.getHeight() / 2+10f);

        ry.setHeight(Gdx.graphics.getHeight() / 7 + 2f);
        ry.setWidth(Gdx.graphics.getWidth() / 7);
        ry.setPosition(Gdx.graphics.getWidth() - rz.getWidth() - ry.getWidth() + 2f, 0 + ry.getHeight() / 2+10f);

        rx.setHeight(Gdx.graphics.getHeight() / 7 + 2f);
        rx.setWidth(Gdx.graphics.getWidth() / 7);
        rx.setPosition(Gdx.graphics.getWidth() - rz.getWidth() - ry.getWidth() - rx.getWidth(), 0 + ry.getHeight() / 2+10f);

        play = new Button(assets.miscS.getDrawable("pause"),assets.miscS.getDrawable("play"),assets.miscS.getDrawable("play"));
        play.setHeight(Gdx.graphics.getHeight() / 8);
        play.setWidth(Gdx.graphics.getWidth() / 8);
        play.setPosition(0, Gdx.graphics.getHeight() - play.getHeight());

        clock = new Button(assets.other.getDrawable("c"), assets.other.getDrawable("cc"), assets.other.getDrawable("cc"));
        clock.setWidth(Gdx.graphics.getWidth() / 8);
        clock.setHeight(Gdx.graphics.getHeight() / 8);
        clock.setPosition(Gdx.graphics.getWidth() - rz.getWidth() - ry.getWidth(), rz.getHeight() + rz.getHeight() / 2);
        a.setPosition(Gdx.graphics.getWidth() - rz.getWidth() - ry.getWidth(), -10);

        cameraB = new TextButton("camera",assets.smallStyle);
        cameraB.setHeight(Gdx.graphics.getWidth() / 4);
        cameraB.setWidth(Gdx.graphics.getWidth() / 3);
        cameraB.setPosition(-40, Gdx.graphics.getHeight() - 2 * play.getHeight() - cameraB.getHeight() / 2 - play.getHeight());

        quit = new TextButton("QUIT",assets.smallStyle);
        quit.setWidth(Gdx.graphics.getWidth() / 4);
        quit.setHeight(Gdx.graphics.getWidth() / 4);
        quit.setPosition(Gdx.graphics.getWidth()/2+quit.getWidth()/2,Gdx.graphics.getHeight() - play.getHeight()*3);
    }

    public void generateMenuButtons(){
        //Menu Buttons
        menuPlay = new TextButton("Play", assets.buttonStyle);
        menuPlay.setWidth(Gdx.graphics.getWidth() / 2);
        menuPlay.setHeight(Gdx.graphics.getHeight() / 2);
        menuPlay.setPosition((Gdx.graphics.getWidth() / 2 - menuPlay.getWidth() / 2+music.getWidth()/4), (Gdx.graphics.getHeight() / 2 - menuPlay.getHeight() / 2));

        menuTut = new TextButton("Tutorial", assets.buttonStyle);
        menuTut.setWidth(Gdx.graphics.getWidth());
        menuTut.setHeight(Gdx.graphics.getHeight() / 2);
        menuTut.setPosition((Gdx.graphics.getWidth() / 2 - menuTut.getWidth() / 2+music.getWidth()/2), (Gdx.graphics.getHeight() / 5 - menuTut.getHeight() / 2));
    }

    public TextButton generateBackButton(String text){
        //back button
        back = new TextButton(text,assets.buttonStyle);
        back.setWidth(Gdx.graphics.getWidth() / 3);
        back.setHeight(Gdx.graphics.getHeight() / 4);
        back.setPosition(-Gdx.graphics.getWidth() / 11+music.getWidth()/8, Gdx.graphics.getHeight() / 5 - back.getHeight());
        return back;
    }

    public TextButton generateForwardButton(String text){
        //Forward button
        forward = new TextButton(text, assets.buttonStyle);
        forward.setWidth(Gdx.graphics.getWidth()/3);
        forward.setHeight(Gdx.graphics.getHeight() / 4);
        forward.setPosition(Gdx.graphics.getWidth()-forward.getWidth()+music.getWidth(),Gdx.graphics.getHeight() / 5 - back.getHeight());
        return forward;

    }
}
