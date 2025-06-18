package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.objetos.Coin;
import com.nopalsoft.slamthebird.objetos.Enemy;
import com.nopalsoft.slamthebird.objetos.Platform;
import com.nopalsoft.slamthebird.objetos.Player;
import com.nopalsoft.slamthebird.objetos.PowerUp;
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

        renderFondo();
        renderPlataformas();
        renderBoost();
        renderCoins();
        renderEnemies();
        renderPlayer();

        batch.end();
    }

    private void renderFondo() {
        batch.draw(Assets.background, 0, 0, WIDTH, HEIGHT + 3);
    }

    private void renderPlataformas() {

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

    private void renderBoost() {

        for (PowerUp obj : worldGame.arrayPowerUps) {
            TextureRegion keyFrame = switch (obj.type) {
                case PowerUp.TYPE_COIN_RAIN -> Assets.coinRainBoost;
                case PowerUp.TYPE_FREEZE -> Assets.freezeBoost;
                case PowerUp.TYPE_SUPER_JUMP -> Assets.superJumpBoost;
                default -> Assets.invincibilityBoost;
            };

            batch.draw(keyFrame, obj.position.x - .175f,
                    obj.position.y - .15f, .35f, .3f);
        }
    }

    private void renderCoins() {

        for (Coin obj : worldGame.arrayCoins) {
            batch.draw(Assets.coinAnimation.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .15f, obj.position.y - .15f, .3f, .34f);
        }
    }

    public void renderEnemies() {
        for (Enemy obj : worldGame.arrayEnemies) {
            if (obj.state == Enemy.STATE_JUST_APPEARED) {
                batch.draw(Assets.flapSpawnRegion, obj.position.x - .25f,
                        obj.position.y - .25f, .25f, .25f, .5f, .5f,
                        obj.visualScale, obj.visualScale, 0);
                continue;
            }

            TextureRegion keyFrame;
            if (obj.state == Enemy.STATE_FLYING) {
                if (obj.lives >= 3)
                    keyFrame = Assets.redWingsFlapAnimation.getKeyFrame(
                            obj.stateTime, true);
                else
                    keyFrame = Assets.blueWingsFlapAnimation.getKeyFrame(
                            obj.stateTime, true);
            } else if (obj.state == Enemy.STATE_EVOLVING) {
                keyFrame = Assets.evolvingFlapAnimation.getKeyFrame(obj.stateTime, true);
            } else {
                keyFrame = Assets.flapBlueRegion;
            }

            if (obj.velocity.x > 0)
                batch.draw(keyFrame, obj.position.x - .285f,
                        obj.position.y - .21f, .57f, .42f);
            else
                batch.draw(keyFrame, obj.position.x + .285f,
                        obj.position.y - .21f, -.57f, .42f);
        }
    }

    private void renderPlayer() {

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

        // c

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

        // TODO el personaje cuando se muere no tiene velocidad por lo que no aparece el keyframe sino que agarra assetspersonaje

        // Esto renderear los boost arriba de la cabeza del personaje
        renderBoostActivo(obj);
    }

    private void renderBoostActivo(Player obj) {
        if (obj.isInvincible || obj.isSuperJump) {
            float timeToAlert = 2.5f;// Tiempo para que empieze a parpaderar el boost
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
            // batcher.draw(boostKeyFrame, obj.position.x - .0875f, obj.position.y + .3f, .175f, .15f);
            batch.draw(boostKeyFrame, obj.position.x - .175f,
                    obj.position.y + .3f, .35f, .3f);
        }
    }
}
