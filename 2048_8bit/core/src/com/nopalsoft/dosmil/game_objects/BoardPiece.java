package com.nopalsoft.dosmil.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.dosmil.Assets;

import java.util.LinkedHashMap;

public class BoardPiece extends Actor {
    public boolean justChanged = false;

    // Positions start counting from left to right from top to bottom.
    final static LinkedHashMap<Integer, Vector2> positionsMap = new LinkedHashMap<>();

    static {
        positionsMap.put(0, new Vector2(20, 350));
        positionsMap.put(1, new Vector2(130, 350));
        positionsMap.put(2, new Vector2(240, 350));
        positionsMap.put(3, new Vector2(350, 350));
        positionsMap.put(4, new Vector2(20, 240));
        positionsMap.put(5, new Vector2(130, 240));
        positionsMap.put(6, new Vector2(240, 240));
        positionsMap.put(7, new Vector2(350, 240));
        positionsMap.put(8, new Vector2(20, 130));
        positionsMap.put(9, new Vector2(130, 130));
        positionsMap.put(10, new Vector2(240, 130));
        positionsMap.put(11, new Vector2(350, 130));
        positionsMap.put(12, new Vector2(20, 20));
        positionsMap.put(13, new Vector2(130, 20));
        positionsMap.put(14, new Vector2(240, 20));
        positionsMap.put(15, new Vector2(350, 20));
    }

    final float SIZE = 110;// Final size of the tab
    public int position;

    // The value worth of this BoardPiece
    private int worth;// I made this piece private because when I change its value I also have to change the image of this piece.
    TextureRegion keyframe;

    public BoardPiece(int position, int worth) {
        this.position = position;
        setWidth(SIZE);
        setHeight(SIZE);
        setOrigin(SIZE / 2f, SIZE / 2f);

        setPosition(positionsMap.get(position).x, positionsMap.get(position).y);
        setWorth(worth);

        if (worth != 0) {// If the piece is worth 0, it is a blue square that has nothing.
            setScale(.8f);
            addAction(Actions.scaleTo(1, 1, .25f));
            Gdx.app.log("Se creo pieza en ", position + "");
        }
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
        switch (worth) {
            case 2:
                keyframe = Assets.piece2AtlasRegion;
                break;
            case 4:
                keyframe = Assets.piece4AtlasRegion;
                break;
            case 8:
                keyframe = Assets.piece8AtlasRegion;
                break;
            case 16:
                keyframe = Assets.piece16AtlasRegion;
                break;
            case 32:
                keyframe = Assets.piece32AtlasRegion;
                break;
            case 64:
                keyframe = Assets.piece64AtlasRegion;
                break;
            case 128:
                keyframe = Assets.piece128AtlasRegion;
                break;
            case 256:
                keyframe = Assets.piece256AtlasRegion;
                break;
            case 512:
                keyframe = Assets.piece512AtlasRegion;
                break;
            case 1024:
                keyframe = Assets.piece1024AtlasRegion;
                break;
            case 2048:
                keyframe = Assets.piece2048AtlasRegion;
                break;
            case 0:
            default:
                keyframe = Assets.emptyPieceAtlasRegion;
                break;
        }
    }

    @Override
    public void act(float delta) {
        justChanged = false;
        super.act(delta);
    }

    public void moveToPosition(int pos) {
        this.position = pos;
        Gdx.app.log("Move to ", pos + "");
        addAction(Actions.moveTo(positionsMap.get(position).x, positionsMap.get(position).y, .075f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
