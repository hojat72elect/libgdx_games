package com.nopalsoft.zombiewars.game_objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombiePan(body: Body) : BasePlayer(body) {
    init {
        DURATION_ATTACK = Assets.zombiePanAttack!!.animationDuration
        DURATION_DEAD = Assets.zombiePanDie!!.animationDuration + .2f
        VELOCIDAD_WALK = .75f
        DISTANCE_ATTACK = .35f
        DAMAGE = 1
        vidas = 3
        isFacingLeft = true
        tipo = TIPO_NO_RANGO
    }
}
