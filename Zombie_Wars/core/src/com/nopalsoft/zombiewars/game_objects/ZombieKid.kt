package com.nopalsoft.zombiewars.game_objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieKid(body: Body) : BasePlayer(body) {
    init {
        DURATION_ATTACK = Assets.zombieKidAttack!!.animationDuration
        DURATION_DEAD = Assets.zombieKidDie!!.animationDuration + .2f
        VELOCIDAD_WALK = .3f
        DISTANCE_ATTACK = .35f
        DAMAGE = 1
        vidas = 5
        isFacingLeft = true
        tipo = TIPO_NO_RANGO
    }
}
