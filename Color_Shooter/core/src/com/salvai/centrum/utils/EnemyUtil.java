package com.salvai.centrum.utils;

import com.salvai.centrum.actors.enemies.Enemy;
import com.salvai.centrum.actors.enemies.EnemyBall;
import com.salvai.centrum.actors.player.Ball;
import com.salvai.centrum.actors.player.Missile;


public class EnemyUtil {

    public static boolean hitsCentre(Enemy enemy, Ball ball) {
        return CircleKt.circleHitsCircle(ball.shape, ((EnemyBall) enemy).shape);
    }

    public static boolean isHitByMissile(Enemy enemy, Missile missile) {
        return CircleKt.circleHitsCircle(missile.shape, ((EnemyBall) enemy).shape);
    }
}
