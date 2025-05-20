package com.nopalsoft.superjumper.objetos;

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

public class Moneda implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_TAKEN = 1;
	public int state;

	public final static float DRAW_WIDTH = .27f;
	public final static float DRAW_HEIGHT = .34f;
	public final static float WIDTH = .25f;
	public final static float HEIGHT = .32f;

	public final Vector2 position;

	public float stateTime;

	public Moneda() {
		position = new Vector2();
	}

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

	final static float SEPARACION_MONEDAS = .025f;// Variable para que las monedas no esten pegadas

	public static void createMoneda(World worldBox, Array<Moneda> arrMonedas, float y) {
		createCubo(worldBox, arrMonedas, y);
	}

	public static void createUnaMoneda(World worldBox, Array<Moneda> arrMonedas, float y) {
		createMoneda(worldBox, arrMonedas, generaPosX(1), y);
	}

	private static void createCubo(World worldBox, Array<Moneda> arrMonedas, float y) {
		int renMax = MathUtils.random(25) + 1;
		int colMax = MathUtils.random(6) + 1;

		float x = generaPosX(colMax);
		for (int col = 0; col < colMax; col++) {
			for (int ren = 0; ren < renMax; ren++) {
				createMoneda(worldBox, arrMonedas, x + (col * (WIDTH + SEPARACION_MONEDAS)), y + (ren * (HEIGHT + SEPARACION_MONEDAS)));
			}
		}

	}

	/**
	 * Genera una posicion en X dependiendo del numero de monedas del renglon para que no se salgan de la pantalla por la derecha o por la izquieda
	 * 
	 * @param numeroMonedasDelRenglon
	 */
	private static float generaPosX(int numeroMonedasDelRenglon) {
		float x = MathUtils.random(Screens.WORLD_WIDTH) + WIDTH / 2f;
		if (x + (numeroMonedasDelRenglon * (WIDTH + SEPARACION_MONEDAS)) > Screens.WORLD_WIDTH) {
			x -= (x + (numeroMonedasDelRenglon * (WIDTH + SEPARACION_MONEDAS))) - Screens.WORLD_WIDTH;// Saca la diferencia del ancho y lo que se pasa
			x += WIDTH / 2f;// Le suma la mitad para que quede pegado
		}
		return x;
	}

	private static void createMoneda(World worldBox, Array<Moneda> arrMonedas, float x, float y) {
		Moneda oMoneda = Pools.obtain(Moneda.class);
		oMoneda.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.StaticBody;
		Body oBody = worldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		oBody.createFixture(fixture);
		oBody.setUserData(oMoneda);
		shape.dispose();
		arrMonedas.add(oMoneda);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
