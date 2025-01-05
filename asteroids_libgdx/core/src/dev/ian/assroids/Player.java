package dev.ian.assroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

import dev.ian.assroids.entity.Entity;
import dev.ian.assroids.entity.GameObject;
import dev.ian.assroids.entity.Health;
import dev.ian.assroids.entity.Ship;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 8:24 PM
 */
public class Player implements Entity {

    private Stack<Health> lives;
    private int damage;
    private Ship ship;

    public Player(Ship ship) {
        this.ship = ship;
        initHealth();
    }

    private void initHealth(){
        lives = new Stack<Health>();
        for (int i = 0; i < 5; i++) {
            Health life = new Health();
            float x = (Gdx.graphics.getWidth() - 115) + (i * life.getWidth());
            float y = Gdx.graphics.getHeight() - (life.getHeight() + 15);
            life.setPosition(x, y);
            lives.add(life);
        }
    }

    public void handleEvent() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) ship.setDy(5);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) ship.setDy(-5);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ship.setDx(5);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) ship.setDx(-5);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) ship.fire();
    }

    @Override
    public void update(float delta) {
        if (isHasLife()) {
            Health life = lives.peek();
            life.decreaseHealthCost(damage);
            if (life.isHealthZero()) lives.pop();
            damage = 0;
        }
    }

    public void decreaseHealth(int damage) {
        this.damage = damage;
    }

    public boolean isHasLife() {
        return !lives.empty();
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (GameObject life : lives) {
            life.draw(batch);
        }
    }
}
