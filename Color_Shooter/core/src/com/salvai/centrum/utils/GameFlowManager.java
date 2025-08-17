package com.salvai.centrum.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Array;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.actors.effects.Explosion;
import com.salvai.centrum.actors.enemies.EnemyBall;
import com.salvai.centrum.actors.player.Ball;
import com.salvai.centrum.actors.player.Missile;
import com.salvai.centrum.enums.GameState;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.levels.BallInfo;

public class GameFlowManager {

    public Ball ball;
    public Texture ballTexture;
    public Array<Missile> missiles;
    public Array<EnemyBall> enemies;
    public Array<Explosion> explosions;
    public boolean gameOver;
    public CentrumGameClass game;
    public Sound successSound;
    private final Sound enemyHitSound;
    private final Sound pointSound;
    private final Sound comboResetSound;
    private final Sound gameOverSound;
    private final Array<Integer> enemyCreationDelays;
    private int difficultyScore;
    private int enemyCount;
    private final ParticleEffect particleEffect;
    private int ballInfoIndex;

    public GameFlowManager(CentrumGameClass gameClass) {
        game = gameClass;
        enemyCount = Constants.MIN_DIFFICULTY;
        difficultyScore = Constants.DIFFICULTY_CHANGE;
        gameOver = false;

        ballInfoIndex = 0;

        game.levelSucceed = false;

        particleEffect = game.assetsManager.manager.get(Constants.PARTICLE_EFFECT_FILE_NAME, ParticleEffect.class);

        gameOverSound = game.assetsManager.manager.get(Constants.GAME_OVER_SOUND_NAME, Sound.class);
        enemyHitSound = game.assetsManager.manager.get(Constants.POP_SOUND_NAME, Sound.class);
        pointSound = game.assetsManager.manager.get(Constants.COIN_SOUND_NAME, Sound.class);
        comboResetSound = game.assetsManager.manager.get(Constants.COMBO_RESET_SOUND_NAME, Sound.class);
        successSound = game.assetsManager.manager.get(Constants.SUCCESS_SOUND_NAME, Sound.class);

        ballTexture = game.assetsManager.manager.get(Constants.BALL_IMAGE_NAME, Texture.class);

        //GameObjects
        ball = new Ball(Constants.WIDTH_CENTER, Constants.HEIGHT_CENTER, ballTexture);
        missiles = new Array<>();
        enemies = new Array<>();
        explosions = new Array<>();
        enemyCreationDelays = new Array<>();


        if (this.game.gameType == GameType.ENDLESS) {
            for (int i = 1; i <= enemyCount; i++)
                enemyCreationDelays.add(50 * i);
        } else
            for (BallInfo ballInfo : game.getCurrentLevel().ballInfos)
                enemyCreationDelays.add(ballInfo.time);
    }

    public void update(float delta) {
        if (!gameOver) {
            //missiles
            moveMissiles(delta);
            checkMissiles();

            //enemies
            if (game.gameType == GameType.ENDLESS) {
                checkEndlessDelays(delta);
                createEndlessEnemies();
            } else {
                checkLevelDelays(delta);

                //check if level finished
                if (enemyCreationDelays.size == 0 && enemies.size == 0) {
                    game.levelSucceed = true;
                    gameOver = true;
                    if (game.soundOn)
                        successSound.play();
                }
            }

            //check score and if game over
            checkScoreAndGameOver(delta);

            updateParticleEffect();
        } else
            finishGame();
    }

    private void updateParticleEffect() {
        for (int i = 0; i < explosions.size; i++) {
            if (explosions.get(i).getParticleEffect().isComplete())
                explosions.removeIndex(i);
        }
    }

    private void finishGame() {
        for (Explosion explosion : explosions) {
            if (explosion.getParticleEffect().isComplete()) {
                explosions.removeValue(explosion, false);
            }
        }
    }

    private void checkScoreAndGameOver(float delta) {

        //Check score and game over
        for (EnemyBall enemy : enemies) {
            if (EnemyKt.hitsCentre(enemy, ball))
                if (enemy.color.equals(ball.getColor())) {
                    game.score++;
                    explosions.add(new Explosion(enemy.position, enemy.color, pointSound, game.soundOn, particleEffect));
                    enemies.removeValue(enemy, true);
                    if (game.gameType == GameType.ENDLESS)
                        enemyCreationDelays.add(RandomUtil.getRandomDelay());
                } else {
                    gameOver = true;
                    explosions.clear();
                    for (EnemyBall enemyBall : enemies)
                        explosions.add(new Explosion(enemyBall.position, enemyBall.color, enemyHitSound, false, particleEffect));
                    //center ball
                    explosions.add(new Explosion(ball.getColor(), gameOverSound, game.soundOn, particleEffect));
                    enemies.clear();
                    break;
                }
            else
                enemy.move(delta);
        }
    }


    private void checkEndlessDelays(float delta) {
        for (int i = 0; i < enemyCreationDelays.size; i++) {
            enemyCreationDelays.set(i, (int) (enemyCreationDelays.get(i) - delta));
            if (enemyCreationDelays.get(i) <= 0)
                enemyCreationDelays.removeIndex(i);
        }
    }

    private void checkLevelDelays(float delta) {
        for (int i = 0; i < enemyCreationDelays.size; i++) {
            enemyCreationDelays.set(i, (int) (enemyCreationDelays.get(i) - delta));
            if (enemyCreationDelays.get(i) <= 0) {
                //create enemies
                enemyCreationDelays.removeIndex(i);
                BallInfo ballInfo = game.getCurrentLevel().ballInfos.get(ballInfoIndex);

                enemies.add(new EnemyBall(ballInfo.x, ballInfo.y, ballInfo.speed, ThemeKt.getGameTheme().get(ballInfo.color), game.assetsManager.manager.get(Constants.BALL_IMAGE_NAME, Texture.class)));
                ballInfoIndex++;
            }
        }
    }

    private void checkMissiles() {
        //missile and bonus enemy
        for (Missile missile : missiles) {
            //missile hits enemy
            for (EnemyBall enemy : enemies) {
                if (EnemyKt.isHitByMissile(enemy, missile)) {
                    if (enemy.color.equals(ball.getColor())) {
                        explosions.add(new Explosion(enemy.position, enemy.color, comboResetSound, game.soundOn, particleEffect));
                    } else
                        explosions.add(new Explosion(enemy.position, enemy.color, enemyHitSound, game.soundOn, particleEffect));
                    enemies.removeValue(enemy, true);
                    missiles.removeValue(missile, true);
                    if (game.gameType == GameType.ENDLESS)
                        enemyCreationDelays.add(RandomUtil.getRandomDelay());
                }
            }

            if (!missile.inScreenBounds()) {
                missiles.removeValue(missile, true);
            }
        }
    }


    private void moveMissiles(float delta) {
        for (Missile missile : missiles)
            if (game.gameState != GameState.PAUSED)
                missile.move(delta);
    }

    private void createEndlessEnemies() {
        if (enemyCount <= Constants.MAX_DIFFICULTY && game.score > difficultyScore) {
            difficultyScore += Constants.DIFFICULTY_CHANGE;
            enemyCount++;
        }

        if (enemies.size < enemyCount - enemyCreationDelays.size) {
            EnemyBall enemy = new EnemyBall(game.assetsManager.manager.get(Constants.BALL_IMAGE_NAME, Texture.class));
            if (enemy.getSpeed() + (game.score * Constants.ENEMY_SPEED_INCREASE) < Constants.ENEMY_TOTAL_MAX_SPEED)
                enemy.setSpeed(RandomUtil.getRandomEnemySpeed((int) (enemy.getSpeed() + (game.score * Constants.ENEMY_SPEED_INCREASE))));
            else
                enemy.setSpeed(RandomUtil.getRandomEnemySpeed(Constants.ENEMY_TOTAL_MAX_SPEED));
            enemies.add(enemy);
        }
    }
}
