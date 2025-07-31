package com.nopalsoft.zombiekiller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
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
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.game_objects.Bullet;
import com.nopalsoft.zombiekiller.game_objects.Crate;
import com.nopalsoft.zombiekiller.game_objects.Hero;
import com.nopalsoft.zombiekiller.game_objects.ItemGem;
import com.nopalsoft.zombiekiller.game_objects.ItemHeart;
import com.nopalsoft.zombiekiller.game_objects.ItemMeat;
import com.nopalsoft.zombiekiller.game_objects.ItemShield;
import com.nopalsoft.zombiekiller.game_objects.ItemSkull;
import com.nopalsoft.zombiekiller.game_objects.ItemStar;
import com.nopalsoft.zombiekiller.game_objects.Items;
import com.nopalsoft.zombiekiller.game_objects.Platform;
import com.nopalsoft.zombiekiller.game_objects.Saw;
import com.nopalsoft.zombiekiller.game_objects.Zombie;

public class WorldGame {
    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;
    static final int STATE_NEXT_LEVEL = 2;
    public int state;
    public int tiledWidth;
    public int tiledHeight;

    /**
     * At the moment I did a test with 110 tiles width and everything works fine, the background does not go through.
     */
    public World world;
    public int gems;
    public int skulls;
    public int TOTAL_ZOMBIES_LEVEL;
    public int totalZombiesKilled;
    public int bonus;// If all zombies are killed, the collected gems x2
    float TIME_TO_FIRE_AGAIN = .3f;
    float timeToFireAgain;

    /*
     * My tiles are 32px, so the unit would be 1/32 with a 10x15 orthographic camera to see 10 tiles wide and 15 tiles high. The problem is that my camera
     *  is 8x4.8px, so I have to change the scale. With 1/32, I would only see 8 tiles wide and 4.8 tiles high, due to the way the camera is configured.
     * With 1/96, I see 24 tiles wide.
     */
    float unitScale = 1 / 76f;
    Hero hero;

    Array<Zombie> zombies;
    Array<Items> items;
    Array<Crate> crates;
    Array<Bullet> bullets;
    Array<Saw> saws;
    Array<Body> bodies;

    public WorldGame() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new CollisionHandler());

        items = new Array<>();
        zombies = new Array<>();
        bullets = new Array<>();
        crates = new Array<>();
        saws = new Array<>();
        bodies = new Array<>();

        new TiledMapManagerBox2d(this, unitScale).createObjectsFromTiled(Assets.map);
        tiledWidth = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getWidth();
        tiledHeight = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getHeight();

        if (tiledWidth * tiledHeight > 2500) {
            Gdx.app.log("Advertencia de rendimiento", "Hay mas de 2500 tiles " + tiledWidth + " x " + tiledHeight + " = "
                    + (tiledWidth * tiledHeight));
        }

        Gdx.app.log("Tile Width", tiledWidth + "");
        Gdx.app.log("Tile Height", tiledHeight + "");

        createHero();

        state = STATE_RUNNING;
    }

    private void createHero() {
        hero = new Hero(1.35f, 1.6f, Settings.skinSeleccionada);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = hero.position.x;
        bodyDefinition.position.y = hero.position.y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.17f, .32f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 8;
        fixtureDefinition.friction = 0;
        Fixture heroFixture = body.createFixture(fixtureDefinition);
        heroFixture.setUserData("cuerpo");

        PolygonShape sensorPiesShape = new PolygonShape();
        sensorPiesShape.setAsBox(.11f, .025f, new Vector2(0, -.32f), 0);
        fixtureDefinition.shape = sensorPiesShape;
        fixtureDefinition.density = 0;
        fixtureDefinition.restitution = 0f;
        fixtureDefinition.friction = 0;
        fixtureDefinition.isSensor = true;
        Fixture sensorPies = body.createFixture(fixtureDefinition);
        sensorPies.setUserData("pies");

        body.setFixedRotation(true);
        body.setUserData(hero);
        body.setBullet(true);

        shape.dispose();
    }

    private void createBullet(boolean isFiring, float delta) {
        // Can't shoot if climbing
        if (hero.isClimbing || hero.state == Hero.STATE_HURT || hero.state == Hero.STATE_DEAD)
            return;

        if (isFiring) {
            timeToFireAgain += delta;
            if (timeToFireAgain >= TIME_TO_FIRE_AGAIN) {
                timeToFireAgain -= TIME_TO_FIRE_AGAIN;
                createBullet();
                Assets.playSound(Assets.shoot1, .75f);
            }
        } else {
            timeToFireAgain = TIME_TO_FIRE_AGAIN;
        }
    }

    private void createBullet() {
        boolean isFacingLeft = hero.isFacingLeft;
        Bullet obj;

        if (isFacingLeft) {
            obj = new Bullet(hero.position.x - .42f, hero.position.y - .14f, true);
        } else {
            obj = new Bullet(hero.position.x + .42f, hero.position.y - .14f, false);
        }

        if (!hero.isWalking)
            hero.fire();// Puts the state on fire and the animation appears

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = obj.position.x;
        bodyDefinition.position.y = obj.position.y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.1f, .1f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 1;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);

        body.setFixedRotation(true);
        body.setUserData(obj);
        body.setBullet(true);
        body.setGravityScale(0);
        bullets.add(obj);

        if (isFacingLeft)
            body.setLinearVelocity(-Bullet.VELOCIDAD, 0);
        else
            body.setLinearVelocity(Bullet.VELOCIDAD, 0);
    }

    private void createItemFromZombie(float x, float y) {
        Items obj;
        int tipo = MathUtils.random(4);

        switch (tipo) {
            case 0:
                obj = new ItemGem(x, y);
                break;
            case 1:
                obj = new ItemHeart(x, y);
                break;
            case 2:
                obj = new ItemShield(x, y);
                break;
            default:
                obj = new ItemMeat(x, y);
                break;
        }

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.y = obj.position.y;
        bodyDefinition.position.x = obj.position.x;
        bodyDefinition.type = BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(.15f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.restitution = .3f;
        fixtureDefinition.density = 1;
        fixtureDefinition.friction = 1f;
        fixtureDefinition.filter.groupIndex = -1;

        Body body = world.createBody(bodyDefinition);
        body.createFixture(fixtureDefinition);

        body.setUserData(obj);
        items.add(obj);
        shape.dispose();
    }

    public void update(float delta, boolean didJump, boolean isFiring, float accelX, float accelY) {
        world.step(delta, 8, 4);

        removeObjects();

        createBullet(isFiring, delta);

        world.getBodies(bodies);

        for (Body body : bodies) {
            if (body.getUserData() instanceof Hero) {
                updateHeroPlayer(delta, body, didJump, accelX, accelY);
            } else if (body.getUserData() instanceof Zombie) {
                updateZombieMalo(delta, body);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(delta, body);
            } else if (body.getUserData() instanceof Crate) {
                updateCrate(delta, body);
            } else if (body.getUserData() instanceof Saw) {
                updateSaw(delta, body);
            } else if (body.getUserData() instanceof Items) {
                updateItems(delta, body);
            }
        }

        if (hero.state == Hero.STATE_DEAD && hero.stateTime >= Hero.DURATION_DEAD)
            state = STATE_GAMEOVER;
    }

    private void updateZombieMalo(float delta, Body body) {
        Zombie obj = (Zombie) body.getUserData();

        if (obj.position.x > hero.position.x - 2 && obj.position.x < hero.position.x + 2 && obj.position.y < hero.position.y + .5f
                && obj.position.y > hero.position.y - .5f && !obj.canUpdate) {
            obj.canUpdate = true;
            Sound sound = null;
            switch (obj.tipo) {

                case Zombie.TIPO_CUASY:
                    sound = Assets.zombieCuasy;
                    break;
                case Zombie.TIPO_FRANK:
                    sound = Assets.zombieFrank;
                    break;
                case Zombie.TIPO_KID:
                    sound = Assets.zombieKid;
                    break;
                case Zombie.TIPO_MUMMY:
                    sound = Assets.zombieMummy;
                    break;
                case Zombie.TIPO_PAN:
                    sound = Assets.zombiePan;
                    break;
            }
            Assets.playSound(sound, 1);
        }

        obj.update(delta, body, 0, hero);

        if (obj.position.y < -.5f) {
            obj.die();
        }
    }

    private void updateHeroPlayer(float delta, Body body, boolean didJump, float accelX, float accelY) {
        hero.update(delta, body, didJump, accelX, accelY);

        if (hero.position.y < -.5f) {
            hero.die();
        }
    }

    private void updateBullet(float delta, Body body) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(delta, body);

        if (obj.position.x > hero.position.x + 4 || obj.position.x < hero.position.x - 4)
            obj.state = Bullet.STATE_DESTROY;
    }

    private void updateCrate(float delta, Body body) {
        Crate obj = (Crate) body.getUserData();
        obj.update(delta, body);
    }

    private void updateSaw(float delta, Body body) {
        Saw obj = (Saw) body.getUserData();
        obj.update(delta, body);
    }

    private void updateItems(float delta, Body body) {
        Items obj = (Items) body.getUserData();
        obj.update(delta, body);
    }

    private void removeObjects() {
        world.getBodies(bodies);

        for (Body body : bodies) {
            if (!world.isLocked()) {

                if (body.getUserData() instanceof Items) {
                    Items obj = (Items) body.getUserData();
                    if (obj.state == Items.STATE_TAKEN) {
                        items.removeValue(obj, true);
                        world.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Zombie) {
                    Zombie obj = (Zombie) body.getUserData();
                    if (obj.state == Zombie.STATE_DEAD && obj.stateTime >= Zombie.DURATION_DEAD) {
                        float x = obj.position.x;
                        float y = obj.position.y;
                        zombies.removeValue(obj, true);
                        world.destroyBody(body);
                        totalZombiesKilled++;
                        Settings.gemsTotal += 3;
                        gems += 3;

                        if (MathUtils.random(50) <= ((Settings.LEVEL_CHANCE_DROP + 1) * 2))
                            createItemFromZombie(x, y);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet obj = (Bullet) body.getUserData();
                    if (obj.state == Bullet.STATE_DESTROY) {
                        bullets.removeValue(obj, true);
                        world.destroyBody(body);
                    }
                }
            }
        }
    }

    class CollisionHandler implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Hero)
                handleHeroCollisions(a, b);
            else if (b.getBody().getUserData() instanceof Hero)
                handleHeroCollisions(b, a);

            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBulletOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBulletOtraCosa(b, a);
        }

        private void handleHeroCollisions(Fixture heroFixture, Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("ladder")) {
                hero.isOnStairs = true;
            } else if (otherObject.equals("suelo") || otherObject instanceof Platform) {
                if (heroFixture.getUserData().equals("pies"))
                    hero.canJump = true;
            } else if (otherObject instanceof Crate) {

                if (heroFixture.getUserData().equals("pies")) {
                    hero.canJump = true;
                    hero.bodyCrate = otherFixture.getBody();
                }
            } else if (otherObject.equals("spikes")) {
                hero.die();
            } else if (otherObject instanceof Saw) {
                hero.die();
            } else if (otherObject instanceof ItemGem) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken();
                    Settings.gemsTotal++;
                    gems++;

                    Assets.playSound(Assets.gem, .075f);
                }
            } else if (otherObject instanceof ItemHeart) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken();
                    hero.getHeart();

                    Assets.playSound(Assets.hearth, 1);
                }
            } else if (otherObject instanceof ItemSkull) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken();
                    skulls++;

                    Assets.playSound(Assets.skull, .3f);
                }
            } else if (otherObject instanceof ItemMeat) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken();

                    Assets.playSound(Assets.hearth, 1);
                }
            } else if (otherObject instanceof ItemShield) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken();
                    hero.getShield();
                    Assets.playSound(Assets.shield, 1);
                }
            } else if (otherObject instanceof ItemStar) {
                Items obj = (Items) otherObject;
                if (hero.state != Hero.STATE_DEAD && state == STATE_RUNNING) {
                    obj.taken();
                    state = STATE_NEXT_LEVEL;
                    if (totalZombiesKilled == TOTAL_ZOMBIES_LEVEL) {
                        bonus = (int) (gems * 2.5f);
                        Settings.gemsTotal += bonus;
                    }
                }
            } else if (otherObject instanceof Zombie) {
                Zombie obj = (Zombie) otherObject;
                if (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT) {
                    hero.getHurt();
                    Sound sound;
                    switch (hero.type) {
                        case Hero.TYPE_FORCE:
                        case Hero.TYPE_RAMBO:
                            sound = Assets.hurt1;
                            break;
                        case Hero.TYPE_SWAT:
                            sound = Assets.hurt2;
                            break;
                        default:
                            sound = Assets.hurt3;
                            break;
                    }
                    Assets.playSound(sound, 1);

                    float impulseX = obj.isFacingLeft ? -obj.FORCE_IMPACT : obj.FORCE_IMPACT;
                    float impulseY = 2.5f;

                    heroFixture.getBody().setLinearVelocity(impulseX, impulseY);
                }
                obj.isTouchingPlayer = true;
            }
        }

        private void beginContactBulletOtraCosa(Fixture fixBullet, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

            if (oOtraCosa instanceof Zombie) {
                if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
                    Zombie obj = (Zombie) oOtraCosa;
                    if (obj.state != Zombie.STATE_RISE && obj.state != Zombie.STATE_DEAD) {

                        obj.getHurt(oBullet.DAMAGE);
                        oBullet.hit();

                        float impulse = 0;
                        switch (obj.tipo) {
                            case Zombie.TIPO_KID:
                            case Zombie.TIPO_MUMMY:
                            case Zombie.TIPO_CUASY:
                                impulse = oBullet.isFacingLeft ? -oBullet.FORCE_IMPACT : oBullet.FORCE_IMPACT;
                                break;
                        }
                        otraCosa.getBody().setLinearVelocity(impulse, .5f);
                    }
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a == null || b == null)
                return;

            if (a.getBody().getUserData() instanceof Hero)
                endContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Hero)
                endContactHeroOtraCosa(b, a);
        }

        private void endContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            if (oOtraCosa.equals("ladder")) {
                hero.isOnStairs = false;
            } else if (oOtraCosa instanceof Zombie) {
                Zombie obj = (Zombie) oOtraCosa;
                obj.isTouchingPlayer = false;
            } else if (oOtraCosa instanceof Crate) {
                if (fixHero.getUserData().equals("pies")) {
                    hero.bodyCrate = null;
                }
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Hero)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Hero)
                preSolveHero(b, a, contact);

            if (a.getBody().getUserData() instanceof Zombie)
                preSolveZombie(a, b, contact);
            else if (b.getBody().getUserData() instanceof Zombie)
                preSolveZombie(b, a, contact);
        }

        private void preSolveHero(Fixture fixHero, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform) {
                Platform obj = (Platform) oOtraCosa;

                if (hero.isClimbing) {
                    contact.setEnabled(false);
                    return;
                }
                // Si el pony su centro - la mitad de su altura y el piso su centro mas la mitad de su altura
                // Si ponyY es menor significa q esta por abajo.

                float ponyY = fixHero.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + obj.height / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);
            } else if (oOtraCosa instanceof Zombie) {
                contact.setEnabled(false);
            }
        }

        // Para que el zombie no se atore con las plataformas si va caminando
        private void preSolveZombie(Fixture fixZombie, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            Zombie oZombie = (Zombie) fixZombie.getBody().getUserData();
            if (oOtraCosa instanceof Platform) {
                Platform obj = (Platform) oOtraCosa;

                float ponyY = fixZombie.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + obj.height / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);
            } else if (oOtraCosa instanceof Crate) {
                if (oZombie.state == Zombie.STATE_RISE)
                    contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }
}
