package com.nopalsoft.clumsy.game.classic;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.objects.Pipes;
import com.nopalsoft.clumsy.objects.Tail;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.Screens;

public class WorldGameRenderer {

	final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
	final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

	SpriteBatch batcher;
	WorldGameClassic oWorld;
	OrthographicCamera OrthoCam;

	Box2DDebugRenderer renderBox;

	public WorldGameRenderer(SpriteBatch batcher, WorldGameClassic oWorld) {

		this.OrthoCam = new OrthographicCamera(WIDTH, HEIGHT);
		this.OrthoCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		this.batcher = batcher;
		this.oWorld = oWorld;
		this.renderBox = new Box2DDebugRenderer();
	}

	public void render(float delta) {

		OrthoCam.update();
		batcher.setProjectionMatrix(OrthoCam.combined);

		batcher.begin();
		batcher.disableBlending();
		renderBackground(delta);
		batcher.enableBlending();
		drawTuberia(delta);
		drawArcoiris();
		drawBird(delta);

		batcher.end();

		// renderBox.render(oWorld.oWorldBox, OrthoCam.combined);
	}

	private void drawArcoiris() {
		Iterator<Tail> i = oWorld.arrTail.iterator();
		while (i.hasNext()) {
			Tail obj = i.next();
			batcher.draw(Assets.arcoiris, obj.position.x - .15f, obj.position.y - .12f, .3f, .24f);
		}

	}

	private void drawBird(float delta) {
		Ufo obj = oWorld.oUfo;
		TextureRegion keyFrame;

		if (obj.state == Ufo.STATE_NORMAL) {
			keyFrame = Assets.bird.getKeyFrame(obj.stateTime, true);
		}
		else {
			keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false);
		}
		batcher.draw(keyFrame, obj.position.x - .25f, obj.position.y - .2f, .25f, .2f, .5f, .4f, 1, 1,
				(float) Math.toDegrees(obj.angleRad));
	}

	private void drawTuberia(float delta) {
		Iterator<Pipes> i = oWorld.arrTuberias.iterator();
		while (i.hasNext()) {
			Pipes obj = i.next();
			if (obj.tipo == Pipes.TIPO_ABAJO)
				batcher.draw(Assets.tuboAbajo, obj.position.x - .35f, obj.position.y - 2f, .7f, 4);
			else
				batcher.draw(Assets.tuboArriba, obj.position.x - .35f, obj.position.y - 2f, .7f, 4);
		}

	}

	private void renderBackground(float delta) {
		// batcher.draw(Assets.fondo, 0, 0, WIDTH, HEIGHT);

	}
}
