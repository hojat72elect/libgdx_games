package com.gamestudio24.martianrun.enums

import com.badlogic.gdx.math.Vector2
import com.gamestudio24.martianrun.utils.Constants

enum class Difficulty(
    val level: Int,
    obstacleLinearVelocity: Vector2,
    val runnerGravityScale: Float,
    val runnerJumpingLinearImpulse: Vector2,
    val scoreMultiplier: Int
) {
    DIFFICULTY_1(1, Constants.ENEMY_LINEAR_VELOCITY, Constants.RUNNER_GRAVITY_SCALE, Constants.RUNNER_JUMPING_LINEAR_IMPULSE, 5),
    DIFFICULTY_2(2, Vector2(-12f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.1f, Vector2(0f, 13f), 10),
    DIFFICULTY_3(3, Vector2(-14f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.1f, Vector2(0f, 13f), 20),
    DIFFICULTY_4(4, Vector2(-16f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.1f, Vector2(0f, 13f), 40),
    DIFFICULTY_5(5, Vector2(-18f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.1f, Vector2(0f, 13f), 80),
    DIFFICULTY_6(6, Vector2(-20f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.3f, Vector2(0f, 14f), 120),
    DIFFICULTY_7(7, Vector2(-22f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.3f, Vector2(0f, 14f), 160),
    DIFFICULTY_8(8, Vector2(-24f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.3f, Vector2(0f, 14f), 200),
    DIFFICULTY_9(9, Vector2(-26f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.5f, Vector2(0f, 15f), 250),
    DIFFICULTY_10(10, Vector2(-28f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.5f, Vector2(0f, 15f), 300),
    DIFFICULTY_11(11, Vector2(-30f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.6f, Vector2(0f, 15f), 350),
    DIFFICULTY_12(12, Vector2(-32f, 0f), Constants.RUNNER_GRAVITY_SCALE * 1.7f, Vector2(0f, 16f), 400),
    DIFFICULTY_13(13, Vector2(-34f, 0f), Constants.RUNNER_GRAVITY_SCALE * 2.1f, Vector2(0f, 18f), 500);

    val enemyLinearVelocity: Vector2? = obstacleLinearVelocity

}
