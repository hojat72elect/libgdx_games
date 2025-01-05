package dev.ian.assroids.entity.bullet;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 8:09 PM
 */
public enum BulletType {

    NORMAL("Normal"), LAVA("Lava bending"), LASER("laser"), VENOM("Venom breath"), EAGLE("Eagle eye sight");
    private String name;

    BulletType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
