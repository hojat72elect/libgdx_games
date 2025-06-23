package com.salvai.centrum.utils;

import com.salvai.centrum.actors.enemys.Enemy;
import com.salvai.centrum.actors.enemys.EnemyBall;
import com.salvai.centrum.actors.player.Ball;
import com.salvai.centrum.actors.player.Missile;


public class EnemyUtil {

    public static boolean hitsCentre(Enemy enemy, Ball ball) {
        return ColissionUtil.circleHitsCircle(ball.shape, ((EnemyBall) enemy).shape);
    }

    public static boolean isHitByMissile(Enemy enemy, Missile missile) {
        return ColissionUtil.circleHitsCircle(missile.shape, ((EnemyBall) enemy).shape);
    }
}
