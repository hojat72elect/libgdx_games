package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game_objects.Item;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyBean;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyCorn;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyJelly;
import com.nopalsoft.ninjarunner.game_objects.ItemCoin;
import com.nopalsoft.ninjarunner.game_objects.ItemEnergy;
import com.nopalsoft.ninjarunner.game_objects.ItemHeart;
import com.nopalsoft.ninjarunner.game_objects.ItemMagnet;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.game_objects.Missile;
import com.nopalsoft.ninjarunner.game_objects.Obstacle;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.game_objects.Platform;
import com.nopalsoft.ninjarunner.game_objects.Player;
import com.nopalsoft.ninjarunner.game_objects.Wall;
import com.nopalsoft.ninjarunner.screens.Screens;

import java.util.Iterator;

public class WorldGameRenderer {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch batch;
    GameWorld gameWorld;
    OrthographicCamera camera;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batch, GameWorld gameWorld) {

        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batch = batch;
        this.gameWorld = gameWorld;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render(float delta) {

        camera.position.set(gameWorld.player.position.x + 1.5f, gameWorld.player.position.y, 0);

        if (camera.position.y < HEIGHT / 2f)
            camera.position.y = HEIGHT / 2f;
        else if (camera.position.y > HEIGHT / 2f)
            camera.position.y = HEIGHT / 2f;

        if (camera.position.x < WIDTH / 2f)
            camera.position.x = WIDTH / 2f;

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.enableBlending();

        renderPlataformas();
        renderPared();

        renderItems();

        renderPersonaje(delta);
        renderMascota(delta);

        renderObstaculos(delta);
        renderMissil(delta);

        batch.end();

        // renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private void renderItems() {

        Iterator<Item> i = gameWorld.arrItems.iterator();
        while (i.hasNext()) {
            Item obj = i.next();

            Sprite spriteFrame = null;

            if (obj.state == ItemCoin.STATE_NORMAL) {
                if (obj instanceof ItemCoin) {
                    spriteFrame = Assets.coinAnimation.getKeyFrame(obj.stateTime, true);
                } else if (obj instanceof ItemMagnet) {
                    spriteFrame = Assets.magnet;
                } else if (obj instanceof ItemEnergy) {
                    spriteFrame = Assets.energy;
                } else if (obj instanceof ItemHeart) {
                    spriteFrame = Assets.hearth;
                } else if (obj instanceof ItemCandyJelly) {
                    spriteFrame = Assets.jellyRed;
                } else if (obj instanceof ItemCandyBean) {
                    spriteFrame = Assets.beanRed;
                } else if (obj instanceof ItemCandyCorn) {
                    spriteFrame = Assets.candyCorn;
                }
            } else {
                if (obj instanceof ItemCandyJelly) {
                    spriteFrame = Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else if (obj instanceof ItemCandyBean) {
                    spriteFrame = Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else {
                    spriteFrame = Assets.pickUpAnimation.getKeyFrame(obj.stateTime, false);
                }
            }

            if (spriteFrame != null) {
                spriteFrame.setPosition(obj.position.x - obj.WIDTH / 2f, obj.position.y - obj.HEIGHT / 2f);
                spriteFrame.setSize(obj.WIDTH, obj.HEIGHT);
                spriteFrame.draw(batch);
            }
        }
    }

    private void renderPlataformas() {

        Iterator<Platform> i = gameWorld.arrPlataformas.iterator();
        while (i.hasNext()) {
            Platform obj = i.next();

            Sprite spriteFrame;

            spriteFrame = Assets.platform;

            spriteFrame.setPosition(obj.position.x - Platform.WIDTH / 2f, obj.position.y - Platform.HEIGHT / 2f);
            spriteFrame.setSize(Platform.WIDTH, Platform.HEIGHT);
            spriteFrame.draw(batch);
        }
    }

    private void renderMascota(float delta) {
        Mascot oMas = gameWorld.oMascot;

        Sprite spriteFrame;

        float width = oMas.drawWidth;
        float height = oMas.drawHeight;

        if (oMas.mascotType == Mascot.MascotType.BOMB) {
            spriteFrame = Assets.mascotBombFlyAnimation.getKeyFrame(oMas.stateTime, true);
        } else {
            if (gameWorld.player.isDash) {
                spriteFrame = Assets.mascotBirdDashAnimation.getKeyFrame(oMas.stateTime, true);
                width = oMas.dashDrawWidth;
                height = oMas.dashDrawHeight;
            } else
                spriteFrame = Assets.mascotBirdFlyAnimation.getKeyFrame(oMas.stateTime, true);
        }

        spriteFrame.setPosition(oMas.position.x - width + Mascot.RADIUS, gameWorld.oMascot.position.y - height / 2f);
        spriteFrame.setSize(width, height);
        spriteFrame.draw(batch);
    }

    private void renderPared() {

        Iterator<Wall> i = gameWorld.arrPared.iterator();
        while (i.hasNext()) {
            Wall obj = i.next();

            Sprite spriteFrame = Assets.wall;
            spriteFrame.setPosition(obj.position.x - Wall.WIDTH / 2f, obj.position.y - Wall.HEIGHT / 2f);
            spriteFrame.setSize(Wall.WIDTH, Wall.HEIGHT);
            spriteFrame.draw(batch);
        }
    }

    private void renderObstaculos(float delta) {
        Iterator<Obstacle> i = gameWorld.arrObstaculos.iterator();
        while (i.hasNext()) {
            Obstacle obj = i.next();

            if (obj.state == Obstacle.STATE_NORMAL) {

                float width, height;
                Sprite spriteFrame;

                if (obj instanceof ObstacleBoxes4) {
                    width = ObstacleBoxes4.DRAW_WIDTH;
                    height = ObstacleBoxes4.DRAW_HEIGHT;
                    spriteFrame = Assets.boxes4Sprite;
                } else {
                    width = ObstacleBoxes7.DRAW_WIDTH;
                    height = ObstacleBoxes7.DRAW_HEIGHT;
                    spriteFrame = Assets.boxes7Sprite;
                }
                spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
                spriteFrame.setSize(width, height);
                spriteFrame.draw(batch);
            } else {

                obj.effect.draw(batch, delta);
            }
        }
    }

    private void renderMissil(float delta) {
        Iterator<Missile> i = gameWorld.arrMissiles.iterator();
        while (i.hasNext()) {
            Missile obj = i.next();

            Sprite spriteFrame;
            float width, height;

            if (obj.state == Missile.STATE_NORMAL) {
                width = Missile.WIDTH;
                height = Missile.HEIGHT;
                spriteFrame = Assets.missileAnimation.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == Missile.STATE_EXPLODE) {
                width = 1f;
                height = .84f;
                spriteFrame = Assets.explosionAnimation.getKeyFrame(obj.stateTime, false);
            } else
                continue;

            spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
            spriteFrame.setSize(width, height);
            spriteFrame.draw(batch);
        }
    }

    private void renderPersonaje(float delta) {
        Player oPer = gameWorld.player;

        Sprite spriteFrame = null;
        float offsetY = 0;

        AnimationSprite animIdle;
        AnimationSprite animJump;
        AnimationSprite animRun;
        AnimationSprite animSlide;
        AnimationSprite animDash;
        AnimationSprite animHurt;
        AnimationSprite animDizzy;
        AnimationSprite animDead;

        switch (oPer.tipo) {
            case Player.TIPO_GIRL:
                animIdle = Assets.girlIdleAnimation;
                animJump = Assets.girlJumpAnimation;
                animRun = Assets.girlRunAnimation;
                animSlide = Assets.girlSlideAnimation;
                animDash = Assets.girlDashAnimation;
                animHurt = Assets.girlHurtAnimation;
                animDizzy = Assets.girlDizzyAnimation;
                animDead = Assets.girlDeathAnimation;
                break;
            case Player.TIPO_BOY:
                animIdle = Assets.girlIdleAnimation;
                animJump = Assets.girlJumpAnimation;
                animRun = Assets.girlRunAnimation;
                animSlide = Assets.girlSlideAnimation;
                animDash = Assets.girlDashAnimation;
                animHurt = Assets.girlHurtAnimation;
                animDizzy = Assets.girlDizzyAnimation;
                animDead = Assets.girlDeathAnimation;
                break;
            case Player.TIPO_NINJA:
            default:
                animIdle = Assets.ninjaIdleAnimation;
                animJump = Assets.ninjaJumpAnimation;
                animRun = Assets.ninjaRunAnimation;
                animSlide = Assets.ninjaSlideAnimation;
                animDash = Assets.ninjaDashAnimation;
                animHurt = Assets.ninjaHurtAnimation;
                animDizzy = Assets.ninjaDizzyAnimation;
                animDead = Assets.ninjaDeathAnimation;
                break;
        }

        if (oPer.state == Player.STATE_NORMAL) {

            if (oPer.isIdle) {
                spriteFrame = animIdle.getKeyFrame(oPer.stateTime, true);
            } else if (oPer.isJumping) {
                spriteFrame = animJump.getKeyFrame(oPer.stateTime, false);
            } else if (oPer.isSlide) {
                spriteFrame = animSlide.getKeyFrame(oPer.stateTime, true);
            } else if (oPer.isDash) {
                spriteFrame = animDash.getKeyFrame(oPer.stateTime, true);
            } else {
                spriteFrame = animRun.getKeyFrame(oPer.stateTime, true);
            }
            offsetY = .1f;
        } else if (oPer.state == Player.STATE_HURT) {
            spriteFrame = animHurt.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;
        } else if (oPer.state == Player.STATE_DIZZY) {
            spriteFrame = animDizzy.getKeyFrame(oPer.stateTime, true);
            offsetY = .1f;
        } else {
            spriteFrame = animDead.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;
        }

        spriteFrame.setPosition(gameWorld.player.position.x - .75f, gameWorld.player.position.y - .52f - offsetY);
        spriteFrame.setSize(Player.DRAW_WIDTH, Player.DRAW_HEIGHT);
        spriteFrame.draw(batch);
    }
}
