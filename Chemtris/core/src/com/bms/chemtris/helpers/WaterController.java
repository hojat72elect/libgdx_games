package com.bms.chemtris.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.bms.chemtris.elements.Element;

/**
 * Class to control water.
 */
public class WaterController {
    private WaterStorage storage;
    private float time_elasped_anim;
    private float time_elasped_delay;
    private float descend_time;
    private ModelInstance box;

    //return codes
    public static final int ANIMATION = -10, CONTINUE = -20, DONE = -30, OVER = -100;

    public WaterController(WaterStorage store, float descend, ModelInstance platform_box){
        //initialize members
        storage = store;
        time_elasped_anim = 0f;
        time_elasped_delay = 0f;
        descend_time = descend;
        box = platform_box;
    }

    public int run(float delta, Element current){
        //for desktop version
        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP)){
            current.moveUp();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_DOWN)){
            current.moveDown();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_RIGHT)){
            current.moveRight();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT)){
            current.moveLeft();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            current.rotateX();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            current.rotateY();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            current.moveUp();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            current.descend();
        }

        //increment descend time
        time_elasped_delay += delta;

        //if descend time has been meet or block can no longer go down
        if (time_elasped_delay >= descend_time || !current.canMove(0, -1, 0)) {
            if (current.canMove(0, -1, 0)) {
                current.descend();
                time_elasped_delay = 0f;
                return CONTINUE;
            }
            else {
                time_elasped_anim += delta;
                if (time_elasped_anim < .65f){
                    //change color to red
                    box.materials.get(0).set(ColorAttribute.createDiffuse(255 / 255f, 99 / 255f, 71 / 255f, 1f));
                    return ANIMATION;
                }
                else {
                    //change color to white
                    box.materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));
                    time_elasped_delay = 0f;
                    time_elasped_anim = 0f;
                    return DONE;
                }
            }
        }else{
            return CONTINUE;
        }
    }

}
