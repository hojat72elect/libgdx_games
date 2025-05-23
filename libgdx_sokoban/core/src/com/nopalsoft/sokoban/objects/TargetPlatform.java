package com.nopalsoft.sokoban.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.nopalsoft.sokoban.Assets;

public class TargetPlatform extends Tiles {
    int numColor;

    AtlasRegion keyFrame;

    public TargetPlatform(int position, String color) {
        super(position);

        switch (color) {
            case "brown":
                numColor = Box.COLOR_BROWN;
                break;
            case "gray":
                numColor = Box.COLOR_GRAY;
                break;
            case "purple":
                numColor = Box.COLOR_PURPLE;
                break;
            case "blue":
                numColor = Box.COLOR_BLUE;
                break;
            case "black":
                numColor = Box.COLOR_BLACK;
                break;
            case "beige":
                numColor = Box.COLOR_BEIGE;
                break;
            case "yellow":
                numColor = Box.COLOR_YELLOW;
                break;
            case "red":
                numColor = Box.COLOR_RED;
                break;
        }

        setTextureColor(numColor);
    }

    private void setTextureColor(int numColor) {
        switch (numColor) {
            case Box.COLOR_BEIGE:
                keyFrame = Assets.beigeTargetPlatform;
                break;

            case Box.COLOR_BLACK:
                keyFrame = Assets.blackTargetPlatform;
                break;

            case Box.COLOR_BLUE:
                keyFrame = Assets.blueTargetPlatform;
                break;

            case Box.COLOR_BROWN:
                keyFrame = Assets.brownTargetPlatform;
                break;

            case Box.COLOR_GRAY:
                keyFrame = Assets.grayTargetPlatform;
                break;

            case Box.COLOR_RED:
                keyFrame = Assets.redTargetPlatform;
                break;

            case Box.COLOR_YELLOW:
                keyFrame = Assets.yellowTargetPlatform;
                break;

            case Box.COLOR_PURPLE:
                keyFrame = Assets.purpleTargetPlatform;
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
    }
}
