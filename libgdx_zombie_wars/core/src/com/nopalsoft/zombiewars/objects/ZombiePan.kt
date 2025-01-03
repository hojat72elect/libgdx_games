package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class ZombiePan(body: Body) : GameCharacter(body) {
    init {
        attackAnimationDuration = Assets.zombiePanAttack!!.animationDuration
        deathAnimationDuration = Assets.zombiePanDie!!.animationDuration + .2F
        walkSpeed = .75F
        attackDistance = .35F
        damage = 1
        lives = 3
        isFacingLeft = true
        type = TYPE_NO_RANGE
    }
}
