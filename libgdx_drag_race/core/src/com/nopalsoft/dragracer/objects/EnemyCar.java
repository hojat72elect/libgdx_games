package com.nopalsoft.dragracer.objects;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.Settings;
import com.nopalsoft.dragracer.shop.PersonajesSubMenu;

public class EnemyCar extends Actor {

	int tipo;
	boolean isSuperSpeed;
	TextureRegion keyframe;

	private Rectangle bounds = new Rectangle();
	private MoveToAction moveAction;

	public EnemyCar(float x, float y) {

		tipo = MathUtils.random(16);

		float width, height;

		switch (tipo) {
		case PersonajesSubMenu.SKIN_CARRO_DIABLO:
			keyframe = Assets.carroDiablo;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_BANSHEE:
			keyframe = Assets.carroBanshee;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_TORNADO:
			keyframe = Assets.carroTornado;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_TURISMO:
			keyframe = Assets.carroTurismo;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_AUDI_S5:
			keyframe = Assets.audiS5;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_BMW_X6:
			keyframe = Assets.bmwX6;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_BULLET:
			keyframe = Assets.carroBullet;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_CHEVRLOTE_CROSSFIRE:
			keyframe = Assets.chevroletCrossfire;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_CITROEN_C4:
			keyframe = Assets.citroenC4;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_DODGE_CHARGER:
			keyframe = Assets.dodgeCharger;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_FIAT_500_LOUNGE:
			keyframe = Assets.fiat500Lounge;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_HONDA_CRV:
			keyframe = Assets.hondaCRV;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_MAZDA_6:
			keyframe = Assets.mazda6;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_MAZDA_RX8:
			keyframe = Assets.mazdaRX8;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_SEAT_IBIZA:
			keyframe = Assets.seatIbiza;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		case PersonajesSubMenu.SKIN_CARRO_VOLKSWAGEN_SCIROCCO:
		default:
			keyframe = Assets.volkswagenScirocco;
			width = keyframe.getRegionWidth();
			height = keyframe.getRegionHeight();
			break;
		}

		// Le resto menos 5 para que los bounds no esten tan grandes : VEr metodo draw
		setWidth(width - 20);
		setHeight(height - 20);
		setPosition(x - getWidth() / 2f, y);

		moveAction = new MoveToAction();
		moveAction.setPosition(getX(), -getHeight());
		moveAction.setDuration(MathUtils.random(4.0f, 6.0f));



		addAction(moveAction);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateBounds();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// le sumo mas 20 a los bounds actuales para que el draw quede mejor
		float drawWidth = getWidth() + 20;
		float drawHeight = getHeight() + 20;
		// Le resto 10 porque es la mitad de los +20
		batch.draw(keyframe, getX() - 10, getY() - 10, drawWidth / 2f,
				drawHeight / 2f, drawWidth, drawHeight, 1, 1, getRotation());

		if (Settings.drawDebugLines) {
			batch.end();
			renders.setProjectionMatrix(batch.getProjectionMatrix());
			renders.begin(ShapeType.Line);
			renders.rect(bounds.x, bounds.y, bounds.width, bounds.height);
			renders.end();
			batch.begin();
		}
	}

	ShapeRenderer renders = new ShapeRenderer();

	private void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());

	}

	public void setSpeed() {
		if (!isSuperSpeed) {
			isSuperSpeed = true;
			moveAction.reset();
			moveAction.setDuration(1f);
			addAction(moveAction);
		}
	}

	public void crash(boolean front, boolean above) {
		clearActions();
		addAction(fadeOut(1f));

		if (front && above)
			agregarAccion(-360, 200, 200);

		if (front && !above)
			agregarAccion(360, 200, -200);

		if (!front && above)
			agregarAccion(360, -200, 200);

		if (!front && !above)
			agregarAccion(-360, -200, -200);
	}

	private void agregarAccion(float rotacion, float posX, float posY) {
		addAction(sequence(
				parallel(Actions.rotateBy(rotacion, 1.5f),
						Actions.moveBy(posX, posY, 1.5f)), removeActor()));
	}

	public Rectangle getBounds() {
		return bounds;
	}
}
