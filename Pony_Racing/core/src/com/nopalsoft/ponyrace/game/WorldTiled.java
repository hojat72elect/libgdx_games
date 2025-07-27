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
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game_objects.BloodStone;
import com.nopalsoft.ponyrace.game_objects.Bomba;
import com.nopalsoft.ponyrace.game_objects.Candy;
import com.nopalsoft.ponyrace.game_objects.Chile;
import com.nopalsoft.ponyrace.game_objects.Flag;
import com.nopalsoft.ponyrace.game_objects.Fogata;
import com.nopalsoft.ponyrace.game_objects.Globo;
import com.nopalsoft.ponyrace.game_objects.Moneda;
import com.nopalsoft.ponyrace.game_objects.Pisable;
import com.nopalsoft.ponyrace.game_objects.Pluma;
import com.nopalsoft.ponyrace.game_objects.Pony;
import com.nopalsoft.ponyrace.game_objects.PonyMalo;
import com.nopalsoft.ponyrace.game_objects.PonyPlayer;
import com.nopalsoft.ponyrace.game_objects.TiledMapManagerBox2d;
import com.nopalsoft.ponyrace.game_objects.Wood;

import java.util.Comparator;
import java.util.Random;

public class WorldTiled {
    public MainPonyRace game;
    public Vector2 finJuego;
    public int nivelTiled;
    public World oWorldBox;
    public float m_units = 1 / 100.0f;
    public Array<Body> arrBodys;
    public PonyPlayer oPony;
    public Array<Fogata> arrFogatas;
    public Array<PonyMalo> arrPonysMalos;
    public Array<Pluma> arrPlumas;
    public Array<BloodStone> arrBloodStone;
    public Array<Bomba> arrBombas;
    public Array<Wood> arrWoods;
    public Array<Moneda> arrMonedas;
    public Array<Chile> arrChiles;
    public Array<Globo> arrGlobos;
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

    public WorldTiled(MainPonyRace game, int nivelTiled) {
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

        new TiledMapManagerBox2d(this, m_units).createObjetosDesdeTiled(game.oAssets.tiledMap);

        arrBodys = new Array<>();

        oWorldBox.getBodies(arrBodys);

        // ------
        arrPosiciones = new Array<>();
        for (PonyMalo obj : arrPonysMalos) {
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
        tamanoMapaX = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tamanoMapaX", String.class));
        tamanoMapaX = tamanoMapaX * 16 * m_units;

        tamanoMapaY = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tamanoMapaY", String.class));
        tamanoMapaY = tamanoMapaY * 16 * m_units;

        switch (Settings.dificultadActual) {
            case Settings.DIFICULTAD_EASY:
                tiempoLeft = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tiempoEasy", String.class));
                break;
            case Settings.DIFICULTAD_NORMAL:
                tiempoLeft = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tiempoNormal", String.class));
                break;
            case Settings.DIFICULTAD_HARD:
                tiempoLeft = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tiempoHard", String.class));
                break;
            case Settings.DIFICULTAD_SUPERHARD:
                tiempoLeft = Integer.parseInt(game.oAssets.tiledMap.getProperties().get("tiempoSuperHard", String.class));
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
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Pluma) {
                Pluma objPluma = ((Pluma) obj.getUserData());
                objPluma.update(delta);
                if (objPluma.state == Pluma.State.tomada && !oWorldBox.isLocked()) {
                    arrPlumas.removeValue(objPluma, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Bomba) {
                Bomba oBomb = ((Bomba) obj.getUserData());
                oBomb.update(delta, obj);
                if (oBomb.state == Bomba.State.explode && !oWorldBox.isLocked() && oBomb.stateTime >= Bomba.TIEMPO_EXPLOSION) {
                    arrBombas.removeValue(oBomb, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Wood) {
                Wood oWood = ((Wood) obj.getUserData());
                oWood.update(delta, obj);
                if (oWood.state == Wood.State.hit && !oWorldBox.isLocked() && oWood.stateTime >= Bomba.TIEMPO_EXPLOSION) {
                    arrWoods.removeValue(oWood, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Moneda) {
                Moneda objMo = ((Moneda) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Moneda.State.tomada && !oWorldBox.isLocked() && objMo.stateTime >= Moneda.TIEMPO_TOMADA) {
                    arrMonedas.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Chile) {
                Chile objMo = ((Chile) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Chile.State.tomada && !oWorldBox.isLocked() && objMo.stateTime >= Chile.TIEMPO_TOMADA) {
                    arrChiles.removeValue(objMo, true);
                    arrBodys.removeValue(obj, true);
                    oWorldBox.destroyBody(obj);
                }
            } else if (obj.getUserData() != null && obj.getUserData() instanceof Globo) {
                Globo objMo = ((Globo) obj.getUserData());
                objMo.update(delta);
                if (objMo.state == Globo.State.tomada && !oWorldBox.isLocked() && objMo.stateTime >= Globo.TIEMPO_TOMADA) {
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
        isMalo = obj.getUserData() instanceof PonyMalo;

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
                jump = ((PonyMalo) ponyDataBody).hasToJump;

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
                ((PonyMalo) ponyDataBody).hasToJump = false;
                game.oAssets.playSound(game.oAssets.jump, .4f);
            } else
                game.oAssets.playSound(game.oAssets.jump);
        }

        float velChange = accelX - vel.x;
        float impulsoX = obj.getMass() * velChange; // disregard time factor

        impulso.set(impulsoX, impulsoY);
        obj.applyLinearImpulse(impulso, obj.getWorldCenter(), true);
    }

    private void updateFogatas(float delta) {
        for (Fogata obj : arrFogatas) {
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

        Bomba oBomba = new Bomba(oPonyBomba.position.x, oPonyBomba.position.y, this);

        arrBombas.add(oBomba);
        arrBodys.add(oBody);
        oBody.setUserData(oBomba);

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
                    game.achievements.checkWorldComplete(nivelTiled);
                    game.achievements.checkVictoryMoreThan15Secs(nivelTiled, tiempoLeft);
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

            if (Adata instanceof PonyMalo)
                endContactPonyMalo(a, b);
            else if (Bdata instanceof PonyMalo)
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

            if (fixPony.getBody().getUserData() instanceof PonyMalo) {
                ponyDataBody = (PonyMalo) fixPony.getBody().getUserData();
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
                            ((Pony) otraCosaDataBody).getHurt(Chile.TIEMPO_HURT);
                        }
                    } else if (fixOtraCosa.getUserData().equals("cuerpoSensor") && ((Pony) (otraCosaDataBody)).isChile) {
                        if (fixPony.getUserData().equals("cuerpo")) {
                            ponyDataBody.getHurt(Chile.TIEMPO_HURT);
                        }
                    }
                }
            } else if (fixPony.getUserData() != null && fixPony.getUserData().equals("cuerpo")) {
                if (otraCosaDataBody.equals("regresoHoyo")) {
                    ponyDataBody.regresoHoyo.set(ponyDataBody.position.x, ponyDataBody.position.y);
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData().equals("nucleoBomba")) {
                    ((Bomba) otraCosaDataBody).explode(fixOtraCosa.getBody());
                    ponyDataBody.getHurt(((Bomba) otraCosaDataBody).TIEMPO_HURT);
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData().equals("nucleoWood")) {
                    Wood oWood = (Wood) otraCosaDataBody;
                    if (oWood.state == Wood.State.normal) {
                        oWood.hitByPony(fixOtraCosa.getBody());
                        ponyDataBody.getHurt(((Wood) otraCosaDataBody).TIEMPO_HURT);
                    }
                } else if (otraCosaDataBody instanceof Moneda && !isMalo) {
                    Moneda oMoneda = ((Moneda) otraCosaDataBody);
                    if (oMoneda.state == Moneda.State.normal) {

                        int valorMoneda;
                        if (random.nextBoolean()) {
                            switch (Settings.nivelCoin) {

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
                        game.oAssets.playSound(game.oAssets.pickCoin);
                        oMoneda.hitPony();
                    }
                } else if (otraCosaDataBody instanceof Chile) {
                    Chile oChile = ((Chile) otraCosaDataBody);
                    if (oChile.state == Chile.State.normal) {
                        oChile.hitPony();
                        ponyDataBody.tocoChile();
                        ponyDataBody.chilesRecolectados++;
                    }
                } else if (otraCosaDataBody instanceof Globo) {
                    Globo oGlobo = ((Globo) otraCosaDataBody);
                    if (oGlobo.state == Globo.State.normal) {
                        oGlobo.hitPony();
                        if (!isMalo) {
                            switch (Settings.nivelChocolate) {
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
                            ((PonyMalo) ponyDataBody).hitSimpleJump(Pony.STATE_WALK_RIGHT);
                        } else if (otraCosaDataBody.equals("saltoIzquierda")) {
                            ((PonyMalo) ponyDataBody).hitSimpleJump(Pony.STATE_WALK_LEFT);
                        } else if (otraCosaDataBody.equals("salto")) {
                            ((PonyMalo) ponyDataBody).hitSimpleJump(Pony.STATE_STAND);
                        } else if (otraCosaDataBody.equals("caminarIzquierda")) {
                            ((PonyMalo) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_WALK_LEFT);
                        } else if (otraCosaDataBody.equals("caminarDerecha")) {
                            ((PonyMalo) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_WALK_RIGHT);
                        } else if (otraCosaDataBody.equals("caer")) {
                            ((PonyMalo) ponyDataBody).hitCaminarOtraDireccion(Pony.STATE_STAND);
                        } else if (otraCosaDataBody.equals("bandera")) {
                            ((PonyMalo) ponyDataBody).tocoBandera = true;
                        } else if (otraCosaDataBody instanceof Flag) {
                            Flag oBan = (Flag) otraCosaDataBody;
                            PonyMalo oPon = (PonyMalo) ponyDataBody;

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
            PonyMalo oPonyMalo = (PonyMalo) fixPony.getBody().getUserData();

            if (fixPony.getUserData() != null && fixPony.getUserData().equals("cuerpo") && fixOtraCosa.getUserData() != null
                    && fixOtraCosa.getUserData().equals("sensorBomba")) {
                if (((Bomba) otraCosaData).state == Bomba.State.explode) {
                    oPonyMalo.getHurt(((Bomba) otraCosaData).TIEMPO_HURT);
                }
            }
        }
    }
}
