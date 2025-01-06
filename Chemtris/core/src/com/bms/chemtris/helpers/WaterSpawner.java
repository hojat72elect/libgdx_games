package com.bms.chemtris.helpers;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.bms.chemtris.elements.Carbon;
import com.bms.chemtris.elements.Element;
import com.bms.chemtris.elements.Hydrogen;
import com.bms.chemtris.elements.Oxygen;

import java.util.Random;

/**
 * Class to spawn water.
 */
public class WaterSpawner {
    private Random random;
    private static boolean [][][] matrix;
    private PerspectiveCamera camera;
    public static final int H = -1, O = -2, C = -3;
    public int current;

    public WaterSpawner(boolean [][][] mx, PerspectiveCamera cam){
        random = new Random();
        matrix = mx;
        camera = cam;
    }

    public Element spawn(){
        int randNum = random.nextInt(5);

        switch (randNum){
            case 0:
                current = H;
                return new Hydrogen(1,matrix,camera);
            case 1:
                current = O;
                return new Oxygen(2,matrix,camera);
            case 2:
                current = H;
                return new Hydrogen(1,matrix,camera);
            case 3:
                current = O;
                return new Oxygen(2,matrix,camera);
            case 4:
                current = H;
                return new Hydrogen(1,matrix,camera);
            default:
                current = H;
                return new Hydrogen(1,matrix,camera);
        }
    }

    public Element spawn2(){
        int randNum = random.nextInt(5);

        switch (randNum){
            case 0:
                current = H;
                return new Hydrogen(1,matrix,camera);
            case 1:
                current = C;
                return new Carbon(4,matrix,camera);
            case 2:
                current = H;
                return new Hydrogen(1,matrix,camera);
            case 3:
                current = C;
                return new Carbon(4,matrix,camera);
            case 4:
                current = H;
                return new Hydrogen(1,matrix,camera);
            default:
                current = H;
                return new Hydrogen(1,matrix,camera);
        }
    }
}
