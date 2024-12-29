package com.nopalsoft.sokoban.game_objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.nopalsoft.sokoban.Assets;

public class EndPoint extends Tile {
    int numColors;

    AtlasRegion keyFrame;

    public EndPoint(int position, String color) {
        super(position);

        switch (color) {
            case "brown":
                numColors = Box.COLOR_BROWN;
                break;
            case "gray":
                numColors = Box.COLOR_GRAY;
                break;
            case "purple":
                numColors = Box.COLOR_PURPLE;
                break;
            case "blue":
                numColors = Box.COLOR_BLUE;
                break;
            case "black":
                numColors = Box.COLOR_BLACK;
                break;
            case "beige":
                numColors = Box.COLOR_BEIGE;
                break;
            case "yellow":
                numColors = Box.COLOR_YELLOW;
                break;
            case "red":
                numColors = Box.COLOR_RED;
                break;
        }

        setTextureColor(numColors);
    }

    private void setTextureColor(int numColor) {
        switch (numColor) {
            case Box.COLOR_BEIGE:
                keyFrame = Assets.endPointBeige;
                break;

            case Box.COLOR_BLACK:
                keyFrame = Assets.endPointBlack;
                break;

            case Box.COLOR_BLUE:
                keyFrame = Assets.endPointBlue;
                break;

            case Box.COLOR_BROWN:
                keyFrame = Assets.endPointBrown;
                break;

            case Box.COLOR_GRAY:
                keyFrame = Assets.endPointGray;
                break;

            case Box.COLOR_RED:
                keyFrame = Assets.endPointRed;
                break;

            case Box.COLOR_YELLOW:
                keyFrame = Assets.endPointYellow;
                break;

            case Box.COLOR_PURPLE:
                keyFrame = Assets.endPointPurple;
                break;

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
    }
}
