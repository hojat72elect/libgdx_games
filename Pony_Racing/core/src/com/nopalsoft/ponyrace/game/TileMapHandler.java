package com.nopalsoft.ponyrace.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game_objects.Balloons;
import com.nopalsoft.ponyrace.game_objects.BloodStone;
import com.nopalsoft.ponyrace.game_objects.Bomb;
import com.nopalsoft.ponyrace.game_objects.Bonfire;
import com.nopalsoft.ponyrace.game_objects.Candy;
import com.nopalsoft.ponyrace.game_objects.Chili;
import com.nopalsoft.ponyrace.game_objects.Coin;
import com.nopalsoft.ponyrace.game_objects.Flag;
import com.nopalsoft.ponyrace.game_objects.OpponentPony;
import com.nopalsoft.ponyrace.game_objects.Pisable;
import com.nopalsoft.ponyrace.game_objects.Pony;
import com.nopalsoft.ponyrace.game_objects.PonyPlayer;
import com.nopalsoft.ponyrace.game_objects.TiledMapManagerBox2d;
import com.nopalsoft.ponyrace.game_objects.Wing;
import com.nopalsoft.ponyrace.game_objects.Wood;

import java.util.Comparator;
import java.util.Random;

public class TileMapHandler {
    public PonyRacingGame game;
    public Vector2 finJuego;
    public int nivelTiled;
    public World oWorldBox;
    public float m_units = 1 / 100.0f;
    public Array<Body> arrBodys;
    public PonyPlayer oPony;
    public Array<Bonfire> arrFogatas;
    public Array<OpponentPony> arrPonysMalos;
    public Array<Wing> arrPlumas;
    public Array<BloodStone> arrBloodStone;
    public Array<Bomb> arrBombas;
    public Array<Wood> arrWoods;
    public Array<Coin> arrMonedas;
    public Array<Chili> arrChiles;
    public Array<Balloons> arrGlobos;
    public Array<Candy> arrDulces;
    public Random random;
    public float tamanoMapaX;
    public float tamanoMapaY;
    public float tiempoLeft;// El tiempo que le queda.
    public float tiempoLap;
    Vector2 gravedad = new Vector2(0, -9.5f);
    Array<Pony> arrPosiciones;// Arreglo para ordenar los jugadores
    State state;
    Comparator<Pony> checadorPosiciones = new Comparator<Pony>() {
        @Override
        public int compare(Pony arg0, Pony arg1) {
            if (arg0.position.x < arg1.position.x)
                return 1;
            return -1;
        }
    };
    Vector2 impulso = new Vector2();

    public TileMapHandler(PonyRacingGame game, int nivelTiled) {
        this.game = game;
        this.nivelTiled = nivelTiled;
        boolean sleep = true;
        oWorldBox = new World(gravedad, sleep);
        oWorldBox.setContactListener(new plataformasContact());
        state = State.running;
        random = new Random();

        arrFogatas = new Array<>();
        arrPonysMalos = new Array<>();
        arrPlumas = new Array<>();
        arrBloodStone = new Array<>();
        arrBombas = new Array<>();
        arrMonedas = new Array<>();
        arrChiles = new Array<>();
        arrGlobos = new Array<>();
        arrDulces = new Array<>();
        arrWoods = new Array<>();

        new TiledMapManagerBox2d(this, m_units).createObjetosDesdeTiled(game.assetsHandler.tiledMap);

        arrBodys = new Array<>();

        oWorldBox.getBodies(arrBodys);

        // ------
        arrPosiciones = new Array<>();
        for (OpponentPony obj : arrPonysMalos) {
            arrPosiciones.add(obj);
        }
        arrPosiciones.add(oPony);

        checarPosicionCarrera();
        inicializarVariablesDesdeMapas();
    }

    private void checarPosicionCarrera() {

        arrPosiciones.sort(checadorPosiciones);

        int posicion = 1;
        for (Pony obj : arrPosiciones) {
            obj.lugarEnLaCarrera = posicion;
            posicion++;
        }
    }

    public void inicializarVariablesDesdeMapas() {
        tamanoMapaX = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tamanoMapaX", String.class));
        tamanoMapaX = tamanoMapaX * 16 * m_units;

        tamanoMapaY = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tamanoMapaY", String.class));
        tamanoMapaY = tamanoMapaY * 16 * m_units;

        switch (Settings.difficultyLevel) {
            case Settings.DIFFICULTY_EASY:
                tiempoLeft = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tiempoEasy", String.class));
                break;
            case Settings.DIFFICULTY_NORMAL:
                tiempoLeft = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tiempoNormal", String.class));
                break;
            case Settings.DIFFICULTY_HARD:
                tiempoLeft = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tiempoHard", String.class));
                break;
            case Settings.DIFFICULTY_VERY_HARD:
                tiempoLeft = Integer.parseInt(game.assetsHandler.tiledMap.getProperties().get("tiempoSuperHard", String.class));
                break;
        }
    }

    public void update(float delta, WorldTiledRenderer render) {
        update(delta, 0, false, false, false, render);
    }

    public void update(float delta, float accelX, boolean jump, boolean fireBomb, boolean fireWood, WorldTiledRenderer render) {
        oWorldBox.step(delta, 8, 4); // para hacer mas lento el juego 1/300f
        oWorldBox.clearForces();
        // Actualizo

        oPony.fireBomb = fireBomb;
        oPony.fireWood = fireWood;

        for (Body obj : arrBodys) {
            if (obj.getUserData() != null && obj.getUserData() instanceof Pony) {

                updatePonys(delta, obj, accelX, jump);
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Wing) {
                Wing objWing = ((Wing) obj.getUserData());
                objWing.update(delta);
                if (objWing.state == Wing.State.ACTIVE && !oWorldBox.isLocked()) {
                    arrPlumas.removeValue(objWing, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Bomb) {
                Bomb oBomb = ((Bomb) obj.getUserData());
                oBomb.update(delta, obj);
                if (oBomb.state == Bomb.State.explode && !oWorldBox.isLocked() && oBomb.stateTime >= Bomb.TIEMPO_EXPLOSION) {
                    arrBombas.removeValue(oBomb, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Wood) {
                Wood oWood = ((Wood) obj.getUserData());
                oWood.update(delta, obj);
                if (oWood.state == Wood.State.hit && !oWorldBox.isLocked() && oWood.stateTime >= Bomb.TIEMPO_EXPLOSION) {
                    arrWoods.removeValue(oWood, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Coin) {
                Coin objMo = ((Coin) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Coin.State.TAKEN && !oWorldBox.isLocked() && objMo.stateTime >= Coin.TIEMPO_TOMADA) {
                    arrMonedas.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Chili) {
                Chili objMo = ((Chili) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Chili.State.TAKEN && !oWorldBox.isLocked() && objMo.stateTime >= Chili.HIT_ANIMATION_DURATION) {
                    arrChiles.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Balloons) {
                Balloons objMo = ((Balloons) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Balloons.State.TAKEN && !oWorldBox.isLocked() && objMo.stateTime >= Balloons.TAKEN_ANIMATION_DURATION) {
                    arrGlobos.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Candy) {
                Candy objMo = ((Candy) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Candy.State.ACTIVE && !oWorldBox.isLocked() && objMo.stateTime >= Candy.PICK_UP_DURATION) {
                    arrDulces.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Flag) {
                Flag objBa = ((Flag) obj.getUserData());
                if (objBa.state == Flag.State.ACTIVE && !oWorldBox.isLocked()) {
                    oWorldBox.destroyBody(obj);
                }
            }
        }

        /*
         * Las cosas que se actualizan aqui no tienen Box2d para nada
         */
        updateFogatas(delta);
        updateBloodStone(delta);

        checarPosicionCarrera();
        checarGameOver(delta);
    }

    private void updatePonys(float delta, Body obj, float accelX, boolean jump) {
        boolean isMalo;
        isMalo = obj.getUserData() instanceof OpponentPony;

        Pony ponyDataBody = (Pony) obj.getUserData();

        /*
         * PAra disparar
         */
        if (ponyDataBody.fireBomb) {
            lanzarBomba(ponyDataBody);
            ponyDataBody.fireBomb = false;
        } else if (ponyDataBody.fireWood) {
            fireWood(ponyDataBody);
            ponyDataBody.fireWood = false;
        }

        /*
         * En disparar
         */
        if (ponyDataBody.cayoEnHoyo) {
            obj.setTransform(ponyDataBody.regresoHoyo.x, ponyDataBody.regresoHoyo.y, 0);
            ponyDataBody.cayoEnHoyo = false;
        }

        if (isMalo || ponyDataBody.pasoLaMeta) {// Esto puede pasar si es malo y si es normal y paso la meta
            if (ponyDataBody.state == Pony.STATE_WALK_LEFT)
                accelX = -1;
            else if (ponyDataBody.state == Pony.STATE_WALK_RIGHT)
                accelX = 1;
            else
                accelX = 0;

            if (isMalo)// /esto solo puede pasar si es malo
                jump = ((OpponentPony) ponyDataBody).hasToJump;

            if (!ponyDataBody.tocoElPisoDespuesCaerHoyo)
                accelX = 0;
        }

        if (ponyDataBody.isHurt) {
            accelX = 0;
            jump = false;
        }

        ponyDataBody.update(delta, obj, accelX);

        if (!isMalo && (ponyDataBody.isChile || ponyDataBody.isDulce))
            accelX *= Pony.VEL_RUN * 1.5f;
        else
            accelX *= Pony.VEL_RUN;

        Vector2 vel = obj.getLinearVelocity();

        float impulsoY = 0;
        if (jump && !ponyDataBody.isDoubleJump) {
            float velChange = Pony.VEL_JUMP - vel.y;
            impulsoY = obj.getMass() * velChange; // disregard time factor
            ponyDataBody.jump();

            if (isMalo) {
                ((OpponentPony) ponyDataBody).hasToJump = false;
                game.assetsHandler.playSound(game.assetsHandler.jump, .4f);
            } else
                game.assetsHandler.playSound(game.assetsHandler.jump);
        }

        float velChange = accelX - vel.x;
        float impulsoX = obj.getMass() * velChange; // disregard time factor

        impulso.set(impulsoX, impulsoY);
        obj.applyLinearImpulse(impulso, obj.getWorldCenter(), true);
    }

    private void updateFogatas(float delta) {
        for (Bonfire obj : arrFogatas) {
            obj.update(delta);
        }
    }

    private void updateBloodStone(float delta) {
        for (BloodStone obj : arrBloodStone) {
            obj.update(delta);
        }
    }

    private void lanzarBomba(Pony oPonyBomba) {
        if (oPonyBomba instanceof PonyPlayer) {
            if (Settings.numeroBombas <= 0)
                return;
            else
                Settings.numeroBombas--;
        }

        int velX;
        if (oPonyBomba.state == Pony.STATE_WALK_LEFT)
            velX = -4;
        else
            velX = 4;

        Vector2 velocidad = new Vector2(velX, 5);
        BodyDef bd = new BodyDef();
        bd.position.y = oPonyBomba.position.y + .4f;
        bd.position.x = oPonyBomba.position.x + .3f;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        // Nucleo que si puede chocar con el piso pero no con los corredores
        CircleShape circulo = new CircleShape();
        circulo.setRadius(.05f);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = circulo;
        fixture.density = .1f;
        fixture.restitution = .4f;
        fixture.friction = .25f;
        fixture.filter.groupIndex = TiledMapManagerBox2d.CONTACT_CORREDORES;
        oBody.createFixture(fixture);

        // Nucleo sensor que me dice si acabo de tocar un corredor para explotar la bomba
        circulo.setRadius(.1f);
        circulo.setPosition(new Vector2(0, 0));
        fixture.shape = circulo;
        fixture.isSensor = true;
        fixture.filter.groupIndex = 0;
        Fixture sensorBomba = oBody.createFixture(fixture);
        sensorBomba.setUserData("nucleoBomba");
        oBody.createFixture(fixture);

        // sensor que dice el radio de explosion de la bomba
        circulo.setRadius(.5f);
        circulo.setPosition(new Vector2(0, 0));
        fixture.shape = circulo;
        fixture.isSensor = true;
        fixture.filter.groupIndex = 0;
        sensorBomba = oBody.createFixture(fixture);
        sensorBomba.setUserData("sensorBomba");
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setLinearVelocity(velocidad);

        Bomb oBomb = new Bomb(oPonyBomba.position.x, oPonyBomba.position.y, this);

        arrBombas.add(oBomb);
        arrBodys.add(oBody);
        oBody.setUserData(oBomb);

        circulo.dispose();
    }

    private void fireWood(Pony oPonyWood) {
        if (oPonyWood instanceof PonyPlayer) {
            if (Settings.numeroWoods <= 0)
                return;
            else
                Settings.numeroWoods--;
        }

        float pos;
        if (oPonyWood.state == Pony.STATE_WALK_LEFT)
            pos = .5f;
        else
            pos = -.5f;

        BodyDef bd = new BodyDef();
        bd.position.y = oPonyWood.position.y;
        bd.position.x = oPonyWood.position.x + pos;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        // Nucleo que si puede chocar con el piso pero no con los corredores
        CircleShape circulo = new CircleShape();
        circulo.setRadius(.05f);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = circulo;
        fixture.density = .1f;
        fixture.restitution = .4f;
        fixture.friction = .25f;
        fixture.filter.groupIndex = TiledMapManagerBox2d.CONTACT_CORREDORES;
        oBody.createFixture(fixture);

        // Nucleo sensor que me dice si acabo de tocar un corredor para explotar la bomba
        PolygonShape po = new PolygonShape();
        po.setAsBox(.22f, .10f);
        fixture.shape = po;
        fixture.isSensor = true;
        fixture.filter.groupIndex = 0;
        Fixture sensorBomba = oBody.createFixture(fixture);
        sensorBomba.setUserData("nucleoWood");
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);

        Wood oWood = new Wood(oPonyWood.position.x, oPonyWood.position.y, oPonyWood, this);

        arrWoods.add(oWood);
        arrBodys.add(oBody);
        oBody.setUserData(oWood);

        circulo.dispose();
    }

    private void checarGameOver(float delta) {
        for (int i = 0; i < arrPosiciones.size; i++) {
            Pony objPony = arrPosiciones.get(i);

            if (objPony.position.x > finJuego.x) {
                objPony.pasoLaMeta = true;

                if (objPony.lugarEnLaCarrera == 1) {
                    if (objPony.position.x >= finJuego.x + 2.7f)
                        objPony.state = Pony.STATE_STAND;
                } else if (objPony.lugarEnLaCarrera == 2) {
                    if (objPony.position.x >= finJuego.x + 2.2)
                        objPony.state = Pony.STATE_STAND;
                } else if (objPony.lugarEnLaCarrera == 3) {
                    if (objPony.position.x >= finJuego.x + 1.6f)
                        objPony.state = Pony.STATE_STAND;
                } else if (objPony.lugarEnLaCarrera == 4) {
                    if (objPony.position.x >= finJuego.x + 1)
                        objPony.state = Pony.STATE_STAND;
                } else if (objPony.lugarEnLaCarrera == 5) {
                    if (objPony.position.x >= finJuego.x + .4f)
                        objPony.state = Pony.STATE_STAND;
                }
            }
        }

        if (!oPony.pasoLaMeta) {
            tiempoLeft -= delta;
            tiempoLap += delta;
            if (tiempoLeft <= 0 && state != State.timeUp) {
                state = State.timeUp;
                oPony.die();
            }
        } else {
            if (oPony.lugarEnLaCarrera == 1) {
                if (state != State.nextLevel) {// Solo lo checa 1 vez
                    game.achievementsHandler.checkWorldComplete(nivelTiled);
                    game.achievementsHandler.checkVictoryMoreThan15Secs(nivelTiled, tiempoLeft);
                }
                state = State.nextLevel;
            } else
                state = State.tryAgain;
        }
    }

    // Next level significa que se paso el nivel.
    enum State {
        running, timeUp, tryAgain, nextLevel
    }

    public class plataformasContact implements ContactListener {

        Array<Contact> arrContacs;

        public plataformasContact() {
            arrContacs = new Array<>();
        }

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            Object Adata = a.getBody().getUserData();
            Object Bdata = b.getBody().getUserData();

            if (Adata instanceof Pony)
                beginContactPony(a, b);
            else if (Bdata instanceof Pony)
                beginContactPony(b, a);
        }

        @Override
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a == null || b == null)
                return;

            Object Adata = a.getBody().getUserData();
            Object Bdata = b.getBody().getUserData();

            if (Adata instanceof OpponentPony)
                endContactPonyMalo(a, b);
            else if (Bdata instanceof OpponentPony)
                endContactPonyMalo(b, a);
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            Object Adata = a.getBody().getUserData();
            Object Bdata = b.getBody().getUserData();

            if (Adata instanceof Pony)
                preSolveContactPony(a, b, contact);
            else if (Bdata instanceof Pony)
                preSolveContactPony(b, a, contact);
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

        /**
         * Checa las colisiones que hay en un pony, puede ser pony IA o pony Jugador
         */
        public void beginContactPony(Fixture fixPony, Fixture fixOtraCosa) {

            Pony ponyDataBody;
            boolean isMalo;

            if (fixPony.getBody().getUserData() instanceof OpponentPony) {
                ponyDataBody = (OpponentPony) fixPony.getBody().getUserData();
                isMalo = true;
            } else {
                ponyDataBody = (PonyPlayer) fixPony.getBody().getUserData();
                isMalo = false;
            }

            Object otraCosaDataBody = fixOtraCosa.getBody().getUserData();

            if (fixPony.getUserData() != null && (fixPony.getUserData().equals("sensorBottomIzq") || fixPony.getUserData().equals("sensorBottomDer"))) {
                if (otraCosaDataBody.equals("suelo")) {
                    ponyDataBody.tocoElPiso();
                } else if (otraCosaDataBody.equals("sueloInclinado")) {
                    ponyDataBody.tocoPisoInclinado();
                } else if (otraCosaDataBody.equals("hoyo")) {
                    ponyDataBody.cayoEnHoyo();
                } else if (otraCosaDataBody instanceof Pisable) {
                    ponyDataBody.tocoElPiso();
                }
            } else if (otraCosaDataBody instanceof Pony) {
                if (fixPony.getUserData() != null && fixOtraCosa.getUserData() != null) {
                    if (ponyDataBody.isChile) {
                        if (fixPony.getUserData().equals("cuerpoSensor") && fixOtraCosa.getUserData().equals("cuerpo")) {
                            ((Pony) otraCosaDataBody).getHurt(Chili.HURT_DURATION);
                        }
                    } else if (fixOtraCosa.getUserData().equals("cuerpoSensor") && ((Pony) (otraCosaDataBody)).isChile) {
                        if (fixPony.getUserData().equals("cuerpo")) {
                            ponyDataBody.getHurt(Chili.HURT_DURATION);
                        }
                    }
                }
            } else if (fixPony.getUserData() != null && fixPony.getUserData().equals("cuerpo")) {
                if (otraCosaDataBody.equals("regresoHoyo")) {
                    ponyDataBody.regresoHoyo.set(ponyDataBody.position.x, ponyDataBody.position.y);
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData().equals("nucleoBomba")) {
                    ((Bomb) otraCosaDataBody).explode(fixOtraCosa.getBody());
                    ponyDataBody.getHurt(((Bomb) otraCosaDataBody).TIEMPO_HURT);
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData().equals("nucleoWood")) {
                    Wood oWood = (Wood) otraCosaDataBody;
                    if (oWood.state == Wood.State.normal) {
                        oWood.hitByPony(fixOtraCosa.getBody());
                        ponyDataBody.getHurt(((Wood) otraCosaDataBody).TIEMPO_HURT);
                    }
                } else if (otraCosaDataBody instanceof Coin && !isMalo) {
                    Coin oCoin = ((Coin) otraCosaDataBody);
                    if (oCoin.state == Coin.State.IDLE) {

                        int valorMoneda;
                        if (random.nextBoolean()) {
                            switch (Settings.coinLevel) {

                                default:
                                case 0:
                                    valorMoneda = 1;

                                    break;

                                case 1:
                                case 2:
                                    valorMoneda = 2;

                                    break;

                                case 3:
                                case 4:
                                    valorMoneda = 3;

                                    break;

                                case 5:
                                    valorMoneda = 5;

                                    break;
                            }
                        } else {
                            valorMoneda = 1;
                        }
                        Settings.sumarMonedas(valorMoneda);
                        ponyDataBody.monedasRecolectadas += valorMoneda;
                        game.assetsHandler.playSound(game.assetsHandler.pickCoin);
                        oCoin.hitPony();
                    }
                } else if (otraCosaDataBody instanceof Chili) {
                    Chili oChili = ((Chili) otraCosaDataBody);
                    if (oChili.state == Chili.State.IDLE) {
                        oChili.hitPony();
                        ponyDataBody.tocoChile();
                        ponyDataBody.chilesRecolectados++;
                    }
                } else if (otraCosaDataBody instanceof Balloons) {
                    Balloons oBalloons = ((Balloons) otraCosaDataBody);
                    if (oBalloons.state == Balloons.State.IDLE) {
                        oBalloons.hitPony();
                        if (!isMalo) {
                            switch (Settings.chocolateLevel) {
                                default:
                                case 0:
                                    tiempoLeft += 5;
                                    break;
                                case 1:
                                    tiempoLeft += 7.5f;
                                    break;
                                case 2:
                                    tiempoLeft += 10.5f;
                                    break;
                                case 3:
                                    tiempoLeft += 14f;
                                    break;
                                case 4:
                                    tiempoLeft += 15;
                                    break;
                                case 5:
                                    tiempoLeft += 18f;
                                    break;
                            }
                            ponyDataBody.globosRecolectados++;
                        }
                    }
                } else if (otraCosaDataBody instanceof Candy) {
                    Candy oCandy = ((Candy) otraCosaDataBody);
                    if (oCandy.state == Candy.State.NORMAL) {
                        oCandy.hitPony();
                        ponyDataBody.tocoDulce();
                        ponyDataBody.dulcesRecolectados++;
                    }
                }
                /*
                 * COSAS PONYS MALOS SOLAMENTE
                 */
                {
                    if (isMalo) {

                        if (otraCosaDataBody.equals("saltoDerecha")) {
                            ((OpponentPony) ponyDataBody).hitSimpleJump(Pony.STATE_WALK_RIGHT);
                        } else if (otraCosaDataBody.equals("saltoIzquierda")) {
                            ((OpponentPony) ponyDataBody).hitSimpleJump(Pony.STATE_WALK_LEFT);
                        } else if (otraCosaDataBody.equals("salto")) {
                            ((OpponentPony) ponyDataBody).hitSimpleJump(Pony.STATE_STAND);
                        } else if (otraCosaDataBody.equals("caminarIzquierda")) {
                            ((OpponentPony) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_WALK_LEFT);
                        } else if (otraCosaDataBody.equals("caminarDerecha")) {
                            ((OpponentPony) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_WALK_RIGHT);
                        } else if (otraCosaDataBody.equals("caer")) {
                            ((OpponentPony) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_STAND);
                        } else if (otraCosaDataBody.equals("bandera")) {
                            ((OpponentPony) ponyDataBody).didTouchFlag = true;
                        } else if (otraCosaDataBody instanceof Flag) {
                            Flag oBan = (Flag) otraCosaDataBody;
                            OpponentPony oPon = (OpponentPony) ponyDataBody;

                            if (oBan.permitirSalto()) {

                                if (oBan.actionType == Flag.ActionType.JUMP_LEFT)
                                    oPon.hitSimpleJump(Pony.STATE_WALK_LEFT);
                                else if (oBan.actionType == Flag.ActionType.JUMP_RIGHT)
                                    oPon.hitSimpleJump(Pony.STATE_WALK_RIGHT);
                                else if (oBan.actionType == Flag.ActionType.JUMP)
                                    oPon.hitSimpleJump(Pony.STATE_STAND);
                            }
                        }
                    }
                }
                /*
                 * FIN COSAS PONYS MALOS
                 */
            }
        }

        public void preSolveContactPony(Fixture fixPony, Fixture fixOtraCosa, Contact contact) {

            Object otraCosaDataBody = fixOtraCosa.getBody().getUserData();

            if (otraCosaDataBody instanceof Pisable) {
                // Si el pony su centro - la mitad de su altura y el piso su centro mas la mitad de su altura
                // Si ponyY es menor significa q esta por abajo.
                Pisable oPis = (Pisable) otraCosaDataBody;
                float ponyY = fixPony.getBody().getPosition().y - .25f;
                float pisY = oPis.position.y + oPis.alto / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);
            }
        }

        public void endContactPonyMalo(Fixture fixPony, Fixture fixOtraCosa) {
            Object otraCosaData = fixOtraCosa.getBody().getUserData();
            OpponentPony oOpponentPony = (OpponentPony) fixPony.getBody().getUserData();

            if (fixPony.getUserData() != null && fixPony.getUserData().equals("cuerpo") && fixOtraCosa.getUserData() != null
                    && fixOtraCosa.getUserData().equals("sensorBomba")) {
                if (((Bomb) otraCosaData).state == Bomb.State.explode) {
                    oOpponentPony.getHurt(((Bomb) otraCosaData).TIEMPO_HURT);
                }
            }
        }
    }
}
