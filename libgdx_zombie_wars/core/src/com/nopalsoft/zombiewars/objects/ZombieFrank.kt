package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieFrank(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.zombieFrankAttack!!.animationDuration
        deathAnimationDuration = Assets.zombieFrankDie!!.animationDuration + .2F
        walkSpeed = 1F
        attackDistance = .35F
        damage = 5
        lives = 10
        isFacingLeft = true
        type = TYPE_NO_RANGE
    }
}
