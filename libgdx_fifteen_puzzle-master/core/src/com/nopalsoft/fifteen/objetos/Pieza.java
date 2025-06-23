package com.nopalsoft.fifteen.objetos;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.fifteen.Assets;

public class Pieza extends Actor {

	// //Las posiciones empiezan a contar de izq a derecha desde arriba hacia abajo
	final static LinkedHashMap<Integer, Vector2> mapPosiciones = new LinkedHashMap<Integer, Vector2>();
	static {
		mapPosiciones.put(0, new Vector2(20, 350));
		mapPosiciones.put(1, new Vector2(130, 350));
		mapPosiciones.put(2, new Vector2(240, 350));
		mapPosiciones.put(3, new Vector2(350, 350));
		mapPosiciones.put(4, new Vector2(20, 240));
		mapPosiciones.put(5, new Vector2(130, 240));
		mapPosiciones.put(6, new Vector2(240, 240));
		mapPosiciones.put(7, new Vector2(350, 240));
		mapPosiciones.put(8, new Vector2(20, 130));
		mapPosiciones.put(9, new Vector2(130, 130));
		mapPosiciones.put(10, new Vector2(240, 130));
		mapPosiciones.put(11, new Vector2(350, 130));
		mapPosiciones.put(12, new Vector2(20, 20));
		mapPosiciones.put(13, new Vector2(130, 20));
		mapPosiciones.put(14, new Vector2(240, 20));
		mapPosiciones.put(15, new Vector2(350, 20));
	}

	final float SIZE = 110;// Tamano final de la ficha
	public int posicion;

	public int valor;// esta pieza la hice privada porque cuando cambio su valor tambien tengo que cambiar la imagen de esta pieza
	TextureRegion keyframe;

	public Pieza(int posicion, int valor) {
		this.posicion = posicion;
		setWidth(SIZE);
		setHeight(SIZE);
		setOrigin(SIZE / 2f, SIZE / 2f);

		setPosition(mapPosiciones.get(posicion).x,
				mapPosiciones.get(posicion).y);
		this.valor = valor;

		switch (valor) {
		default:
		case -10:
			keyframe = null;
			break;
		case 0:
			keyframe = Assets.piezaVacia;
			break;
		case 1:
			keyframe = Assets.pieza1;
			break;
		case 2:
			keyframe = Assets.pieza2;
			break;
		case 3:
			keyframe = Assets.pieza3;
			break;
		case 4:
			keyframe = Assets.pieza4;
			break;
		case 5:
			keyframe = Assets.pieza5;
			break;
		case 6:
			keyframe = Assets.pieza6;
			break;
		case 7:
			keyframe = Assets.pieza7;
			break;
		case 8:
			keyframe = Assets.pieza8;
			break;
		case 9:
			keyframe = Assets.pieza9;
			break;
		case 10:
			keyframe = Assets.pieza10;
			break;
		case 11:
			keyframe = Assets.pieza11;
			break;
		case 12:
			keyframe = Assets.pieza12;
			break;
		case 13:
			keyframe = Assets.pieza13;
			break;
		case 14:
			keyframe = Assets.pieza14;
			break;
		case 15:
			keyframe = Assets.pieza15;
			break;
		}

	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public void moveToPosition(int pos) {
		this.posicion = pos;
		Gdx.app.log("Move to ", pos + "");
		addAction(Actions.moveTo(mapPosiciones.get(posicion).x,
				mapPosiciones.get(posicion).y, .085f));
	}

	public void moveInstantly(int pos) {
		this.posicion = pos;
		setPosition(mapPosiciones.get(posicion).x,
				mapPosiciones.get(posicion).y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (keyframe == null)
			return;
		batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}

}
