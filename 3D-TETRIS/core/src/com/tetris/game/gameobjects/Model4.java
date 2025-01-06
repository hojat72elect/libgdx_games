package com.tetris.game.gameobjects;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Model4 extends Pieces {
    //Constructor
    public Model4(){
        super();
        shift(b(), v().x, v().y + 1f, v().z);
        shift(c(), v().x, v().y + 2f, v().z);
        shift(d(),v().x,v().y+3f,v().z);
    }
    //Rotate Methods
    @Override
    public void rotateX(boolean[][][] positions, boolean clock) {
        if(y() == "two"){
            if(x() == "one"){
                if(valid(positions,v().x+1f,v().y,v().z) && valid(positions,v().x+2f,v().y,v().z) && valid(positions,v().x+3f,v().y,v().z)){
                    shift(b(),v().x+1f,v().y,v().z);
                    shift(c(),v().x+2f,v().y,v().z);
                    shift(d(),v().x+3f,v().y,v().z);
                    setX("two");
                }
            }
            else{
                if(valid(positions,v().x,v().y,v().z-1f) && valid(positions,v().x,v().y,v().z-2f) && valid(positions,v().x,v().y,v().z-3f)){
                    shift(b(),v().x,v().y,v().z-1f);
                    shift(c(),v().x,v().y,v().z-2f);
                    shift(d(),v().x,v().y,v().z-3f);
                    setX("one");
                }
            }
        }
    }

    @Override
    public void rotateY(boolean[][][] positions, boolean clock) {
        if(y() == "one"){
            if(valid(positions,v().x,v().y,v().z-1f) && valid(positions,v().x,v().y,v().z-2f) && valid(positions,v().x,v().y,v().z-3f)){
                shift(b(),v().x,v().y,v().z-1f);
                shift(c(),v().x,v().y,v().z-2f);
                shift(d(),v().x,v().y,v().z-3f);
                setY("two");
            }
        }
        else{
            if(valid(positions,v().x,v().y+1f,v().z) && valid(positions,v().x,v().y+2f,v().z) && valid(positions,v().x,v().y+3f,v().z)) {
                shift(b(), v().x, v().y + 1f, v().z);
                shift(c(), v().x, v().y + 2f, v().z);
                shift(d(),v().x,v().y+3f,v().z);
                setY("one");
            }
        }
    }

    @Override
    public void rotateZ(boolean[][][] positions, boolean clock) {
        rotateY(positions,clock);
    }
}
