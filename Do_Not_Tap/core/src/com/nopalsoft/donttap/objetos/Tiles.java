package com.nopalsoft.donttap.objetos;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.game.WorldGame;

public class Tiles extends Actor implements Poolable {
	public static final int STATE_NORMAL = 0;
	public static final int STATE_TAP = 1;
	public int state;

	public static float WIDTH = 120;
	public static float HEIGHT = 180;

	static public final int TIPO_MALO = 0;
	static public final int TIPO_BUENO = 1;

	public int tipo;
	public boolean canBeTap;// no se puede tocar hasta que se toca el de mas abajo

	final static LinkedHashMap<Integer, Vector2> mapPosiciones = new LinkedHashMap<Integer, Vector2>();
	static {
		// Los que tienen en -1 son los que estan mas abajo donde no se ven
		mapPosiciones.put(0, new Vector2(0, 720));
		mapPosiciones.put(1, new Vector2(120, 720));
		mapPosiciones.put(2, new Vector2(240, 720));
		mapPosiciones.put(3, new Vector2(360, 720));
		mapPosiciones.put(4, new Vector2(0, 540));
		mapPosiciones.put(5, new Vector2(120, 540));
		mapPosiciones.put(6, new Vector2(240, 540));
		mapPosiciones.put(7, new Vector2(360, 540));
		mapPosiciones.put(8, new Vector2(0, 360));
		mapPosiciones.put(9, new Vector2(120, 360));
		mapPosiciones.put(10, new Vector2(240, 360));
		mapPosiciones.put(11, new Vector2(360, 360));
		mapPosiciones.put(12, new Vector2(0, 180));
		mapPosiciones.put(13, new Vector2(120, 180));
		mapPosiciones.put(14, new Vector2(240, 180));
		mapPosiciones.put(15, new Vector2(360, 180));
		mapPosiciones.put(16, new Vector2(0, 0));
		mapPosiciones.put(17, new Vector2(120, 0));
		mapPosiciones.put(18, new Vector2(240, 0));
		mapPosiciones.put(19, new Vector2(360, 0));
		mapPosiciones.put(20, new Vector2(0, -180));
		mapPosiciones.put(21, new Vector2(120, -180));
		mapPosiciones.put(22, new Vector2(240, -180));
		mapPosiciones.put(23, new Vector2(360, -180));
	}

	public int posicionTabla;
	TextureRegion keyframe;
	WorldGame oWorld;

	public Tiles() {
		setSize(WIDTH, HEIGHT);
		setOrigin(WIDTH / 2f, HEIGHT / 2f);
		addListener(inputListener);

	}

	public void init(WorldGame oWorld, int posicionTabla, boolean canStep, boolean isFirstRow) {
		this.posicionTabla = posicionTabla;
		this.oWorld = oWorld;
		setPosition(mapPosiciones.get(posicionTabla).x, mapPosiciones.get(posicionTabla).y);

		clearActions();
		getColor().a = 1;
		if (!canStep) {
			tipo = TIPO_MALO;
			keyframe = Assets.tileBlanco;
		}
		else {
			switch (MathUtils.random(4)) {
			case 0:

				keyframe = Assets.tileRojo;
				break;
			case 1:
				keyframe = Assets.tileAmarillo;
				break;
			case 2:
				keyframe = Assets.tileAzul;
				break;
			case 3:
				keyframe = Assets.tileMorado;
				break;
			case 4:
				keyframe = Assets.tileNaranja;
				break;
			}
			tipo = TIPO_BUENO;
			addAction(Actions.forever(Actions.sequence(Actions.alpha(.6f, .5f), Actions.alpha(1, .35f))));

		}

		if (isFirstRow && canStep) {
			canBeTap = true;
			state = Tiles.STATE_TAP;
		}
		else {
			setScale(1f);
			canBeTap = false;
			state = STATE_NORMAL;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, getColor().a);
		batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		if (state == STATE_TAP) {
			TextureRegion step;
			if (tipo == TIPO_BUENO)
				step = Assets.step1;
			else
				step = Assets.wrong;
			batch.draw(step, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}

		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

	}

	InputListener inputListener = new InputListener() {
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			if (oWorld.state == WorldGame.STATE_READY) {
				oWorld.state = WorldGame.STATE_RUNNING;
			}

			if (oWorld.state == WorldGame.STATE_RUNNING) {
				if (state == STATE_NORMAL && canBeTap || tipo == TIPO_MALO) {
					state = STATE_TAP;

					switch (oWorld.mode) {
					case WorldGame.MODE_CLASSIC:
						oWorld.addRow();
						if (tipo == TIPO_BUENO) {
							oWorld.score--;
							Assets.playTapSound();
						}
						else {
							Assets.soundWrong.play();
						}
						break;
					case WorldGame.MODE_TIME:
						oWorld.addRow();
						if (tipo == TIPO_BUENO) {
							oWorld.score++;
							Assets.playTapSound();
						}
						else {
							Assets.soundWrong.play();
						}
						break;

					case WorldGame.MODE_ENDLESS:
						if (tipo == TIPO_BUENO) {
							oWorld.score++;
							Assets.playTapSound();
						}
						else {
							Assets.soundWrong.play();
						}
						break;
					}

				}
			}
			return true;
		};
	};

	public void moveUp() {
		posicionTabla -= 4;
		if (posicionTabla < 0) {
			return;
		}
		addAction(Actions.moveTo(mapPosiciones.get(posicionTabla).x, mapPosiciones.get(posicionTabla).y, .75f));

	}

	public void moveDown() {
		posicionTabla += 4;
		if (posicionTabla > 23) {
			return;
		}

		float time = .1f;
		if (oWorld.mode == WorldGame.MODE_ENDLESS)
			time = oWorld.TIME_TO_SPWAN_ROW;
		addAction(Actions.moveTo(mapPosiciones.get(posicionTabla).x, mapPosiciones.get(posicionTabla).y, time));
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
