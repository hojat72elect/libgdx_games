package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.objects.Bullet;
import com.nopalsoft.superjumper.objects.Cloud;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Enemy;
import com.nopalsoft.superjumper.objects.Item;
import com.nopalsoft.superjumper.objects.LightningBolt;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Player;
import com.nopalsoft.superjumper.screens.Screens;

public class WorldGameRender {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    WorldGame oWorld;
    SpriteBatch batcher;
    OrthographicCamera camera;
    Box2DDebugRenderer boxRender;

    public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {
        this.oWorld = oWorld;
        this.batcher = batcher;

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        boxRender = new Box2DDebugRenderer();
    }

    public void unprojectToWorldCoords(Vector3 touchPoint) {
        camera.unproject(touchPoint);
    }

    public void render() {
        if (oWorld.state == WorldGame.STATE_RUNNING)
            camera.position.y = oWorld.oPer.position.y;

        if (camera.position.y < Screens.WORLD_HEIGHT / 2f) {
            camera.position.y = Screens.WORLD_HEIGHT / 2f;
        }

        camera.update();
        batcher.setProjectionMatrix(camera.combined);

        batcher.begin();

        renderPersonaje();
        renderPlataformas();
        renderPiezasPlataformas();
        renderCoins();
        renderItems();
        renderEnemigo();
        renderNube();
        renderRayo();
        renderBullet();

        batcher.end();

    }

    private void renderPersonaje() {
        AtlasRegion keyframe;

        Player obj = oWorld.oPer;

        if (obj.speed.y > 0)
            keyframe = Assets.personajeJump;
        else
            keyframe = Assets.personajeStand;

        if (obj.speed.x > 0)
            batcher.draw(keyframe, obj.position.x + Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGHT / 2f,
                    -Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGHT / 2f, -Player.DRAW_WIDTH, Player.DRAW_HEIGHT, 1, 1, obj.angleDegree);

        else
            batcher.draw(keyframe, obj.position.x - Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGHT / 2f,
                    Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGHT / 2f, Player.DRAW_WIDTH, Player.DRAW_HEIGHT, 1, 1, obj.angleDegree);

        if (obj.isJetPack) {
            batcher.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f);

            TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(obj.durationJetPack, true);
            batcher.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f);
        }
        if (obj.isBubble) {
            batcher.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1, 1);
        }
    }

    private void renderPlataformas() {
        for (Platform obj : oWorld.arrPlataformas) {
            AtlasRegion keyframe = null;

            if (obj.type == Platform.TYPE_BREAKABLE) {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.plataformaBeigeBroken;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.plataformaBlueBroken;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.plataformaGrayBroken;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.plataformaGreenBroken;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.plataformaMulticolorBroken;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.plataformaPinkBroken;
                        break;
                }
            } else {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.plataformaBeige;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.plataformaBlue;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.plataformaGray;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.plataformaGreen;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.plataformaMulticolor;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.plataformaPink;
                        break;
                    case Platform.COLOR_BEIGE_LIGHT:
                        keyframe = Assets.plataformaBeigeLight;
                        break;
                    case Platform.COLOR_BLUE_LIGHT:
                        keyframe = Assets.plataformaBlueLight;
                        break;
                    case Platform.COLOR_GRAY_LIGHT:
                        keyframe = Assets.plataformaGrayLight;
                        break;
                    case Platform.COLOR_GREEN_LIGHT:
                        keyframe = Assets.plataformaGreenLight;
                        break;
                    case Platform.COLOR_MULTICOLOR_LIGHT:
                        keyframe = Assets.plataformaMulticolorLight;
                        break;
                    case Platform.COLOR_PINK_LIGHT:
                        keyframe = Assets.plataformaPinkLight;
                        break;
                }
            }
            batcher.draw(keyframe, obj.position.x - Platform.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Platform.DRAW_HEIGHT_NORMAL / 2f,
                    Platform.DRAW_WIDTH_NORMAL, Platform.DRAW_HEIGHT_NORMAL);
        }
    }

    private void renderPiezasPlataformas() {
        for (PlatformPiece obj : oWorld.arrPiezasPlataformas) {
            AtlasRegion keyframe = null;

            if (obj.type == PlatformPiece.TYPE_LEFT) {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.plataformaBeigeLeft;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.plataformaBlueLeft;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.plataformaGrayLeft;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.plataformaGreenLeft;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.plataformaMulticolorLeft;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.plataformaPinkLeft;
                        break;
                }
            } else {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.plataformaBeigeRight;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.plataformaBlueRight;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.plataformaGrayRight;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.plataformaGreenRight;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.plataformaMulticolorRight;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.plataformaPinkRight;
                        break;
                }
            }

            batcher.draw(keyframe, obj.position.x - PlatformPiece.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PlatformPiece.DRAW_HEIGHT_NORMAL
                            / 2f, PlatformPiece.DRAW_WIDTH_NORMAL / 2f, PlatformPiece.DRAW_HEIGHT_NORMAL / 2f, PlatformPiece.DRAW_WIDTH_NORMAL,
                    PlatformPiece.DRAW_HEIGHT_NORMAL, 1, 1, obj.angleDegree);
        }
    }

    private void renderCoins() {
        for (Coin obj : oWorld.arrMonedas) {
            batcher.draw(Assets.coin, obj.position.x - Coin.DRAW_WIDTH / 2f, obj.position.y - Coin.DRAW_HEIGHT / 2f, Coin.DRAW_WIDTH,
                    Coin.DRAW_HEIGHT);
        }
    }

    private void renderItems() {
        for (Item obj : oWorld.arrItem) {
            TextureRegion keyframe = null;

            switch (obj.tipo) {
                case Item.TIPO_BUBBLE:
                    keyframe = Assets.bubbleSmall;
                    break;
                case Item.TIPO_JETPACK:
                    keyframe = Assets.jetpackSmall;
                    break;
                case Item.TIPO_GUN:
                    keyframe = Assets.gun;
                    break;
            }

            batcher.draw(keyframe, obj.position.x - Item.DRAW_WIDTH / 2f, obj.position.y - Item.DRAW_HEIGHT / 2f, Item.DRAW_WIDTH, Item.DRAW_HEIGHT);
        }
    }

    private void renderEnemigo() {
        for (Enemy obj : oWorld.arrEnemigo) {
            TextureRegion keyframe = Assets.enemigo.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - Enemy.DRAW_WIDTH / 2f, obj.position.y - Enemy.DRAW_HEIGHT / 2f, Enemy.DRAW_WIDTH,
                    Enemy.DRAW_HEIGHT);
        }
    }

    private void renderNube() {
        for (Cloud obj : oWorld.arrNubes) {
            TextureRegion keyframe = null;

            switch (obj.type) {
                case Cloud.TYPE_ANGRY:
                    keyframe = Assets.nubeAngry;
                    break;
                case Cloud.TYPE_HAPPY:
                    keyframe = Assets.nubeHappy;
                    break;
            }

            batcher.draw(keyframe, obj.position.x - Cloud.DRAW_WIDTH / 2f, obj.position.y - Cloud.DRAW_HEIGHT / 2f, Cloud.DRAW_WIDTH, Cloud.DRAW_HEIGHT);

            if (obj.isBlowing) {
                batcher.draw(Assets.nubeViento, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f);
            }
        }
    }

    private void renderRayo() {
        for (LightningBolt obj : oWorld.arrRayos) {
            TextureRegion keyframe = Assets.rayo.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - LightningBolt.DRAW_WIDTH / 2f, obj.position.y - LightningBolt.DRAW_HEIGHT / 2f, LightningBolt.DRAW_WIDTH, LightningBolt.DRAW_HEIGHT);
        }
    }

    private void renderBullet() {
        for (Bullet obj : oWorld.arrBullets) {
            batcher.draw(Assets.bullet, obj.position.x - Bullet.SIZE / 2f, obj.position.y - Bullet.SIZE / 2f, Bullet.SIZE, Bullet.SIZE);
        }
    }
}
