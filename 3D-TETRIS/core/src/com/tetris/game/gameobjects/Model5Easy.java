package com.tetris.game.gameobjects;

/**
 * Created by mkabi_000 on 8/10/2015.
 */
public class Model5Easy extends PiecesEasy {
    //Constructor
    public Model5Easy(){
        super();
        shift(b(), v().x + 1f, v().y, v().z);
        shift(c(),v().x,v().y+1f,v().z);
        shift(d(),v().x+1f,v().y+1f,v().z);
    }
    //Rotate Methods
    @Override
    public void rotateX(boolean[][][] positions, boolean clock) {
        if(y() == "one"){
            if(x() == "one"){
                if(valid(positions,v().x, v().y, v().z-1f)&&valid(positions,v().x,v().y+1f,v().z) && valid(positions,v().x,v().y+1f,v().z-1f)){
                    shift(b(),v().x, v().y, v().z-1f);
                    shift(c(),v().x,v().y+1f,v().z);
                    shift(d(),v().x,v().y+1f,v().z-1f);
                    setX("two");
                }
            }
            else{
                if(valid(positions,v().x + 1f, v().y, v().z)&&valid(positions,v().x,v().y+1f,v().z)&&valid(positions,v().x+1f,v().y+1f,v().z)){
                    shift(b(), v().x + 1f, v().y, v().z);
                    shift(c(),v().x,v().y+1f,v().z);
                    shift(d(),v().x+1f,v().y+1f,v().z);
                    setX("one");
                }
            }
        }
    }

    @Override
    public void rotateY(boolean[][][] positions, boolean clock) {
        if(y() == "one"){
            if(valid(positions,v().x + 1f, v().y, v().z)&&valid(positions,v().x,v().y,v().z-1f)&&valid(positions,v().x+1f,v().y,v().z-1f)){
                shift(b(), v().x + 1f, v().y, v().z);
                shift(c(),v().x,v().y,v().z-1f);
                shift(d(),v().x+1f,v().y,v().z-1f);
                setY("two");
            }
        }
        else{
            if(valid(positions,v().x + 1f, v().y, v().z)&&valid(positions,v().x,v().y+1f,v().z)&&valid(positions,v().x+1f,v().y+1f,v().z)){
                shift(b(), v().x + 1f, v().y, v().z);
                shift(c(),v().x,v().y+1f,v().z);
                shift(d(),v().x+1f,v().y+1f,v().z);
                setY("one");
            }
        }
    }

    @Override
    public void rotateZ(boolean[][][] positions, boolean clock) {
        rotateY(positions,clock);
    }
}

