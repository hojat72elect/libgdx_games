package com.bms.chemtris.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * Class to control carbon model.
 */
public class Carbon extends Element {
    //members
    public static final String NAME = "Carbon", //member for element name
            TOP_VE_1 = "Carbon_Valence_1", //member for the first free electron
            TOP_VE_2 = "Carbon_Valence_2", //member for the second free electron
            BOTTOM_VE_1 = "Carbon_Valence_3", //member for the third free electron
            BOTTOM_VE_2 = "Carbon_Valence_4"; //member for the fourth free electron

    public static final ModelBuilder carbon_builder = new ModelBuilder();


    public Carbon(int ve, boolean[][][] mtx, PerspectiveCamera cam) {
        super(ve, mtx, cam);

        //begin building the model
        carbon_builder.begin();

        //first node
        Node electron_1 = carbon_builder.node();
        electron_1.id = Carbon.TOP_VE_1;

        //make the cube
        carbon_builder.part(Carbon.TOP_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(new Color(188f/255f,188f/255f,188f/255f,1f))))
                .box(1f, 1f, 1f);

        //second node
        Node electron_2 = carbon_builder.node();
        electron_2.id = Carbon.TOP_VE_2;

        //make the cube
        carbon_builder.part(Carbon.TOP_VE_2, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(new Color(188f/255f,188f/255f,188f/255f,1f))))
                .box(1f, 1f, 1f);

        electron_2.translation.set(1f,0f,0f);

        //third node
        Node electron_3 = carbon_builder.node();
        electron_3.id = Carbon.BOTTOM_VE_1;

        //make the cube
        carbon_builder.part(Carbon.BOTTOM_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(new Color(188f/255f,188f/255f,188f/255f,1f))))
                .box(1f, 1f, 1f);

        electron_3.translation.set(0f,-1f,0f);

        //fourth node
        Node electron_4 = carbon_builder.node();
        electron_4.id = Carbon.BOTTOM_VE_2;

        //make the cube
        carbon_builder.part(Carbon.BOTTOM_VE_2, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(new Color(188f/255f,188f/255f,188f/255f,1f))))
                .box(1f, 1f, 1f);


        electron_4.translation.set(1f,-1f,0f);

        Model carbonModel = carbon_builder.end();

        atom = new ModelInstance(carbonModel,2,10,2);
        shadow = new ModelInstance(carbonModel,2,10,2);
        frame = new ModelInstance(carbonModel,2,10,2);
        frame.transform.scl(.01f);

        for(int i = 0; i < shadow.materials.size; ++i){
            shadow.materials.get(i).set(ColorAttribute.createDiffuse(105/255f,105/255f,105/255f,1f));
            frame.materials.get(i).set(ColorAttribute.createDiffuse(Color.WHITE));
        }
    }
}
