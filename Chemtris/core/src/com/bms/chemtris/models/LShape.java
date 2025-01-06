package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class to control 1x2 L shape.
 */
public class LShape extends GameModel {

    private enum X{
        RIGHT, LEFT
    }

    private enum Y{
        UP, DOWN
    }

    private enum Z{
        FRONT,BACK
    }

    private X x;
    private Y y;
    private Z z;
    private int last;


    public LShape(PerspectiveCamera cam, boolean[][][] collision) {
        super(cam, collision);
        x = X.RIGHT;
        y = Y.UP;
        z = Z.FRONT;

        for(float y = 10f; y < 12f; ++y){
            ModelInstance p = new ModelInstance(white,2f,y,2f);
            ModelInstance s = new ModelInstance(gray,2f,y,2f);
            ModelInstance f = new ModelInstance(wire,2f,y,2f);

            parts.add(p);
            shadow.add(s);
            frame.add(f);
        }

        ModelInstance p = new ModelInstance(white,3f,10f,2f);
        ModelInstance s = new ModelInstance(gray,3f,10f,2f);
        ModelInstance f = new ModelInstance(wire,3f,10f,2f);
        parts.add(p);
        shadow.add(s);
        frame.add(f);

        last = parts.size -1;

    }

    @Override
    public void rotateX() {

        switch (y){
            case UP:
                switch (x){
                    case RIGHT:
                        if(canMovePart(-1,0,0)){
                            movePart(last,-1f,0f,0f);
                            x = X.LEFT;
                        }
                        break;

                    case LEFT:
                        if(canMovePart(1,0,0)){
                            movePart(last,1f,0f,0f);
                            x = X.RIGHT;
                        }
                        break;
                }
                break;

            case DOWN:
                switch (x){
                    case RIGHT:
                        if(canMovePart(-1,1,0)){
                            movePart(last,-1f,1f,0f);
                            x = X.LEFT;
                        }
                        break;

                    case LEFT:
                        if(canMovePart(1,1,0)){
                            movePart(last,1f,1f,0f);
                            x = X.RIGHT;
                        }
                        break;
                }
                break;
        }

    }

    @Override
    public void rotateY() {

        switch (y){
            case UP:
                if(canMovePartW(last,0,1,0)){
                    movePartW(last,0f,1f,0f);
                    y = Y.DOWN;
                }
                break;

            case DOWN:
                if(canMovePartW(last,0,-1,0)){
                    movePartW(last,0f,-1f,0f);
                    y = Y.UP;
                }
                break;
        }

    }

    @Override
    public void rotateZ() {

        switch (y){
            case UP:
                switch (z){
                    case FRONT:
                        if(canMovePart(0,0,-1)){
                            movePart(last,0f,0f,-1f);
                            z = Z.BACK;
                        }
                        break;

                    case BACK:
                        if(canMovePart(0,0,1)){
                            movePart(last,0f,0f,1f);
                            z = Z.BACK;
                        }
                        break;
                }
                break;

            case DOWN:
                switch (z){
                    case FRONT:
                        if(canMovePart(0,1,-1)){
                            movePart(last,0f,1f,-1f);
                            z = Z.BACK;
                        }
                        break;

                    case BACK:
                        if(canMovePart(0,1,1)){
                            movePart(last,0f,1f,1f);
                            z = Z.BACK;
                        }
                        break;
                }
                break;
        }

    }
}
