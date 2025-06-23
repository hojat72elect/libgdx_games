package com.nopalsoft.fifteen.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.fifteen.Assets;
import com.nopalsoft.fifteen.objetos.Pieza;
import com.nopalsoft.fifteen.screens.Screens;

public class Tablero extends Group {
	static public final int STATE_SHUFFLE = 0;
	static public final int STATE_RUNNING = 1;
	static public final int STATE_NO_MORE_MOVES = 2;
	static public final int STATE_GAMEOVER = 3;
	public int state;
	Array<Pieza> arrPiezasNums;
	Pieza oPiezaBlanca;

	public float tiempo;
	public int moves;

	public boolean moveUp, moveDown, moveLeft, moveRight, shuffle;

	public Tablero() {
		setSize(480, 480);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);
		addBackground();

		// Inicializco el tablero con puros ceros
		for (int i = 0; i < 16; i++) {
			addActor(new Pieza(i, 0));
		}

		arrPiezasNums = new Array<Pieza>();
		for (int i = 1; i < 16; i++) {
			Pieza obj = new Pieza(i - 1, i);
			addActor(obj);
			arrPiezasNums.add(obj);
		}
		oPiezaBlanca = new Pieza(15, -10);// La pieza que no tiene nada vale -10;
		arrPiezasNums.add(oPiezaBlanca);
		addActor(oPiezaBlanca);

		shuffle();// Dentro de shuffle pongo el estado

	}

	private void addBackground() {
		Image background = new Image(Assets.fondoTablero);
		background.setSize(getWidth(), getHeight());
		background.setPosition(0, 0);
		addActor(background);

	}

	private void shuffle() {
		state = STATE_SHUFFLE;
		int timesToShuffle = 3500;

		for (int i = 0; i < timesToShuffle; i++) {
			int shuffle = MathUtils.random(4);
			switch (shuffle) {
			case 0:
				if (!moveUp())
					i--;
				break;
			case 1:
				if (!moveDown())
					i--;
				break;
			case 2:
				if (!moveLeft())
					i--;
				break;
			case 3:
			default:
				if (!moveRight())
					i--;
				break;
			}
		}
		tiempo = moves = 0;
		state = STATE_RUNNING;
	}

	// Revisa si la posicion de la izquierda esta en el mismo renglon
	private boolean canMoveLeft(int nextPos) {
		if (nextPos == 11 || nextPos == 7 || nextPos == 3 || nextPos == -1)
			return false;
		return true;
	}

	// Revisa si la posicion de la derecha esta en el mismo renglon
	private boolean canMoveRight(int nextPos) {
		if (nextPos == 4 || nextPos == 8 || nextPos == 12 || nextPos == 16)
			return false;
		return true;
	}

	private boolean canMoveUp(int nextPos) {
		if (nextPos < 0)
			return false;
		return true;
	}

	private boolean canMoveDown(int nextPos) {
		if (nextPos > 15)
			return false;
		return true;
	}

	// Intercambia la posicion de dos piezas
	private void swapPieces(Pieza a, Pieza b) {
		int posA = a.posicion;
		int posB = b.posicion;

		if (state == STATE_SHUFFLE) {
			a.moveInstantly(posB);
			b.moveInstantly(posA);
		}
		else {
			a.moveToPosition(posB);
			b.moveToPosition(posA);
		}
		moves++;
		if (state == STATE_RUNNING)
			Assets.playSoundMove();
	}

	float acum;

	@Override
	public void act(float delta) {
		super.act(delta);

		if (state == STATE_NO_MORE_MOVES) {
			int numActions = 0;
			Iterator<Pieza> i = arrPiezasNums.iterator();
			while (i.hasNext()) {
				numActions += i.next().getActions().size;
			}
			numActions += getActions().size;
			if (numActions == 0)
				state = STATE_GAMEOVER;
			return;
		}

		if (moveUp) {
			moveUp();
		}
		else if (moveDown) {
			moveDown();
		}
		else if (moveLeft) {
			moveLeft();
		}
		else if (moveRight) {
			moveRight();
		}

		if (shuffle)
			shuffle();

		moveDown = moveLeft = moveRight = moveUp = shuffle = false;

		if (checkWinClassic()) {
			Gdx.app.log("WIN CLASSIC", "");
			state = STATE_NO_MORE_MOVES;
		}

		tiempo += Gdx.graphics.getRawDeltaTime();

	}

	private Pieza getPiezaEnPos(int pos) {
		ArrayIterator<Pieza> ite = new ArrayIterator<Pieza>(arrPiezasNums);
		while (ite.hasNext()) {
			Pieza obj = ite.next();
			if (obj.posicion == pos)
				return obj;
		}
		return null;

	}

	/**
	 * Verdadero si se logro hacer el movimiento
	 * 
	 * @return
	 */
	public boolean moveUp() {
		int nextPos = oPiezaBlanca.posicion - 4;
		if (canMoveUp(nextPos)) {
			swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos));
			return true;
		}
		return false;
	}

	/**
	 * Verdadero si se logro hacer el movimiento
	 * 
	 * @return
	 */
	public boolean moveDown() {
		int nextPos = oPiezaBlanca.posicion + 4;
		if (canMoveDown(nextPos)) {
			swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos));
			return true;
		}
		return false;
	}

	/**
	 * Verdadero si se logro hacer el movimiento
	 * 
	 * @return
	 */
	public boolean moveRight() {
		int nextPos = oPiezaBlanca.posicion + 1;
		if (canMoveRight(nextPos)) {
			swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos));
			return true;
		}
		return false;
	}

	/**
	 * Verdadero si se logro hacer el movimiento
	 * 
	 * @return
	 */
	public boolean moveLeft() {
		int nextPos = oPiezaBlanca.posicion - 1;
		if (canMoveLeft(nextPos)) {
			swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos));
			return true;
		}
		return false;
	}

	boolean checkWinClassic() {
		for (int i = 0; i < 15; i++) {
			Pieza obj = getPiezaEnPos(i);

			if (i + 1 != obj.valor)
				return false;

		}
		return true;
	}

}
