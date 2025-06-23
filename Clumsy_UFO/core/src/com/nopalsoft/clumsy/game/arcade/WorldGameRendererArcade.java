package com.nopalsoft.clumsy.game.arcade;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.objects.Meteoro;
import com.nopalsoft.clumsy.objects.Meteoro1;
import com.nopalsoft.clumsy.objects.Meteoro2;
import com.nopalsoft.clumsy.objects.Meteoro3;
import com.nopalsoft.clumsy.objects.Meteoro4;
import com.nopalsoft.clumsy.objects.Meteoro5;
import com.nopalsoft.clumsy.objects.Meteoro6;
import com.nopalsoft.clumsy.objects.Tail;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.Screens;

import java.util.Iterator;

public class WorldGameRendererArcade {

    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

    SpriteBatch batcher;
    WorldGameArcade oWorld;
    OrthographicCamera OrthoCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRendererArcade(SpriteBatch batcher, WorldGameArcade oWorld) {

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
        drawMeteoro(delta);
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
        } else {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false);
        }
        batcher.draw(keyFrame, obj.position.x - .25f, obj.position.y - .2f, .25f, .2f, .5f, .4f, 1, 1,
                (float) Math.toDegrees(obj.angleRad));
    }

    private void drawMeteoro(float delta) {
        Iterator<Meteoro> i = oWorld.arrMeteoros.iterator();
        while (i.hasNext()) {
            Meteoro obj = i.next();

            if (obj instanceof Meteoro1) {
                batcher.draw(Assets.meteor1, obj.position.x - .07f, obj.position.y - .07f, .07f, .07f, .14f, .14f, 1,
                        1, obj.angleDeg);
            } else if (obj instanceof Meteoro2) {
                batcher.draw(Assets.meteor2, obj.position.x - .11f, obj.position.y - .11f, .11f, .11f, .22f, .22f, 1,
                        1, obj.angleDeg);
            } else if (obj instanceof Meteoro3) {
                batcher.draw(Assets.meteor3, obj.position.x - .14f, obj.position.y - .13f, .14f, .13f, .18f, .26f, 1,
                        1, obj.angleDeg);
            } else if (obj instanceof Meteoro4) {
                batcher.draw(Assets.meteor4, obj.position.x - .15f, obj.position.y - .15f, .15f, .15f, .3f, .3f, 1, 1,
                        obj.angleDeg);
            } else if (obj instanceof Meteoro5) {
                batcher.draw(Assets.meteor5, obj.position.x - .24f, obj.position.y - .21f, .24f, .21f, .48f, .42f, 1,
                        1, obj.angleDeg);
            } else if (obj instanceof Meteoro6) {
                batcher.draw(Assets.meteor6, obj.position.x - .28f, obj.position.y - .27f, .28f, .27f, .56f, .54f, 1,
                        1, obj.angleDeg);
            }
        }
    }

    private void renderBackground(float delta) {
        // batcher.draw(Assets.fondo, 0, 0, WIDTH, HEIGHT);

    }
}
