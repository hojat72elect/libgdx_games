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
 * Class to control hydrogen model.
 */
public class Hydrogen extends Element {

    //members
    public static final String NAME = "Hydrogen", //member for element name
            VE_1 = "Hydro_Valence"; //member for the only valence electron of element

    public static final ModelBuilder hydrogen_builder = new ModelBuilder();


    public Hydrogen(int ve, boolean[][][] mtx, PerspectiveCamera cam) {
        super(ve, mtx, cam);

        //begin building the model
        hydrogen_builder.begin();

        //first node
        Node electron_1 = hydrogen_builder.node();
        electron_1.id = Hydrogen.VE_1;

        //make the cube
        hydrogen_builder.part(Hydrogen.VE_1, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(new Color(195f/255f,207f/255f,255f/255f,1f))))
                .box(1f, 1f, 1f);

        //end the model
        Model hModel = hydrogen_builder.end();

        atom = new ModelInstance(hModel,2,10,2);
        shadow = new ModelInstance(hModel,2,10,2);
        frame = new ModelInstance(hModel,2,10,2);
        frame.transform.scl(.01f);

        for(int i = 0; i < shadow.materials.size; ++i){
            shadow.materials.get(i).set(ColorAttribute.createDiffuse(105/255f,105/255f,105/255f,1f));
            frame.materials.get(i).set(ColorAttribute.createDiffuse(Color.WHITE));
        }
    }
}
