package com.tetris.game.gameobjects;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Model1 extends Pieces {
    //Constructor
    public Model1(){
        super();
        shift(b(),v().x+1f,v().y,v().z);
        shift(c(), v().x+2f,v().y,v().z);
        shift(d(),v().x,v().y+1f,v().z);
    }

    //Rotation Methods
    @Override
    public void rotateX(boolean[][][] positions, boolean clock) {
        if(clock){
            if(z() == "oneC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if( valid(positions,v().x,v().y,v().z-1f) &&
                                valid(positions,v().x,v().y,v().z-2f) &&
                                valid(positions,v().x,v().y+1f,v().z) ) {
                            shift(b(),v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)){
                            shift(b(), v().x - 1f, v().y, v().z);
                            shift(c(), v().x - 2f, v().y, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(),v().x + 1f, v().y, v().z);
                            shift(c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("one");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if(valid(positions,v().x,v().y+1f,v().z-1f)&&
                                valid(positions,v().x,v().y+1f,v().z-2f)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x, v().y + 1f, v().z - 1f);
                            shift(c(), v().x, v().y + 1f, v().z - 2f);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x-1f,v().y+1f,v().z)&&
                                valid(positions,v().x-2f,v().y+1f,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x - 1f, v().y + 1f, v().z);
                            shift(c(), v().x - 2f, v().y + 1f, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y+1f,v().z)&&
                                valid(positions,v().x+2f,v().y+1f,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x + 1f, v().y + 1f, v().z);
                            shift(c(), v().x + 2f, v().y + 1f, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("one");
                        }
                    }
                }
            }
            else if (z() == "twoC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if( valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-2f)&&
                                valid(positions,v().x-1f,v().y,v().z)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x - 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-2f,v().y,v().z-1f)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x - 1f, v().y, v().z - 1f);
                            shift(d(), v().x - 2f, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift( b(), v().x + 1f, v().y, v().z);
                            shift( c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("one");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if(valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-1f,v().y,v().z-2f)&&
                                valid(positions,v().x-1f,v().y,v().z)) {
                            shift(b(), v().x - 1f, v().y, v().z - 1f);
                            shift(c(), v().x - 1f, v().y, v().z - 2f);
                            shift(d(), v().x - 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if( valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x - 1f, v().y, v().z);
                            shift(c(), v().x - 2f, v().y, v().z);
                            shift( d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z-1f)&&
                                valid(positions,v().x+2f,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x + 1f, v().y, v().z - 1f);
                            shift(c(), v().x + 2f, v().y, v().z - 1f);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("one");
                        }
                    }
                }
            }
            else if(z() == "threeC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if(valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-2f)&&
                                valid(positions,v().x+1f,v().y,v().z)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x + 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift( b(), v().x - 1f, v().y, v().z);
                            shift( c(), v().x - 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z+1f)) {
                            shift(b(), v().x + 1f, v().y, v().z);
                            shift(c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z + 1f);
                            setX("one");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if( valid(positions,v().x+1f,v().y,v().z-1f)&&
                                valid(positions,v().x+1f,v().y,v().z-2f)&&
                                valid(positions,v().x+1f,v().y,v().z)) {
                            shift( b(), v().x + 1f, v().y, v().z - 1f);
                            shift(c(), v().x + 1f, v().y, v().z - 2f);
                            shift(d(), v().x + 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if( valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-2f,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x - 1f, v().y, v().z - 1f);
                            shift( c(), v().x - 2f, v().y, v().z - 1f);
                            shift( d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if( valid(positions,v().x+1f,v().y,v().z+1f)&&
                                valid(positions,v().x+2f,v().y,v().z+1f)&&
                                valid(positions,v().x,v().y,v().z+1f)) {
                            shift(b(), v().x + 1f, v().y, v().z + 1f);
                            shift(c(), v().x + 2f, v().y, v().z + 1f);
                            shift(d(), v().x, v().y, v().z + 1f);
                            setX("one");
                        }
                    }
                }
            }
        }
        else{
            if(z() == "oneC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if(valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)){
                            shift(b(), v().x - 1f, v().y, v().z);
                            shift(c(), v().x - 2f, v().y, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("three");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(),v().x + 1f, v().y, v().z);
                            shift(c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("one");
                        }
                    }
                    else{
                        if( valid(positions,v().x,v().y,v().z-1f) &&
                                valid(positions,v().x,v().y,v().z-2f) &&
                                valid(positions,v().x,v().y+1f,v().z) ) {
                            shift(b(),v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("two");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if(valid(positions,v().x-1f,v().y+1f,v().z)&&
                                valid(positions,v().x-2f,v().y+1f,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x - 1f, v().y + 1f, v().z);
                            shift(c(), v().x - 2f, v().y + 1f, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("three");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x+1f,v().y+1f,v().z)&&
                                valid(positions,v().x+2f,v().y+1f,v().z)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x + 1f, v().y + 1f, v().z);
                            shift(c(), v().x + 2f, v().y + 1f, v().z);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("one");
                        }
                    }
                    else{
                        if(valid(positions,v().x,v().y+1f,v().z-1f)&&
                                valid(positions,v().x,v().y+1f,v().z-2f)&&
                                valid(positions,v().x,v().y+1f,v().z)) {
                            shift(b(), v().x, v().y + 1f, v().z - 1f);
                            shift(c(), v().x, v().y + 1f, v().z - 2f);
                            shift(d(), v().x, v().y + 1f, v().z);
                            setX("two");
                        }
                    }
                }
            }
            else if (z() == "twoC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if( valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-2f)&&
                                valid(positions,v().x-1f,v().y,v().z)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x - 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-2f,v().y,v().z-1f)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x - 1f, v().y, v().z - 1f);
                            shift(d(), v().x - 2f, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift( b(), v().x + 1f, v().y, v().z);
                            shift( c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("one");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if(valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-1f,v().y,v().z-2f)&&
                                valid(positions,v().x-1f,v().y,v().z)) {
                            shift(b(), v().x - 1f, v().y, v().z - 1f);
                            shift(c(), v().x - 1f, v().y, v().z - 2f);
                            shift(d(), v().x - 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                    else if(x() == "two"){
                        if( valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x - 1f, v().y, v().z);
                            shift(c(), v().x - 2f, v().y, v().z);
                            shift( d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else{
                        if(valid(positions,v().x+1f,v().y,v().z-1f)&&
                                valid(positions,v().x+2f,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x + 1f, v().y, v().z - 1f);
                            shift(c(), v().x + 2f, v().y, v().z - 1f);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("one");
                        }
                    }
                }
            }
            else if(z() == "threeC"){
                if(y() == "one"){
                    if(x() == "one"){
                        if(valid(positions,v().x-1f,v().y,v().z)&&
                                valid(positions,v().x-2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift( b(), v().x - 1f, v().y, v().z);
                            shift( c(), v().x - 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }
                    }
                    else if(x() == "two"){
                        if(valid(positions,v().x+1f,v().y,v().z)&&
                                valid(positions,v().x+2f,v().y,v().z)&&
                                valid(positions,v().x,v().y,v().z+1f)) {
                            shift(b(), v().x + 1f, v().y, v().z);
                            shift(c(), v().x + 2f, v().y, v().z);
                            shift(d(), v().x, v().y, v().z + 1f);
                            setX("one");
                        }
                    }
                    else{
                        if(valid(positions,v().x,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-2f)&&
                                valid(positions,v().x+1f,v().y,v().z)) {
                            shift(b(), v().x, v().y, v().z - 1f);
                            shift(c(), v().x, v().y, v().z - 2f);
                            shift(d(), v().x + 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                }
                else{
                    if(x() == "one"){
                        if( valid(positions,v().x-1f,v().y,v().z-1f)&&
                                valid(positions,v().x-2f,v().y,v().z-1f)&&
                                valid(positions,v().x,v().y,v().z-1f)) {
                            shift(b(), v().x - 1f, v().y, v().z - 1f);
                            shift( c(), v().x - 2f, v().y, v().z - 1f);
                            shift( d(), v().x, v().y, v().z - 1f);
                            setX("three");
                        }

                    }
                    else if(x() == "two"){
                        if( valid(positions,v().x+1f,v().y,v().z+1f)&&
                                valid(positions,v().x+2f,v().y,v().z+1f)&&
                                valid(positions,v().x,v().y,v().z+1f)) {
                            shift(b(), v().x + 1f, v().y, v().z + 1f);
                            shift(c(), v().x + 2f, v().y, v().z + 1f);
                            shift(d(), v().x, v().y, v().z + 1f);
                            setX("one");
                        }
                    }
                    else{
                        if( valid(positions,v().x+1f,v().y,v().z-1f)&&
                                valid(positions,v().x+1f,v().y,v().z-2f)&&
                                valid(positions,v().x+1f,v().y,v().z)) {
                            shift( b(), v().x + 1f, v().y, v().z - 1f);
                            shift(c(), v().x + 1f, v().y, v().z - 2f);
                            shift(d(), v().x + 1f, v().y, v().z);
                            setX("two");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void rotateY(boolean[][][] positions, boolean clock) {
        if(y() == "one"){
            if(x() == "one"){
                if(valid(positions,v().x+1f,v().y+1f,v().z)&&
                        valid(positions,v().x+2f,v().y+1f,v().z)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(),v().x + 1f, v().y+1f, v().z);
                    shift(c(), v().x + 2f, v().y+1f, v().z);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("two");
                }
            }
            else if(x() == "two"){
                if(valid(positions,v().x,v().y+1f,v().z-1f)&&
                        valid(positions,v().x,v().y+1f,v().z-2f)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(), v().x, v().y + 1f, v().z - 1f);
                    shift(c(), v().x, v().y + 1f, v().z - 2f);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("two");
                }
            }
            else{
                if(valid(positions,v().x-1f,v().y+1f,v().z)&&
                        valid(positions,v().x-2f,v().y+1f,v().z)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(), v().x-1f, v().y + 1f, v().z);
                    shift(c(), v().x-2f, v().y + 1f, v().z);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("two");
                }
            }
        }
        else{
            if(x() == "one"){
                if(valid(positions,v().x+1f,v().y,v().z)&&
                        valid(positions,v().x+2f,v().y,v().z)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(),v().x + 1f, v().y, v().z);
                    shift(c(), v().x + 2f, v().y, v().z);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("one");
                }
            }
            else if(x() == "two"){
                if(valid(positions,v().x,v().y,v().z-1f)&&
                        valid(positions,v().x,v().y,v().z-2f)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(), v().x, v().y, v().z - 1f);
                    shift(c(), v().x, v().y, v().z - 2f);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("one");
                }
            }
            else{
                if(valid(positions,v().x-1f,v().y,v().z)&&
                        valid(positions,v().x-2f,v().y,v().z)&&
                        valid(positions,v().x,v().y+1f,v().z)) {
                    shift(b(), v().x-1f, v().y, v().z);
                    shift(c(), v().x-2f, v().y, v().z);
                    shift(d(), v().x, v().y + 1f, v().z);
                    setY("one");
                }
            }
        }
    }

    @Override
    public void rotateZ(boolean[][][] positions, boolean clock) {
        if (clock) {
            if (z() == "oneC") {
                if(x() == "one"){
                    if(valid(positions,v().x+1f,v().y,v().z)&&
                            valid(positions,v().x+2f,v().y,v().z)&&
                            valid(positions,v().x,v().y,v().z-1f)) {
                        shift( b(), v().x + 1f, v().y, v().z);
                        shift( c(), v().x + 2f, v().y, v().z);
                        shift(d(), v().x, v().y, v().z - 1f);
                        setZ("twoC");
                    }
                }
                else if(x() == "two"){
                    if( valid(positions,v().x,v().y,v().z-1f)&&
                            valid(positions,v().x,v().y,v().z-2f)&&
                            valid(positions,v().x-1f,v().y,v().z)) {
                        shift(b(), v().x, v().y, v().z - 1f);
                        shift(c(), v().x, v().y, v().z - 2f);
                        shift(d(), v().x - 1f, v().y, v().z);
                        setZ("twoC");
                    }
                }
                else{
                    if(valid(positions,v().x,v().y,v().z-1f)&&
                            valid(positions,v().x-1f,v().y,v().z-1f)&&
                            valid(positions,v().x-2f,v().y,v().z-1f)) {
                        shift(b(), v().x, v().y, v().z - 1f);
                        shift(c(), v().x - 1f, v().y, v().z - 1f);
                        shift(d(), v().x - 2f, v().y, v().z - 1f);
                        setZ("twoC");
                    }
                }

            } else if (z() == "twoC") {

            } else if (z() == "threeC") {
                if(x() == "one"){
                    if(valid(positions,v().x+1f,v().y,v().z)&&
                            valid(positions,v().x+2f,v().y,v().z)&&
                            valid(positions,v().x,v().y+1f,v().z)) {
                        shift(b(),v().x + 1f, v().y, v().z);
                        shift(c(), v().x + 2f, v().y, v().z);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setZ("oneC");
                    }
                }
                else if(x() == "two"){
                    if( valid(positions,v().x,v().y,v().z-1f) &&
                            valid(positions,v().x,v().y,v().z-2f) &&
                            valid(positions,v().x,v().y+1f,v().z) ) {
                        shift(b(),v().x, v().y, v().z - 1f);
                        shift(c(), v().x, v().y, v().z - 2f);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setZ("oneC");
                    }
                }
                else{
                    if(valid(positions,v().x-1f,v().y,v().z)&&
                            valid(positions,v().x-2f,v().y,v().z)&&
                            valid(positions,v().x,v().y+1f,v().z)){
                        shift(b(), v().x - 1f, v().y, v().z);
                        shift(c(), v().x - 2f, v().y, v().z);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setZ("oneC");
                    }
                }

            }
        } else {
            if (z() == "oneC") {
                if(x() == "one"){
                    if(valid(positions,v().x+1f,v().y,v().z)&&
                            valid(positions,v().x+2f,v().y,v().z)&&
                            valid(positions,v().x,v().y,v().z+1f)) {
                        shift(b(), v().x + 1f, v().y, v().z);
                        shift(c(), v().x + 2f, v().y, v().z);
                        shift(d(), v().x, v().y, v().z + 1f);
                        setZ("threeC");
                    }
                }

                else if(x() == "two"){
                    if(valid(positions,v().x,v().y,v().z-1f)&&
                            valid(positions,v().x,v().y,v().z-2f)&&
                            valid(positions,v().x+1f,v().y,v().z)) {
                        shift(b(), v().x, v().y, v().z - 1f);
                        shift(c(), v().x, v().y, v().z - 2f);
                        shift(d(), v().x + 1f, v().y, v().z);
                        setZ("threeC");
                    }
                }
                else{
                    if(valid(positions,v().x-1f,v().y,v().z)&&
                            valid(positions,v().x-2f,v().y,v().z)&&
                            valid(positions,v().x,v().y,v().z-1f)) {
                        shift( b(), v().x - 1f, v().y, v().z);
                        shift( c(), v().x - 2f, v().y, v().z);
                        shift(d(), v().x, v().y, v().z - 1f);
                        setZ("threeC");
                    }
                }
            } else if (z() == "twoC") {
                if(x() == "one"){
                    if(valid(positions,v().x+1f,v().y,v().z)&&
                            valid(positions,v().x+2f,v().y,v().z)&&
                            valid(positions,v().x,v().y+1f,v().z)) {
                        shift(b(),v().x + 1f, v().y, v().z);
                        shift(c(), v().x + 2f, v().y, v().z);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setX("oneC");
                    }
                }
                else if(x() == "two"){
                    if( valid(positions,v().x,v().y,v().z-1f) &&
                            valid(positions,v().x,v().y,v().z-2f) &&
                            valid(positions,v().x,v().y+1f,v().z) ) {
                        shift(b(),v().x, v().y, v().z - 1f);
                        shift(c(), v().x, v().y, v().z - 2f);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setX("oneC");
                    }
                }
                else{
                    if(valid(positions,v().x-1f,v().y,v().z)&&
                            valid(positions,v().x-2f,v().y,v().z)&&
                            valid(positions,v().x,v().y+1f,v().z)){
                        shift(b(), v().x - 1f, v().y, v().z);
                        shift(c(), v().x - 2f, v().y, v().z);
                        shift(d(), v().x, v().y + 1f, v().z);
                        setZ("oneC");
                    }
                }


            } else if (z() == "threeC") {

            }
        }
    }
}
