package com.bms.chemtris.helpers;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bms.chemtris.models.GameModel;
import com.bms.chemtris.render.WireframeShader;

import java.util.Random;

/**
 * Class to store game pieces.
 */

public class Storage {

    //game pieces
    public Array<ModelInstance> level_1;
    public Array<ModelInstance> level_2;
    public Array<ModelInstance> level_3;
    public Array<ModelInstance> level_4;
    public Array<ModelInstance> level_5;

    public Array<ModelInstance> level_1_w;
    public Array<ModelInstance> level_2_w;
    public Array<ModelInstance> level_3_w;
    public Array<ModelInstance> level_4_w;
    public Array<ModelInstance> level_5_w;

    private static Environment environment; //rendering environment
    public int cc_type;

    private int frame;

    //ModelBatch Renderer and Wireframe shader
    private static final ModelBatch fillRenderer = new ModelBatch();
    private static final ModelBatch wireRenderer = new ModelBatch(new DefaultShaderProvider() {
        @Override
        protected Shader createShader(Renderable renderable) {
            return new WireframeShader(renderable, config);
        }
    });

    //colors
    private Color dark = new Color(255/255f,8/255f,0f,1f);
    private Color lDark = new Color(255/255f,69/255f,0f,1f);
    private Color medium = new Color(255/255f,127/255f,0f,1f);
    private Color normal = new Color(255/255f,155/255f,0f,1f);
    private Color light = new Color(255/255f,204/255f,0f,1f);

    //perspective camera
    private PerspectiveCamera camera;

    //collision matrix
    private static boolean [][][] matrix;

    private static final Random rand = new Random();


    //constructor
    public Storage(boolean [][][] collision, PerspectiveCamera cam){
        camera = cam;
        matrix = collision;
        frame = 0;

        level_1 =  new Array<ModelInstance>();
        level_2 =  new Array<ModelInstance>();
        level_3 =  new Array<ModelInstance>();
        level_4 =  new Array<ModelInstance>();
        level_5 =  new Array<ModelInstance>();

        level_1_w =  new Array<ModelInstance>();
        level_2_w =  new Array<ModelInstance>();
        level_3_w =  new Array<ModelInstance>();
        level_4_w =  new Array<ModelInstance>();
         level_5_w =  new Array<ModelInstance>();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cc_type = rand.nextInt(3);

        if(cc_type == 0){
            dark = new Color(255/255f,8/255f,0f,1f);
            lDark = new Color(255/255f,69/255f,0f,1f);
            medium = new Color(255/255f,127/255f,0f,1f);
            normal = new Color(255/255f,155/255f,0f,1f);
            light = new Color(255/255f,204/255f,0f,1f);
        }
        else if(cc_type == 1){
            dark = new Color(115/255f,158/255f,225/255f,1f);
            lDark = new Color(138/255f,146/255f,236/255f,1f);
            medium = new Color(150/255f,140/255f,241/255f,1f);
            normal = new Color(161/255f,134/255f,247/255f,1f);
            light = new Color(173/255f,128/255f,252/255f,1f);
        }else{
            dark = new Color(224/255f,240/255f,53/255f,1f);
            lDark = new Color(199/255f,240/255f,64/255f,1f);
            medium = new Color(175/255f,240/255f,76/255f,1f);
            normal = new Color(114/255f,240/255f,105/255f,1f);
            light = new Color(53/255f,240/255f,134/255f,1f);
        }
    }

    public boolean add(GameModel model){

        try {

            for (int i = 0; i < model.parts.size; ++i) {

                Vector3 v = new Vector3();
                model.parts.get(i).transform.getTranslation(v);

                ModelInstance p = model.parts.get(i);
                ModelInstance f = model.frame.get(i);

                int x = Math.round(v.x);
                int z = Math.round(v.z);
                int y = Math.round(v.y);

                if (y < 6) {
                    matrix[x][y][z] = true;
                }

                switch (y) {
                    case 1:
                        level_1.add(p);
                        p.materials.get(0).set(ColorAttribute.createDiffuse(dark));
                        level_1_w.add(f);
                        break;

                    case 2:
                        level_2.add(p);
                        p.materials.get(0).set(ColorAttribute.createDiffuse(lDark));
                        level_2_w.add(f);
                        break;

                    case 3:
                        level_3.add(p);
                        p.materials.get(0).set(ColorAttribute.createDiffuse(medium));
                        level_3_w.add(f);
                        break;

                    case 4:
                        level_4.add(p);
                        p.materials.get(0).set(ColorAttribute.createDiffuse(normal));
                        level_4_w.add(f);
                        break;

                    case 5:
                        level_5.add(p);
                        p.materials.get(0).set(ColorAttribute.createDiffuse(light));
                        level_5_w.add(f);
                        break;

                    default:
                        return false;
                }
            }

            return true;
        }catch (Exception e){
            return false;
        }

    }

    public void shift_row(Array<ModelInstance> level, Array<ModelInstance> addRow, boolean wire){
        try {
            for (ModelInstance part : level) {

                Vector3 v = new Vector3();
                part.transform.getTranslation(v);

                int x = Math.round(v.x);
                int z = Math.round(v.z);
                int y = Math.round(v.y);

                part.transform.translate(0f, -1f, 0f);

                if (!wire) {
                    matrix[x][y][z] = false;
                    matrix[x][y - 1][z] = true;
                }

                addRow.add(part);
            }

            level.clear();
        }catch (Exception e){
            return;
        }
    }

    public void clear_row(int row){
        switch (row){
            case 1:
                shift_row(level_2,level_1,false);
                shift_row(level_2_w,level_1_w,true);
                shift_row(level_3,level_2,false);
                shift_row(level_3_w,level_2_w,true);
                shift_row(level_4,level_3,false);
                shift_row(level_4_w,level_3_w,false);
                shift_row(level_5,level_4,false);
                shift_row(level_5_w,level_4_w,false);
                break;

            case 2:
                shift_row(level_3,level_2,false);
                shift_row(level_3_w,level_2_w,true);
                shift_row(level_4,level_3,false);
                shift_row(level_4_w,level_3_w,false);
                shift_row(level_5,level_4,false);
                shift_row(level_5_w,level_4_w,false);
                break;

            case 3:
                shift_row(level_4,level_3,false);
                shift_row(level_4_w,level_3_w,false);
                shift_row(level_5,level_4,false);
                shift_row(level_5_w,level_4_w,false);
                break;

            case 4:
                shift_row(level_5,level_4,false);
                shift_row(level_5_w,level_4_w,false);
                break;

            case 5:
                for(int i = 0;  i < level_5.size; ++i){
                    Vector3 v = new Vector3();
                    level_5.get(i).transform.getTranslation(v);

                    int x = Math.round(v.x);
                    int z = Math.round(v.z);
                    int y = Math.round(v.y);

                    matrix[x][y][z] = false;
                }

                level_5.clear();
                level_5_w.clear();
                break;

            default:
                return;
        }
    }



    public void swap(Array<ModelInstance>first,Array<ModelInstance>second){

        for(int i = 0; i < first.size; i++){
            Vector3 t  = new Vector3();
            first.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
            matrix[x][y][z] = false;
        }
        first.clear();
        for(int i = 0; i < second.size; i++){
            Vector3 t  = new Vector3();
            second.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
            matrix[x][y][z] = false;
            second.get(i).transform.translate(0,-1f,0);
            first.add(second.get(i));
            matrix[x][y-1][z]= true;
        }
        second.clear();

    }

    public void swap_f(Array<ModelInstance>first,Array<ModelInstance>second){

        for(int i = 0; i < first.size; i++){
            Vector3 t  = new Vector3();
            first.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
        }
        first.clear();
        for(int i = 0; i < second.size; i++){
            Vector3 t  = new Vector3();
            second.get(i).transform.getTranslation(t);
            int x = Math.round(t.x);
            int y = Math.round(t.y);
            int z = Math.round(t.z);
            second.get(i).transform.translate(0,-1f,0);
            first.add(second.get(i));
        }
        second.clear();

    }


    public void render(){
        for(ModelInstance part : level_1){
            part.materials.get(0).set(ColorAttribute.createDiffuse(dark));
            if(frame == 0){
                part.transform.translate(-0.1f,0,0);
            }else{
                part.transform.translate(0.1f,0,0);
            }
        }

        for(ModelInstance part : level_2){
            part.materials.get(0).set(ColorAttribute.createDiffuse(lDark));
            if(frame == 0){
                part.transform.translate(-0.1f,0,0);
            }else{
                part.transform.translate(0.1f,0,0);
            }
        }

        for(ModelInstance part : level_3){
            part.materials.get(0).set(ColorAttribute.createDiffuse(medium));
            if(frame == 0){
                part.transform.translate(-0.1f,0,0);
            }else{
                part.transform.translate(0.1f,0,0);
            }
        }

        for(ModelInstance part : level_4){
            part.materials.get(0).set(ColorAttribute.createDiffuse(normal));
            if(frame == 0){
                part.transform.translate(-0.1f,0,0);
            }else{
                part.transform.translate(0.1f,0,0);
            }
        }

        for(ModelInstance part : level_5){
            part.materials.get(0).set(ColorAttribute.createDiffuse(light));
            if(frame == 0){
                part.transform.translate(-0.1f,0,0);
            }else{
                part.transform.translate(0.1f,0,0);
            }
        }

        fillRenderer.begin(camera);
        fillRenderer.render(level_1,environment);
        fillRenderer.render(level_2,environment);
        fillRenderer.render(level_3,environment);
        fillRenderer.render(level_4,environment);
        fillRenderer.render(level_5,environment);
        fillRenderer.end();

        wireRenderer.begin(camera);
        wireRenderer.render(level_1_w,environment);
        wireRenderer.render(level_2_w,environment);
        wireRenderer.render(level_3_w,environment);
        wireRenderer.render(level_4_w,environment);
        wireRenderer.render(level_5_w,environment);
        wireRenderer.end();

        if(frame == 0){
            frame = 1;
        }else{
            frame = 0;
        }


    }






}
