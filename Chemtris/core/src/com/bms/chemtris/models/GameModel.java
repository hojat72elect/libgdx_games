package com.bms.chemtris.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bms.chemtris.render.WireframeShader;

/**
 * Class to control all game models.
 */
public abstract class GameModel {

    public Array<ModelInstance> parts; //model parts
    public Array<ModelInstance>shadow; //model shadow
    public Array<ModelInstance>frame; //model frame

    //model builder to make the model
    private static final ModelBuilder builder = new ModelBuilder();

    //3d models to make up the parts
    public static final Model white = builder.createBox(
            1f,1f,1f,
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
    );

    public static final Model gray = builder.createBox(
            1f,1f,1f,
            new Material(ColorAttribute.createDiffuse(105/255f,105/255f,105/255f,1f)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
    );


    public static final Model wire = builder.createBox(
            1.01f,1.01f,1.01f,
            new Material(ColorAttribute.createDiffuse(184/255f,182/255f,182/255f,1f)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
    );

    private static Environment environment; //rendering environment

    //ModelBatch Renderer and Wireframe shader
    private static final ModelBatch fillRenderer = new ModelBatch();
    private static final ModelBatch wireRenderer = new ModelBatch(new DefaultShaderProvider() {
        @Override
        protected Shader createShader(Renderable renderable) {
            return new WireframeShader(renderable, config);
        }
    });

    //collision array to check
    private static boolean [][][] matrix;

    //perspective camera
    private PerspectiveCamera camera;

    //constructor
    public GameModel(PerspectiveCamera cam, boolean [][][] collision){
        //setup the model instances
        parts = new Array<ModelInstance>();
        frame = new Array<ModelInstance>();
        shadow = new Array<ModelInstance>();

        //setup environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        //setup collision matrix
        matrix = collision;

        //setup camera
        camera = cam;



    }

    //method to check if models can move to specific location
    public boolean canMove(int xOffset, int yOffset, int zOffset){

        try {

            for (ModelInstance part : parts) {
                Vector3 v = new Vector3();
                part.transform.getTranslation(v);

                int x = Math.round(v.x);
                int z = Math.round(v.z);
                int y = Math.round(v.y);

                if(x+xOffset < 0){
                    return false;
                }
                if(z + zOffset < 0){
                    return false;
                }
                if(x+xOffset > matrix.length-1){
                    return false;
                }
                if(z+zOffset > matrix[0][0].length-1){
                    return false;
                }
                if (y == 1) {
                    return false;
                } else if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
                    return false;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;

    }

    public void descend(){
        if (canMove(0,-1,0)){
            for(int i = 0; i < parts.size; ++i){
                parts.get(i).transform.translate(0,-1f,0);
                frame.get(i).transform.translate(0,-1f,0);
            }
        }
    }

    public boolean canMovePart(int xOffset, int yOffset, int zOffset){
        Vector3 v = new Vector3();
        parts.get(0).transform.getTranslation(v);

        int x = Math.round(v.x);
        int z = Math.round(v.z);
        int y = Math.round(v.y);

        if(x+xOffset < 0){
            return false;
        }
        if(z + zOffset < 0){
            return false;
        }
        if(x+xOffset > matrix.length-1){
            return false;
        }
        if(z+zOffset > matrix[0][0].length-1){
            return false;
        }
        if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
            return false;
        }


        return true;
    }

    public void movePart(int part, float x, float y, float z){
        parts.get(part).transform.set(parts.get(0).transform);
        parts.get(part).transform.translate(x,y,z);
    }

    public boolean canMovePartW (int part, int xOffset, int yOffset, int zOffset){
        Vector3 v = new Vector3();
        parts.get(part).transform.getTranslation(v);

        int x = Math.round(v.x);
        int z = Math.round(v.z);
        int y = Math.round(v.y);

        if(x+xOffset < 0){
            return false;
        }
        if(z + zOffset < 0){
            return false;
        }
        if(x+xOffset > matrix.length-1){
            return false;
        }
        if(z+zOffset > matrix[0][0].length-1){
            return false;
        }
        if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
            return false;
        }

        return true;
    }

    public void movePartW(int part, float x, float y, float z){
        parts.get(part).transform.translate(x,y,z);
    }

    public boolean moveShadow(int xOffset, int yOffset, int zOffset){
        try {

            for (ModelInstance part : shadow) {
                Vector3 v = new Vector3();
                part.transform.getTranslation(v);

                int x = Math.round(v.x);
                int z = Math.round(v.z);
                int y = Math.round(v.y);

                if(x+xOffset < 0){
                    return false;
                }
                if(z + zOffset < 0){
                    return false;
                }
                if(x+xOffset > matrix.length-1){
                    return false;
                }
                if(z+zOffset > matrix[0][0].length-1){
                    return false;
                }
                if (y == 1) {
                    return false;
                } else if (matrix[x + xOffset][y + yOffset][z + zOffset]) {
                    return false;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    //method to render the parts
    public void renderParts(){
        //render parts
        fillRenderer.begin(camera);
        fillRenderer.render(parts,environment);
        fillRenderer.end();
    }

    //method to render the shadow
    public void renderShadow(){
        //render shadow
        fillRenderer.begin(camera);
        fillRenderer.render(shadow,environment);
        fillRenderer.end();

    }

    //method to render wireframe
    public void renderWireframe(){
        //render wireframe shapes
        wireRenderer.begin(camera);
        wireRenderer.render(frame,environment);
        wireRenderer.end();
    }



    //method to move right
    public void moveRight(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(1f,0f,0f);
                    shadow.get(i).transform.translate(1f,0f,0f);
                    frame.get(i).transform.translate(1f,0f,0f);
                }
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(1f,0f,0f);
                    shadow.get(i).transform.translate(1f,0f,0f);
                    frame.get(i).transform.translate(1f,0f,0f);
                }
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(0,0,1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,1f);
                    shadow.get(i).transform.translate(0f,0f,1f);
                    frame.get(i).transform.translate(0f,0f,1f);
                }
            }
        }
        //right
        else{
            if(canMove(0,0,-1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,-1f);
                    shadow.get(i).transform.translate(0f,0f,-1f);
                    frame.get(i).transform.translate(0f,0f,-1f);
                }
            }
        }
    }

    //method to move left
    public void moveLeft(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(-1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(-1f,0f,0f);
                    shadow.get(i).transform.translate(-1f,0f,0f);
                    frame.get(i).transform.translate(-1f,0f,0f);
                }
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(-1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(-1f,0f,0f);
                    shadow.get(i).transform.translate(-1f,0f,0f);
                    frame.get(i).transform.translate(-1f,0f,0f);
                }
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(0,0,-1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,-1f);
                    shadow.get(i).transform.translate(0f,0f,-1f);
                    frame.get(i).transform.translate(0f,0f,-1f);
                }
            }
        }
        //right
        else{
            if(canMove(0,0,1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,1f);
                    shadow.get(i).transform.translate(0f,0f,1f);
                    frame.get(i).transform.translate(0f,0f,1f);
                }
            }
        }
    }

    //method to move up
    public void moveUp(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(0,0,-1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,-1f);
                    shadow.get(i).transform.translate(0f,0f,-1f);
                    frame.get(i).transform.translate(0f,0f,-1f);
                }
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(0,0,1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,1f);
                    shadow.get(i).transform.translate(0f,0f,1f);
                    frame.get(i).transform.translate(0f,0f,1f);
                }
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(1f,0f,0f);
                    shadow.get(i).transform.translate(1f,0f,0f);
                    frame.get(i).transform.translate(1f,0f,0f);
                }
            }
        }
        //right
        else{
            if(canMove(-1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(-1f,0f,0f);
                    shadow.get(i).transform.translate(-1f,0f,0f);
                    frame.get(i).transform.translate(-1f,0f,0f);
                }
            }
        }
    }

    //method to move down
    public void moveDown(){
        //front
        if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z > 0){
            if(canMove(0,0,1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,1f);
                    shadow.get(i).transform.translate(0f,0f,1f);
                    frame.get(i).transform.translate(0f,0f,1f);
                }
            }
        }
        //back
        else if(camera.position.x >= 0 && camera.position.x < 15 && camera.position.z < 0){
            if(canMove(0,0,-1)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(0f,0f,-1f);
                    shadow.get(i).transform.translate(0f,0f,-1f);
                    frame.get(i).transform.translate(0f,0f,-1f);
                }
            }
        }
        //left
        else if(camera.position.x < 0){
            if(canMove(-1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(-1f,0f,0f);
                    shadow.get(i).transform.translate(-1f,0f,0f);
                    frame.get(i).transform.translate(-1f,0f,0f);
                }
            }
        }
        //right
        else{
            if(canMove(1,0,0)){
                for(int i = 0; i < parts.size; ++i){
                    parts.get(i).transform.translate(1f,0f,0f);
                    shadow.get(i).transform.translate(1f,0f,0f);
                    frame.get(i).transform.translate(1f,0f,0f);
                }
            }
        }
    }

    //method to trace shadow
    public void traceShadow(){
        for(int i = 0; i < parts.size; ++i){
            shadow.get(i).transform.set(parts.get(i).transform);
            frame.get(i).transform.set(parts.get(i).transform);
        }

        while (moveShadow(0,-1,0)){
            for(int i = 0; i < shadow.size; ++i){
                shadow.get(i).transform.translate(0f,-1f,0f);
            }
        }

        while (moveShadow(0,-1,0)){
            for(int i = 0; i < shadow.size; ++i){
                shadow.get(i).transform.translate(0f,-1f,0f);
            }
        }

        while (moveShadow(0,-1,0)){
            for(int i = 0; i < shadow.size; ++i){
                shadow.get(i).transform.translate(0f,-1f,0f);
            }
        }


    }

    //method to rotate in x direction
    public abstract void rotateX();

    //method to rotate in y direction
    public abstract void rotateY();

    //method to rotate in z direction
    public abstract void rotateZ();

}
