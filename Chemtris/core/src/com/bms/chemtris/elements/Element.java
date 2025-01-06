package com.bms.chemtris.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.bms.chemtris.render.WireframeShader;

/**
 * Class to make an element.
 */
public class Element {

    //collision matrix
    private boolean [][][] matrix;
    //model instances
    public ModelInstance atom, shadow, frame;
    //number of valence electrons
    private int valence;
    //perspective camera
    private PerspectiveCamera camera;

    private static Environment environment; //rendering environment

    //ModelBatch Renderer and Wireframe shader
    private static final ModelBatch fillRenderer = new ModelBatch();
    private static final ModelBatch wireRenderer = new ModelBatch(new DefaultShaderProvider() {
        @Override
        protected Shader createShader(Renderable renderable) {
            return new WireframeShader(renderable, config);
        }
    });

    //constructor
    public Element(int ve, boolean[][][] mtx, PerspectiveCamera cam){
        valence = ve;
        matrix = mtx;
        camera = cam;

        //setup environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

    }

    //method to get valence electron
    public int getValence(){
        return valence;
    }

    //method to check if the element is stable
    public boolean isStable(){
        return valence == 0;
    }

    //method to reduce free valence electron
    public void reduceValence(){
        --valence;
    }

    //method to fill board
    public void fillBoard(){
        //get position
        Vector3 v = new Vector3();
        atom.transform.getTranslation(v);
        //go through nodes
        for(int i = 0; i < atom.nodes.size; ++i){
            int x = Math.round(atom.nodes.get(i).translation.x+v.x);
            int y = Math.round(atom.nodes.get(i).translation.y+v.y);
            int z = Math.round(atom.nodes.get(i).translation.z+v.z);
            //set matrix
            matrix[x][y][z] = true;
        }
    }

    public void unfillBoard(){
        //get position
        Vector3 v = new Vector3();
        atom.transform.getTranslation(v);
        //go through nodes
        for(int i = 0; i < atom.nodes.size; ++i){
            int x = Math.round(atom.nodes.get(i).translation.x+v.x);
            int y = Math.round(atom.nodes.get(i).translation.y+v.y);
            int z = Math.round(atom.nodes.get(i).translation.z+v.z);
            //set matrix
            matrix[x][y][z] = false;
        }
    }

    public boolean canMove(int xOffset, int yOffset, int zOffset){
        //get position
        Vector3 v = new Vector3();
        atom.transform.getTranslation(v);
        try {
            //go through nodes
            for (int i = 0; i < atom.nodes.size; ++i) {
                int x = Math.round(atom.nodes.get(i).translation.x + v.x);
                int y = Math.round(atom.nodes.get(i).translation.y + v.y);
                int z = Math.round(atom.nodes.get(i).translation.z + v.z);

                if (x + xOffset < 0) {
                    return false;
                }
                if (z + zOffset < 0) {
                    return false;
                }
                if (x + xOffset > matrix.length - 1) {
                    return false;
                }
                if (z + zOffset > matrix[0][0].length - 1) {
                    return false;
                }

                if (y == 1) {
                    return false;
                } else if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
                    return false;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean canMoveShadow(int xOffset, int yOffset, int zOffset){
        //get position
        Vector3 v = new Vector3();
        shadow.transform.getTranslation(v);
        try {
            //go through nodes
            for (int i = 0; i < atom.nodes.size; ++i) {
                int x = Math.round(atom.nodes.get(i).translation.x + v.x);
                int y = Math.round(atom.nodes.get(i).translation.y + v.y);
                int z = Math.round(atom.nodes.get(i).translation.z + v.z);

                if (x + xOffset < 0) {
                    return false;
                }
                if (z + zOffset < 0) {
                    return false;
                }
                if (x + xOffset > matrix.length - 1) {
                    return false;
                }
                if (z + zOffset > matrix[0][0].length - 1) {
                    return false;
                }

                if (y == 1) {
                    return false;
                } else if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void moveUp(){
        //get position
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(0,0,-1)){
                atom.transform.trn(0f,0f,-1f);
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(0,0,1)){
                atom.transform.trn(0f,0f,1f);
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(1,0,0)){
                atom.transform.trn(1f,0f,0f);
            }
        }
        //right
        else{
            if(canMove(-1,0,0)){
                atom.transform.trn(-1f,0f,0f);
            }
        }
    }

    //method to move right
    public void moveRight(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(1,0,0)){
                atom.transform.trn(1f,0f,0f);
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(1,0,0)){
                atom.transform.trn(1f,0f,0f);
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(0,0,1)){
                atom.transform.trn(0f,0f,1f);
            }
        }
        //right
        else{
            if(canMove(0,0,-1)){
                atom.transform.trn(0f,0f,-1f);
            }
        }
    }

    //method to move left
    public void moveLeft(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(-1,0,0)){
                atom.transform.trn(-1f,0f,0f);
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(-1,0,0)){
                atom.transform.trn(-1f,0f,0f);
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(0,0,-1)){
                atom.transform.trn(0f,0f,-1f);
            }
        }
        //right
        else{
            if(canMove(0,0,1)){
                atom.transform.trn(0f,0f,1f);
            }
        }
    }



    //method to move down
    public void moveDown(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(0,0,1)){
                atom.transform.trn(0f,0f,1f);
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(0,0,-1)){
                atom.transform.trn(0f,0f,-1f);
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(-1,0,0)){
                atom.transform.trn(-1f,0f,0f);
            }
        }
        //right
        else{
            if(canMove(1,0,0)){
                atom.transform.trn(1f,0f,0f);
            }
        }
    }

    public void rotateX(){
        for (int i = 0; i < atom.nodes.size; ++i) {
            atom.nodes.get(i).translation.rotate(Vector3.Y, 90f);
            frame.nodes.get(i).translation.rotate(Vector3.Y,90f);
            shadow.nodes.get(i).translation.rotate(Vector3.Y,90f);
        }

        atom.calculateTransforms();
        frame.calculateTransforms();
        shadow.calculateTransforms();
    }

    public void rotateY(){
        for (int i = 0; i < atom.nodes.size; ++i) {
            atom.nodes.get(i).translation.rotate(Vector3.Z, 90f);
            frame.nodes.get(i).translation.rotate(Vector3.Z,90f);
            shadow.nodes.get(i).translation.rotate(Vector3.Z,90f);
        }

        atom.calculateTransforms();
        frame.calculateTransforms();
        shadow.calculateTransforms();
    }

    public void rotateZ(){
        for (int i = 0; i < atom.nodes.size; ++i) {
            atom.nodes.get(i).translation.rotate(Vector3.X, 90f);
            shadow.nodes.get(i).translation.rotate(Vector3.X, 90f);
            frame.nodes.get(i).translation.rotate(Vector3.X, 90f);
        }

        atom.calculateTransforms();
        frame.calculateTransforms();
        shadow.calculateTransforms();
    }

    public void traceShadow(){
        atom.calculateTransforms();
        shadow.transform.set(atom.transform);
        for (int i = 0; i < atom.nodes.size; ++i) {
            shadow.nodes.get(i).translation.set(atom.nodes.get(i).translation);
        }

        while (canMoveShadow(0,-1,0)){
            shadow.transform.trn(0f,-1f,0f);
        }
    }

    public void traceFrame(){
        atom.calculateTransforms();
        frame.transform.set(atom.transform);
        for (int i = 0; i < atom.nodes.size; ++i) {
            frame.nodes.get(i).translation.set(atom.nodes.get(i).translation);
        }
    }

    //method to render the parts
    public void renderParts(){
        //render parts
        fillRenderer.begin(camera);
        fillRenderer.render(atom,environment);
        fillRenderer.end();

        traceFrame();
        wireRenderer.begin(camera);
        wireRenderer.render(frame,environment);
        wireRenderer.end();
    }

    //method to render shadow
    public void renderShadow(){
        traceShadow();
        //render shadow
        fillRenderer.begin(camera);
        fillRenderer.render(shadow,environment);
        fillRenderer.end();
    }

    //method to make the model descend
    public void descend(){
        while (canMove(0,-1,0)){
            atom.transform.trn(0f,-1f,0f);
        }

        fillBoard();
    }

}
