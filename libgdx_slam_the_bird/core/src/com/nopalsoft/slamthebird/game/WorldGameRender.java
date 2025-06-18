package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.game_objects.Coin;
import com.nopalsoft.slamthebird.game_objects.Enemy;
import com.nopalsoft.slamthebird.game_objects.Platform;
import com.nopalsoft.slamthebird.game_objects.Player;
import com.nopalsoft.slamthebird.game_objects.PowerUp;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class WorldGameRender {

    final float WIDTH = BaseScreen.WORLD_SCREEN_WIDTH;
    final float HEIGHT = BaseScreen.WORLD_SCREEN_HEIGHT;

    SpriteBatch batch;
    WorldGame worldGame;

    OrthographicCamera camera;

    Box2DDebugRenderer renderBox;

    public WorldGameRender(SpriteBatch batch, WorldGame worldGame) {

        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        this.batch = batch;
        this.worldGame = worldGame;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render() {

        camera.position.y = worldGame.player.position.y;

        if (camera.position.y < HEIGHT / 2f)
            camera.position.y = HEIGHT / 2f;
        else if (camera.position.y > HEIGHT / 2f + 3)
            camera.position.y = HEIGHT / 2f + 3;

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.enableBlending();

        drawBackground();
        drawPlatforms();
        drawPowerUps();
        drawCoins();
        drawEnemies();
        drawPlayer();

        batch.end();
    }

    private void drawBackground() {
        batch.draw(Assets.background, 0, 0, WIDTH, HEIGHT + 3);
    }

    private void drawPlatforms() {

        for (Platform obj : worldGame.arrayPlatforms) {
            TextureRegion keyFrame = Assets.platform;

            if (obj.state == Platform.STATE_BROKEN) {
                if (obj.stateTime < Assets.platformBreakAnimation.getAnimationDuration())
                    keyFrame = Assets.platformBreakAnimation.getKeyFrame(
                            obj.stateTime, false);
                else
                    continue;
            }

            if (obj.state == Platform.STATE_BREAKABLE)
                keyFrame = Assets.platformBreakAnimation.getKeyFrame(0);

            if (obj.state == Platform.STATE_CHANGING)
                batch.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .1f, .5f, .15f, 1f, .3f,
                        obj.animationScale, obj.animationScale, 0);
            else
                batch.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .15f, 1f, .3f);

            if (obj.state == Platform.STATE_FIRE)
                batch.draw(Assets.platformFireAnimation.getKeyFrame(
                                obj.stateTime, true), obj.position.x - .5f,
                        obj.position.y + .1f, 1f, .3f);
        }
    }

    private void drawPowerUps() {

        for (PowerUp powerUp : worldGame.arrayPowerUps) {
            TextureRegion keyFrame = switch (powerUp.type) {
                case PowerUp.TYPE_COIN_RAIN -> Assets.coinRainBoost;
                case PowerUp.TYPE_FREEZE -> Assets.freezeBoost;
                case PowerUp.TYPE_SUPER_JUMP -> Assets.superJumpBoost;
                default -> Assets.invincibilityBoost;
            };

            batch.draw(keyFrame, powerUp.position.x - .175f,
                    powerUp.position.y - .15f, .35f, .3f);
        }
    }

    private void drawCoins() {

        for (Coin coin : worldGame.arrayCoins) {
            batch.draw(Assets.coinAnimation.getKeyFrame(coin.stateTime, true),
                    coin.position.x - .15f, coin.position.y - .15f, .3f, .34f);
        }
    }

    public void drawEnemies() {
        for (Enemy enemy : worldGame.arrayEnemies) {
            if (enemy.state == Enemy.STATE_JUST_APPEARED) {
                batch.draw(Assets.flapSpawnRegion, enemy.position.x - .25f,
                        enemy.position.y - .25f, .25f, .25f, .5f, .5f,
                        enemy.visualScale, enemy.visualScale, 0);
                continue;
            }

            TextureRegion keyFrame;
            if (enemy.state == Enemy.STATE_FLYING) {
                if (enemy.lives >= 3)
                    keyFrame = Assets.redWingsFlapAnimation.getKeyFrame(
                            enemy.stateTime, true);
                else
                    keyFrame = Assets.blueWingsFlapAnimation.getKeyFrame(
                            enemy.stateTime, true);
            } else if (enemy.state == Enemy.STATE_EVOLVING) {
                keyFrame = Assets.evolvingFlapAnimation.getKeyFrame(enemy.stateTime, true);
            } else {
                keyFrame = Assets.flapBlueRegion;
            }

            if (enemy.velocity.x > 0)
                batch.draw(keyFrame, enemy.position.x - .285f,
                        enemy.position.y - .21f, .57f, .42f);
            else
                batch.draw(keyFrame, enemy.position.x + .285f,
                        enemy.position.y - .21f, -.57f, .42f);
        }
    }

    private void drawPlayer() {

        Player obj = worldGame.player;
        TextureRegion keyFrame;

        if (obj.slam && obj.state == Player.STATE_FALLING) {
            keyFrame = Assets.playerSlamAnimation.getKeyFrame(obj.stateTime);
            batch.draw(Assets.slamAnimation.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .4f, obj.position.y - .55f, .8f, .5f);
        } else if (obj.state == Player.STATE_FALLING
                || obj.state == Player.STATE_JUMPING) {
            keyFrame = Assets.playerJumpAnimation
                    .getKeyFrame(obj.stateTime, true);
        } else
            keyFrame = Assets.playerHitAnimation.getKeyFrame(obj.stateTime, true);

        if (obj.velocity.x > .1f)
            batch.draw(keyFrame, obj.position.x - .3f, obj.position.y - .3f,
                    .3f, .3f, .6f, .6f, 1, 1, obj.angleDegrees);
        else if (obj.velocity.x < -.1f)
            batch.draw(keyFrame, obj.position.x + .3f, obj.position.y - .3f,
                    -.3f, .3f, -.6f, .6f, 1, 1, obj.angleDegrees);
        else
            batch.draw(Assets.player, obj.position.x - .3f,
                    obj.position.y - .3f, .3f, .3f, .6f, .6f, 1, 1,
                    obj.angleDegrees);

        // This will render the boosts above the character's head.
        drawActivePowerUp(obj);
    }

    private void drawActivePowerUp(Player obj) {
        if (obj.isInvincible || obj.isSuperJump) {
            float timeToAlert = 2.5f;// Time for the boost to start flashing
            TextureRegion boostKeyFrame;
            if (obj.isInvincible) {
                if (obj.INVINCIBLE_DURATION - obj.invincibilityDuration <= timeToAlert) {
                    boostKeyFrame = Assets.invincibilityBoostEndAnimation.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.invincibilityBoost;
            } else {// jump
                if (obj.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
                    boostKeyFrame = Assets.superJumpBoostEndAnimation.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.superJumpBoost;
            }
            batch.draw(boostKeyFrame, obj.position.x - .175f,
                    obj.position.y + .3f, .35f, .3f);
        }
    }
}
