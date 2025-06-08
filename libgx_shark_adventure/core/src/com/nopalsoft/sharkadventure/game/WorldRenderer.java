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
import com.nopalsoft.sharkadventure.objects.Mine;
import com.nopalsoft.sharkadventure.objects.Shark;
import com.nopalsoft.sharkadventure.objects.Submarine;
import com.nopalsoft.sharkadventure.objects.Torpedo;
import com.nopalsoft.sharkadventure.screens.Screens;

public class WorldRenderer {

    SpriteBatch batch;
    OrthographicCamera camera;

    GameWorld gameWorld;

    Box2DDebugRenderer renderBox;

    public WorldRenderer(SpriteBatch batch, GameWorld gameWorld) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        renderBox = new Box2DDebugRenderer();

        camera = new OrthographicCamera(Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT);
        camera.position.set(Screens.WORLD_WIDTH / 2f, Screens.WORLD_HEIGHT / 2f, 0);

    }

    public void render(float delta) {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.disableBlending();
        batch.begin();
        drawBackground();
        batch.end();

        drawBackgroundLayer(delta);

        batch.enableBlending();
        batch.begin();
        drawBackgroundParticles(delta);
        drawShark(delta);
        drawBarrels(delta);
        drawSubmarines();
        drawTorpedo(delta);
        drawMines();
        drawChains();
        drawItems();
        drawBlast();

        batch.end();
        drawFrontLayer(delta);

    }

    private void drawItems() {
        for (Items obj : gameWorld.arrayItems) {
            AtlasRegion keyFrame;
            if (obj.type == Items.TYPE_MEAT)
                keyFrame = Assets.meat;
            else
                keyFrame = Assets.heart;

            batch.draw(keyFrame, obj.position.x - Items.DRAW_WIDTH / 2f, obj.position.y - Items.DRAW_HEIGHT / 2f, Items.DRAW_WIDTH,
                    Items.DRAW_HEIGHT);
        }
    }

    private void drawSubmarines() {
        for (Submarine obj : gameWorld.arraySubmarines) {
            AtlasRegion keyFrame;
            switch (obj.type) {
                case Submarine.TYPE_YELLOW:
                    keyFrame = Assets.yellowSubmarine;
                    break;

                default:
                case Submarine.TYPE_RED:
                    keyFrame = Assets.redSubmarine;
                    break;
            }

            if (obj.speed.x > 0)
                batch.draw(keyFrame, obj.position.x - Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f, Submarine.DRAW_WIDTH,
                        Submarine.DRAW_HEIGHT);
            else
                batch.draw(keyFrame, obj.position.x + Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f,
                        -Submarine.DRAW_WIDTH, Submarine.DRAW_HEIGHT);

            if (obj.state == Submarine.STATE_EXPLODE) {
                drawSubmarineExplosion(obj.position.x - .4f, obj.position.y, obj.explosionStateTimes[0]);
                drawSubmarineExplosion(obj.position.x - .4f, obj.position.y - .4f, obj.explosionStateTimes[1]);
                drawSubmarineExplosion(obj.position.x, obj.position.y, obj.explosionStateTimes[2]);
                drawSubmarineExplosion(obj.position.x + .4f, obj.position.y, obj.explosionStateTimes[3]);
                drawSubmarineExplosion(obj.position.x + .4f, obj.position.y - .4f, obj.explosionStateTimes[4]);
            }
        }
    }

    private void drawSubmarineExplosion(float x, float y, float stateTime) {
        if (stateTime >= 0 && stateTime <= Submarine.EXPLOSION_DURATION) {
            batch.draw(Assets.explosionAnimation.getKeyFrame(stateTime), x - .2f, y - .2f, .4f, .4f);
        }
    }

    private void drawTorpedo(float delta) {
        for (Torpedo obj : gameWorld.arrayTorpedoes) {
            if (obj.state == Torpedo.STATE_EXPLODE) {
                batch.draw(Assets.explosionAnimation.getKeyFrame(obj.stateTime), obj.position.x - .4f, obj.position.y - .4f, .8f, .8f);
            } else if (obj.state == Torpedo.STATE_NORMAL) {

                if (obj.isGoingLeft) {
                    batch.draw(Assets.torpedo, obj.position.x + Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                            -Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT);

                    Assets.torpedoBubbleRightSideParticleEffect.setPosition(obj.position.x + .34f, obj.position.y - .075f);
                    Assets.torpedoBubbleRightSideParticleEffect.draw(batch, delta);
                } else {
                    batch.draw(Assets.torpedo, obj.position.x - Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                            Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT);

                    Assets.torpedoBubbleLeftSideParticleEffect.setPosition(obj.position.x - .45f, obj.position.y - .075f);
                    Assets.torpedoBubbleLeftSideParticleEffect.draw(batch, delta);
                }
            }
        }
    }

    private void drawBlast() {
        for (Blast obj : gameWorld.arrayBlasts) {
            if (obj.state == Blast.STATE_HIT) {
                batch.draw(Assets.blastHit.getKeyFrame(obj.stateTime), obj.position.x - .25f, obj.position.y - .25f, .5f, .5f);
            } else {

                if (obj.velocity.x > 0)
                    batch.draw(Assets.blast, obj.position.x - Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, Blast.DRAW_WIDTH,
                            Blast.DRAW_HEIGHT);
                else
                    batch.draw(Assets.blast, obj.position.x + Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, -Blast.DRAW_WIDTH,
                            Blast.DRAW_HEIGHT);
            }
        }
    }

    private void drawBackgroundParticles(float delta) {
        Assets.fishParticleEffect.setPosition(0, 0);
        Assets.fishParticleEffect.draw(batch, delta);

        Assets.mediumFishParticleEffect.setPosition(Screens.WORLD_WIDTH, 0);
        Assets.mediumFishParticleEffect.draw(batch, delta);
    }

    private void drawBackgroundLayer(float delta) {
        Assets.parallaxBackground.render(delta);
    }

    private void drawBackground() {
        batch.draw(Assets.background, 0, 0, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT);
    }

    private void drawFrontLayer(float delta) {
        Assets.parallaxForeground.render(delta);
    }

    private void drawBarrels(float delta) {

        Assets.bubbleParticleEffect.update(delta);

        for (Barrel obj : gameWorld.arrayBarrels) {
            TextureRegion keyframe;

            if (obj.state == Barrel.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime);
                batch.draw(keyframe, obj.position.x - .4f, obj.position.y - .4f, .8f, .8f);
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

                batch.draw(keyframe, obj.position.x - Barrel.DRAW_WIDTH / 2f, obj.position.y - Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH / 2f,
                        Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH, Barrel.DRAW_HEIGHT, 1, 1, obj.angleDegree);

                Assets.bubbleParticleEffect.setPosition(obj.position.x, obj.position.y);
                Assets.bubbleParticleEffect.draw(batch);
            }
        }
    }

    private void drawMines() {
        for (Mine obj : gameWorld.arrayMines) {
            TextureRegion keyframe;

            if (obj.state == Mine.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime);
                batch.draw(keyframe, obj.position.x - .3f, obj.position.y - .3f, .6f, .6f);
            } else if (obj.state == Mine.STATE_NORMAL) {

                switch (obj.type) {
                    case Mine.TYPE_GRAY:
                        keyframe = Assets.grayMine;
                        break;
                    case Mine.TYPE_RUSTY:
                    default:
                        keyframe = Assets.rustyMine;
                        break;
                }

                batch.draw(keyframe, obj.position.x - Mine.DRAW_WIDTH / 2f, obj.position.y - Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH / 2f,
                        Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH, Mine.DRAW_HEIGHT, 1, 1, 0);
            }
        }
    }

    private void drawChains() {
        for (Chain obj : gameWorld.arrayChains) {
            batch.draw(Assets.chain, obj.position.x - Chain.DRAW_WIDTH / 2f, obj.position.y - Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH / 2f,
                    Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH, Chain.DRAW_HEIGHT, 1, 1, obj.angleDegree);
        }
    }

    private void drawShark(float delta) {
        Shark obj = gameWorld.oShark;

        TextureRegion keyframe;

        if (obj.state == Shark.STATE_DEAD) {
            keyframe = Assets.sharkDead;
        } else if (obj.isFiring) {// Shooting overwrites everything else
            keyframe = Assets.sharkFireAnimation.getKeyFrame(obj.stateTime);
        } else if (obj.isTurbo) {
            keyframe = Assets.sharkDashAnimation.getKeyFrame(obj.stateTime, true);
        } else
            keyframe = Assets.sharkSwimAnimation.getKeyFrame(obj.stateTime, true);

        if (obj.isTurbo) {
            batch.draw(Assets.turboTail, obj.position.x - 1f, obj.position.y - .27f, .96f, .48f);
        }

        if (obj.isFacingLeft) {
            batch.draw(keyframe, obj.position.x + .6f, obj.position.y - .39f, -.6f, .39f, -1.2f, .78f, 1, 1, obj.angleDegree);
        } else {
            batch.draw(keyframe, obj.position.x - .6f, obj.position.y - .39f, .6f, .39f, 1.2f, .78f, 1, 1, obj.angleDegree);
        }

        Assets.sharkBubbleParticleEffect.setPosition(obj.position.x, obj.position.y);
        Assets.sharkBubbleParticleEffect.draw(batch, delta);
    }
}
