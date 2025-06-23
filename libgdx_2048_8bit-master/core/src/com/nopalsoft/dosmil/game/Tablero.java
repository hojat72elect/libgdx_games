package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.objetos.Pieza;
import com.nopalsoft.dosmil.screens.Screens;

import java.util.Iterator;

public class Tablero extends Group {
	static public final int STATE_RUNNING = 1;
	static public final int STATE_NO_MORE_MOVES = 2;
	static public final int STATE_GAMEOVER = 3;
	public int state;
	Array<Pieza> arrPiezas;

	public float tiempo;
	public long score;

	public boolean moveUp, moveDown, moveLeft, moveRight;
	public boolean didWin;

	public Tablero() {
		setSize(480, 480);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);
		addBackground();

		arrPiezas = new Array<Pieza>(16);

		didWin = false;

		// Inicializco el tablero con puros ceros
		for (int i = 0; i < 16; i++) {
			addActor(new Pieza(i, 0));
		}

		addPieza();
		addPieza();
		state = STATE_RUNNING;
	}

	private void addBackground() {
		Image background = new Image(Assets.fondoTablero);
		background.setSize(getWidth(), getHeight());
		background.setPosition(0, 0);
		addActor(background);

	}

	public void addPieza() {
		if (isTableroFull())
			return;
		boolean vacio = false;
		int num = 0;
		while (!vacio) {
			num = MathUtils.random(15);
			vacio = checarEspacioVacio(num);
		}
		int valor = MathUtils.random(1) == 0 ? 2 : 4;// Las valor inicial puede ser 2 o 4
		Pieza obj = new Pieza(num, valor);
		arrPiezas.add(obj);
		addActor(obj);

	}

	public void addPieza(int pos) {
		if (isTableroFull())
			return;

		Pieza obj = new Pieza(pos, 4);
		arrPiezas.add(obj);
		addActor(obj);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		// Si ya no hay acciones pendientes ahora si me pongo en gameover
		if (state == STATE_NO_MORE_MOVES) {
			int numActions = 0;
			Iterator<Pieza> i = arrPiezas.iterator();
			while (i.hasNext()) {
				numActions += i.next().getActions().size;
			}
			numActions += getActions().size;
			if (numActions == 0)
				state = STATE_GAMEOVER;
			return;
		}

		boolean didMovePieza = false;

		if (moveUp) {
			for (int con = 0; con < 4; con++) {
				Iterator<Pieza> i = arrPiezas.iterator();
				while (i.hasNext()) {
					Pieza obj = i.next();
					int nextPos = obj.posicion - 4;
					// Primero reviso si se puede juntar
					if (checarMergeUp(obj.posicion, nextPos)) {
						Pieza objNext = getPiezaEnPos(nextPos);
						if (!objNext.justChanged && !obj.justChanged) {
							i.remove();
							removePieza(obj);
							objNext.setValor(objNext.getValor() * 2);
							score += objNext.getValor();
							objNext.justChanged = true;
							didMovePieza = true;
							continue;
						}
					}
					if (checarEspacioVacioUp(nextPos)) {
						obj.moveToPosition(nextPos);
						didMovePieza = true;
					}
				}
			}
		}
		else if (moveDown) {
			for (int con = 0; con < 4; con++) {
				Iterator<Pieza> i = arrPiezas.iterator();
				while (i.hasNext()) {
					Pieza obj = i.next();
					int nextPos = obj.posicion + 4;
					// Primero reviso si se puede juntar
					if (checarMergeUp(obj.posicion, nextPos)) {
						Pieza objNext = getPiezaEnPos(nextPos);
						if (!objNext.justChanged && !obj.justChanged) {
							i.remove();
							removePieza(obj);
							objNext.setValor(objNext.getValor() * 2);
							score += objNext.getValor();
							objNext.justChanged = true;
							didMovePieza = true;
							continue;
						}
					}
					if (checarEspacioVacioDown(nextPos)) {
						obj.moveToPosition(nextPos);
						didMovePieza = true;
					}
				}
			}
		}
		else if (moveLeft) {
			for (int con = 0; con < 4; con++) {
				Iterator<Pieza> i = arrPiezas.iterator();
				while (i.hasNext()) {
					Pieza obj = i.next();
					int nextPos = obj.posicion - 1;
					// Primero reviso si se puede juntar
					if (checarMergeSides(obj.posicion, nextPos)) {
						Pieza objNext = getPiezaEnPos(nextPos);
						if (!objNext.justChanged && !obj.justChanged) {
							i.remove();
							removePieza(obj);
							objNext.setValor(objNext.getValor() * 2);
							score += objNext.getValor();
							objNext.justChanged = true;
							didMovePieza = true;
							continue;
						}
					}
					if (checarEspacioVacioLeft(nextPos)) {
						obj.moveToPosition(nextPos);
						didMovePieza = true;
					}
				}
			}
		}
		else if (moveRight) {
			for (int con = 0; con < 4; con++) {
				Iterator<Pieza> i = arrPiezas.iterator();
				while (i.hasNext()) {
					Pieza obj = i.next();
					int nextPos = obj.posicion + 1;
					// Primero reviso si se puede juntar
					if (checarMergeSides(obj.posicion, nextPos)) {
						Pieza objNext = getPiezaEnPos(nextPos);
						if (!objNext.justChanged && !obj.justChanged) {
							i.remove();
							removePieza(obj);
							objNext.setValor(objNext.getValor() * 2);
							score += objNext.getValor();
							objNext.justChanged = true;
							didMovePieza = true;
							continue;
						}
					}
					if (checarEspacioVacioRight(nextPos)) {
						obj.moveToPosition(nextPos);
						didMovePieza = true;
					}
				}
			}
		}

		if (didWin()) {
			state = STATE_NO_MORE_MOVES;
			didWin = true;
		}

		if ((moveUp || moveDown || moveRight || moveLeft) && didMovePieza) {
			addPieza();
			Assets.playSoundMove();
		}

		if (isTableroFull() && !isPosibleToMove()) {
			state = STATE_NO_MORE_MOVES;
		}

		moveDown = moveLeft = moveRight = moveUp = false;

		tiempo += Gdx.graphics.getRawDeltaTime();

	}

	private boolean checarMergeSides(int posActual, int nextPosition) {
		if ((posActual == 3 || posActual == 7 || posActual == 11) && nextPosition > posActual)// Solo pueden juntarse las del mismo rengolon
			return false;
		if ((posActual == 12 || posActual == 8 || posActual == 4) && nextPosition < posActual)
			return false;
		Pieza obj1 = getPiezaEnPos(posActual);
		Pieza obj2 = getPiezaEnPos(nextPosition);

		if (obj1 == null || obj2 == null)
			return false;
		else if (obj1.getValor() != obj2.getValor())
			return false;
		else
			return true;

	}

	private boolean checarMergeUp(int posActual, int nextPosition) {

		Pieza obj1 = getPiezaEnPos(posActual);
		Pieza obj2 = getPiezaEnPos(nextPosition);

		if (obj1 == null || obj2 == null)
			return false;
		else if (obj1.getValor() != obj2.getValor())
			return false;
		else
			return true;

	}

	private boolean checarEspacioVacio(int pos) {
		ArrayIterator<Pieza> ite = new ArrayIterator<Pieza>(arrPiezas);
		while (ite.hasNext()) {
			if (ite.next().posicion == pos)
				return false;
		}
		return true;
	}

	private boolean checarEspacioVacioUp(int pos) {
		if (pos < 0)
			return false;
		return checarEspacioVacio(pos);
	}

	private boolean checarEspacioVacioDown(int pos) {
		if (pos > 15)
			return false;
		return checarEspacioVacio(pos);
	}

	private boolean checarEspacioVacioRight(int pos) {
		if (pos == 4 || pos == 8 || pos == 12 || pos == 16)
			return false;
		return checarEspacioVacio(pos);
	}

	private boolean checarEspacioVacioLeft(int pos) {
		if (pos == 11 || pos == 7 || pos == 3 || pos == -1)
			return false;
		return checarEspacioVacio(pos);
	}

	private Pieza getPiezaEnPos(int pos) {
		ArrayIterator<Pieza> ite = new ArrayIterator<Pieza>(arrPiezas);
		while (ite.hasNext()) {
			Pieza obj = ite.next();
			if (obj.posicion == pos)
				return obj;
		}
		return null;
	}

	private boolean isTableroFull() {
		if (arrPiezas.size == (16))
			return true;
		else
			return false;
	}

	private boolean didWin() {
		ArrayIterator<Pieza> ite = new ArrayIterator<Pieza>(arrPiezas);
		while (ite.hasNext()) {
			Pieza obj = ite.next();
			if (obj.getValor() >= 2000)// si hay una pieza que valga mas de 15 mil se gana
				return true;
		}
		return false;
	}

	private boolean isPosibleToMove() {

		boolean canMove = false;
		if (isPosibleToMoveRightLeft()) {
			canMove = true;

		}

		if (isPosibleToMoveUpDown()) {
			canMove = true;
		}
		return canMove;

	}

	boolean isPosibleToMoveRightLeft() {
		for (int ren = 0; ren < 16; ren += 4) {
			for (int col = ren; col < ren + 3; col++) {
				if (checarMergeSides(col, col + 1))
					return true;
			}
		}
		return false;
	}

	boolean isPosibleToMoveUpDown() {
		for (int col = 0; col < 4; col++) {
			for (int ren = col; ren < col + 16; ren += 4) {
				if (checarMergeUp(ren, ren + 4))
					return true;
			}
		}
		return false;
	}

	private void removePieza(Pieza obj) {
		removeActor(obj);
		arrPiezas.removeValue(obj, true);
	}
}
