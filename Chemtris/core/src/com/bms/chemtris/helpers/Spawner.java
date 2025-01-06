package com.bms.chemtris.helpers;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.bms.chemtris.models.Box;
import com.bms.chemtris.models.BoxThree;
import com.bms.chemtris.models.BoxTwo;
import com.bms.chemtris.models.Column;
import com.bms.chemtris.models.ColumnTwo;
import com.bms.chemtris.models.GameModel;
import com.bms.chemtris.models.LShape;
import com.bms.chemtris.models.LShapeTwo;

import java.util.Random;

/**
 * Class to spawn game models.
 */
public class Spawner {

    private float r = 70/255f, g = 70/255f, b = 70/255f;
    private int spawn_piece;
    public GameModel current;
    public boolean done, finished_spawning;
    private float time_elasped;
    private Random random;
    private static boolean [][][] matrix;
    private PerspectiveCamera camera;

    //constructor
    public Spawner(int pieces, boolean [][][] mx, PerspectiveCamera cam){
        spawn_piece = pieces;
        done = false;
        time_elasped = 0f;
        random = new Random();
        finished_spawning = true;
        matrix = mx;
        camera = cam;

    }

    //method to get random piece
    private void getPiece(){
        finished_spawning = false;
        int randNum = random.nextInt(spawn_piece);

        switch (randNum) {
            case 0:
                current = new Box(camera, matrix);
                break;
            case 1:
                current = new BoxTwo(camera, matrix);
                break;
            case 2:
                current = new BoxThree(camera, matrix);
                break;
            case 3:
                current = new Column(camera, matrix);
                break;
            case 4:
                current = new ColumnTwo(camera, matrix);
                break;
            case 5:
                current = new LShape(camera,matrix);
                break;
            case 6:
                current = new LShapeTwo(camera,matrix);
                break;

            default:
                current = new Box(camera,matrix);
                break;
        }

        done = true;

    }

    public GameModel run(float delta) {
        //check if piece finished spawning
        if (!done) {
            getPiece();
        }

        //animate fading in effect for pieces
        if (time_elasped == 0f) {
            for (ModelInstance part : current.parts) {
                part.materials.get(0).set(ColorAttribute.createDiffuse(r, g, b, 1f));
            }

        }

        time_elasped += delta;

        if (time_elasped < 1.25f) {
            r += 10 / 255f;
            g += 10 / 255f;
            b += 10 / 255f;
            for (ModelInstance part : current.parts) {
                part.materials.get(0).set(ColorAttribute.createDiffuse(r, g, b, 1f));
            }
            return null;
        }
        else {
            r = 255 / 255f;
            g = 255 / 255f;
            b = 255 / 255f;
            for (ModelInstance part : current.parts) {
                part.materials.get(0).set(ColorAttribute.createDiffuse(r, g, b, 1f));
            }
            done = false;
            finished_spawning = true;
            time_elasped = 0f;
            r = 70/255f;
            g = 70/255f;
            b = 70/255f;

            //when animation is done return the piece
            return current;

        }
    }


}
