package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class HeroFarmer(body: Body) : GameCharacter(body) {

    init {
        attackAnimationDuration = Assets.heroForceShoot!!.animationDuration
        deathAnimationDuration = Assets.heroForceDie!!.animationDuration + .2F
        walkSpeed = 1F
        damage = 1
        attackDistance = 2F
        timeToAttackAgain = 2F
        lives = 5
        isFacingLeft = false
        type = TYPE_RANGE
    }
}