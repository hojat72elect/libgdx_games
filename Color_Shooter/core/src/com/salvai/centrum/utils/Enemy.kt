package com.salvai.centrum.utils

import com.salvai.centrum.actors.enemies.Enemy
import com.salvai.centrum.actors.enemies.EnemyBall
import com.salvai.centrum.actors.player.Ball
import com.salvai.centrum.actors.player.Missile

fun hitsCentre(enemy: Enemy, ball: Ball) = circleHitsCircle(ball.shape, (enemy as EnemyBall).shape)

fun isHitByMissile(enemy: Enemy, missile: Missile) = circleHitsCircle(missile.shape, (enemy as EnemyBall).shape)
