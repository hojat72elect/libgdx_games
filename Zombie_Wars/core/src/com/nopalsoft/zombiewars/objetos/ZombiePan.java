package com.nopalsoft.zombiewars.objetos;

import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiewars.Assets;

public class ZombiePan extends Personajes {

    public ZombiePan(Body body) {
        super(body);
        DURATION_ATTACK = Assets.zombiePanAttack.animationDuration;
        DURATION_DEAD = Assets.zombiePanDie.animationDuration + .2f;
        VELOCIDAD_WALK = .75f;
        DISTANCE_ATTACK = .35f;
        DAMAGE = 1;
        vidas = 3;
        isFacingLeft = true;
        tipo = TIPO_NO_RANGO;
    }
}
