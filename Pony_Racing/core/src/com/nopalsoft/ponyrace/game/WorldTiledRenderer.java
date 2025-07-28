package com.nopalsoft.ponyrace.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.nopalsoft.ponyrace.Assets;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game_objects.*;
import com.nopalsoft.ponyrace.screens.BaseScreen;

public class WorldTiledRenderer {

    final float WIDTH = BaseScreen.WORLD_WIDTH / 10f;
    final float HEIGHT = BaseScreen.WORLD_HEIGHT / 10f;
    public OrthographicCamera OrthoCam;
    public OrthogonalTiledMapRenderer tiledRender;
    WorldTiled oWorld;
    SpriteBatch batch;
    Box2DDebugRenderer renderBox;
    SkeletonRenderer skelrender;
    float fondoLastTime, fondoStateTime;

    public WorldTiledRenderer(SpriteBatch batch, WorldTiled oWorld) {
        this.OrthoCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.OrthoCam.position.set(4, 2.4f, 0);

        this.batch = batch;
        this.oWorld = oWorld;
        tiledRender = new OrthogonalTiledMapRenderer(oWorld.game.assetsHandler.tiledMap, oWorld.m_units);
        renderBox = new Box2DDebugRenderer();
        skelrender = new SkeletonRenderer();
    }

    public void render(float delta) {
        OrthoCam.position.x = oWorld.oPony.position.x;
        OrthoCam.position.y = oWorld.oPony.position.y;

        float xMin, xMax, yMin, yMax;

        xMin = 4.0f;
        xMax = oWorld.tamanoMapaX - 4f; // Menos 4 porque es la mitad de la camara
        yMin = 2.4f;
        yMax = oWorld.tamanoMapaY - 2.4f;

        // Actualizo la camara para que no se salga de los bounds
        if (OrthoCam.position.x < xMin)
            OrthoCam.position.x = xMin;
        else if (OrthoCam.position.x > xMax)
            OrthoCam.position.x = xMax;

        if (OrthoCam.position.y < yMin)
            OrthoCam.position.y = yMin;
        else if (OrthoCam.position.y > yMax)
            OrthoCam.position.y = yMax;

        OrthoCam.update();
        batch.setProjectionMatrix(OrthoCam.combined);

        if (Settings.isBackGroundEnabled) {
            if (OrthoCam.position.x == oWorld.oPony.position.x) {
                if (oWorld.oPony.aceleration.x > 0) {
                    fondoLastTime = fondoStateTime;
                    fondoStateTime += delta;
                } else if (oWorld.oPony.aceleration.x < 0 && fondoLastTime > 0) {
                    fondoLastTime = fondoStateTime;
                    fondoStateTime -= delta;
                }
            }
            batch.begin();
            oWorld.game.assetsHandler.fondoAnim.apply(oWorld.game.assetsHandler.fondoSkeleton, fondoLastTime, fondoStateTime, true, null);
            oWorld.game.assetsHandler.fondoSkeleton.setX(OrthoCam.position.x);
            oWorld.game.assetsHandler.fondoSkeleton.setY(1.55f);
            oWorld.game.assetsHandler.fondoSkeleton.updateWorldTransform();
            oWorld.game.assetsHandler.fondoSkeleton.update(delta);
            skelrender.draw(batch, oWorld.game.assetsHandler.fondoSkeleton);
            batch.end();
        }
        renderBackGround();

        batch.begin();
        batch.enableBlending();

        renderFuegos(delta);
        renderBloodStone(delta);
        renderPonyMalo(delta);
        renderPluma(delta);
        renderPony(delta);
        renderBombas(delta);
        renderWoods();
        renderMonedas(delta);
        renderChiles(delta);
        renderGlobos(delta);
        renderDulces(delta);

        batch.end();

        if (Assets.drawDebugLines)
            renderBox.render(oWorld.oWorldBox, OrthoCam.combined);
    }

    private void renderBloodStone(float delta) {
        for (BloodStone obj : oWorld.arrBloodStone) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 3))
                continue;

            if (obj.type == BloodStone.Type.SMALL) {
                oWorld.game.assetsHandler.bloodStoneAnim.apply(oWorld.game.assetsHandler.bloodStoneSkeleton, obj.lastStateTime, obj.stateTime, true, null);
                oWorld.game.assetsHandler.bloodStoneSkeleton.setX(obj.position.x);
                oWorld.game.assetsHandler.bloodStoneSkeleton.setY(obj.position.y - .12f);
                oWorld.game.assetsHandler.bloodStoneSkeleton.updateWorldTransform();
                oWorld.game.assetsHandler.bloodStoneSkeleton.update(delta);
                skelrender.draw(batch, oWorld.game.assetsHandler.bloodStoneSkeleton);
            } else if (obj.type == BloodStone.Type.MEDIUM) {
                oWorld.game.assetsHandler.bloodStone2Anim.apply(oWorld.game.assetsHandler.bloodStone2Skeleton, obj.lastStateTime, obj.stateTime, true, null);
                oWorld.game.assetsHandler.bloodStone2Skeleton.setX(obj.position.x);
                oWorld.game.assetsHandler.bloodStone2Skeleton.setY(obj.position.y - .12f);
                oWorld.game.assetsHandler.bloodStone2Skeleton.updateWorldTransform();
                oWorld.game.assetsHandler.bloodStone2Skeleton.update(delta);
                skelrender.draw(batch, oWorld.game.assetsHandler.bloodStone2Skeleton);
            }
        }
    }

    private void renderBackGround() {

        tiledRender.setView(OrthoCam);
        tiledRender.render();
    }

    private void renderPony(float delta) {

        oWorld.oPony.animState.update(delta);

        oWorld.oPony.animState.apply(oWorld.oPony.ponySkel);

        if (oWorld.oPony.state == Pony.STATE_WALK_RIGHT) {
            oWorld.oPony.ponySkel.setFlipX(true);
            oWorld.oPony.ponySkel.getRootBone().setRotation((float) Math.toDegrees(-oWorld.oPony.angleRad));
        } else if (oWorld.oPony.state == Pony.STATE_WALK_LEFT) {
            oWorld.oPony.ponySkel.setFlipX(false);
            oWorld.oPony.ponySkel.getRootBone().setRotation((float) Math.toDegrees(oWorld.oPony.angleRad));
        }

        oWorld.oPony.ponySkel.setX(oWorld.oPony.position.x);
        oWorld.oPony.ponySkel.setY(oWorld.oPony.position.y - .26f);

        oWorld.oPony.ponySkel.updateWorldTransform();
        skelrender.draw(batch, oWorld.oPony.ponySkel);
    }

    private void renderPonyMalo(float delta) {
        for (Pony obj : oWorld.arrPonysMalos) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1f))
                continue;

            obj.animState.update(delta);
            obj.animState.apply(obj.ponySkel);

            if (obj.aceleration.x >= 0)
                obj.ponySkel.setFlipX(true);
            else if (obj.aceleration.x < 0)
                obj.ponySkel.setFlipX(false);

            obj.ponySkel.setX(obj.position.x);
            obj.ponySkel.setY(obj.position.y - .26f);

            obj.ponySkel.updateWorldTransform();
            skelrender.draw(batch, obj.ponySkel);
        }
    }

    private void renderBombas(float delta) {

        for (Bomb obj : oWorld.arrBombas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, .5f))
                continue;

            if (obj.state == Bomb.State.normal) {
                oWorld.game.assetsHandler.bombAnim.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null);
            } else {
                oWorld.game.assetsHandler.bombExAnim.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null);
            }
            obj.skelBomb.setX(obj.position.x + .025f);
            obj.skelBomb.setY(obj.position.y - .15f);
            obj.skelBomb.updateWorldTransform();
            obj.skelBomb.update(delta);

            skelrender.draw(batch, obj.skelBomb);
        }
    }

    private void renderWoods() {

        for (Wood obj : oWorld.arrWoods) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, .5f))
                continue;

            TextureRegion keyFrame;

            if (obj.tipo == Wood.Tipo.platano)
                keyFrame = oWorld.game.assetsHandler.platano;
            else
                keyFrame = oWorld.game.assetsHandler.tachuelas;

            batch.draw(keyFrame, obj.position.x - .25f, obj.position.y - .1f, .5f, .25f);
        }
    }

    private void renderMonedas(float delta) {
        for (Moneda obj : oWorld.arrMonedas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;

            Animation anim;
            if (obj.state == Moneda.State.normal)
                anim = oWorld.game.assetsHandler.monedaAnim;
            else
                anim = oWorld.game.assetsHandler.monedaTomadaAnim;

            anim.apply(obj.monedaSkeleton, obj.lastStatetime, obj.stateTime, true, null);
            obj.monedaSkeleton.setX(obj.position.x);
            obj.monedaSkeleton.setY(obj.position.y);
            obj.monedaSkeleton.updateWorldTransform();
            obj.monedaSkeleton.update(delta);
            skelrender.draw(batch, obj.monedaSkeleton);
        }
    }

    private void renderChiles(float delta) {
        for (Chile obj : oWorld.arrChiles) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;

            Animation anim;
            if (obj.state == Chile.State.normal)
                anim = oWorld.game.assetsHandler.chileAnim;
            else
                anim = oWorld.game.assetsHandler.chileTomadaAnim;

            anim.apply(obj.objSkeleton, obj.lastStatetime, obj.stateTime, true, null);
            obj.objSkeleton.setX(obj.position.x);
            obj.objSkeleton.setY(obj.position.y - .05f);
            obj.objSkeleton.updateWorldTransform();
            obj.objSkeleton.update(delta);
            skelrender.draw(batch, obj.objSkeleton);
        }
    }

    private void renderGlobos(float delta) {
        for (Globo obj : oWorld.arrGlobos) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;

            Animation anim;
            if (obj.state == Globo.State.normal)
                anim = oWorld.game.assetsHandler.globoAnim;
            else
                anim = oWorld.game.assetsHandler.globoTomadaAnim;

            anim.apply(obj.objSkeleton, obj.lastStatetime, obj.stateTime, true, null);
            obj.objSkeleton.setX(obj.position.x);
            obj.objSkeleton.setY(obj.position.y - .36f);
            obj.objSkeleton.updateWorldTransform();
            obj.objSkeleton.update(delta);
            skelrender.draw(batch, obj.objSkeleton);
        }
    }

    private void renderDulces(float delta) {
        for (Candy obj : oWorld.arrDulces) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;

            Animation anim;
            if (obj.state == Candy.State.NORMAL)
                anim = oWorld.game.assetsHandler.dulceAnim;
            else
                anim = oWorld.game.assetsHandler.dulceTomadaAnim;

            anim.apply(obj.skeleton, obj.lastStateTime, obj.stateTime, true, null);
            obj.skeleton.setX(obj.position.x);
            obj.skeleton.setY(obj.position.y - .4f);
            obj.skeleton.updateWorldTransform();
            obj.skeleton.update(delta);
            skelrender.draw(batch, obj.skeleton);
        }
    }

    private void renderPluma(float delta) {
        for (Pluma obj : oWorld.arrPlumas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;
            oWorld.game.assetsHandler.plumaAnim.apply(oWorld.game.assetsHandler.plumaSkeleton, obj.lastStatetime, obj.stateTime, true, null);
            oWorld.game.assetsHandler.plumaSkeleton.setX(obj.position.x);
            oWorld.game.assetsHandler.plumaSkeleton.setY(obj.position.y - .2f);
            oWorld.game.assetsHandler.plumaSkeleton.updateWorldTransform();
            oWorld.game.assetsHandler.plumaSkeleton.update(delta);
            skelrender.draw(batch, oWorld.game.assetsHandler.plumaSkeleton);
        }
    }

    private void renderFuegos(float delta) {

        for (Fogata obj : oWorld.arrFogatas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
                continue;
            oWorld.game.assetsHandler.fogataAnim.apply(oWorld.game.assetsHandler.fogataSkeleton, obj.lastStatetime, obj.stateTime, true, null);
            oWorld.game.assetsHandler.fogataSkeleton.setX(obj.position.x);
            oWorld.game.assetsHandler.fogataSkeleton.setY(obj.position.y);
            oWorld.game.assetsHandler.fogataSkeleton.updateWorldTransform();
            oWorld.game.assetsHandler.fogataSkeleton.update(delta);
            skelrender.draw(batch, oWorld.game.assetsHandler.fogataSkeleton);
        }
    }
}
