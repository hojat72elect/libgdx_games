package com.bms.chemtris.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Class to render particles in the game.
 */
public class ParticleRender {
    public boolean isFinished;
    public static final int red = 1, blue = 2, yellow = 3;
    private int current;
    private int ctr;
    private Stage stage;
    private Image particle;
    private String color_path;

    public ParticleRender(int color, Stage stg){
        current = color;
        isFinished = true;
        ctr = 0;
        stage = stg;

        switch (current){
            case red:
                color_path = "img/particle_red/";
                break;

            case blue:
                color_path = "img/particle_blue/";
                break;

            case yellow:
                color_path = "img/particle_yellow/";
                break;
        }
        Texture p = new Texture(color_path+"0.png");
        p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        particle = new Image(p);
        particle.setWidth(Gdx.graphics.getWidth());
        particle.setHeight(Gdx.graphics.getHeight());
        particle.setPosition(0,Gdx.graphics.getHeight()/4);
        ctr = 0;
    }

    public boolean renderParticle(){
        isFinished = false;
        if(ctr == 0){
            stage.addActor(particle);
        }
        if(ctr == 4){
            Texture p = new Texture(color_path+"1.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 8){
            Texture p = new Texture(color_path+"2.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 12){
            Texture p = new Texture(color_path+"3.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 16){
            Texture p = new Texture(color_path+"4.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 20){
            Texture p = new Texture(color_path+"5.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 24){
            Texture p = new Texture(color_path+"6.png");
            p.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            particle.setDrawable(new SpriteDrawable(new Sprite(p)));
        }
        if(ctr == 28){
            isFinished = true;
            particle.remove();
            ctr = 0;
            return isFinished;
        }
        ctr += 1;
        return isFinished;
    }
}
