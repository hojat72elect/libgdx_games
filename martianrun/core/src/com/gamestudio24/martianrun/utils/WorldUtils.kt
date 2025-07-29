package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.gamestudio24.martianrun.box2d.EnemyUserData
import com.gamestudio24.martianrun.box2d.GroundUserData
import com.gamestudio24.martianrun.box2d.RunnerUserData

object WorldUtils {
    fun createWorld(): World {
        return World(Constants.WORLD_GRAVITY, true)
    }

    fun createGround(world: World): Body {
        val bodyDef = BodyDef()
        bodyDef.position.set(Vector2(Constants.GROUND_X, Constants.GROUND_Y))
        val body = world.createBody(bodyDef)
        val shape = PolygonShape()
        shape.setAsBox(Constants.GROUND_WIDTH / 2, Constants.GROUND_HEIGHT / 2)
        body.createFixture(shape, Constants.GROUND_DENSITY)
        body.userData = GroundUserData(Constants.GROUND_WIDTH, Constants.GROUND_HEIGHT)
        shape.dispose()
        return body
    }

    fun createRunner(world: World): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(Vector2(Constants.RUNNER_X, Constants.RUNNER_Y))
        val shape = PolygonShape()
        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2)
        val body = world.createBody(bodyDef)
        body.gravityScale = Constants.RUNNER_GRAVITY_SCALE
        body.createFixture(shape, Constants.RUNNER_DENSITY)
        body.resetMassData()
        body.userData = RunnerUserData(Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT)
        shape.dispose()
        return body
    }

    fun createEnemy(world: World): Body {
        val enemyType = RandomUtils.randomEnemyType
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        bodyDef.position.set(Vector2(enemyType!!.x, enemyType.y))
        val shape = PolygonShape()
        shape.setAsBox(enemyType.width / 2, enemyType.height / 2)
        val body = world.createBody(bodyDef)
        body.createFixture(shape, enemyType.density)
        body.resetMassData()
        val userData = EnemyUserData(
            enemyType.width, enemyType.height,
            enemyType.animationAssetId
        )
        body.userData = userData
        shape.dispose()
        return body
    }
}
