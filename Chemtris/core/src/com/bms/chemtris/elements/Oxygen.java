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
 * Class to control Oxygen Model.
 */
public class Oxygen extends Element {

    public static final ModelBuilder oxygen_builder = new ModelBuilder();

    //members
    public static final String NAME = "Oxygen", //member for element name
            FREE_VE_1 = "Oxygen_Valence_1", //member for the first free electron
            FREE_VE_2 = "Oxygen_Valence_2", //member for the second free electron
            RIGHT_VE_1 = "Oxygen_Valence_Right_1", //member for the first right valence
            RIGHT_VE_2 = "Oxygen_Valence_Right_2", //member for the first right valence
            LEFT_VE_1 = "Oxygen_Valence_LEFT_1", //member for the first left valence
            LEFT_VE_2 = "Oxygen_Valence_LEFT_2"; //member for the first left valence

    public Oxygen(int ve, boolean[][][] mtx, PerspectiveCamera cam) {
        super(ve, mtx, cam);


        //begin building the model
        oxygen_builder.begin();

        //first node
        Node electron_1 = oxygen_builder.node();
        electron_1.id = Oxygen.FREE_VE_1;

        //make the cube
        oxygen_builder.part(Oxygen.FREE_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        //second node
        Node electron_2 = oxygen_builder.node();
        electron_2.id = Oxygen.FREE_VE_2;

        //make the cube
        oxygen_builder.part(Oxygen.FREE_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        electron_2.translation.set(1f,0f,0f);

        //third node
        Node electron_3 = oxygen_builder.node();
        electron_3.id = Oxygen.RIGHT_VE_1;

        //make the cube
        oxygen_builder.part(Oxygen.RIGHT_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        electron_3.translation.set(2f,0f,0f);

        //fourth node
        Node electron_4 = oxygen_builder.node();
        electron_4.id = Oxygen.RIGHT_VE_2;

        //make the cube
        oxygen_builder.part(Oxygen.RIGHT_VE_2, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        electron_4.translation.set(2f,1f,0f);

        //fifth node
        Node electron_5 = oxygen_builder.node();
        electron_5.id = Oxygen.LEFT_VE_1;

        //make the cube
        oxygen_builder.part(Oxygen.LEFT_VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        electron_5.translation.set(-1f,0f,0f);

        //sixth node
        Node electron_6 = oxygen_builder.node();
        electron_6.id = Oxygen.LEFT_VE_2;

        //make the cube
        oxygen_builder.part(Oxygen.LEFT_VE_2, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .box(1f, 1f, 1f);

        electron_6.translation.set(-1f,1f,0f);

        Model oxygenModel = oxygen_builder.end();

        atom = new ModelInstance(oxygenModel,2,10,2);
        shadow = new ModelInstance(oxygenModel,2,10,2);
        frame = new ModelInstance(oxygenModel,2,10,2);
        frame.transform.scl(.01f);

        for(int i = 0; i < shadow.materials.size; ++i){
            shadow.materials.get(i).set(ColorAttribute.createDiffuse(105/255f,105/255f,105/255f,1f));
            frame.materials.get(i).set(ColorAttribute.createDiffuse(Color.WHITE));
        }
    }
}
