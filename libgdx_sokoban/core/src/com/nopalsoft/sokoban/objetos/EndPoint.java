package com.nopalsoft.sokoban.objetos;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.nopalsoft.sokoban.Assets;

public class EndPoint extends Tiles {
	int numColor;

	AtlasRegion keyFrame;

	public EndPoint(int posicion, String color) {
		super(posicion);

		if (color.equals("brown")) {
			numColor = Box.COLOR_BROWN;
		}
		else if (color.equals("gray")) {
			numColor = Box.COLOR_GRAY;
		}
		else if (color.equals("purple")) {
			numColor = Box.COLOR_PURPLE;
		}
		else if (color.equals("blue")) {
			numColor = Box.COLOR_BLUE;
		}
		else if (color.equals("black")) {
			numColor = Box.COLOR_BLACK;
		}
		else if (color.equals("beige")) {
			numColor = Box.COLOR_BEIGE;
		}
		else if (color.equals("yellow")) {
			numColor = Box.COLOR_YELLOW;
		}
		else if (color.equals("red")) {
			numColor = Box.COLOR_RED;
		}

		setTextureColor(numColor);
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
