package com.nGame.utils.ng.input;

import com.badlogic.gdx.Input;

/**
 * XBOX Controller mapping
 *
 * @author Oliver Eichner
 */
public class XboxController {
    public static final int A = 0;
    public static final int B = 1;
    public static final int X = 2;
    public static final int Y = 3;
    public static final int LB = 4;
    public static final int RB = 5;
    public static final int BACK = 6;
    public static final int START = 7;
    public static final int LS = 8;
    public static final int RS = 9;
    public static final int RSY = 0;
    public static final int RSX = 1;
    public static final int LSY = 2;
    public static final int LSX = 3;

    public static GamePadMapping getMenuMapping() {
        GamePadMapping mapping = new GamePadMapping(10, 4);

        mapping.mapButtonToKey(A, Input.Keys.ENTER);
        mapping.mapButtonToKey(B, Input.Keys.BACKSPACE);
        mapping.mapButtonToKey(X, Input.Keys.ENTER);
        mapping.mapButtonToKey(Y, Input.Keys.ENTER);
        mapping.mapButtonToKey(LB, Input.Keys.ENTER);
        mapping.mapButtonToKey(RB, Input.Keys.ENTER);
        mapping.mapButtonToKey(BACK, Input.Keys.ENTER);
        mapping.mapButtonToKey(START, Input.Keys.ESCAPE);
        mapping.mapButtonToKey(LS, Input.Keys.ENTER);
        mapping.mapButtonToKey(RS, Input.Keys.ENTER);

        return mapping;
    }
}
