package com.nopalsoft.zombiewars.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.zombiewars.Assets;
import com.nopalsoft.zombiewars.Settings;
import com.nopalsoft.zombiewars.objetos.Bullet;
import com.nopalsoft.zombiewars.objetos.Personajes;
import com.nopalsoft.zombiewars.screens.Screens;

public class WorldGame {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;
    static final int STATE_NEXT_LEVEL = 2;
    public int state;

    final float TIME_TO_SPAWN_ZOMBIE_FRANK;
    float timeToSpwanZombieFrank;

    final float TIME_TO_SPAWN_ZOMBIE_CUASY;
    float timeToSpwanZombieCuasy;

    final float TIME_TO_SPAWN_ZOMBIE_KID;
    float timeToSpwanZombieKid;

    final float TIME_TO_SPAWN_ZOMBIE_MUMMY;
    float timeToSpwanZombieMummy;

    final float TIME_TO_SPAWN_ZOMBIE_PAN;
    float timeToSpwanZombiePan;

    /**
     * Mis tiles son de 32px, asi que la unidad seria 1/32 con una camara ortograpicha de 10x15 para ver 10 tiles de ancho y 15 de alto. El probema es que mi camara es de 8x4.8f por eso tengo que
     * cambiar la escala, con 1/32 solo veria 8 tiles a lo ancho y de altura 4.8 por como esta configurada la camara.
     * <p>
     * con 1/96 veo 24 tiles a lo ancho
     */
    float unitScale = 1 / 76f;
    public int tiledWidth;
    public int tiledHeight;

    float xMin, xMax, yMin;

    public World oWorldBox;
    public ObjectCreatorManagerBox2d objectCreatorManager;

    public Vector2 posCamara;

    Array<Personajes> arrFacingRight;
    Array<Personajes> arrFacingLeft;
    Array<Bullet> arrBullets;
    Array<Body> arrBodies;

    public WorldGame(int nivel) {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        arrFacingRight = new Array<Personajes>();
        arrFacingLeft = new Array<Personajes>();
        arrBullets = new Array<Bullet>();
        arrBodies = new Array<Body>();

        objectCreatorManager = new ObjectCreatorManagerBox2d(this);
        new TiledMapManagerBox2d(this, unitScale).createObjetosDesdeTiled(Assets.map);
        tiledWidth = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getWidth();
        tiledHeight = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getHeight();

        if (tiledWidth * tiledHeight > 2500) {
            Gdx.app.log("Advertencia de rendimiento", "Hay mas de 2500 tiles " + tiledWidth + " x " + tiledHeight + " = "
                    + (tiledWidth * tiledHeight));
        }

        Gdx.app.log("Tile Width", tiledWidth + "");
        Gdx.app.log("Tile Height", tiledHeight + "");

        xMin = 4.0f;// Inicia en 4 porque la camara esta centrada no en el origen
        xMax = unitScale * tiledWidth * 32 - 4;// Menos 4 porque la camara esta centrada en el origen
        yMin = 2.4f;

        posCamara = new Vector2(xMin, yMin);
        state = STATE_RUNNING;

        switch (nivel) {
            case 0:
                TIME_TO_SPAWN_ZOMBIE_KID = 3f;
                TIME_TO_SPAWN_ZOMBIE_CUASY = 10f;
                TIME_TO_SPAWN_ZOMBIE_MUMMY = 15f;
                TIME_TO_SPAWN_ZOMBIE_PAN = 20f;
                TIME_TO_SPAWN_ZOMBIE_FRANK = 25f;
                break;

            default:
                TIME_TO_SPAWN_ZOMBIE_KID = 0f;
                TIME_TO_SPAWN_ZOMBIE_CUASY = 0f;
                TIME_TO_SPAWN_ZOMBIE_MUMMY = 0f;
                TIME_TO_SPAWN_ZOMBIE_PAN = 0f;
                TIME_TO_SPAWN_ZOMBIE_FRANK = 0f;
                break;

        }

    }

    public void update(float delta, float accelCamX) {
        oWorldBox.step(delta, 8, 4);
        updateCamara(delta, accelCamX);

        eliminarObjetos();

        spawnStuff(delta);

        oWorldBox.getBodies(arrBodies);
        Iterator<Body> i = arrBodies.iterator();

        while (i.hasNext()) {
            Body body = i.next();
            if (body.getUserData() instanceof Personajes) {
                Personajes obj = (Personajes) body.getUserData();
                if (obj.isFacingLeft)
                    updateFacingLeft(delta, obj);
                else
                    updateFacingRight(delta, obj);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(delta, body);
            }
        }
    }

    public void atackaLL() {
        Iterator<Personajes> i = arrFacingLeft.iterator();

        while (i.hasNext()) {

            Personajes obj = i.next();
            if (obj.attack())

                objectCreatorManager.createBullet(obj);
        }
    }

    public void dieALl() {
        Iterator<Personajes> i = arrFacingLeft.iterator();

        while (i.hasNext()) {

            Personajes obj = i.next();
            obj.die();
        }
    }

    private void spawnStuff(float delta) {

        timeToSpwanZombieKid += delta;
        if (timeToSpwanZombieKid >= TIME_TO_SPAWN_ZOMBIE_KID) {
            timeToSpwanZombieKid -= TIME_TO_SPAWN_ZOMBIE_KID;
            objectCreatorManager.createZombieKid();
        }

        timeToSpwanZombieCuasy += delta;
        if (timeToSpwanZombieCuasy >= TIME_TO_SPAWN_ZOMBIE_CUASY) {
            timeToSpwanZombieCuasy -= TIME_TO_SPAWN_ZOMBIE_CUASY;
            objectCreatorManager.createZombieCuasy();
        }

        timeToSpwanZombieMummy += delta;
        if (timeToSpwanZombieMummy >= TIME_TO_SPAWN_ZOMBIE_MUMMY) {
            timeToSpwanZombieMummy -= TIME_TO_SPAWN_ZOMBIE_MUMMY;
            objectCreatorManager.createZombieMummy();
        }

        timeToSpwanZombiePan += delta;
        if (timeToSpwanZombiePan >= TIME_TO_SPAWN_ZOMBIE_PAN) {
            timeToSpwanZombiePan -= TIME_TO_SPAWN_ZOMBIE_PAN;
            objectCreatorManager.createZombiePan();
        }

        timeToSpwanZombieFrank += delta;
        if (timeToSpwanZombieFrank >= TIME_TO_SPAWN_ZOMBIE_FRANK) {
            timeToSpwanZombieFrank -= TIME_TO_SPAWN_ZOMBIE_FRANK;
            objectCreatorManager.createZombieFrank();
        }

    }

    private void updateCamara(float delta, float accelCamX) {

        if (accelCamX != 0)
            posCamara.x += (delta * accelCamX);

        if (posCamara.x < xMin * Settings.zoom) {
            posCamara.x = xMin * Settings.zoom;
        } else if (posCamara.x > (xMax - (xMin * (Settings.zoom - 1)))) {
            posCamara.x = xMax - (xMin * (Settings.zoom - 1));
        }

        posCamara.y = yMin * Settings.zoom;

    }

    private void updateFacingRight(float delta, Personajes obj) {
        obj.update(delta);

        int len = arrFacingLeft.size;
        for (int i = 0; i < len; i++) {
            Personajes objMalo = arrFacingLeft.get(i);

            if (obj.position.dst(objMalo.position.x, objMalo.position.y) <= obj.DISTANCE_ATTACK) {
                if (obj.attack())
                    objectCreatorManager.createBullet(obj);

            }
        }
    }

    private void updateFacingLeft(float delta, Personajes obj) {
        obj.update(delta);

        int len = arrFacingRight.size;
        for (int i = 0; i < len; i++) {
            Personajes objBueno = arrFacingRight.get(i);
            // if (obj.position.dst(objBueno.position.x, objBueno.position.y) <= obj.DISTANCE_ATTACK) {
            // obj.attack();
            //
            // if (obj.attack) {
            // objBueno.getHurt(obj.DAMAGE);
            // obj.didAttackEnemy();
            // }
            // }
            if (obj.position.dst(objBueno.position.x, objBueno.position.y) <= obj.DISTANCE_ATTACK) {
                if (obj.attack())
                    objectCreatorManager.createBullet(obj);

            }
        }
    }

    private void updateBullet(float delta, Body body) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(delta);

        if (obj.position.x > xMax + 3 || obj.position.x < xMin - 3)
            obj.state = Bullet.STATE_DESTROY;

    }

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrBodies);
        Iterator<Body> i = arrBodies.iterator();

        while (i.hasNext()) {
            Body body = i.next();

            if (!oWorldBox.isLocked()) {
                if (body.getUserData() instanceof Personajes) {
                    Personajes obj = (Personajes) body.getUserData();
                    if (obj.state == Personajes.STATE_DEAD && obj.stateTime >= obj.DURATION_DEAD) {
                        if (obj.isFacingLeft)
                            arrFacingLeft.removeValue(obj, true);
                        else
                            arrFacingRight.removeValue(obj, true);

                        oWorldBox.destroyBody(body);
                        continue;
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet obj = (Bullet) body.getUserData();
                    if (obj.state == Bullet.STATE_DESTROY) {
                        arrBullets.removeValue(obj, true);
                        oWorldBox.destroyBody(body);
                        continue;
                    }
                }

            }
        }
    }

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();
            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBulletOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBulletOtraCosa(b, a);

        }

        private void beginContactBulletOtraCosa(Fixture fixBullet, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

            if (oOtraCosa instanceof Personajes) {
                if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
                    Personajes obj = (Personajes) oOtraCosa;

                    if (obj.isFacingLeft == oBullet.isFacingLeft)// Si van hacia el mismo lado son amigos
                        return;

                    if (obj.state != Personajes.STATE_DEAD) {
                        // if (obj.state != Zombie.STATE_DEAD)
                        // Assets.zombieHit.play();

                        obj.getHurt(oBullet.DAMAGE);
                        oBullet.hit();

                    }
                }
            }

        }

        @Override
        public void endContact(Contact contact) {
            // TODO Auto-generated method stub

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            // TODO Auto-generated method stub

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }

    }

}
