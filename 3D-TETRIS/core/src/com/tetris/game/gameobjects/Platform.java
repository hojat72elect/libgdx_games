package com.tetris.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Platform {
    Model black,magenta;
    ModelBuilder mb;
    Array<ModelInstance> instances;

    //Constructor
    public Platform() {
        mb = new ModelBuilder();
        black = mb.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.BLACK)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        magenta = mb.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.MAGENTA)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        instances = new Array<ModelInstance>();
    }

    //large platform
    public void makeLargePlatform(){
        for(float x = 8f; x < 18f; x+=2f){
            for(float z = 18f; z > 8f; z-=2f) {
                ModelInstance wi = new ModelInstance(black);
                wi.transform.setToTranslation(x,0,z);
                instances.add(wi);
            }

        }

        for(float x = 8f; x < 18f; x+=2f){
            for(float z = 17f; z > 7f; z-=2f) {
                ModelInstance bi = new ModelInstance(magenta);
                bi.transform.setToTranslation(x,0,z);
                instances.add(bi);
            }

        }

        for(float x = 9f; x < 19f; x+=2f){
            for(float z = 18f; z > 8f; z-=2f) {
                ModelInstance bi = new ModelInstance(magenta);
                bi.transform.setToTranslation(x,0,z);
                instances.add(bi);
            }
        }

        for(float x = 9f; x < 19f; x+=2f){
            for(float z = 17f; z > 7f; z-=2f) {
                ModelInstance wi = new ModelInstance(black);
                wi.transform.setToTranslation(x,0,z);
                instances.add(wi);
            }
        }

    }

    //small platform
    public void makeSmallPlatform(){
        for(float x = 8f; x < 16f; x+=2f){
            for(float z = 16f; z > 8f; z-=2f) {
                ModelInstance wi = new ModelInstance(black);
                wi.transform.setToTranslation(x,0,z);
                instances.add(wi);
            }

        }

        for(float x = 8f; x < 16f; x+=2f){
            for(float z = 15f; z > 7f; z-=2f) {
                ModelInstance bi = new ModelInstance(magenta);
                bi.transform.setToTranslation(x,0,z);
                instances.add(bi);
            }

        }

        for(float x = 9f; x < 17f; x+=2f){
            for(float z = 16f; z > 8f; z-=2f) {
                ModelInstance bi = new ModelInstance(magenta);
                bi.transform.setToTranslation(x,0,z);
                instances.add(bi);
            }
        }

        for(float x = 9f; x < 17f; x+=2f){
            for(float z = 15f; z > 7f; z-=2f) {
                ModelInstance wi = new ModelInstance(black);
                wi.transform.setToTranslation(x,0,z);
                instances.add(wi);
            }
        }

    }

    //Instance Array Getter
    public Array<ModelInstance> create(){
        return instances;
    }

    //Dispose Method
    public void dispose(){
        magenta.dispose();
        black.dispose();
    }


}

