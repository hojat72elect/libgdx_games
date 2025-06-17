package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.objetos.Boost;
import com.nopalsoft.slamthebird.objetos.Coin;
import com.nopalsoft.slamthebird.objetos.Enemy;
import com.nopalsoft.slamthebird.objetos.Platform;
import com.nopalsoft.slamthebird.objetos.Robot;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class WorldGameRender {

    final float WIDTH = BaseScreen.WORLD_SCREEN_WIDTH;
    final float HEIGHT = BaseScreen.WORLD_SCREEN_HEIGHT;

    SpriteBatch batcher;
    WorldGame oWorld;

    OrthographicCamera oCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {

        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        this.batcher = batcher;
        this.oWorld = oWorld;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render() {

        oCam.position.y = oWorld.oRobo.position.y;

        if (oCam.position.y < HEIGHT / 2f)
            oCam.position.y = HEIGHT / 2f;
        else if (oCam.position.y > HEIGHT / 2f + 3)
            oCam.position.y = HEIGHT / 2f + 3;

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();

        batcher.enableBlending();

        renderFondo();
        renderPlataformas();
        renderBoost();
        renderMonedas();
        renderEnemigos();
        renderPersonaje();

        batcher.end();
//		renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private void renderFondo() {
        batcher.draw(Assets.fondo, 0, 0, WIDTH, HEIGHT + 3);
    }

    private void renderPlataformas() {

        for (Platform obj : oWorld.arrPlataformas) {
            TextureRegion keyFrame = Assets.plataforma;

            if (obj.state == Platform.STATE_BROKEN) {
                if (obj.stateTime < Assets.plataformBreakable.getAnimationDuration())
                    keyFrame = Assets.plataformBreakable.getKeyFrame(
                            obj.stateTime, false);
                else
                    continue;
            }

            if (obj.state == Platform.STATE_BREAKABLE)
                keyFrame = Assets.plataformBreakable.getKeyFrame(0);

            if (obj.state == Platform.STATE_CHANGING)
                batcher.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .1f, .5f, .15f, 1f, .3f,
                        obj.animationScale, obj.animationScale, 0);
            else
                batcher.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .15f, 1f, .3f);

            if (obj.state == Platform.STATE_FIRE)
                batcher.draw(Assets.animPlataformFire.getKeyFrame(
                                obj.stateTime, true), obj.position.x - .5f,
                        obj.position.y + .1f, 1f, .3f);
        }
    }

    private void renderBoost() {

        for (Boost obj : oWorld.arrBoost) {
            TextureRegion keyFrame = switch (obj.type) {
                case Boost.TYPE_COIN_RAIN -> Assets.boostCoinRain;
                case Boost.TYPE_FREEZE -> Assets.boostIce;
                case Boost.TYPE_SUPER_JUMP -> Assets.boostSuperSalto;
                default -> Assets.boostInvencible;
            };

            batcher.draw(keyFrame, obj.position.x - .175f,
                    obj.position.y - .15f, .35f, .3f);
        }
    }

    private void renderMonedas() {

        for (Coin obj : oWorld.arrMonedas) {
            batcher.draw(Assets.animMoneda.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .15f, obj.position.y - .15f, .3f, .34f);
        }
    }

    public void renderEnemigos() {
        for (Enemy obj : oWorld.arrEnemigos) {
            if (obj.state == Enemy.STATE_JUST_APPEARED) {
                batcher.draw(Assets.flapSpawn, obj.position.x - .25f,
                        obj.position.y - .25f, .25f, .25f, .5f, .5f,
                        obj.visualScale, obj.visualScale, 0);
                continue;
            }

            TextureRegion keyFrame;
            if (obj.state == Enemy.STATE_FLYING) {
                if (obj.lives >= 3)
                    keyFrame = Assets.animflapAlasRojo.getKeyFrame(
                            obj.stateTime, true);
                else
                    keyFrame = Assets.animflapAlasAzul.getKeyFrame(
                            obj.stateTime, true);
            } else if (obj.state == Enemy.STATE_EVOLVING) {
                keyFrame = Assets.animEvolving.getKeyFrame(obj.stateTime, true);
            } else {
                keyFrame = Assets.flapAzul;
            }

            if (obj.velocity.x > 0)
                batcher.draw(keyFrame, obj.position.x - .285f,
                        obj.position.y - .21f, .57f, .42f);
            else
                batcher.draw(keyFrame, obj.position.x + .285f,
                        obj.position.y - .21f, -.57f, .42f);
        }
    }

    private void renderPersonaje() {

        Robot obj = oWorld.oRobo;
        TextureRegion keyFrame;

        if (obj.slam && obj.state == Robot.STATE_FALLING) {
            keyFrame = Assets.animPersonajeSlam.getKeyFrame(obj.stateTime);
            batcher.draw(Assets.slam.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .4f, obj.position.y - .55f, .8f, .5f);
        } else if (obj.state == Robot.STATE_FALLING
                || obj.state == Robot.STATE_JUMPING) {
            keyFrame = Assets.animPersonajeJump
                    .getKeyFrame(obj.stateTime, true);
        } else
            keyFrame = Assets.animPersonajeHit.getKeyFrame(obj.stateTime, true);

        // c

        if (obj.velocity.x > .1f)
            batcher.draw(keyFrame, obj.position.x - .3f, obj.position.y - .3f,
                    .3f, .3f, .6f, .6f, 1, 1, obj.angleDegrees);
        else if (obj.velocity.x < -.1f)
            batcher.draw(keyFrame, obj.position.x + .3f, obj.position.y - .3f,
                    -.3f, .3f, -.6f, .6f, 1, 1, obj.angleDegrees);
        else
            batcher.draw(Assets.personaje, obj.position.x - .3f,
                    obj.position.y - .3f, .3f, .3f, .6f, .6f, 1, 1,
                    obj.angleDegrees);

        // TODO el personaje cuando se muere no tiene velocidad por lo que no aparece el keyframe sino que agarra assetspersonaje

        // Esto renderear los boost arriba de la cabeza del personaje
        renderBoostActivo(obj);
    }

    private void renderBoostActivo(Robot obj) {
        if (obj.isInvincible || obj.isSuperJump) {
            float timeToAlert = 2.5f;// Tiempo para que empieze a parpaderar el boost
            TextureRegion boostKeyFrame;
            if (obj.isInvincible) {
                if (obj.INVINCIBLE_DURATION - obj.invincibilityDuration <= timeToAlert) {
                    boostKeyFrame = Assets.animBoostEndInvencible.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.boostInvencible;
            } else {// jump
                if (obj.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
                    boostKeyFrame = Assets.animBoostEndSuperSalto.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.boostSuperSalto;
            }
            // batcher.draw(boostKeyFrame, obj.position.x - .0875f, obj.position.y + .3f, .175f, .15f);
            batcher.draw(boostKeyFrame, obj.position.x - .175f,
                    obj.position.y + .3f, .35f, .3f);
        }
    }
}
