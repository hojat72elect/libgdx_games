package com.nopalsoft.flappy.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.flappy.Assets;
import com.nopalsoft.flappy.objetos.Bird;
import com.nopalsoft.flappy.objetos.Pipe;
import com.nopalsoft.flappy.screens.Screens;

public class WorldGameRenderer {

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch spriteBatch;
    WorldGame oWorld;
    OrthographicCamera oCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batcher, WorldGame oWorld) {

        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.spriteBatch = batcher;
        this.oWorld = oWorld;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render(float delta) {

        oCam.update();
        spriteBatch.setProjectionMatrix(oCam.combined);

        spriteBatch.begin();
        spriteBatch.disableBlending();
        drawBackground(delta);
        spriteBatch.enableBlending();
        drawPipe(delta);
        drawBird(delta);

        spriteBatch.end();

//        renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private void drawBackground(float delta) {
        spriteBatch.draw(Assets.background, 0, 0, WIDTH, HEIGHT);
    }

    private void drawPipe(float delta) {
        for (Pipe obj : oWorld.arrPipes) {
            if (obj.type == Pipe.TYPE_DOWN)
                spriteBatch.draw(Assets.downPipe, obj.position.x - .5f,
                        obj.position.y - 2f, 1f, 4);
            else
                spriteBatch.draw(Assets.upPipe, obj.position.x - .5f,
                        obj.position.y - 2f, 1f, 4);
        }
    }

    private void drawBird(float delta) {
        Bird obj = oWorld.oBird;
        TextureRegion keyFrame;

        if (obj.state == Bird.STATE_NORMAL) {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, true);
        } else {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false);
        }
        spriteBatch.draw(keyFrame, obj.position.x - .3f, obj.position.y - .25f, .6f, .5f);
    }

}
