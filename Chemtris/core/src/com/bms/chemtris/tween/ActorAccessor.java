package com.bms.chemtris.tween;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

//Class to animate with actors (buttons, labels, images)
public class ActorAccessor implements TweenAccessor<Actor> {

    //
    public static final int Y = 0, //variable to move the actors up/down
            ALPHA = 1; //variable to control actor transparency

    //get member values (Y,ALPHA)
    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case Y:
                returnValues[0]= target.getY();
                return 1;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    //set member values (Y,ALPHA)
    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType){
            case Y:
                target.setY(newValues[0]);
                break;
            case ALPHA:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;
            default:
                assert false;
                break;
        }
    }
}
