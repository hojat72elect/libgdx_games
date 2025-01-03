package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class HeroLumber(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.heroLumberShoot!!.animationDuration
        deathAnimationDuration = Assets.heroForceDie!!.animationDuration + .2F
        walkSpeed = 1F
        damage = 1
        attackDistance = .5F
        timeToAttackAgain = 0F
        lives = 5
        isFacingLeft = false
        type = TYPE_NO_RANGE
    }
}