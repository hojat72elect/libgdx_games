package com.nopalsoft.sokoban.objetos;

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

		if (color.equals("brown")) {
			numColor = COLOR_BROWN;
		}
		else if (color.equals("gray")) {
			numColor = COLOR_GRAY;
		}
		else if (color.equals("purple")) {
			numColor = COLOR_PURPLE;
		}
		else if (color.equals("blue")) {
			numColor = COLOR_BLUE;
		}
		else if (color.equals("black")) {
			numColor = COLOR_BLACK;
		}
		else if (color.equals("beige")) {
			numColor = COLOR_BEIGE;
		}
		else if (color.equals("yellow")) {
			numColor = COLOR_YELLOW;
		}
		else if (color.equals("red")) {
			numColor = COLOR_RED;
		}

		setTextureColor(numColor);
	}

	private void setTextureColor(int numColor) {
		switch (numColor) {
		case COLOR_BEIGE:
			keyFrame = Assets.cajaBeige;
			break;
		case COLOR_DARK_BEIGE:
			keyFrame = Assets.cajaDarkBeige;
			break;
		case COLOR_BLACK:
			keyFrame = Assets.cajaBlack;
			break;
		case COLOR_DARK_BLACK:
			keyFrame = Assets.cajaDarkBlack;
			break;
		case COLOR_BLUE:
			keyFrame = Assets.cajaBlue;
			break;
		case COLOR_DARK_BLUE:
			keyFrame = Assets.cajaDarkBlue;
			break;
		case COLOR_BROWN:
			keyFrame = Assets.cajaBrown;
			break;
		case COLOR_DARK_BROWN:
			keyFrame = Assets.cajaDarkBrown;
			break;
		case COLOR_GRAY:
			keyFrame = Assets.cajaGray;
			break;
		case COLOR_DARK_GRAY:
			keyFrame = Assets.cajaDarkGray;
			break;
		case COLOR_RED:
			keyFrame = Assets.cajaRed;
			break;
		case COLOR_DARK_RED:
			keyFrame = Assets.cajaDarkRed;
			break;
		case COLOR_YELLOW:
			keyFrame = Assets.cajaYellow;
			break;
		case COLOR_DARK_YELLOW:
			keyFrame = Assets.cajaDarkYellow;
			break;
		case COLOR_PURPLE:
			keyFrame = Assets.cajaPurple;
			break;
		case COLOR_DARK_PURPLE:
			keyFrame = Assets.cajaDarkPurple;
			break;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
	}

	public void setIsInEndPoint(EndPoint endPoint) {
		numColor = Math.abs(numColor);
		isInRightEndPoint = false;
		if (endPoint != null && endPoint.numColor == numColor) {
			numColor = -numColor;
			isInRightEndPoint = true;
		}
		setTextureColor(numColor);

	}
}
