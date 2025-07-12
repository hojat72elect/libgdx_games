package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.game_objects.BoardPiece;
import com.nopalsoft.dosmil.screens.Screens;

import java.util.Iterator;

public class GameBoard extends Group {
    static public final int STATE_RUNNING = 1;
    static public final int STATE_NO_MORE_MOVES = 2;
    static public final int STATE_GAMEOVER = 3;
    public int state;
    Array<BoardPiece> boardPieces;

    public float elapsedTime;
    public long score;

    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean didWin;

    public GameBoard() {
        setSize(480, 480);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);
        addBackground();

        boardPieces = new Array<>(16);

        didWin = false;

        // I initialize the board with all zeros
        for (int i = 0; i < 16; i++) {
            addActor(new BoardPiece(i, 0));
        }

        addPiece();
        addPiece();
        state = STATE_RUNNING;
    }

    private void addBackground() {
        Image background = new Image(Assets.backgroundBoardAtlasRegion);
        background.setSize(getWidth(), getHeight());
        background.setPosition(0, 0);
        addActor(background);
    }

    public void addPiece() {
        if (isBoardFull())
            return;
        boolean isEmpty = false;
        int num = 0;
        while (!isEmpty) {
            num = MathUtils.random(15);
            isEmpty = isSpaceEmpty(num);
        }
        int valor = MathUtils.random(1) == 0 ? 2 : 4;// The initial value can be 2 or 4
        BoardPiece obj = new BoardPiece(num, valor);
        boardPieces.add(obj);
        addActor(obj);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // If there are no pending actions now, I'll put myself in gameover.
        if (state == STATE_NO_MORE_MOVES) {
            int numActions = 0;
            for (BoardPiece arrBoardPiece : boardPieces) {
                numActions += arrBoardPiece.getActions().size;
            }
            numActions += getActions().size;
            if (numActions == 0)
                state = STATE_GAMEOVER;
            return;
        }

        boolean didPieceMove = false;

        if (moveUp) {
            for (int con = 0; con < 4; con++) {
                Iterator<BoardPiece> i = boardPieces.iterator();
                while (i.hasNext()) {
                    BoardPiece obj = i.next();
                    int nextPos = obj.position - 4;
                    // First I check if it can be put together
                    if (canMergeUp(obj.position, nextPos)) {
                        BoardPiece objNext = getPieceAtPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            objNext.setValor(objNext.getValor() * 2);
                            score += objNext.getValor();
                            objNext.justChanged = true;
                            didPieceMove = true;
                            continue;
                        }
                    }
                    if (isSpaceAboveEmpty(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didPieceMove = true;
                    }
                }
            }
        } else if (moveDown) {
            for (int con = 0; con < 4; con++) {
                Iterator<BoardPiece> i = boardPieces.iterator();
                while (i.hasNext()) {
                    BoardPiece obj = i.next();
                    int nextPos = obj.position + 4;
                    // First I check if it can be put together
                    if (canMergeUp(obj.position, nextPos)) {
                        BoardPiece objNext = getPieceAtPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            objNext.setValor(objNext.getValor() * 2);
                            score += objNext.getValor();
                            objNext.justChanged = true;
                            didPieceMove = true;
                            continue;
                        }
                    }
                    if (isSpaceUnderEmpty(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didPieceMove = true;
                    }
                }
            }
        } else if (moveLeft) {
            for (int con = 0; con < 4; con++) {
                Iterator<BoardPiece> i = boardPieces.iterator();
                while (i.hasNext()) {
                    BoardPiece obj = i.next();
                    int nextPos = obj.position - 1;
                    // First I check if it can be put together
                    if (canMergeTiles(obj.position, nextPos)) {
                        BoardPiece objNext = getPieceAtPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            objNext.setValor(objNext.getValor() * 2);
                            score += objNext.getValor();
                            objNext.justChanged = true;
                            didPieceMove = true;
                            continue;
                        }
                    }
                    if (isSpaceLeftEmpty(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didPieceMove = true;
                    }
                }
            }
        } else if (moveRight) {
            for (int con = 0; con < 4; con++) {
                Iterator<BoardPiece> i = boardPieces.iterator();
                while (i.hasNext()) {
                    BoardPiece obj = i.next();
                    int nextPos = obj.position + 1;
                    // First I check if it can be put together
                    if (canMergeTiles(obj.position, nextPos)) {
                        BoardPiece objNext = getPieceAtPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            objNext.setValor(objNext.getValor() * 2);
                            score += objNext.getValor();
                            objNext.justChanged = true;
                            didPieceMove = true;
                            continue;
                        }
                    }
                    if (isSpaceRightEmpty(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didPieceMove = true;
                    }
                }
            }
        }

        if (didWin()) {
            state = STATE_NO_MORE_MOVES;
            didWin = true;
        }

        if ((moveUp || moveDown || moveRight || moveLeft) && didPieceMove) {
            addPiece();
            Assets.playSoundMove();
        }

        if (isBoardFull() && !isPossibleToMove()) {
            state = STATE_NO_MORE_MOVES;
        }

        moveDown = moveLeft = moveRight = moveUp = false;

        elapsedTime += Gdx.graphics.getRawDeltaTime();
    }

    /**
     * Checks to see if two tiles can be merged together.
     */
    private boolean canMergeTiles(int posActual, int nextPosition) {
        if ((posActual == 3 || posActual == 7 || posActual == 11) && nextPosition > posActual) // Only those of the same rank can be merged together.
            return false;
        if ((posActual == 12 || posActual == 8 || posActual == 4) && nextPosition < posActual)
            return false;
        BoardPiece obj1 = getPieceAtPosition(posActual);
        BoardPiece obj2 = getPieceAtPosition(nextPosition);

        if (obj1 == null || obj2 == null)
            return false;
        else return obj1.getValor() == obj2.getValor();
    }

    /**
     * If it's possible to merge this tile with the tile above it.
     */
    private boolean canMergeUp(int posActual, int nextPosition) {

        BoardPiece obj1 = getPieceAtPosition(posActual);
        BoardPiece obj2 = getPieceAtPosition(nextPosition);

        if (obj1 == null || obj2 == null)
            return false;
        else return obj1.getValor() == obj2.getValor();
    }

    /**
     * Check if the space at this position is empty.
     */
    private boolean isSpaceEmpty(int pos) {
        ArrayIterator<BoardPiece> ite = new ArrayIterator<>(boardPieces);
        while (ite.hasNext()) {
            if (ite.next().position == pos)
                return false;
        }
        return true;
    }

    /**
     * Checks to see if this tile can move upwards.
     */
    private boolean isSpaceAboveEmpty(int pos) {
        if (pos < 0)
            return false;
        return isSpaceEmpty(pos);
    }

    /**
     * Checks to see if this tile can move downwards.
     */
    private boolean isSpaceUnderEmpty(int pos) {
        if (pos > 15)
            return false;
        return isSpaceEmpty(pos);
    }

    /**
     * Checks to see if this tile can move to the right.
     */
    private boolean isSpaceRightEmpty(int pos) {
        if (pos == 4 || pos == 8 || pos == 12 || pos == 16)
            return false;
        return isSpaceEmpty(pos);
    }

    /**
     * Checks to see if this tile can move to the left.
     */
    private boolean isSpaceLeftEmpty(int pos) {
        if (pos == 11 || pos == 7 || pos == 3 || pos == -1)
            return false;
        return isSpaceEmpty(pos);
    }

    /**
     * Get access to the  board piece at this position.
     */
    private BoardPiece getPieceAtPosition(int pos) {
        ArrayIterator<BoardPiece> ite = new ArrayIterator<>(boardPieces);
        while (ite.hasNext()) {
            BoardPiece obj = ite.next();
            if (obj.position == pos)
                return obj;
        }
        return null;
    }

    /**
     * Checks to see if the game board is full.
     */
    private boolean isBoardFull() {
        return boardPieces.size == (16);
    }

    private boolean didWin() {
        ArrayIterator<BoardPiece> ite = new ArrayIterator<>(boardPieces);
        while (ite.hasNext()) {
            BoardPiece obj = ite.next();
            if (obj.getValor() >= 2000)// If there is a piece worth more than 15 thousand, you win.
                return true;
        }
        return false;
    }

    private boolean isPossibleToMove() {

        boolean canMove = isPossibleToMoveRightLeft();

        if (isPossibleToMoveUpDown()) {
            canMove = true;
        }
        return canMove;
    }

    boolean isPossibleToMoveRightLeft() {
        for (int ren = 0; ren < 16; ren += 4) {
            for (int col = ren; col < ren + 3; col++) {
                if (canMergeTiles(col, col + 1))
                    return true;
            }
        }
        return false;
    }

    boolean isPossibleToMoveUpDown() {
        for (int col = 0; col < 4; col++) {
            for (int ren = col; ren < col + 16; ren += 4) {
                if (canMergeUp(ren, ren + 4))
                    return true;
            }
        }
        return false;
    }

    private void removePiece(BoardPiece obj) {
        removeActor(obj);
        boardPieces.removeValue(obj, true);
    }
}
