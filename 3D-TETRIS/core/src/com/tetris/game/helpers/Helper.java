package com.tetris.game.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tetris.game.gameobjects.*;

import java.util.Random;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Helper {
    //Members
    Array<ModelInstance> l1,l2,l3,l4,l5,l6;
    boolean over = false;
    int score, counter;

    //Constructor
    public Helper(){
        l1 = new Array<ModelInstance>();
        l2 = new Array<ModelInstance>();
        l3 = new Array<ModelInstance>();
        l4 = new Array<ModelInstance>();
        l5 = new Array<ModelInstance>();
        l6 = new Array<ModelInstance>();
        score = 0;
        counter = 0;
    }

    //Adding Instance Method
    public void addInstance(ModelInstance instance){
        Vector3 temp = new Vector3();
        instance.transform.getTranslation(temp);
        int y = Math.round(temp.y);
        if(y == 1){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLUE));
            l1.add(instance);
        }
        else if(y == 2){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.CYAN));
            l2.add(instance);
        }
        else if(y == 3){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));
            l3.add(instance);
        }
        else if(y == 4){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.YELLOW));
            l4.add(instance);
        }
        else if(y == 5){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.ORANGE));
            l5.add(instance);
        }
        else if(y  == 6){
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
            l6.add(instance);
        }
        else{
            over =  true;
        }
    }

    //Clearing blocks method
    public boolean clearBlocks(boolean[][][]fixed){
        if(l1.size == 100){
            swap(l1,l2,Color.BLUE,fixed);
            swap(l2,l3,Color.CYAN,fixed);
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l2.size == 100){
            swap(l2,l3,Color.CYAN,fixed);
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l3.size == 100){
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l4.size == 100){
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l5.size ==100){
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l6.size == 100){
            for(int i = 0; i < l6.size; i++){
                Vector3 t = new Vector3();
                l6.get(i).transform.getTranslation(t);
                int x = Math.round(t.x);
                int y = Math.round(t.y);
                int z = Math.round(t.z);
                fixed[x][y][z] = false;
            }
            l6.clear();
            counter++;
            return true;
        }

        return false;
    }

    public boolean clearBlocksEasy(boolean[][][]fixed){
        if(l1.size == 64){
            swap(l1,l2,Color.BLUE,fixed);
            swap(l2,l3,Color.CYAN,fixed);
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l2.size == 64){
            swap(l2,l3,Color.CYAN,fixed);
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l3.size == 64){
            swap(l3,l4,Color.GREEN,fixed);
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l4.size == 64){
            swap(l4,l5,Color.YELLOW,fixed);
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l5.size ==64){
            swap(l5,l6,Color.ORANGE,fixed);
            counter++;
            return true;
        }
        if(l6.size == 64){
            for(int i = 0; i < l6.size; i++){
                Vector3 t = new Vector3();
                l6.get(i).transform.getTranslation(t);
                int x = Math.round(t.x);
                int y = Math.round(t.y);
                int z = Math.round(t.z);
                fixed[x][y][z] = false;
            }
            l6.clear();
            counter++;
            return true;
        }

        return false;
    }

    //Calculating score method
    public int getScore(){
        if(counter == 1){
            score++;
            counter = 0;
        }
        else if(counter == 2){
            score+=1;
            score*=2;
            counter = 0;
        }
        else if(counter == 3){
            score+=2;
            score*=3;
            counter = 0;
        }
        else if(counter == 4){
            score+=3;
            score*=4;
        }
        else if(counter > 4){
            score+=4;
            score*=8;

        }
        counter = 0;
        return score;
    }

    //Spawning random pieces
    public Pieces spawnPiece(){
        Random random = new Random();
        int num = random.nextInt(6);

        if(num == 0){
            return new Model1();
        }
        else if(num == 1){
            return new Model2();
        }
        else if(num == 2){
            return new Model3();
        }
        else if(num == 3){
            return new Model4();
        }
        else if(num == 4) {
            return new Model5();
        }
        return new Model6();
    }

    public PiecesEasy spawnPieceEasy(){
        Random random = new Random();
        int num = random.nextInt(6);
        if(num == 0){
            return new Model1Easy();
        }
        else if(num == 1){
            return new Model2Easy();
        }
        else if(num == 2){
            return new Model3Easy();
        }
        else if(num == 3){
            return new Model4Easy();
        }
        else if(num == 4) {
            return new Model5Easy();
        }
        return new Model6Easy();
    }

    public  boolean gameOver(){
        return over;
    }

    //Rendering blocks method
    public void render(ModelBatch batch, Environment environment){
        batch.render(l1,environment);
        batch.render(l2,environment);
        batch.render(l3,environment);
        batch.render(l4,environment);
        batch.render(l5,environment);
        batch.render(l6,environment);
    }

    //Swap rows when clearing method
    public void swap(Array<ModelInstance>first,Array<ModelInstance>second, Color color, boolean[][][] fixed){
        for(int i = 0; i < first.size; i++){
            Vector3 t  = new Vector3();
            first.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
            fixed[x][y][z] = false;
        }
        first.clear();
        for(int i = 0; i < second.size; i++){
            Vector3 t  = new Vector3();
            second.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
            fixed[x][y][z] = false;
            second.get(i).transform.translate(0,-1f,0);
            second.get(i).materials.get(0).set(ColorAttribute.createDiffuse(color));
            first.add(second.get(i));
            fixed[x][y-1][z]= true;
        }
        second.clear();

    }
}

