package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombieKid(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.zombieKidAttack!!.animationDuration
        deathAnimationDuration = Assets.zombieKidDie!!.animationDuration + .2F
        walkSpeed = .3F
        attackDistance = .35F
        damage = 1
        lives = 5
        isFacingLeft = true
        type = TYPE_NO_RANGE
    }
}
