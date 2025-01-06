package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class to control 1x2 column.
 */
public class Column extends GameModel {

    private enum X{
        FRONT, SIDE
    }

    private enum Y{
        UP, DOWN
    }

    private X x;
    private Y y;


    public Column(PerspectiveCamera cam, boolean[][][] collision) {
        super(cam, collision);
        x = X.FRONT;
        y = Y.UP;

        for(float y = 10f; y < 12f; ++y){
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
                        if(canMovePart(1,0,0)){
                            System.out.println("yes");
                            movePart(1,1f,0f,0f);
                            x = X.SIDE;
                        }
                        break;
                    case SIDE:
                        if(canMovePart(0,0,-1)){
                            movePart(1,0f,0f,-1f);
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
                System.out.println("yes");
                if(canMovePart(0,0,-1)){
                    movePart(1,0f,0f,-1f);
                    y = Y.DOWN;
                }
                break;

            case DOWN:
                if(canMovePart(0,1,0)){
                    movePart(1,0f,1f,0f);
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
