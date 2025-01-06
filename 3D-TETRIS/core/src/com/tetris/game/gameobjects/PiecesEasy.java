package com.tetris.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by mkabi_000 on 8/10/2015.
 */
public abstract class PiecesEasy {
    //Members
    private Model model;
    private ModelInstance a,b,c,d, e, f, g, h;
    private Vector3 base;
    private ModelBuilder mb;
    private Array<ModelInstance> parts;
    private String x = "one",y="one",z="oneC";
    private Model shadowModel;

    //Constructor
    public PiecesEasy(){
        mb = new ModelBuilder();
        model = mb.createBox(1f,1f,1f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        shadowModel = mb.createBox(1f,1f,1f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);


        a = new ModelInstance(model);
        b = new ModelInstance(model);
        c = new ModelInstance(model);
        d = new ModelInstance(model);

        e = new ModelInstance(shadowModel);
        f = new ModelInstance(shadowModel);
        g = new ModelInstance(shadowModel);
        h = new ModelInstance(shadowModel);

        parts = new Array<ModelInstance>();
        parts.add(a);
        parts.add(b);
        parts.add(c);
        parts.add(d);


        a.transform.setToTranslation(11f, 0f, 12f);
        a.transform.translate(0f,8f,0f);
        base = new Vector3();
    }

    //Orientation getters and setters
    public String x(){return x;}
    public String y(){return y;}
    public String z(){return z;}
    public void setX(String p){x = p;}
    public void setY(String p){y = p;}
    public void setZ(String p){z = p;}

    //Base Position getters and setters
    public Vector3 v(){
        a.transform.getTranslation(base);
        return base;
    }

    //Model and instance getters
    public Model getModel(){
        return model;
    }
    public Model getShadowModel(){
        return shadowModel;
    }
    public ModelInstance a(){return a;}
    public ModelInstance b(){return b;}
    public ModelInstance c(){return c;}
    public ModelInstance d(){return d;}

    //Instance adder method
    public void addInstance(ModelInstance instance){
        parts.add(instance);
    }

    //Instance Array Getter
    public Array<ModelInstance> create(){return parts;}

    //Transformation Method
    public void shift(ModelInstance m, float x, float y, float z){
        m.transform.setToTranslation(x,y,z);
    }

    //Validation Methods
    public boolean valid(boolean[][][]positions, float x, float y, float z){
        int xp = Math.round(x);
        int yp = Math.round(y);
        int zp = Math.round(z);
        if(xp <8 || xp > 17 || yp < 0 || zp > 18 || zp < 9){
            return false;
        }
        if(positions[xp][yp][zp]){
            return false;
        }
        return true;
    }


    public boolean linearCheck(boolean[][][]positions, String direction){
        for(int i = 0; i < parts.size; i++){
            Vector3 temp = new Vector3();
            parts.get(i).transform.getTranslation(temp);
            int x = Math.round(temp.x);
            int y = Math.round(temp.y);
            int z = Math.round(temp.z);

            if(temp.x <8 || temp.x > 15 || temp.y < 0 || temp.z > 16 || temp.z < 9){
                return false;
            }

            if(direction == "right" &&(x == 15 || positions[x+1][y][z])){
                return false;
            }
            else if(direction == "left" ){
                if(x == 8){
                    return false;
                }
                if(positions[x-1][y][z]){
                    return false;
                }
            }
            else if(direction == "up"&&(z == 9 || positions[x][y][z-1])){
                return false;
            }
            else if(direction == "down"&&(z == 16 || positions[x][y][z+1]) ){
                return false;
            }
            else if(direction == "des" && (positions[x][y-1][z])){
                return false;
            }
        }
        return true;
    }

    //Movement methods
    public void left(boolean[][][]positions){
        boolean canMove = linearCheck(positions,"left");
        if(canMove){
            for(int i =0; i < parts.size; i++){
                parts.get(i).transform.translate(-1f,0,0);
            }
        }
    }

    public void right(boolean[][][]positions){
        boolean canMove = linearCheck(positions,"right");
        if(canMove){
            for(int i =0; i < parts.size; i++){
                parts.get(i).transform.translate(1f,0,0);
            }
        }
    }

    public void up(boolean[][][]positions){
        boolean canMove = linearCheck(positions,"up");
        if(canMove){
            for(int i =0; i < parts.size; i++){
                parts.get(i).transform.translate(0,0,-1f);
            }
        }
    }

    public void down(boolean[][][]positions){
        boolean canMove = linearCheck(positions,"down");
        if(canMove){
            for(int i =0; i < parts.size; i++){
                parts.get(i).transform.translate(0,0,1f);
            }
        }
    }

    public boolean descend(boolean[][][]positions){
        boolean canMove = linearCheck(positions,"des");
        if(canMove){
            for(int i =0; i < parts.size; i++){
                parts.get(i).transform.translate(0, -1f, 0);
            }
            return true;
        }
        return false;
    }

    //Dispose Method
    public void dispose(){
        model.dispose();
        shadowModel.dispose();
    }

    //Shadow Methods
    public void createShadow(boolean[][][]fixed){
        Vector3 va = new Vector3();
        a.transform.getTranslation(va);
        Vector3 vb = new Vector3();
        b.transform.getTranslation(vb);
        Vector3 vc = new Vector3();
        c.transform.getTranslation(vc);
        Vector3 vd = new Vector3();
        d.transform.getTranslation(vd);

        e.transform.setToTranslation(va.x, va.y, va.z);
        f.transform.setToTranslation(vb.x, vb.y, vb.z);
        g.transform.setToTranslation(vc.x, vc.y, vc.z);
        h.transform.setToTranslation(vd.x,vd.y,vd.z);

        float ey = va.y;
        float fy = vb.y;
        float gy = vc.y;
        float hy = vd.y;
        boolean check = valid(fixed,va.x,ey-1,va.z) && valid(fixed,vb.x,fy-1,vb.z) && valid(fixed,vc.x,gy-1,vc.z) && valid(fixed,vd.x,hy-1,vd.z);
        while(check){
            shiftShadow();
            ey--;
            fy--;
            gy--;
            hy--;
            check = valid(fixed,va.x,ey-1,va.z) && valid(fixed,vb.x,fy-1,vb.z) && valid(fixed,vc.x,gy-1,vc.z) && valid(fixed,vd.x,hy-1,vd.z);

        }

    }

    public void renderShadow(ModelBatch batch, Environment environment){
        batch.render(e,environment);
        batch.render(f,environment);
        batch.render(g, environment);
        batch.render(h,environment);
    }


    public void shiftShadow(){
        e.transform.translate(0, -1f, 0);
        f.transform.translate(0, -1f, 0);
        g.transform.translate(0, -1f, 0);
        h.transform.translate(0, -1f, 0);
    }

    //Shadow Instance Getters
    public ModelInstance e(){
        return e;
    }
    public ModelInstance f(){
        return f;
    }
    public ModelInstance g(){
        return g;
    }
    public ModelInstance h(){
        return h;
    }

    //Abstract Methods
    public abstract void rotateX(boolean[][][]positions, boolean clock);
    public abstract void rotateY(boolean[][][]positions,boolean clock);
    public abstract void rotateZ(boolean[][][]positions,boolean clock);

}
