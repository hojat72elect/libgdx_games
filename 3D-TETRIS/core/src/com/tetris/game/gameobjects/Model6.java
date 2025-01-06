package com.tetris.game.gameobjects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
public class Model6 extends Pieces {
    private ModelInstance a2,b2,c2,d2,e2,f2,g2,h2;
    //Constructor
    public Model6(){
        super();
        shift(b(), v().x + 1f, v().y, v().z);
        shift(c(),v().x,v().y+1f,v().z);
        shift(d(),v().x+1f,v().y+1f,v().z);
        b2 = new ModelInstance(this.getModel());
        a2 = new ModelInstance(this.getModel());
        c2 = new ModelInstance(this.getModel());
        d2 = new ModelInstance(this.getModel());
        shift(b2,v().x + 1f, v().y, v().z-1f);
        shift(c2,v().x,v().y+1f,v().z-1f);
        shift(d2,v().x+1f,v().y+1f,v().z);
        d2.transform.translate(0, 0, -1f);
        shift(a2, v().x, v().y, v().z - 1f);
        addInstance(a2);
        addInstance(b2);
        addInstance(c2);
        addInstance(d2);

        e2 = new ModelInstance(this.getShadowModel());
        f2 = new ModelInstance(this.getShadowModel());
        g2 = new ModelInstance(this.getShadowModel());
        h2 = new ModelInstance(this.getShadowModel());
    }

    //Overwritten Shadow Methods
    @Override
    public void createShadow(boolean[][][]fixed){
        Vector3 va = new Vector3();
        a().transform.getTranslation(va);
        Vector3 vb = new Vector3();
        b().transform.getTranslation(vb);
        Vector3 vc = new Vector3();
        c().transform.getTranslation(vc);
        Vector3 vd = new Vector3();
        d().transform.getTranslation(vd);

        Vector3 va2 = new Vector3();
        a2.transform.getTranslation(va2);
        Vector3 vb2 = new Vector3();
        b2.transform.getTranslation(vb2);
        Vector3 vc2 = new Vector3();
        c2.transform.getTranslation(vc2);
        Vector3 vd2 = new Vector3();
        d2.transform.getTranslation(vd2);


        e().transform.setToTranslation(va.x, va.y, va.z);
        f().transform.setToTranslation(vb.x, vb.y, vb.z);
        g().transform.setToTranslation(vc.x, vc.y, vc.z);
        h().transform.setToTranslation(vd.x, vd.y, vd.z);

        e2.transform.setToTranslation(va2.x, va2.y, va2.z);
        f2.transform.setToTranslation(vb2.x, vb2.y, vb2.z);
        g2.transform.setToTranslation(vc2.x, vc2.y, vc2.z);
        h2.transform.setToTranslation(vd2.x,vd2.y,vd2.z);

        float ey = va.y;
        float fy = vb.y;
        float gy = vc.y;
        float hy = vd.y;
        float ey2 = va2.y;
        float fy2 = vb2.y;
        float gy2 = vc2.y;
        float hy2 = vd2.y;

        boolean check = valid(fixed,va.x,ey-1,va.z) && valid(fixed,vb.x,fy-1,vb.z) && valid(fixed,vc.x,gy-1,vc.z) && valid(fixed,vd.x,hy-1,vd.z)
                && valid(fixed,va2.x,ey2-1,va2.z) && valid(fixed,vb2.x,fy2-1,vb2.z) && valid(fixed,vc2.x,gy2-1,vc2.z) && valid(fixed,vd2.x,hy2-1,vd2.z);
        while(check){
            shiftShadow();
            ey--;
            fy--;
            gy--;
            hy--;
            ey2--;
            fy2--;
            gy2--;
            hy2--;
            check = valid(fixed,va.x,ey-1,va.z) && valid(fixed,vb.x,fy-1,vb.z) && valid(fixed,vc.x,gy-1,vc.z) && valid(fixed,vd.x,hy-1,vd.z)
                    && valid(fixed,va2.x,ey2-1,va2.z) && valid(fixed,vb2.x,fy2-1,vb2.z) && valid(fixed,vc2.x,gy2-1,vc2.z) && valid(fixed,vd2.x,hy2-1,vd2.z);

        }
    }
    @Override
    public void renderShadow(ModelBatch batch, Environment environment){
        batch.render(e(),environment);
        batch.render(f(),environment);
        batch.render(g(), environment);
        batch.render(h(),environment);
        batch.render(e2,environment);
        batch.render(f2,environment);
        batch.render(g2, environment);
        batch.render(h2,environment);
    }
    @Override
    public void shiftShadow(){
        e().transform.translate(0,-1f,0);
        f().transform.translate(0,-1f,0);
        g().transform.translate(0,-1f,0);
        h().transform.translate(0,-1f,0);
        e2.transform.translate(0,-1f,0);
        f2.transform.translate(0,-1f,0);
        g2.transform.translate(0,-1f,0);
        h2.transform.translate(0,-1f,0);
    }

    //Rotate Methods
    @Override
    public void rotateX(boolean[][][] positions, boolean clock) {
        return;
    }

    @Override
    public void rotateY(boolean[][][] positions, boolean clock) {
        return;
    }

    @Override
    public void rotateZ(boolean[][][] positions, boolean clock) {
        return;
    }
}
