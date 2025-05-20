package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;

public class LabelMonedas extends Actor {
	int numMonedas;

	public LabelMonedas(float x, float y, int numMonedas) {
		this.numMonedas = numMonedas;
		this.setPosition(x, y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawPuntuacionChicoOrigenDerecha(batch, this.getX(), this.getY(), numMonedas);

	}

	public void drawPuntuacionChicoOrigenDerecha(Batch batcher, float x, float y, int numMonedas) {
		String score = String.valueOf(numMonedas);

		int len = score.length();
		float charWidth = 22;
		float textWidth = 0;
		for (int i = len - 1; i >= 0; i--) {
			AtlasRegion keyFrame;

			charWidth = 22;
			char character = score.charAt(i);

			if (character == '0') {
				keyFrame = Assets.num0Chico;
			}
			else if (character == '1') {
				keyFrame = Assets.num1Chico;
				charWidth = 11f;
			}
			else if (character == '2') {
				keyFrame = Assets.num2Chico;
			}
			else if (character == '3') {
				keyFrame = Assets.num3Chico;
			}
			else if (character == '4') {
				keyFrame = Assets.num4Chico;
			}
			else if (character == '5') {
				keyFrame = Assets.num5Chico;
			}
			else if (character == '6') {
				keyFrame = Assets.num6Chico;
			}
			else if (character == '7') {
				keyFrame = Assets.num7Chico;
			}
			else if (character == '8') {
				keyFrame = Assets.num8Chico;
			}
			else {// 9
				keyFrame = Assets.num9Chico;
			}
			textWidth += charWidth;
			batcher.draw(keyFrame, x - textWidth, y, charWidth, 32);
		}
	}
}
