package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game_objects.Box;
import com.nopalsoft.sokoban.game_objects.EndPoint;
import com.nopalsoft.sokoban.game_objects.Wall;
import com.nopalsoft.sokoban.game_objects.Player;
import com.nopalsoft.sokoban.game_objects.Tile;

public class Board extends Group {
	public static final float UNIT_SCALE = 1f;

	static public final int STATE_RUNNING = 1;
	static public final int STATE_GAME_OVER = 2;
	public int state;

	/**
	 * X previous position, Y current position.
	 */
	Array<Vector2> arrayPlayerMoves;

	/**
	 * X previous position, Y current position
	 */
	Array<Vector2> arrayBoxMoves;

	Array<Tile> arrayTiles;
	private Player player;

	public boolean moveUp, moveDown, moveLeft, moveRight;
	public boolean undo;

	int moves;
	float time;

	public Board() {
		setSize(800, 480);

		arrayTiles = new Array<>(25 * 15);
		arrayPlayerMoves = new Array<>();
		arrayBoxMoves = new Array<>();

		initializeMap("StaticMap");
		initializeMap("Objetos");

		// AFTER initializing the objects I add them to the Board in order so that some are drawn before others.
		addToBoard(Wall.class);
		addToBoard(EndPoint.class);
		addToBoard(Box.class);
		addToBoard(Player.class);

		state = STATE_RUNNING;

		time = moves = 0;
	}

	private void addToBoard(Class<?> type) {
        for (Tile obj : arrayTiles) {
            if (obj.getClass() == type) {
                addActor(obj);
            }

        }
	}

	private void initializeMap(String layerName) {
		TiledMapTileLayer layer = (TiledMapTileLayer) Assets.map.getLayers().get(layerName);
		if (layer != null) {

			int tilePosition = 0;
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 25; x++) {
					Cell cell = layer.getCell(x, y);
					if (cell != null) {
						TiledMapTile tile = cell.getTile();
						if (tile.getProperties() != null) {
							if (tile.getProperties().containsKey("tipo")) {
								String type = tile.getProperties().get("tipo").toString();

								if (type.equals("startPoint")) {
									createPlayer(tilePosition);
								}
								else if (type.equals("pared")) {
									createWall(tilePosition);
								}
								else if (type.equals("caja")) {
									createBox(tilePosition, tile.getProperties().get("color").toString());
								}
								else if (type.equals("endPoint")) {
									createEndPoint(tilePosition, tile.getProperties().get("color").toString());
								}

							}
						}
					}
					tilePosition++;
				}
			}
		}
	}

	private void createPlayer(int tilePosition) {
		Player obj = new Player(tilePosition);
		arrayTiles.add(obj);
		player = obj;

	}

	private void createWall(int tilePosition) {
		Wall obj = new Wall(tilePosition);
		arrayTiles.add(obj);

	}

	private void createBox(int posTile, String color) {
		Box obj = new Box(posTile, color);
		arrayTiles.add(obj);
	}

	private void createEndPoint(int posTile, String color) {
		EndPoint obj = new EndPoint(posTile, color);
		arrayTiles.add(obj);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (state == STATE_RUNNING) {

			if (moves <= 0)
				undo = false;

			if (undo) {
				undo();
			}
			else {
				int auxMoves = 0;
				if (moveUp) {
					auxMoves = 25;
				}
				else if (moveDown) {
					auxMoves = -25;
				}
				else if (moveLeft) {
					auxMoves = -1;
				}
				else if (moveRight) {
					auxMoves = 1;
				}

				if (player.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
					int nextPos = player.position + auxMoves;

					if (checkEmptySpace(nextPos) || (!checkIsBoxInPosition(nextPos) && checkIsEndInPosition(nextPos))) {
						arrayPlayerMoves.add(new Vector2(player.position, nextPos));
						arrayBoxMoves.add(null);
						player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
						moves++;
					}
					else {
						if (checkIsBoxInPosition(nextPos)) {
							int boxNextPos = nextPos + auxMoves;
							if (checkEmptySpace(boxNextPos) || (!checkIsBoxInPosition(boxNextPos) && checkIsEndInPosition(boxNextPos))) {
								Box box = getBoxInPosition(nextPos);

								arrayPlayerMoves.add(new Vector2(player.position, nextPos));
								arrayBoxMoves.add(new Vector2(box.position, boxNextPos));
								moves++;

								box.moveToPosition(boxNextPos, false);
								player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
								box.setIsInEndPoint(getEndPointInPosition(boxNextPos));

							}
						}
					}
				}

				moveDown = moveLeft = moveRight = moveUp = false;

				if (checkBoxesMissingTheRightEndPoint() == 0)
					state = STATE_GAME_OVER;

			}

			if (state == STATE_RUNNING)
				time += Gdx.graphics.getRawDeltaTime();
		}
	}

	private void undo() {
		if (arrayPlayerMoves.size >= moves) {
			Vector2 posAntPersonaje = arrayPlayerMoves.removeIndex(moves - 1);
			player.moveToPosition((int) posAntPersonaje.x, true);
		}
		if (arrayBoxMoves.size >= moves) {
			Vector2 posAntBox = arrayBoxMoves.removeIndex(moves - 1);
			if (posAntBox != null) {
				Box oBox = getBoxInPosition((int) posAntBox.y);
				oBox.moveToPosition((int) posAntBox.x, true);
				oBox.setIsInEndPoint(getEndPointInPosition(oBox.position));
			}
		}
		moves--;
		undo = false;
	}

	private boolean checkEmptySpace(int pos) {
		ArrayIterator<Tile> iterator = new ArrayIterator<>(arrayTiles);
		while (iterator.hasNext()) {
			if (iterator.next().position == pos)
				return false;
		}
		return true;
	}

	/**
	 * Indicates whether the object at position is a box.
	 */
	private boolean checkIsBoxInPosition(int position) {
		boolean isBoxInPosition = false;
		ArrayIterator<Tile> ite = new ArrayIterator<>(arrayTiles);
		while (ite.hasNext()) {
			com.nopalsoft.sokoban.game_objects.Tile obj = ite.next();
			if (obj.position == position && obj instanceof Box)
				isBoxInPosition = true;
		}
		return isBoxInPosition;

	}

	/**
	 * Indicates whether the object at position is endPoint.
	 */
	private boolean checkIsEndInPosition(int position) {
		boolean isEndPointInPosition = false;
		ArrayIterator<Tile> ite = new ArrayIterator<>(arrayTiles);
		while (ite.hasNext()) {
			com.nopalsoft.sokoban.game_objects.Tile obj = ite.next();
			if (obj.position == position && obj instanceof EndPoint)
				isEndPointInPosition = true;
		}
		return isEndPointInPosition;

	}

	private Box getBoxInPosition(int pos) {
		ArrayIterator<Tile> ite = new ArrayIterator<>(arrayTiles);
		while (ite.hasNext()) {
			com.nopalsoft.sokoban.game_objects.Tile obj = ite.next();
			if (obj.position == pos && obj instanceof Box)
				return (Box) obj;
		}
		return null;
	}

	private EndPoint getEndPointInPosition(int pos) {
		ArrayIterator<Tile> ite = new ArrayIterator<>(arrayTiles);
		while (ite.hasNext()) {
			com.nopalsoft.sokoban.game_objects.Tile obj = ite.next();
			if (obj.position == pos && obj instanceof EndPoint)
				return (EndPoint) obj;
		}
		return null;
	}

	private int checkBoxesMissingTheRightEndPoint() {
		int numBox = 0;
		ArrayIterator<Tile> ite = new ArrayIterator<>(arrayTiles);
		while (ite.hasNext()) {
			com.nopalsoft.sokoban.game_objects.Tile obj = ite.next();
			if (obj instanceof Box) {
				Box oBox = (Box) obj;
				if (!oBox.isInRightEndPoint)
					numBox++;
			}

		}
		return numBox;
	}

}
