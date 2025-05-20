package com.nopalsoft.invaders.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.frame.*;
import com.nopalsoft.invaders.screens.Screens;


public class WorldRenderer {

    static final float FRUSTUM_WIDTH = Screens.WORLD_SCREEN_WIDTH;
    static final float FRUSTUM_HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

    World oWorld;
    OrthographicCamera cam;
    SpriteBatch batch;

    public WorldRenderer(SpriteBatch batch, World oWorld) {
        this.oWorld = oWorld;
        this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.cam.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);
        this.batch = batch;
    }

    public void render(float deltaTime) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        renderBackground(deltaTime);
        renderObjects();
    }

    private void renderBackground(float deltaTime) {
        batch.disableBlending();
        batch.begin();
        // batch.draw(Assets.fondo1, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,FRUSTUM_HEIGHT);
        batch.end();
        if (oWorld.state == World.STATE_RUNNING) {
            Assets.parallaxFondo.render(deltaTime);
        } else {// GAMEOVER, PAUSA, READY, ETC
            Assets.parallaxFondo.render(0);
        }

    }

    private void renderObjects() {
        batch.enableBlending();
        batch.begin();
        renderNave();
        renderAliens();
        renderShipBullet();
        renderAlienBullet();
        renderMissil();
        renderBoost();
        batch.end();
        if (Settings.drawDebugLines) {
            renderDebugBounds();
        }
    }

    private void renderNave() {
        TextureRegion keyFrame;
        if (oWorld.oNave.state == Nave.NAVE_STATE_NORMAL) {
            if (oWorld.oNave.velocity.x < -3)
                keyFrame = Assets.naveLeft;
            else if (oWorld.oNave.velocity.x > 3)
                keyFrame = Assets.naveRight;
            else
                keyFrame = Assets.nave;
        } else {
            keyFrame = Assets.explosionFuego.getKeyFrame(oWorld.oNave.stateTime, false);
        }
        batch.draw(keyFrame, oWorld.oNave.position.x - Nave.DRAW_WIDTH / 2f, oWorld.oNave.position.y - Nave.DRAW_HEIGHT
                / 2f, Nave.DRAW_WIDTH, Nave.DRAW_HEIGHT);

        /* Dibuja el escudo de la nave */
        if (oWorld.oNave.vidasEscudo > 0) {
            batch.draw(Assets.shield.getKeyFrame(oWorld.oNave.stateTime, true), oWorld.oNave.position.x - 5.5f,
                    oWorld.oNave.position.y - 5.5f, 11, 11);
        }

    }

    private void renderAliens() {
        int len = oWorld.alienShips.size;
        for (int i = 0; i < len; i++) {
            AlienShip oAlienShip = oWorld.alienShips.get(i);
            TextureRegion keyFrame;
            if (oAlienShip.state == AlienShip.EXPLOTING) {
                keyFrame = Assets.explosionFuego.getKeyFrame(oAlienShip.stateTime, false);
            } else {
                if (oAlienShip.vidasLeft >= 10)
                    keyFrame = Assets.alien4;
                else if (oAlienShip.vidasLeft >= 5)
                    keyFrame = Assets.alien3;
                else if (oAlienShip.vidasLeft >= 2)
                    keyFrame = Assets.alien2;
                else
                    keyFrame = Assets.alien1;
            }

            batch.draw(keyFrame, oAlienShip.position.x - AlienShip.DRAW_WIDTH / 2f, oAlienShip.position.y
                    - AlienShip.DRAW_HEIGHT / 2f, AlienShip.DRAW_WIDTH, AlienShip.DRAW_HEIGHT);
        }

    }

    private void renderShipBullet() {
        for (int i = 0; i < oWorld.shipBullets.size; i++) {
            Bullet bullet = oWorld.shipBullets.get(i);

            if (bullet.level <= 1) {
                batch.draw(Assets.balaNormal, bullet.position.x - 0.15f, bullet.position.y - 0.45f, 0.3f, 0.9f);
            } else if (bullet.level == 2) {
                batch.draw(Assets.balaNivel1, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f);
            } else if (bullet.level == 3) {
                batch.draw(Assets.balaNivel2, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f);
            } else if (bullet.level == 4) {
                batch.draw(Assets.balaNivel3, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f);
            } else {
                batch.draw(Assets.balaNivel4, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f);
            }

        }
    }


    private void renderAlienBullet() {
        int len = oWorld.alienBullets.size;
        for (int i = 0; i < len; i++) {
            Bullet oAlienBullet = oWorld.alienBullets.get(i);
            batch.draw(Assets.balaNormalEnemigo, oAlienBullet.position.x - 0.15f, oAlienBullet.position.y - 0.45f, 0.3f,
                    0.9f);
        }
    }

    private void renderMissil() {
        int len = oWorld.missiles.size;
        for (int i = 0; i < len; i++) {
            Missile oMissile = oWorld.missiles.get(i);
            float widht, heigth;
            TextureRegion keyFrame;
            switch (oMissile.state) {
                case Missile.STATE_DISPARADO:
                    keyFrame = Assets.misil.getKeyFrame(oMissile.stateTime, true);
                    widht = .8f;
                    heigth = 2.5f;
                    break;
                default:
                case Missile.STATE_EXPLOTANDO:
                    keyFrame = Assets.explosionFuego.getKeyFrame(oMissile.stateTime, false);
                    widht = heigth = 15.0f;
                    break;

            }

            // Pa cuando era perseguidor
            // batch.draw(keyFrame, oMissil.position.x-widht/2f, oMissil.position.y-heigth/2f,.5f,.5f,widht,heigth,1,1,oMissil.getVelocity().rotate(-90).angle());
            batch.draw(keyFrame, oMissile.position.x - widht / 2f, oMissile.position.y - heigth / 2f, widht, heigth);

        }
    }

    private void renderBoost() {
        int len = oWorld.boosts.size;
        for (int i = 0; i < len; i++) {
            Boost oBoost = oWorld.boosts.get(i);
            TextureRegion keyFrame;

            switch (oBoost.type) {
                case Boost.VIDA_EXTRA:
                    keyFrame = Assets.upgLife;

                    break;
                case Boost.UPGRADE_NIVEL_ARMAS:
                    keyFrame = Assets.boost1;
                    break;
                case Boost.MISSIL_EXTRA:
                    keyFrame = Assets.boost2;
                    break;
                default:// Boost.SHIELD
                    keyFrame = Assets.boost3;

            }

            batch.draw(keyFrame, oBoost.position.x - Boost.DRAW_SIZE / 2f, oBoost.position.y - Boost.DRAW_SIZE / 2f,
                    Boost.DRAW_SIZE, Boost.DRAW_SIZE);
        }
    }

    private void renderDebugBounds() {
        ShapeRenderer render = new ShapeRenderer();
        render.setProjectionMatrix(cam.combined);
        render.begin(ShapeType.Line);

        Rectangle naveBounds = oWorld.oNave.boundsRectangle;
        render.rect(naveBounds.x, naveBounds.y, naveBounds.width, naveBounds.height);

        for (AlienShip obj : oWorld.alienShips) {
            Circle objBounds = obj.boundsCircle;
            render.circle(objBounds.x, objBounds.y, objBounds.radius);
        }

        for (Boost obj : oWorld.boosts) {
            Circle objBounds = obj.boundsCircle;
            render.circle(objBounds.x, objBounds.y, objBounds.radius);
        }

        render.end();

    }

}
