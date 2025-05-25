package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.objetos.Item;
import com.nopalsoft.ninjarunner.objetos.ItemCandyBean;
import com.nopalsoft.ninjarunner.objetos.ItemCandyCorn;
import com.nopalsoft.ninjarunner.objetos.ItemCandyJelly;
import com.nopalsoft.ninjarunner.objetos.ItemEnergy;
import com.nopalsoft.ninjarunner.objetos.ItemHearth;
import com.nopalsoft.ninjarunner.objetos.ItemMagnet;
import com.nopalsoft.ninjarunner.objetos.ItemMoneda;
import com.nopalsoft.ninjarunner.objetos.Mascot;
import com.nopalsoft.ninjarunner.objetos.Missil;
import com.nopalsoft.ninjarunner.objetos.Obstaculo;
import com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4;
import com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7;
import com.nopalsoft.ninjarunner.objetos.Pared;
import com.nopalsoft.ninjarunner.objetos.Plataforma;
import com.nopalsoft.ninjarunner.objetos.Player;
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

            if (obj.state == ItemMoneda.STATE_NORMAL) {
                if (obj instanceof ItemMoneda) {
                    spriteFrame = Assets.moneda.getKeyFrame(obj.stateTime, true);
                } else if (obj instanceof ItemMagnet) {
                    spriteFrame = Assets.magnet;
                } else if (obj instanceof ItemEnergy) {
                    spriteFrame = Assets.energy;
                } else if (obj instanceof ItemHearth) {
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
                    spriteFrame = Assets.pick.getKeyFrame(obj.stateTime, false);
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

        Iterator<Plataforma> i = gameWorld.arrPlataformas.iterator();
        while (i.hasNext()) {
            Plataforma obj = i.next();

            Sprite spriteFrame;

            spriteFrame = Assets.plataforma;

            spriteFrame.setPosition(obj.position.x - Plataforma.WIDTH / 2f, obj.position.y - Plataforma.HEIGHT / 2f);
            spriteFrame.setSize(Plataforma.WIDTH, Plataforma.HEIGHT);
            spriteFrame.draw(batch);
        }
    }

    private void renderMascota(float delta) {
        Mascot oMas = gameWorld.oMascot;

        Sprite spriteFrame;

        float width = oMas.drawWidth;
        float height = oMas.drawHeight;

        if (oMas.mascotType == Mascot.MascotType.BOMB) {
            spriteFrame = Assets.MascotaBombFly.getKeyFrame(oMas.stateTime, true);
        } else {
            if (gameWorld.player.isDash) {
                spriteFrame = Assets.Mascota1Dash.getKeyFrame(oMas.stateTime, true);
                width = oMas.dashDrawWidth;
                height = oMas.dashDrawHeight;
            } else
                spriteFrame = Assets.Mascota1Fly.getKeyFrame(oMas.stateTime, true);
        }

        spriteFrame.setPosition(oMas.position.x - width + Mascot.RADIUS, gameWorld.oMascot.position.y - height / 2f);
        spriteFrame.setSize(width, height);
        spriteFrame.draw(batch);
    }

    private void renderPared() {

        Iterator<Pared> i = gameWorld.arrPared.iterator();
        while (i.hasNext()) {
            Pared obj = i.next();

            Sprite spriteFrame = Assets.pared;
            spriteFrame.setPosition(obj.position.x - Pared.WIDTH / 2f, obj.position.y - Pared.HEIGHT / 2f);
            spriteFrame.setSize(Pared.WIDTH, Pared.HEIGHT);
            spriteFrame.draw(batch);
        }
    }

    private void renderObstaculos(float delta) {
        Iterator<Obstaculo> i = gameWorld.arrObstaculos.iterator();
        while (i.hasNext()) {
            Obstaculo obj = i.next();

            if (obj.state == Obstaculo.STATE_NORMAL) {

                float width, height;
                Sprite spriteFrame;

                if (obj instanceof ObstaculoCajas4) {
                    width = ObstaculoCajas4.DRAW_WIDTH;
                    height = ObstaculoCajas4.DRAW_HEIGHT;
                    spriteFrame = Assets.cajas4;
                } else {
                    width = ObstaculoCajas7.DRAW_WIDTH;
                    height = ObstaculoCajas7.DRAW_HEIGHT;
                    spriteFrame = Assets.cajas7;
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
        Iterator<Missil> i = gameWorld.arrMissiles.iterator();
        while (i.hasNext()) {
            Missil obj = i.next();

            Sprite spriteFrame;
            float width, height;

            if (obj.state == Missil.STATE_NORMAL) {
                width = Missil.WIDTH;
                height = Missil.HEIGHT;
                spriteFrame = Assets.missil.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == Missil.STATE_EXPLODE) {
                width = 1f;
                height = .84f;
                spriteFrame = Assets.explosion.getKeyFrame(obj.stateTime, false);
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
                animIdle = Assets.personajeIdle;
                animJump = Assets.personajeJump;
                animRun = Assets.personajeRun;
                animSlide = Assets.personajeSlide;
                animDash = Assets.personajeDash;
                animHurt = Assets.personajeHurt;
                animDizzy = Assets.personajeDizzy;
                animDead = Assets.personajeDead;
                break;
            case Player.TIPO_BOY:
                animIdle = Assets.personajeIdle;
                animJump = Assets.personajeJump;
                animRun = Assets.personajeRun;
                animSlide = Assets.personajeSlide;
                animDash = Assets.personajeDash;
                animHurt = Assets.personajeHurt;
                animDizzy = Assets.personajeDizzy;
                animDead = Assets.personajeDead;
                break;
            case Player.TIPO_NINJA:
            default:
                animIdle = Assets.ninjaIdle;
                animJump = Assets.ninjaJump;
                animRun = Assets.ninjaRun;
                animSlide = Assets.ninjaSlide;
                animDash = Assets.ninjaDash;
                animHurt = Assets.ninjaHurt;
                animDizzy = Assets.ninjaDizzy;
                animDead = Assets.ninjaDead;
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
