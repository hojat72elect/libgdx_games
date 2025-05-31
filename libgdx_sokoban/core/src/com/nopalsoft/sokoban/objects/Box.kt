package com.nopalsoft.sokoban.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.nopalsoft.sokoban.Assets;

public class Box extends Tiles {
    public static final int COLOR_BEIGE = 1;
    public static final int COLOR_DARK_BEIGE = -1;
    public static final int COLOR_BLACK = 2;
    public static final int COLOR_DARK_BLACK = -2;
    public static final int COLOR_BLUE = 3;
    public static final int COLOR_DARK_BLUE = -3;
    public static final int COLOR_BROWN = 4;
    public static final int COLOR_DARK_BROWN = -4;
    public static final int COLOR_GRAY = 5;
    public static final int COLOR_DARK_GRAY = -5;
    public static final int COLOR_RED = 6;
    public static final int COLOR_DARK_RED = -6;
    public static final int COLOR_YELLOW = 7;
    public static final int COLOR_DARK_YELLOW = -7;
    public static final int COLOR_PURPLE = 8;
    public static final int COLOR_DARK_PURPLE = -8;
    int numColor;

    public boolean isInRightEndPoint;

    AtlasRegion keyFrame;

    public Box(int posicion, String color) {
        super(posicion);

        isInRightEndPoint = false;

        switch (color) {
            case "brown":
                numColor = COLOR_BROWN;
                break;
            case "gray":
                numColor = COLOR_GRAY;
                break;
            case "purple":
                numColor = COLOR_PURPLE;
                break;
            case "blue":
                numColor = COLOR_BLUE;
                break;
            case "black":
                numColor = COLOR_BLACK;
                break;
            case "beige":
                numColor = COLOR_BEIGE;
                break;
            case "yellow":
                numColor = COLOR_YELLOW;
                break;
            case "red":
                numColor = COLOR_RED;
                break;
        }

        setTextureColor(numColor);
    }

    private void setTextureColor(int numColor) {
        switch (numColor) {
            case COLOR_BEIGE:
                keyFrame = Assets.beigeBox;
                break;
            case COLOR_DARK_BEIGE:
                keyFrame = Assets.darkBeigeBox;
                break;
            case COLOR_BLACK:
                keyFrame = Assets.blackBox;
                break;
            case COLOR_DARK_BLACK:
                keyFrame = Assets.darkBlackBox;
                break;
            case COLOR_BLUE:
                keyFrame = Assets.blueBox;
                break;
            case COLOR_DARK_BLUE:
                keyFrame = Assets.darkBlueBox;
                break;
            case COLOR_BROWN:
                keyFrame = Assets.brownBox;
                break;
            case COLOR_DARK_BROWN:
                keyFrame = Assets.darkBrownBox;
                break;
            case COLOR_GRAY:
                keyFrame = Assets.grayBox;
                break;
            case COLOR_DARK_GRAY:
                keyFrame = Assets.darkGrayBox;
                break;
            case COLOR_RED:
                keyFrame = Assets.redBox;
                break;
            case COLOR_DARK_RED:
                keyFrame = Assets.darkRedBox;
                break;
            case COLOR_YELLOW:
                keyFrame = Assets.yellowBox;
                break;
            case COLOR_DARK_YELLOW:
                keyFrame = Assets.darkYellowBox;
                break;
            case COLOR_PURPLE:
                keyFrame = Assets.purpleBox;
                break;
            case COLOR_DARK_PURPLE:
                keyFrame = Assets.darkPurpleBox;
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
    }

    public void setIsInEndPoint(TargetPlatform endPoint) {
        numColor = Math.abs(numColor);
        isInRightEndPoint = false;
        if (endPoint != null && endPoint.numColor == numColor) {
            numColor = -numColor;
            isInRightEndPoint = true;
        }
        setTextureColor(numColor);
    }
}
