package com.salvai.centrum.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.RandomUtil;

public abstract class MovingGameObject {

    public Vector2 velocity;
    public Vector2 position;
    public Sprite sprite;
    public Color color;


    public MovingGameObject() {
        velocity = new Vector2();
        position = new Vector2();
        color = new Color(RandomUtil.getRandomColor());
    }

    //for level balls
    public MovingGameObject(Color color) {
        velocity = new Vector2();
        position = new Vector2();
        this.color = new Color(color);
    }


    protected void setDirectionToCentreBall() {
        velocity = new Vector2(Constants.WIDTH_CENTER,Constants.HEIGHT_CENTER).sub(position).nor();
    }


    /**
     * returns null if no Vetocr2 Position set
     *
     * @return
     */
    public Boolean inScreenBounds() {
        if (position == null)
            return null;
        return position.x <= Constants.SCREEN_WIDTH && position.x >= 0 && position.y <= Constants.SCREEN_HEIGHT && position.y >= 0;
    }


    public void setColor(Color color) {
        this.color = new Color(color);
        sprite.setColor(color);
    }
}
