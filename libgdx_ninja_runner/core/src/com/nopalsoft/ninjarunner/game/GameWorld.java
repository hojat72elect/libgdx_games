package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game_objects.Item;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyBean;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyCorn;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyJelly;
import com.nopalsoft.ninjarunner.game_objects.ItemCoin;
import com.nopalsoft.ninjarunner.game_objects.ItemEnergy;
import com.nopalsoft.ninjarunner.game_objects.ItemHeart;
import com.nopalsoft.ninjarunner.game_objects.ItemMagnet;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.game_objects.Missile;
import com.nopalsoft.ninjarunner.game_objects.Obstacle;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.game_objects.Platform;
import com.nopalsoft.ninjarunner.game_objects.Player;
import com.nopalsoft.ninjarunner.game_objects.Wall;

import java.util.Iterator;

public class GameWorld {
    static final int STATE_GAMEOVER = 1;
    public int state;

    float timeToSpawnMissile;

    ObjectManagerBox2d physicsManager;
    public World world;

    public Player player;
    public Mascot oMascot;

    Array<Body> arrBodies;
    Array<Platform> arrPlataformas;
    Array<Wall> arrPared;
    Array<Item> arrItems;
    Array<Obstacle> arrObstaculos;
    Array<Missile> arrMissiles;

    /**
     * Variable que indica hasta donde se ha ido creando el mundo
     */
    float mundoCreadoHastaX;

    int monedasTomadas;
    long puntuacion;

    public GameWorld() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new Colisiones());

        physicsManager = new ObjectManagerBox2d(this);

        arrBodies = new Array<Body>();
        arrPlataformas = new Array<Platform>();
        arrItems = new Array<Item>();
        arrPared = new Array<Wall>();
        arrObstaculos = new Array<Obstacle>();
        arrMissiles = new Array<Missile>();

        timeToSpawnMissile = 0;

        physicsManager.createStandingHero(2f, 1f, Settings.skinSeleccionada);
        physicsManager.crearMascota(player.position.x - 1, player.position.y + .75f);

        mundoCreadoHastaX = physicsManager.crearPlataforma(0, 0, 3);

        crearSiguienteParte();
    }

    private void crearSiguienteParte() {
        float x = mundoCreadoHastaX;

        // SEPARACION MAXIMA 3f
        // ALTURA MAXIMA 1.5f

        while (mundoCreadoHastaX < (x + 1)) {

            // Primero creo la plataforma
            int plataformasPegadas = MathUtils.random(1, 20);
            float separacion = MathUtils.random(1f, 3f);
            float y = MathUtils.random(0, 1.5f);

            // y = 0;
            plataformasPegadas = 25;

            mundoCreadoHastaX = physicsManager.crearPlataforma(mundoCreadoHastaX + separacion, y, plataformasPegadas);

            // Despues agrego cosas arriba de la plataforma

            float xAux = x + separacion;

            while (xAux < mundoCreadoHastaX - 2) {
                if (xAux < x + separacion + 2)
                    xAux = addRandomItems(xAux, y);

                if (MathUtils.randomBoolean(.1f)) {
                    xAux = physicsManager.crearCaja4(xAux, y + .8f);
                    xAux = addRandomItems(xAux, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = physicsManager.crearCaja7(xAux, y + 1f);
                    xAux = addRandomItems(xAux, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = physicsManager.crearPared(xAux, y + 3.17f);
                    xAux = addRandomItems(xAux, y);
                } else {
                    xAux = addRandomItems(xAux, y);
                }
            }

            /**
             * PARED y+3.17; CAJA4 y+.8f; CAJA7 y+1f;
             */

            // float xAux = oManager.crearCaja4(x + separacion, y + .8f);
            // xAux = oManager.crearCaja4(xAux, y + .8f);
            // xAux = oManager.crearCaja7(xAux, y + 1f);
            //

            // // SEPRACION DE CADA MONEDA EN X =.4
            // for (float i = x; i < mundoCreadoHastaX; i += .4f) {
            // oManager.crearMoneda(i + separacion, y + 1.5f);
            // oManager.crearMoneda(i + separacion, y + 1f);
            // }

        }

        // mundoCreadoHastaX += crearPlataforma(mundoCreadoHastaX, 1);
        //
        // mundoCreadoHastaX += crearPlataforma(mundoCreadoHastaX, 2);

    }

    private float addRandomItems(float xAux, float y) {

        if (MathUtils.randomBoolean(.3f)) {
            for (int i = 0; i < 5; i++) {
                physicsManager.crearItem(ItemCoin.class, xAux, y + 1.5f);
                xAux = physicsManager.crearItem(ItemCoin.class, xAux, y + 1f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                physicsManager.crearItem(ItemCandyBean.class, xAux, y + .8f);
                physicsManager.crearItem(ItemCandyBean.class, xAux, y + 1.1f);
                xAux = physicsManager.crearItem(ItemCandyJelly.class, xAux, y + 1.5f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                physicsManager.crearItem(ItemCandyCorn.class, xAux, y + .8f);
                physicsManager.crearItem(ItemCandyCorn.class, xAux, y + 1.1f);
                xAux = physicsManager.crearItem(ItemCandyCorn.class, xAux, y + 1.5f);
            }
        }

        if (MathUtils.randomBoolean(.025f)) {

            xAux = physicsManager.crearItem(ItemHeart.class, xAux, y + 1.5f);
            xAux = physicsManager.crearItem(ItemEnergy.class, xAux, y + 1.5f);
        } else if (MathUtils.randomBoolean(.025f)) {

            xAux = physicsManager.crearItem(ItemMagnet.class, xAux, y + 1.5f);
        }

        return xAux;
    }

    public void update(float delta, boolean didJump, boolean isJumpPressed, boolean dash, boolean didSlide) {
        world.step(delta, 8, 4);

        world.getBodies(arrBodies);
        eliminarObjetos();
        world.getBodies(arrBodies);

        Iterator<Body> i = arrBodies.iterator();
        while (i.hasNext()) {
            Body body = i.next();

            if (body.getUserData() instanceof Player) {
                updatePersonaje(delta, body, didJump, isJumpPressed, dash, didSlide);
            } else if (body.getUserData() instanceof Mascot) {
                updateMascota(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(delta, body);
            } else if (body.getUserData() instanceof Wall) {
                updatePared(delta, body);
            } else if (body.getUserData() instanceof Item) {
                updateItem(delta, body);
            } else if (body.getUserData() instanceof Obstacle) {
                updateObstaculos(delta, body);
            } else if (body.getUserData() instanceof Missile) {
                updateMissil(delta, body);
            }
        }

        if (player.position.x > mundoCreadoHastaX - 5)
            crearSiguienteParte();

        if (player.state == Player.STATE_DEAD && player.stateTime >= Player.DURATION_DEAD)
            state = STATE_GAMEOVER;

        timeToSpawnMissile += delta;
        float TIME_TO_SPAWN_MISSIL = 15;
        if (timeToSpawnMissile >= TIME_TO_SPAWN_MISSIL) {
            timeToSpawnMissile -= TIME_TO_SPAWN_MISSIL;

            physicsManager.crearMissil(player.position.x + 10, player.position.y);
        }
    }

    private void eliminarObjetos() {
        Iterator<Body> i = arrBodies.iterator();
        while (i.hasNext()) {
            Body body = i.next();

            if (body.getUserData() instanceof Platform obj) {
                if (obj.state == Platform.STATE_DESTROY) {
                    arrPlataformas.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Wall obj) {
                if (obj.state == Wall.STATE_DESTROY) {
                    arrPared.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Item obj) {
                if (obj.state == Item.STATE_DESTROY && obj.stateTime >= Item.DURATION_PICK) {
                    arrItems.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes4 obj) {

                if (obj.state == ObstacleBoxes4.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrObstaculos.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes7 obj) {

                if (obj.state == ObstacleBoxes7.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrObstaculos.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Missile obj) {
                if (obj.state == Missile.STATE_DESTROY) {
                    arrMissiles.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            }
        }
    }

    boolean bodyIsSLide;// INdica el si el cuarpo que tiene ahorita es sliding;
    boolean recreateFixture = false;

    private void updatePersonaje(float delta, Body body, boolean didJump, boolean isJumpPressed, boolean dash, boolean didSlide) {
        player.update(delta, body, didJump, false, dash, didSlide);

        if (player.position.y < -1) {
            player.die();
        } else if (player.isSlide && !bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = true;
            physicsManager.recreateFixturePersonajeSlide(body);
        } else if (!player.isSlide && bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = false;
            physicsManager.recreateFixturePersonajeStand(body);
        }
    }

    private void updateMascota(float delta, Body body) {

        float targetPositionX = player.position.x - .75f;
        float targetPositionY = player.position.y + .25f;

        if (oMascot.mascotType == Mascot.MascotType.BOMB) {
            Missile oMissile = getNextClosesMissil();
            if (oMissile != null && oMissile.distanceFromPlayer < 5f && oMissile.state == Missile.STATE_NORMAL) {
                targetPositionX = oMissile.position.x;
                targetPositionY = oMissile.position.y;
            }
        } else {
            if (player.isDash) {
                targetPositionX = player.position.x + 4.25f;
                targetPositionY = player.position.y;
            }
        }

        oMascot.update(body, delta, targetPositionX, targetPositionY);
    }

    private void updatePlataforma(float delta, Body body) {
        Platform obj = (Platform) body.getUserData();

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updatePared(float delta, Body body) {
        Wall obj = (Wall) body.getUserData();

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updateItem(float delta, Body body) {
        Item obj = (Item) body.getUserData();
        obj.update(delta, body, oMascot, player);

        if (obj.position.x < player.position.x - 3)
            obj.setPicked();
    }

    private void updateObstaculos(float delta, Body body) {
        Obstacle obj = (Obstacle) body.getUserData();
        obj.update(delta);

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updateMissil(float delta, Body body) {
        Missile obj = (Missile) body.getUserData();
        obj.update(delta, body, player);

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();

        arrMissiles.sort();
    }

    /**
     * Regresa el misil mas cercano al personaje que este en estado normal
     */
    private Missile getNextClosesMissil() {
        for (int i = 0; i < arrMissiles.size; i++) {
            if (arrMissiles.get(i).state == Missile.STATE_NORMAL)
                return arrMissiles.get(i);
        }
        return null;
    }

    /**
     * #### COLISIONES #####
     */

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                beginContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                beginContactHeroOtraCosa(b, a);

            if (a.getBody().getUserData() instanceof Mascot)
                beginContactMascotaOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Mascot)
                beginContactMascotaOtraCosa(b, a);
        }

        private void beginContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform) {
                if (fixHero.getUserData().equals("pies")) {
                    if (recreateFixture)
                        recreateFixture = false;
                    else
                        player.touchFloor();
                }
            } else if (oOtraCosa instanceof Item obj) {
                if (obj.state == Item.STATE_NORMAL) {
                    if (obj instanceof ItemCoin) {
                        monedasTomadas++;
                        puntuacion++;
                        Assets.playSound(Assets.coinSound, 1);
                    } else if (obj instanceof ItemMagnet) {
                        player.setPickUpMagnet();
                    } else if (obj instanceof ItemEnergy) {
                        // oPersonaje.shield++;
                    } else if (obj instanceof ItemHeart) {
                        player.vidas++;
                    } else if (obj instanceof ItemCandyJelly) {
                        Assets.playSound(Assets.candySound, 1);
                        puntuacion += 2;
                    } else if (obj instanceof ItemCandyBean) {
                        Assets.playSound(Assets.candySound, 1);
                        puntuacion += 5;
                    } else if (obj instanceof ItemCandyCorn) {
                        Assets.playSound(Assets.candySound, 1);
                        puntuacion += 15;
                    }

                    obj.setPicked();
                }
            } else if (oOtraCosa instanceof Wall obj) {
                if (obj.state == Wall.STATE_NORMAL) {
                    player.getDizzy();
                }
            } else if (oOtraCosa instanceof Obstacle obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setDestroy();
                    player.getHurt();
                }
            } else if (oOtraCosa instanceof Missile obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                    player.getDizzy();
                }
            }
        }

        public void beginContactMascotaOtraCosa(Fixture fixMascota, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Wall obj && player.isDash) {
                obj.setDestroy();
            } else if (oOtraCosa instanceof Obstacle obj && player.isDash) {
                obj.setDestroy();
            } else if (oOtraCosa instanceof ItemCoin obj) {
                if (obj.state == ItemCoin.STATE_NORMAL) {
                    obj.setPicked();
                    monedasTomadas++;
                    Assets.playSound(Assets.coinSound, 1);
                }
            } else if (oOtraCosa instanceof Missile obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a == null || b == null)
                return;

            if (a.getBody().getUserData() instanceof Player)
                endContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                endContactHeroOtraCosa(b, a);
        }

        private void endContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform) {
                if (fixHero.getUserData().equals("pies"))
                    player.endTouchFloor();
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Player)
                preSolveHero(b, a, contact);
        }

        private void preSolveHero(Fixture fixHero, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform obj) {

                float ponyY = fixHero.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }
    }
}
