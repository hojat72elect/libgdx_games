package com.salvai.centrum.actors.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.salvai.centrum.actors.MovingGameObject;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.GameColorPalette;


public class Missile extends MovingGameObject {

    public Circle shape;

    public Missile(Vector2 touch, Texture texture) {
        super();
        sprite = new Sprite(texture);
        position.x = Constants.WIDTH_CENTER - (Constants.MISSILE_RADIUS * 0.5f);
        position.y = Constants.HEIGHT_CENTER - (Constants.MISSILE_RADIUS * 0.5f);
        velocity = touch.sub(position).nor();
        shape = new Circle(position.x, position.y, Constants.MISSILE_RADIUS);
        sprite.setBounds(position.x, position.y, Constants.MISSILE_RADIUS, Constants.MISSILE_RADIUS);
        sprite.setColor(GameColorPalette.BASIC[0]);
    }


    public void move(float delta) {
        position.set(shape.x + (velocity.x * delta * Constants.MISSILE_SPEED), shape.y + (velocity.y * delta * Constants.MISSILE_SPEED));
        shape.setPosition(shape.x + (velocity.x * delta * Constants.MISSILE_SPEED), shape.y + (velocity.y * delta * Constants.MISSILE_SPEED));
        sprite.setPosition(sprite.getX() + (velocity.x * delta * Constants.MISSILE_SPEED), sprite.getY() + (velocity.y * delta * Constants.MISSILE_SPEED));
    }

}