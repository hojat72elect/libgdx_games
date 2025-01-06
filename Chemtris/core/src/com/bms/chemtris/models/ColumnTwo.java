package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class to maintain the 2x1 column.
 */
public class ColumnTwo extends GameModel {
    private enum X{
        FRONT, SIDE
    }

    private enum Y{
        UP, DOWN
    }

    private X x;
    private Y y;

    public ColumnTwo(PerspectiveCamera cam, boolean[][][] collision) {
        super(cam, collision);
        x = X.FRONT;
        y = Y.UP;

        for(float y = 10f; y < 13f; ++y){
            ModelInstance p = new ModelInstance(white,2f,y,2f);
            ModelInstance s = new ModelInstance(gray,2f,y,2f);
            ModelInstance f = new ModelInstance(wire,2f,y,2f);

            parts.add(p);
            shadow.add(s);
            frame.add(f);
        }

        traceShadow();
    }

    @Override
    public void rotateX() {
        switch (y){
            case DOWN:
                switch (x){
                    case FRONT:
                        if(canMovePart(1,0,0) && canMovePart(2,0,0)){
                            movePart(1,1f,0f,0f);
                            movePart(2,2f,0f,0f);
                            x = X.SIDE;
                        }
                        break;
                    case SIDE:
                        if(canMovePart(0,0,-1) && canMovePart(0,0,-2)){
                            movePart(1,0f,0f,-1f);
                            movePart(1,0f,0f,-2f);
                            x = X.FRONT;
                        }
                        break;
                }
        }
    }

    @Override
    public void rotateY() {
        switch (y){
            case UP:
                if(canMovePart(0,0,-1) && canMovePart(0,0,-2)){
                    movePart(1,0f,0f,-1f);
                    movePart(2,0f,0f,-2f);
                    y = Y.DOWN;
                }
                break;

            case DOWN:
                if(canMovePart(0,1,0) && canMovePart(0,2,0)){
                    movePart(1,0f,1f,0f);
                    movePart(2,0f,2f,0f);
                    y = Y.UP;
                }
                break;
        }
    }

    @Override
    public void rotateZ() {
        rotateY();
    }
}
