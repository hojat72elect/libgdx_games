package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.superjumper.screens.Screens;

public class Coin implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_TAKEN = 1;
    public int state;

    public final static float DRAW_WIDTH = .27f;
    public final static float DRAW_HEIGHT = .34f;
    public final static float WIDTH = .25f;
    public final static float HEIGHT = .32f;

    public final Vector2 position;

    public float stateTime;

    final static float SPACE_BETWEEN_COINS = .025f; // Variable so that the coins are not stuck together

    public void init(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void take() {
        state = STATE_TAKEN;
        stateTime = 0;
    }

    public Coin() {
        position = new Vector2();
    }

    public static void createCoin(World worldBox, Array<Coin> arrMonedas, float y) {
        createCoinCube(worldBox, arrMonedas, y);
    }

    public static void createUnaMoneda(World worldBox, Array<Coin> arrMonedas, float y) {
        createCoin(worldBox, arrMonedas, generaPosX(1), y);
    }

    private static void createCoinCube(World worldBox, Array<Coin> arrMonedas, float y) {
        int renMax = MathUtils.random(25) + 1;
        int colMax = MathUtils.random(6) + 1;

        float x = generaPosX(colMax);
        for (int col = 0; col < colMax; col++) {
            for (int ren = 0; ren < renMax; ren++) {
                createCoin(worldBox, arrMonedas, x + (col * (WIDTH + SPACE_BETWEEN_COINS)), y + (ren * (HEIGHT + SPACE_BETWEEN_COINS)));
            }
        }
    }

    /**
     * Generates an X position depending on the number of coins in the row so
     * that they do not go off the screen to the right or left.
     */
    private static float generaPosX(int numeroMonedasDelRenglon) {
        float x = MathUtils.random(Screens.WORLD_WIDTH) + WIDTH / 2f;
        if (x + (numeroMonedasDelRenglon * (WIDTH + SPACE_BETWEEN_COINS)) > Screens.WORLD_WIDTH) {
            x -= (x + (numeroMonedasDelRenglon * (WIDTH + SPACE_BETWEEN_COINS))) - Screens.WORLD_WIDTH;// Saca la diferencia del ancho y lo que se pasa
            x += WIDTH / 2f; // Add half to make it stick
        }
        return x;
    }

    private static void createCoin(World worldBox, Array<Coin> arrayCoins, float x, float y) {
        Coin oCoin = Pools.obtain(Coin.class);
        oCoin.init(x, y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.StaticBody;
        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);
        body.setUserData(oCoin);
        shape.dispose();
        arrayCoins.add(oCoin);
    }

    @Override
    public void reset() {
    }
}
