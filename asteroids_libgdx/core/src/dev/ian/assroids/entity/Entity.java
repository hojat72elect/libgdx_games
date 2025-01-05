package dev.ian.assroids.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by: Ian Parcon
 * Date created: Sep 03, 2018
 * Time created: 10:51 AM
 */
public interface Entity {

    void update(float delta);

    void draw(SpriteBatch batch);
}
