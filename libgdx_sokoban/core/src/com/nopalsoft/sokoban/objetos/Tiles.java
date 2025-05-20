package com.nopalsoft.sokoban.objetos;

import java.util.LinkedHashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.game.Tablero;

/**
 * Todos los objetos son tiles (el piso, el personaje,la cajas)
 * 
 * @author Yayo
 * 
 */
public class Tiles extends Actor {

	final static LinkedHashMap<Integer, Vector2> mapPosiciones = new LinkedHashMap<Integer, Vector2>();
	static {
		// Las posiciones empiezan de izq a derecha de abajo hacia arriba
		int posicionTile = 0;
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 25; x++) {
				mapPosiciones.put(posicionTile, new Vector2(x * 32 * Tablero.UNIT_SCALE, y * 32 * Tablero.UNIT_SCALE));
				posicionTile++;
			}
		}
	}

	// TODOS LOS MAPAS SON DE 25x15 Tiles de 32px lo que da una resolucion de 800x480
	final float SIZE = 32 * Tablero.UNIT_SCALE;// Tamano de la ficha

	public int posicion;

	public Tiles(int posicion) {
		this.posicion = posicion;
		setSize(SIZE, SIZE);
		setPosition(mapPosiciones.get(posicion).x, mapPosiciones.get(posicion).y);

	}

	/**
	 * Si es UNDO se mueve sin animacion (quickFix)
	 * 
	 * @param pos
	 * @param undo
	 */
	public void moveToPosition(int pos, boolean undo) {
		float time = .05f;
		if (Settings.animationWalkIsON && !undo)
			time = .45f;
		this.posicion = pos;
		addAction(Actions.sequence(Actions.moveTo(mapPosiciones.get(posicion).x, mapPosiciones.get(posicion).y, time), Actions.run(new Runnable() {

			@Override
			public void run() {
				endMovingToPosition();
			}

		})));
	}

	/**
	 * Se llama automaticamente cuando ya se a movido a la posicion
	 */
	protected void endMovingToPosition() {

	}

}
