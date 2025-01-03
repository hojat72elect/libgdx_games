package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieMummy(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.zombieMummyAttack!!.animationDuration
        deathAnimationDuration = Assets.zombieMummyDie!!.animationDuration + .2f
        walkSpeed = .5f
        attackDistance = .35f
        damage = 1
        lives = 3
        isFacingLeft = true
        type = TYPE_NO_RANGE
    }
}
