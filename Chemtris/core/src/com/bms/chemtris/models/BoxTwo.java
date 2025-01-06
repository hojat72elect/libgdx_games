package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class to control 2x2 box
 */
public class BoxTwo extends GameModel {

    public BoxTwo(PerspectiveCamera cam, boolean[][][] collision) {
        super(cam, collision);

        for(float x = 2f; x < 4f; ++x){
            for(float z = 2f; z < 4f; ++z){
                for(float y = 10f; y < 12f; ++y){
                    ModelInstance p = new ModelInstance(white,x,y,z);
                    ModelInstance s = new ModelInstance(gray,x,y,z);
                    ModelInstance f = new ModelInstance(wire,x,y,z);

                    parts.add(p);
                    shadow.add(s);
                    frame.add(f);
                }
            }
        }


        traceShadow();
    }

    @Override
    public void rotateX() {
        return;
    }

    @Override
    public void rotateY() {
        return;
    }

    @Override
    public void rotateZ() {
        return;
    }
}
