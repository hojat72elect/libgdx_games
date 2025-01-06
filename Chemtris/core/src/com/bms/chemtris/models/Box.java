package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class to control basic box model.
 */
public class Box extends GameModel{

    //constructor
    public Box(PerspectiveCamera cam, boolean[][][] collision) {
        super(cam, collision);
        ModelInstance base = new ModelInstance(white,4f,10f,4f);
        ModelInstance sBase = new ModelInstance(gray, 4f,10f,4f);
        ModelInstance fBase = new ModelInstance(wire, 4f,10f,4f);
        parts.add(base);
        shadow.add(sBase);
        frame.add(fBase);

        traceShadow();
    }

    //rotation methods
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
