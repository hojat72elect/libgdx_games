package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.game.WorldGame;

public class LabelCombo extends Actor {
	int comboActual;

	public LabelCombo(float x, float y, int comboActual) {
		this.comboActual = comboActual;
		this.setPosition(x, y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.draw(Assets.combo, getX(), getY(), 65, 27);
		if (comboActual >= WorldGame.COMBO_TO_START_GETTING_COINS) {
			batch.draw(Assets.moneda, getX() + 20, getY() + 35, 23, 26);
		}
		drawPuntuacionChicoOrigenIzq(batch, this.getX() + 70, this.getY() + 2,
				comboActual);

	}

	public void drawPuntuacionChicoOrigenIzq(Batch batcher, float x, float y,
			int comboActual) {
		String score = String.valueOf(comboActual);

		int len = score.length();
		float charWidth = 22;
		float textWidth = 0;
		for (int i = 0; i < len; i++) {
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

			batcher.draw(keyFrame, x + textWidth, y, charWidth, 22);
			textWidth += charWidth;
		}
	}
}
