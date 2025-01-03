package com.nopalsoft.sharkadventure.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.objects.Barrel
import com.nopalsoft.sharkadventure.objects.Blast
import com.nopalsoft.sharkadventure.objects.Chain
import com.nopalsoft.sharkadventure.objects.GameItem
import com.nopalsoft.sharkadventure.objects.Mine
import com.nopalsoft.sharkadventure.objects.Missile
import com.nopalsoft.sharkadventure.objects.Shark
import com.nopalsoft.sharkadventure.objects.Submarine
import com.nopalsoft.sharkadventure.screens.BaseScreen

class WorldRenderer(var batcher: SpriteBatch, var worldGame: WorldGame) {
    var camera: OrthographicCamera = OrthographicCamera(BaseScreen.WORLD_WIDTH, BaseScreen.WORLD_HEIGHT)

    var renderBox: Box2DDebugRenderer = Box2DDebugRenderer()

    init {
        camera.position[BaseScreen.WORLD_WIDTH / 2f, BaseScreen.WORLD_HEIGHT / 2f] = 0f
    }

    fun render(delta: Float) {
        camera.update()
        batcher.projectionMatrix = camera.combined

        batcher.disableBlending()
        batcher.begin()
        drawBackground()
        batcher.end()

        drawSueloAtras(delta)

        batcher.enableBlending()
        batcher.begin()
        drawBackgroundParticles(delta)
        drawTiburon(delta)
        drawBarriles(delta)
        drawSubmarine()
        drawTorpedo(delta)
        drawMinas(delta)
        drawChains(delta)
        drawItems()
        drawBlast()

        batcher.end()
        drawSueloFrente(delta)
    }

    private fun drawItems() {
        for (obj in worldGame.arrayItems) {
            var keyFrame = if (obj.type == GameItem.TYPE_MEAT) Assets.meat
            else Assets.heart

            batcher.draw(
                keyFrame, obj.position.x - GameItem.DRAW_WIDTH / 2f, obj.position.y - GameItem.DRAW_HEIGHT / 2f, GameItem.DRAW_WIDTH,
                GameItem.DRAW_HEIGHT
            )
        }
    }

    private fun drawSubmarine() {
        for (obj in worldGame.arraySubmarines) {
            var keyFrame = when (obj.type) {
                Submarine.TYPE_YELLOW -> Assets.yellowSubmarine
                Submarine.TYPE_RED -> Assets.redSubmarine
                else -> Assets.redSubmarine
            }
            if (obj.velocity.x > 0) batcher.draw(
                keyFrame, obj.position.x - Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f, Submarine.DRAW_WIDTH,
                Submarine.DRAW_HEIGHT
            )
            else batcher.draw(
                keyFrame, obj.position.x + Submarine.DRAW_WIDTH / 2f, obj.position.y - Submarine.DRAW_HEIGHT / 2f,
                -Submarine.DRAW_WIDTH, Submarine.DRAW_HEIGHT
            )

            if (obj.state == Submarine.STATE_EXPLODE) {
                drawExplosionSubmarine(obj.position.x - .4f, obj.position.y, obj.explosionStateTimes[0])
                drawExplosionSubmarine(obj.position.x - .4f, obj.position.y - .4f, obj.explosionStateTimes[1])
                drawExplosionSubmarine(obj.position.x, obj.position.y, obj.explosionStateTimes[2])
                drawExplosionSubmarine(obj.position.x + .4f, obj.position.y, obj.explosionStateTimes[3])
                drawExplosionSubmarine(obj.position.x + .4f, obj.position.y - .4f, obj.explosionStateTimes[4])
            }
        }
    }

    private fun drawExplosionSubmarine(x: Float, y: Float, stateTime: Float) {
        if (stateTime >= 0 && stateTime <= Submarine.EXPLOSION_DURATION) {
            batcher.draw(Assets.explosionAnimation!!.getKeyFrame(stateTime), x - .2f, y - .2f, .4f, .4f)
        }
    }

    private fun drawTorpedo(delta: Float) {
        for (obj in worldGame.arrayMissiles) {
            if (obj.state == Missile.STATE_EXPLODE) {
                batcher.draw(Assets.explosionAnimation!!.getKeyFrame(obj.stateTime), obj.position.x - .4f, obj.position.y - .4f, .8f, .8f)
            } else if (obj.state == Missile.STATE_NORMAL) {
                if (obj.isGoingLeft) {
                    batcher.draw(
                        Assets.torpedo, obj.position.x + Missile.DRAW_WIDTH / 2f, obj.position.y - Missile.DRAW_HEIGHT / 2f,
                        -Missile.DRAW_WIDTH, Missile.DRAW_HEIGHT
                    )

                    Assets.particleEffectTorpedoBubbleRightSide!!.setPosition(obj.position.x + .34f, obj.position.y - .075f)
                    Assets.particleEffectTorpedoBubbleRightSide!!.draw(batcher, delta)
                } else {
                    batcher.draw(
                        Assets.torpedo, obj.position.x - Missile.DRAW_WIDTH / 2f, obj.position.y - Missile.DRAW_HEIGHT / 2f,
                        Missile.DRAW_WIDTH, Missile.DRAW_HEIGHT
                    )

                    Assets.particleEffectTorpedoBubbleLeftSide!!.setPosition(obj.position.x - .45f, obj.position.y - .075f)
                    Assets.particleEffectTorpedoBubbleLeftSide!!.draw(batcher, delta)
                }
            }
        }
    }

    private fun drawBlast() {
        for (obj in worldGame.arrayBlasts) {
            if (obj.state == Blast.STATE_HIT) {
                batcher.draw(Assets.animationBlastHit!!.getKeyFrame(obj.stateTime), obj.position.x - .25f, obj.position.y - .25f, .5f, .5f)
            } else {
                if (obj.velocity.x > 0) batcher.draw(
                    Assets.blast, obj.position.x - Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, Blast.DRAW_WIDTH,
                    Blast.DRAW_HEIGHT
                )
                else batcher.draw(
                    Assets.blast, obj.position.x + Blast.DRAW_WIDTH / 2f, obj.position.y - Blast.DRAW_HEIGHT / 2f, -Blast.DRAW_WIDTH,
                    Blast.DRAW_HEIGHT
                )
            }
        }
    }

    private fun drawBackgroundParticles(delta: Float) {
        Assets.particleEffectFish!!.setPosition(0f, 0f)
        Assets.particleEffectFish!!.draw(batcher, delta)

        Assets.particleEffectMediumFish!!.setPosition(BaseScreen.WORLD_WIDTH, 0f)
        Assets.particleEffectMediumFish!!.draw(batcher, delta)
    }

    private fun drawSueloAtras(delta: Float) {
        Assets.parallaxBack!!.render(delta)
    }

    private fun drawBackground() {
        batcher.draw(Assets.background, 0f, 0f, BaseScreen.WORLD_WIDTH, BaseScreen.WORLD_HEIGHT)
    }

    private fun drawSueloFrente(delta: Float) {
        Assets.parallaxFront!!.render(delta)
    }

    private fun drawBarriles(delta: Float) {
        Assets.particleEffectBubble!!.update(delta)

        val i: Iterator<Barrel> = worldGame.arrayBarrels.iterator()
        while (i.hasNext()) {
            val obj = i.next()
            var keyframe: TextureRegion? = null

            if (obj.state == Barrel.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation!!.getKeyFrame(obj.stateTime)
                batcher.draw(keyframe, obj.position.x - .4f, obj.position.y - .4f, .8f, .8f)
            } else if (obj.state == Barrel.STATE_NORMAL) {
                keyframe = when (obj.type) {
                    Barrel.TYPE_YELLOW -> Assets.yellowBarrel
                    Barrel.TYPE_BLACK -> Assets.blackBarrel
                    Barrel.TYPE_RED -> Assets.redBarrel
                    Barrel.TYPE_GREEN -> Assets.greenBarrel
                    else -> Assets.greenBarrel
                }
                batcher.draw(
                    keyframe, obj.position.x - Barrel.DRAW_WIDTH / 2f, obj.position.y - Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH / 2f,
                    Barrel.DRAW_HEIGHT / 2f, Barrel.DRAW_WIDTH, Barrel.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
                )

                Assets.particleEffectBubble!!.setPosition(obj.position.x, obj.position.y)
                Assets.particleEffectBubble!!.draw(batcher)
            }
        }
    }

    private fun drawMinas(delta: Float) {
        val i: Iterator<Mine> = worldGame.arrayMines.iterator()
        while (i.hasNext()) {
            val obj = i.next()
            var keyframe: TextureRegion? = null

            if (obj.state == Mine.STATE_EXPLODE) {
                keyframe = Assets.explosionAnimation!!.getKeyFrame(obj.stateTime)
                batcher.draw(keyframe, obj.position.x - .3f, obj.position.y - .3f, .6f, .6f)
            } else if (obj.state == Mine.STATE_NORMAL) {
                keyframe = when (obj.type) {
                    Mine.TYPE_GRAY -> Assets.grayMine
                    Mine.TYPE_OXIDE -> Assets.oxideMine
                    else -> Assets.oxideMine
                }
                batcher.draw(
                    keyframe, obj.position.x - Mine.DRAW_WIDTH / 2f, obj.position.y - Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH / 2f,
                    Mine.DRAW_HEIGHT / 2f, Mine.DRAW_WIDTH, Mine.DRAW_HEIGHT, 1f, 1f, 0f
                )
            }
        }
    }

    private fun drawChains(delta: Float) {
        val i: Iterator<Chain> = worldGame.arrayChains.iterator()
        while (i.hasNext()) {
            val obj = i.next()

            batcher.draw(
                Assets.chain, obj.position.x - Chain.DRAW_WIDTH / 2f, obj.position.y - Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH / 2f,
                Chain.DRAW_HEIGHT / 2f, Chain.DRAW_WIDTH, Chain.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
            )
        }
    }

    private fun drawTiburon(delta: Float) {
        val obj = worldGame.shark

        var keyframe: TextureRegion? = null

        keyframe = if (obj.state == Shark.STATE_DEAD) {
            Assets.SharkDead
        } else if (obj.isFiring) { // Disparar sobreescribe todo lo demas
            Assets.animationSharkFire!!.getKeyFrame(obj.stateTime)
        } else if (obj.isTurbo) {
            Assets.animationSharkMove!!.getKeyFrame(obj.stateTime, true)
        } else Assets.animationSharkSwim!!.getKeyFrame(obj.stateTime, true)

        if (obj.isTurbo) {
            batcher.draw(Assets.TurboTail, obj.position.x - 1f, obj.position.y - .27f, .96f, .48f)
        }

        if (obj.isFacingLeft) {
            batcher.draw(keyframe, obj.position.x + .6f, obj.position.y - .39f, -.6f, .39f, -1.2f, .78f, 1f, 1f, obj.angleDegree)
        } else {
            batcher.draw(keyframe, obj.position.x - .6f, obj.position.y - .39f, .6f, .39f, 1.2f, .78f, 1f, 1f, obj.angleDegree)
        }

        Assets.particleEffectSharkBubble!!.setPosition(obj.position.x, obj.position.y)
        Assets.particleEffectSharkBubble!!.draw(batcher, delta)
    }
}
