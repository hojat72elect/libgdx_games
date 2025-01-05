package dev.ian.assroids.entity;

import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.entity.GameObject;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 8:31 PM
 */
public class Health extends GameObject {
    private int value;

    public Health() {
        value = 30;
        sprite = Asset.instance().createSprite("heart");
        sprite.setSize(20, 20);
    }

    public boolean isHealthZero() {
        return value <= 0;
    }

    public void decreaseHealthCost(int damage) {
        this.value = value - damage;
    }
}
