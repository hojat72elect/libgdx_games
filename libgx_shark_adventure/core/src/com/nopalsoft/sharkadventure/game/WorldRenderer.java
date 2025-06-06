package com.nopalsoft.sharkadventure.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.objects.Barrel;
import com.nopalsoft.sharkadventure.objects.Blast;
import com.nopalsoft.sharkadventure.objects.Chain;
import com.nopalsoft.sharkadventure.objects.Items;
import com.nopalsoft.sharkadventure.objects.Mina;
import com.nopalsoft.sharkadventure.objects.Submarino;
import com.nopalsoft.sharkadventure.objects.Tiburon;
import com.nopalsoft.sharkadventure.objects.Torpedo;
import com.nopalsoft.sharkadventure.screens.Screens;

import java.util.Iterator;

public class WorldRenderer {

    SpriteBatch batcher;
    OrthographicCamera oCam;

    GameWorld oWorld;

    Box2DDebugRenderer renderBox;

    public WorldRenderer(SpriteBatch batcher, GameWorld oWorld) {
        this.batcher = batcher;
        this.oWorld = oWorld;
        renderBox = new Box2DDebugRenderer();

        oCam = new OrthographicCamera(Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT);
        oCam.position.set(Screens.WORLD_WIDTH / 2f, Screens.WORLD_HEIGHT / 2f, 0);

    }

    public void render(float delta) {

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.disableBlending();
        batcher.begin();
        drawBackground();
        batcher.end();

        drawSueloAtras(delta);

        batcher.enableBlending();
        batcher.begin();
        drawParticulasFondo(delta);
        drawTiburon(delta);
        drawBarriles(delta);
        drawSubmarinos();
        drawTorpedos(delta);
        drawMinas(delta);
        drawChains(delta);
        drawItems();
        drawBlast(delta);

        batcher.end();
        drawSueloFrente(delta);

    }

    private void drawItems() {
        for (Items obj : oWorld.arrItems) {
            AtlasRegion keyFrame;
            if (obj.tipo == Items.TIPO_CARNE)
                keyFrame = Assets.meat;
            else
                keyFrame = Assets.heart;

            batcher.draw(keyFrame, obj.position.x - Items.DRAW_WIDTH / 2f, obj.position.y - Items.DRAW_HEIGHT / 2f, Items.DRAW_WIDTH,
                    Items.DRAW_HEIGHT);
        }
    }

    private void drawSubmarinos() {
        for (Submarino obj : oWorld.arrSubmarinos) {
            AtlasRegion keyFrame;
            switch (obj.tipo) {
                case Submarino.TIPO_AMARILLO:
                    keyFrame = Assets.yellowSubmarine;
                    break;

                default:
                case Submarino.TIPO_ROJO:
                    keyFrame = Assets.redSubmarine;
                    break;
            }

            if (obj.velocity.x > 0)
                batcher.draw(keyFrame, obj.position.x - Submarino.DRAW_WIDTH / 2f, obj.position.y - Submarino.DRAW_HEIGHT / 2f, Submarino.DRAW_WIDTH,
                        Submarino.DRAW_HEIGHT);
            else
                batcher.draw(keyFrame, obj.position.x + Submarino.DRAW_WIDTH / 2f, obj.position.y - Submarino.DRAW_HEIGHT / 2f,
                        -Submarino.DRAW_WIDTH, Submarino.DRAW_HEIGHT);

            if (obj.state == Submarino.STATE_EXPLODE) {
                drawExplosionSubmarino(obj.position.x - .4f, obj.position.y, obj.explosionStateTimes[0]);
                drawExplosionSubmarino(obj.position.x - .4f, obj.position.y - .4f, obj.explosionStateTimes[1]);
                drawExplosionSubmarino(obj.position.x, obj.position.y, obj.explosionStateTimes[2]);
                drawExplosionSubmarino(obj.position.x + .4f, obj.position.y, obj.explosionStateTimes[3]);
                drawExplosionSubmarino(obj.position.x + .4f, obj.position.y - .4f, obj.explosionStateTimes[4]);
            }
        }
    }

    private void drawExplosionSubmarino(float x, float y, float stateTime) {
        if (stateTime >= 0 && stateTime <= Submarino.DURATION_EXPLOTION) {
            batcher.draw(Assets.explosionAnimation.getKeyFrame(stateTime), x - .2f, y - .2f, .4f, .4f);
        }
    }

    private void drawTorpedos(float delta) {
        Iterator<Torpedo> i = oWorld.arrTorpedos.iterator();
        while (i.hasNext()) {
            Torpedo obj = i.next();

            if (obj.state == Torpedo.STATE_EXPLODE) {
                batcher.draw(Assets.explosionAnimation.getKeyFrame(obj.stateTime), obj.position.x - .4f, obj.position.y - .4f, .8f, .8f);
            } else if (obj.state == Torpedo.STATE_NORMAL) {

                if (obj.isGoingLeft) {
                    batcher.draw(Assets.torpedo, obj.position.x + Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                            -Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT);

                    Assets.torpedoBubbleRightSideParticleEffect.setPosition(obj.position.x + .34f, obj.position.y - .075f);
                    Assets.torpedoBubbleRightSideParticleEffect.draw(batcher, delta);
                } else {
                    batcher.draw(Assets.torpedo, obj.position.x - Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                            Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT);

                    Assets.torpedoBubbleLeftSideParticleEffect.setPosition(obj.position.x - .45f, obj.position.y - .075f);
                    Assets.torpedoBubbleLeftSideParticleEffect.draw(batcher, delta);
                }
            }
        }
    }

    private void drawBlast(float delta) {
        Iterator<Blast> i = oWorld.arrBlasts.iterator();
        while (i.hasNext()) {
            Blast obj = i.next();

            if (obj.state == Blast.STATE_HIT) {
                batcher.draw(Assets.blastHit.getKeyFrame(obj.stateTime), obj.position.x - .25f, obj.position.y - .25f, .5f, .5f);
            } else {

                if (obj.velocity.x > 0)
                    batcher.draw(Assets.blast, obj.position.x - Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, Blast.DRAW_WIDTH,
                            Blast.DRAW_HEIGHT);
                else
                    batcher.draw(Assets.blast, obj.position.x + Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, -Blast.DRAW_WIDTH,
                            Blast.DRAW_HEIGHT);
            }
        }
    }

    private void drawParticulasFondo(float delta) {
        Assets.fishParticleEffect.setPosition(0, 0);
        Assets.fishParticleEffect.draw(batcher, delta);

        Assets.mediumFishParticleEffect.setPosition(Screens.WORLD_WIDTH, 0);
        Assets.mediumFishParticleEffect.draw(batcher, delta);
    }

    private void drawSueloAtras(float delta) {
        Assets.parallaxBackground.render(delta);
    }

    private void drawBackground() {
        batcher.draw(Assets.background, 0, 0, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT);
    }

    private void drawSueloFrente(float delta) {
        Assets.parallaxForeground.render(delta);
    }

    private void drawBarriles(float delta) {

        Assets.bubbleParticleEffect.update(delta);

        Iterator<Barrel> i = oWorld.arrBarriles.iterator();
        while (i.hasNext()) {
            Barrel obj = i.next();
            TextureRegion keyframe = null;

            if (obj.state == Barrel.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime);
                batcher.draw(keyframe, obj.position.x - .4f, obj.position.y - .4f, .8f, .8f);
            } else if (obj.state == Barrel.STATE_NORMAL) {

                switch (obj.type) {
                    case Barrel.TYPE_YELLOW:
                        keyframe = Assets.yellowBarrel;
                        break;
                    case Barrel.TYPE_BLACK:
                        keyframe = Assets.blackBarrel;
                        break;
                    case Barrel.TYPE_RED:
                        keyframe = Assets.redBarrel;
                        break;
                    default:
                    case Barrel.TYPE_GREEN:
                        keyframe = Assets.greenBarrel;
                        break;
                }

                batcher.draw(keyframe, obj.position.x - Barrel.DRAW_WIDTH / 2f, obj.position.y - Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH / 2f,
                        Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH, Barrel.DRAW_HEIGHT, 1, 1, obj.angleDegree);

                Assets.bubbleParticleEffect.setPosition(obj.position.x, obj.position.y);
                Assets.bubbleParticleEffect.draw(batcher);
            }
        }
    }

    private void drawMinas(float delta) {
        Iterator<Mina> i = oWorld.arrMinas.iterator();
        while (i.hasNext()) {
            Mina obj = i.next();
            TextureRegion keyframe = null;

            if (obj.state == Mina.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime);
                batcher.draw(keyframe, obj.position.x - .3f, obj.position.y - .3f, .6f, .6f);
            } else if (obj.state == Mina.STATE_NORMAL) {

                switch (obj.tipo) {
                    case Mina.TIPO_GRIS:
                        keyframe = Assets.grayMine;
                        break;
                    case Mina.TIPO_OXIDO:
                    default:
                        keyframe = Assets.rustyMine;
                        break;
                }

                batcher.draw(keyframe, obj.position.x - Mina.DRAW_WIDTH / 2f, obj.position.y - Mina.DRAW_HEIGHT / 2f, Mina.DRAW_WIDTH / 2f,
                        Mina.DRAW_HEIGHT / 2f, Mina.DRAW_WIDTH, Mina.DRAW_HEIGHT, 1, 1, 0);
            }
        }
    }

    private void drawChains(float delta) {
        Iterator<Chain> i = oWorld.arrChains.iterator();
        while (i.hasNext()) {
            Chain obj = i.next();

            batcher.draw(Assets.chain, obj.position.x - Chain.DRAW_WIDTH / 2f, obj.position.y - Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH / 2f,
                    Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH, Chain.DRAW_HEIGHT, 1, 1, obj.angleDeg);
        }
    }

    private void drawTiburon(float delta) {
        Tiburon obj = oWorld.oTiburon;

        TextureRegion keyframe = null;

        if (obj.state == Tiburon.STATE_DEAD) {
            keyframe = Assets.sharkDead;
        } else if (obj.isFiring) {// Disparar sobreescribe todo lo demas
            keyframe = Assets.sharkFireAnimation.getKeyFrame(obj.stateTime);
        } else if (obj.isTurbo) {
            keyframe = Assets.sharkDashAnimation.getKeyFrame(obj.stateTime, true);
        } else
            keyframe = Assets.sharkSwimAnimation.getKeyFrame(obj.stateTime, true);

        if (obj.isTurbo) {
            batcher.draw(Assets.turboTail, obj.position.x - 1f, obj.position.y - .27f, .96f, .48f);
        }

        if (obj.isFacingLeft) {
            batcher.draw(keyframe, obj.position.x + .6f, obj.position.y - .39f, -.6f, .39f, -1.2f, .78f, 1, 1, obj.angleDeg);
        } else {
            batcher.draw(keyframe, obj.position.x - .6f, obj.position.y - .39f, .6f, .39f, 1.2f, .78f, 1, 1, obj.angleDeg);
        }

        Assets.sharkBubbleParticleEffect.setPosition(obj.position.x, obj.position.y);
        Assets.sharkBubbleParticleEffect.draw(batcher, delta);
    }
}
