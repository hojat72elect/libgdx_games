package com.nopalsoft.clumsy.game.classic;

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

    SpriteBatch batch;
    WorldGameClassic worldGameClassic;
    OrthographicCamera camera;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batch, WorldGameClassic worldGameClassic) {

        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batch = batch;
        this.worldGameClassic = worldGameClassic;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render() {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.disableBlending();
        batch.enableBlending();
        drawTuberia();
        drawArcoiris();
        drawBird();
        batch.end();
    }

    private void drawArcoiris() {
        for (Tail obj : worldGameClassic.arrTail) {
            batch.draw(Assets.rainbowLight, obj.position.x - .15f, obj.position.y - .12f, .3f, .24f);
        }
    }

    private void drawBird() {
        Ufo obj = worldGameClassic.oUfo;
        TextureRegion keyFrame;

        if (obj.state == Ufo.STATE_NORMAL) {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, true);
        } else {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false);
        }
        batch.draw(keyFrame, obj.position.x - .25f, obj.position.y - .2f, .25f, .2f, .5f, .4f, 1, 1,
                (float) Math.toDegrees(obj.angleRad));
    }

    private void drawTuberia() {
        for (Pipes obj : worldGameClassic.arrTuberias) {
            if (obj.type == Pipes.LOWER_PIPE)
                batch.draw(Assets.lowerTube, obj.position.x - .35f, obj.position.y - 2f, .7f, 4);
            else
                batch.draw(Assets.upperTube, obj.position.x - .35f, obj.position.y - 2f, .7f, 4);
        }
    }
}
