package com.nGame.utils.ng.input;

/**
 * Created by n0iz on 02.02.2015.
 */
public abstract class InputResponceEvent {

    String identifier;

    public InputResponceEvent(String identifier){
        this.identifier=identifier;
    }

    public abstract void onAction(float value);
}
