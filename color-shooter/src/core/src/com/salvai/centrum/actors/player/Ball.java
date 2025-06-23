package com.salvai.centrum.actors.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.GameColorPalette;

public class Ball {

    public Circle shape;
    public Sprite sprite;
    public Color color;

    public Ball(float x, float y, Texture texture) {
        shape = new Circle(x, y, Constants.PLANET_DIAMETER * 0.5f);
        x -= Constants.PLANET_DIAMETER * 0.5f;
        y -= Constants.PLANET_DIAMETER * 0.5f;
        sprite = new Sprite(texture);
        sprite.setBounds(x, y, Constants.PLANET_DIAMETER, Constants.PLANET_DIAMETER);
        //set color white
        color = new Color(GameColorPalette.BASIC[0]);
        sprite.setColor(color);
    }



    public void setColor(Color color) {
        this.color = color;
        sprite.setColor(color);
    }

}
