package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieCuasy(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.zombieCuasyAttack!!.animationDuration
        deathAnimationDuration = Assets.zombieCuasyDie!!.animationDuration + .2F
        walkSpeed = .15F
        attackDistance = .35F
        damage = 1
        lives = 5
        isFacingLeft = true
        type = TYPE_NO_RANGE
    }
}
