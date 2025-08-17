package com.nopalsoft.zombiewars.game_objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class HeroForce(body: Body) : BasePlayer(body) {
    init {
        DURATION_ATTACK = Assets.heroForceShoot!!.animationDuration
        DURATION_DEAD = Assets.heroForceDie!!.animationDuration + .2f
        VELOCIDAD_WALK = 1f
        DAMAGE = 1
        DISTANCE_ATTACK = 2f
        TIME_TO_ATTACK_AGAIN = 2f
        vidas = 5
        isFacingLeft = false
        tipo = TIPO_RANGO
    }
}
