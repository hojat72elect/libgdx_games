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
import com.badlogic.gdx.utils.Array;
import com.bms.chemtris.render.WireframeShader;

/**
 * Class to make platform.
 */
public class Platform {

    //platform parts
    public Array<ModelInstance> parts;
    //platform wires
    public Array<ModelInstance> wires;
    //platform box
    public ModelInstance box;

    private static Environment environment; //rendering environment

    //ModelBatch Renderer and Wireframe shader
    private static final ModelBatch fillRenderer = new ModelBatch();
    private static final ModelBatch wireRenderer = new ModelBatch(new DefaultShaderProvider() {
        @Override
        protected Shader createShader(Renderable renderable) {
            return new WireframeShader(renderable, config);
        }
    });

    //perspective camera
    private PerspectiveCamera camera;

    //constructor
    public Platform(float size, PerspectiveCamera cam, boolean [][][] matrix){

        //setup models
        ModelBuilder builder = new ModelBuilder();
        Model white = builder.createBox(
                1f,1f,1f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        Model wire = builder.createBox(
                1.01f,1.01f,1.01f,
                new Material(ColorAttribute.createDiffuse(Color.BLACK)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        Model boxModel = builder.createBox(
                size,6f,size,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        parts = new Array<ModelInstance>();
        wires = new Array<ModelInstance>();

        camera = cam;

        //setup environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        for(float x = 0f; x < size; ++x){
            for(float z = 0f; z < size; ++z){
                ModelInstance p = new ModelInstance(white,x,0f,z);
                ModelInstance w = new ModelInstance(wire,x,0f,z);
                parts.add(p);
                wires.add(w);

                matrix[Math.round(x)][0][Math.round(z)] = true;
            }
        }

        float offset = (size/2f)-.5f;

        box = new ModelInstance(boxModel,offset,2.5f,offset);

    }

    public void render(){
        fillRenderer.begin(camera);
        fillRenderer.render(parts, environment);
        fillRenderer.end();

        wireRenderer.begin(camera);
        wireRenderer.render(wires,environment);
        wireRenderer.render(box,environment);
        wireRenderer.end();
    }
}
