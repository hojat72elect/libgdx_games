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
import com.nopalsoft.sokoban.objects.Box;
import com.nopalsoft.sokoban.objects.TargetPlatform;
import com.nopalsoft.sokoban.objects.Player;
import com.nopalsoft.sokoban.objects.Tiles;
import com.nopalsoft.sokoban.objects.Wall;

public class GameBoard extends Group {
    public static final float UNIT_SCALE = 1f;

    static public final int STATE_RUNNING = 1;
    static public final int STATE_GAME_OVER = 2;
    public int state;

    /**
     * X previous position, Y current position.
     */
    Array<Vector2> playerMoves;

    /**
     * X previous position, Y current position.
     */
    Array<Vector2> boxMoves;

    Array<Tiles> tiles;
    private Player player;

    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean undo;

    int moves;
    float time;

    public GameBoard() {
        setSize(800, 480);

        tiles = new Array<>(25 * 15);
        playerMoves = new Array<>();
        boxMoves = new Array<>();

        initializeMap("StaticMap");
        initializeMap("Objetos");

        // AFTER initializing the objects I add them to the Board in order so that some are drawn before others
        addByTypeToBoard(Wall.class);
        addByTypeToBoard(TargetPlatform.class);
        addByTypeToBoard(Box.class);
        addByTypeToBoard(Player.class);

        state = STATE_RUNNING;

        time = moves = 0;
    }

    private void addByTypeToBoard(Class<?> tileType) {
        for (Tiles tile : tiles) {
            if (tile.getClass() == tileType) {
                addActor(tile);
            }
        }
    }

    private void initializeMap(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) Assets.map.getLayers().get(layerName);
        if (layer != null) {

            int posTile = 0;
            for (int y = 0; y < 15; y++) {
                for (int x = 0; x < 25; x++) {
                    Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        TiledMapTile tile = cell.getTile();
                        if (tile.getProperties() != null) {
                            if (tile.getProperties().containsKey("tipo")) {
                                String tipo = tile.getProperties().get("tipo").toString();

                                if (tipo.equals("startPoint")) {
                                    createPlayer(posTile);
                                } else if (tipo.equals("pared")) {
                                    createWall(posTile);
                                } else if (tipo.equals("caja")) {
                                    createBox(posTile, tile.getProperties().get("color").toString());
                                } else if (tipo.equals("endPoint")) {
                                    createTargetPlatform(posTile, tile.getProperties().get("color").toString());
                                }
                            }
                        }
                    }
                    posTile++;
                }
            }
        }
    }

    private void createPlayer(int position) {
        Player playerTile = new Player(position);
        tiles.add(playerTile);
        player = playerTile;
    }

    private void createWall(int position) {
        Wall wallTile = new Wall(position);
        tiles.add(wallTile);
    }

    private void createBox(int position, String color) {
        Box boxTile = new Box(position, color);
        tiles.add(boxTile);
    }

    private void createTargetPlatform(int position, String color) {
        TargetPlatform targetPlatformTile = new TargetPlatform(position, color);
        tiles.add(targetPlatformTile);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (state == STATE_RUNNING) {

            if (moves <= 0)
                undo = false;

            if (undo) {
                undo();
            } else {
                int auxMoves = 0;
                if (moveUp) {
                    auxMoves = 25;
                } else if (moveDown) {
                    auxMoves = -25;
                } else if (moveLeft) {
                    auxMoves = -1;
                } else if (moveRight) {
                    auxMoves = 1;
                }

                if (player.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
                    int nextPos = player.posicion + auxMoves;

                    if (checarEspacioVacio(nextPos) || (!isBoxAtPosition(nextPos) && checarIsEndInPosition(nextPos))) {
                        playerMoves.add(new Vector2(player.posicion, nextPos));
                        boxMoves.add(null);
                        player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
                        moves++;
                    } else {
                        if (isBoxAtPosition(nextPos)) {
                            int boxNextPosition = nextPos + auxMoves;
                            if (checarEspacioVacio(boxNextPosition) || (!isBoxAtPosition(boxNextPosition) && checarIsEndInPosition(boxNextPosition))) {
                                Box oBox = getBoxInPosition(nextPos);

                                playerMoves.add(new Vector2(player.posicion, nextPos));
                                boxMoves.add(new Vector2(oBox.posicion, boxNextPosition));
                                moves++;

                                oBox.moveToPosition(boxNextPosition, false);
                                player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
                                oBox.setIsInEndPoint(getEndPointInPosition(boxNextPosition));
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
        if (playerMoves.size >= moves) {
            Vector2 posAntPersonaje = playerMoves.removeIndex(moves - 1);
            player.moveToPosition((int) posAntPersonaje.x, true);
        }
        if (boxMoves.size >= moves) {
            Vector2 posAntBox = boxMoves.removeIndex(moves - 1);
            if (posAntBox != null) {
                Box oBox = getBoxInPosition((int) posAntBox.y);
                oBox.moveToPosition((int) posAntBox.x, true);
                oBox.setIsInEndPoint(getEndPointInPosition(oBox.posicion));
            }
        }
        moves--;
        undo = false;
    }

    private boolean checarEspacioVacio(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<>(tiles);
        while (ite.hasNext()) {
            if (ite.next().posicion == pos)
                return false;
        }
        return true;
    }

    /**
     * Indicates whether the object at position is a box.
     */
    private boolean isBoxAtPosition(int position) {
        boolean isBoxInPosition = false;
        ArrayIterator<Tiles> iterator = new ArrayIterator<>(tiles);
        while (iterator.hasNext()) {
            Tiles obj = iterator.next();
            if (obj.posicion == position && obj instanceof Box)
                isBoxInPosition = true;
        }
        return isBoxInPosition;
    }

    /**
     * Indicates whether the object at position is endPoint.
     */
    private boolean checarIsEndInPosition(int pos) {
        boolean isEndPointInPosition = false;
        ArrayIterator<Tiles> ite = new ArrayIterator<>(tiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof TargetPlatform)
                isEndPointInPosition = true;
        }
        return isEndPointInPosition;
    }

    private Box getBoxInPosition(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<>(tiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof Box)
                return (Box) obj;
        }
        return null;
    }

    private TargetPlatform getEndPointInPosition(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<>(tiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof TargetPlatform)
                return (TargetPlatform) obj;
        }
        return null;
    }

    private int checkBoxesMissingTheRightEndPoint() {
        int numBox = 0;
        ArrayIterator<Tiles> ite = new ArrayIterator<>(tiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj instanceof Box) {
                Box oBox = (Box) obj;
                if (!oBox.isInRightEndPoint)
                    numBox++;
            }
        }
        return numBox;
    }
}
