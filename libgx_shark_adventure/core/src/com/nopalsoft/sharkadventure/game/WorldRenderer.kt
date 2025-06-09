package com.nopalsoft.sharkadventure.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.objects.Barrel
import com.nopalsoft.sharkadventure.objects.Blast
import com.nopalsoft.sharkadventure.objects.Chain
import com.nopalsoft.sharkadventure.objects.Items
import com.nopalsoft.sharkadventure.objects.Mine
import com.nopalsoft.sharkadventure.objects.Shark
import com.nopalsoft.sharkadventure.objects.Submarine
import com.nopalsoft.sharkadventure.objects.Torpedo
import com.nopalsoft.sharkadventure.screens.Screens

class WorldRenderer(var batch: SpriteBatch, var gameWorld: GameWorld) {
    var camera: OrthographicCamera = OrthographicCamera(Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT)

    init {
        camera.position.set(Screens.WORLD_WIDTH / 2f, Screens.WORLD_HEIGHT / 2f, 0f)
    }

    fun render(delta: Float) {
        camera.update()
        batch.setProjectionMatrix(camera.combined)

        batch.disableBlending()
        batch.begin()
        drawBackground()
        batch.end()

        drawBackgroundLayer(delta)

        batch.enableBlending()
        batch.begin()
        drawBackgroundParticles(delta)
        drawShark(delta)
        drawBarrels(delta)
        drawSubmarines()
        drawTorpedo(delta)
        drawMines()
        drawChains()
        drawItems()
        drawBlast()

        batch.end()
        drawFrontLayer(delta)
    }

    private fun drawItems() {
        for (obj in gameWorld.arrayItems) {
            val keyFrame = if (obj!!.type == Items.TYPE_MEAT) Assets.meat
            else Assets.heart

            batch.draw(
                keyFrame, obj.position.x - Items.DRAW_WIDTH / 2f, obj.position.y - Items.DRAW_HEIGHT / 2f, Items.DRAW_WIDTH,
                Items.DRAW_HEIGHT
            )
        }
    }

    private fun drawSubmarines() {
        for (obj in gameWorld.arraySubmarines) {
            val keyFrame = when (obj!!.type) {
                Submarine.TYPE_YELLOW -> Assets.yellowSubmarine
                Submarine.TYPE_RED -> Assets.redSubmarine
                else -> Assets.redSubmarine
            }

            if (obj.speed.x > 0) batch.draw(
                keyFrame, obj.position.x - Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f, Submarine.DRAW_WIDTH,
                Submarine.DRAW_HEIGHT
            )
            else batch.draw(
                keyFrame, obj.position.x + Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f,
                -Submarine.DRAW_WIDTH, Submarine.DRAW_HEIGHT
            )

            if (obj.state == Submarine.STATE_EXPLODE) {
                drawSubmarineExplosion(obj.position.x - .4f, obj.position.y, obj.explosionStateTimes[0])
                drawSubmarineExplosion(obj.position.x - .4f, obj.position.y - .4f, obj.explosionStateTimes[1])
                drawSubmarineExplosion(obj.position.x, obj.position.y, obj.explosionStateTimes[2])
                drawSubmarineExplosion(obj.position.x + .4f, obj.position.y, obj.explosionStateTimes[3])
                drawSubmarineExplosion(obj.position.x + .4f, obj.position.y - .4f, obj.explosionStateTimes[4])
            }
        }
    }

    private fun drawSubmarineExplosion(x: Float, y: Float, stateTime: Float) {
        if (stateTime >= 0 && stateTime <= Submarine.EXPLOSION_DURATION) {
            batch.draw(Assets.explosionAnimation.getKeyFrame(stateTime), x - .2f, y - .2f, .4f, .4f)
        }
    }

    private fun drawTorpedo(delta: Float) {
        for (obj in gameWorld.arrayTorpedoes) {
            if (obj!!.state == Torpedo.STATE_EXPLODE) {
                batch.draw(Assets.explosionAnimation.getKeyFrame(obj.stateTime), obj.position.x - .4f, obj.position.y - .4f, .8f, .8f)
            } else if (obj.state == Torpedo.STATE_NORMAL) {
                if (obj.isGoingLeft) {
                    batch.draw(
                        Assets.torpedo, obj.position.x + Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                        -Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT
                    )

                    Assets.torpedoBubbleRightSideParticleEffect.setPosition(obj.position.x + .34f, obj.position.y - .075f)
                    Assets.torpedoBubbleRightSideParticleEffect.draw(batch, delta)
                } else {
                    batch.draw(
                        Assets.torpedo, obj.position.x - Torpedo.DRAW_WIDTH / 2f, obj.position.y - Torpedo.DRAW_HEIGHT / 2f,
                        Torpedo.DRAW_WIDTH, Torpedo.DRAW_HEIGHT
                    )

                    Assets.torpedoBubbleLeftSideParticleEffect.setPosition(obj.position.x - .45f, obj.position.y - .075f)
                    Assets.torpedoBubbleLeftSideParticleEffect.draw(batch, delta)
                }
            }
        }
    }

    private fun drawBlast() {
        for (obj in gameWorld.arrayBlasts) {
            if (obj!!.state == Blast.STATE_HIT) {
                batch.draw(Assets.blastHit.getKeyFrame(obj.stateTime), obj.position.x - .25f, obj.position.y - .25f, .5f, .5f)
            } else {
                if (obj.velocity.x > 0) batch.draw(
                    Assets.blast, obj.position.x - Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, Blast.DRAW_WIDTH,
                    Blast.DRAW_HEIGHT
                )
                else batch.draw(
                    Assets.blast, obj.position.x + Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, -Blast.DRAW_WIDTH,
                    Blast.DRAW_HEIGHT
                )
            }
        }
    }

    private fun drawBackgroundParticles(delta: Float) {
        Assets.fishParticleEffect.setPosition(0f, 0f)
        Assets.fishParticleEffect.draw(batch, delta)

        Assets.mediumFishParticleEffect.setPosition(Screens.WORLD_WIDTH, 0f)
        Assets.mediumFishParticleEffect.draw(batch, delta)
    }

    private fun drawBackgroundLayer(delta: Float) {
        Assets.parallaxBackground.render(delta)
    }

    private fun drawBackground() {
        batch.draw(Assets.background, 0f, 0f, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT)
    }

    private fun drawFrontLayer(delta: Float) {
        Assets.parallaxForeground.render(delta)
    }

    private fun drawBarrels(delta: Float) {
        Assets.bubbleParticleEffect.update(delta)

        for (obj in gameWorld.arrayBarrels) {
            val keyframe: TextureRegion?

            if (obj!!.state == Barrel.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime)
                batch.draw(keyframe, obj.position.x - .4f, obj.position.y - .4f, .8f, .8f)
            } else if (obj.state == Barrel.STATE_NORMAL) {
                keyframe = when (obj.type) {
                    Barrel.TYPE_YELLOW -> Assets.yellowBarrel
                    Barrel.TYPE_BLACK -> Assets.blackBarrel
                    Barrel.TYPE_RED -> Assets.redBarrel
                    Barrel.TYPE_GREEN -> Assets.greenBarrel
                    else -> Assets.greenBarrel
                }

                batch.draw(
                    keyframe, obj.position.x - Barrel.DRAW_WIDTH / 2f, obj.position.y - Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH / 2f,
                    Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH, Barrel.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
                )

                Assets.bubbleParticleEffect.setPosition(obj.position.x, obj.position.y)
                Assets.bubbleParticleEffect.draw(batch)
            }
        }
    }

    private fun drawMines() {
        for (obj in gameWorld.arrayMines) {
            val keyframe: TextureRegion?

            if (obj!!.state == Mine.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation.getKeyFrame(obj.stateTime)
                batch.draw(keyframe, obj.position.x - .3f, obj.position.y - .3f, .6f, .6f)
            } else if (obj.state == Mine.STATE_NORMAL) {
                keyframe = when (obj.type) {
                    Mine.TYPE_GRAY -> Assets.grayMine
                    Mine.TYPE_RUSTY -> Assets.rustyMine
                    else -> Assets.rustyMine
                }

                batch.draw(
                    keyframe, obj.position.x - Mine.DRAW_WIDTH / 2f, obj.position.y - Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH / 2f,
                    Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH, Mine.DRAW_HEIGHT, 1f, 1f, 0f
                )
            }
        }
    }

    private fun drawChains() {
        for (obj in gameWorld.arrayChains) {
            batch.draw(
                Assets.chain, obj!!.position.x - Chain.DRAW_WIDTH / 2f, obj.position.y - Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH / 2f,
                Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH, Chain.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
            )
        }
    }

    private fun drawShark(delta: Float) {
        val obj = gameWorld.oShark

        val keyframe = if (obj.state == Shark.STATE_DEAD) {
            Assets.sharkDead
        } else if (obj.isFiring) { // Shooting overwrites everything else
            Assets.sharkFireAnimation.getKeyFrame(obj.stateTime)
        } else if (obj.isTurbo) {
            Assets.sharkDashAnimation.getKeyFrame(obj.stateTime, true)
        } else Assets.sharkSwimAnimation.getKeyFrame(obj.stateTime, true)

        if (obj.isTurbo) {
            batch.draw(Assets.turboTail, obj.position.x - 1f, obj.position.y - .27f, .96f, .48f)
        }

        if (obj.isFacingLeft) {
            batch.draw(keyframe, obj.position.x + .6f, obj.position.y - .39f, -.6f, .39f, -1.2f, .78f, 1f, 1f, obj.angleDegree)
        } else {
            batch.draw(keyframe, obj.position.x - .6f, obj.position.y - .39f, .6f, .39f, 1.2f, .78f, 1f, 1f, obj.angleDegree)
        }

        Assets.sharkBubbleParticleEffect.setPosition(obj.position.x, obj.position.y)
        Assets.sharkBubbleParticleEffect.draw(batch, delta)
    }
}
