package com.nopalsoft.zombiewars.objetos

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieFrank(body: Body) : BasePlayer(body) {
    init {
        DURATION_ATTACK = Assets.zombieFrankAttack.animationDuration
        DURATION_DEAD = Assets.zombieFrankDie.animationDuration + .2f
        VELOCIDAD_WALK = 1f
        DISTANCE_ATTACK = .35f
        DAMAGE = 5
        vidas = 10
        isFacingLeft = true
        tipo = TIPO_NO_RANGO
    }
}
