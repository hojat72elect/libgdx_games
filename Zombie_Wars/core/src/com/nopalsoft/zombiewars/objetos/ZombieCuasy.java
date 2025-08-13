package com.nopalsoft.zombiewars.objetos;

import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiewars.Assets;

public class ZombieCuasy extends Personajes {

    public ZombieCuasy(Body body) {
        super(body);
        DURATION_ATTACK = Assets.zombieCuasyAttack.animationDuration;
        DURATION_DEAD = Assets.zombieCuasyDie.animationDuration + .2f;
        VELOCIDAD_WALK = .15f;
        DISTANCE_ATTACK = .35f;
        DAMAGE = 1;
        vidas = 5;
        isFacingLeft = true;
        tipo = TIPO_NO_RANGO;
    }
}
