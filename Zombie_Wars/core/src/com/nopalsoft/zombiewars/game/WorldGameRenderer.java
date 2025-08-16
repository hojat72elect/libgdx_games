package com.nopalsoft.zombiewars.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.zombiewars.AnimationSprite;
import com.nopalsoft.zombiewars.Assets;
import com.nopalsoft.zombiewars.Settings;
import com.nopalsoft.zombiewars.objetos.BasePlayer;
import com.nopalsoft.zombiewars.objetos.Bullet;
import com.nopalsoft.zombiewars.objetos.HeroFarmer;
import com.nopalsoft.zombiewars.objetos.HeroForce;
import com.nopalsoft.zombiewars.objetos.HeroLumber;
import com.nopalsoft.zombiewars.objetos.ZombieCuasy;
import com.nopalsoft.zombiewars.objetos.ZombieFrank;
import com.nopalsoft.zombiewars.objetos.ZombieKid;
import com.nopalsoft.zombiewars.objetos.ZombieMummy;
import com.nopalsoft.zombiewars.objetos.ZombiePan;
import com.nopalsoft.zombiewars.screens.Screens;

public class WorldGameRenderer {

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch batcher;
    GameWorld oWorld;
    OrthographicCamera oCam;
    OrthogonalTiledMapRenderer tiledRender;

    Box2DDebugRenderer renderBox;

    TiledMapTileLayer map1;
    TiledMapTileLayer map2;
    TiledMapTileLayer map3;
    TiledMapTileLayer map4;

    TiledMapTileLayer mapInFront;// Enfrente del mono

    public WorldGameRenderer(SpriteBatch batcher, GameWorld oWorld) {

        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batcher = batcher;
        this.oWorld = oWorld;
        this.renderBox = new Box2DDebugRenderer();
        tiledRender = new OrthogonalTiledMapRenderer(Assets.map, oWorld.getUnitScale());

        /*
         * Entre mas chico el numero se renderean primero.
         */
        map1 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("1");
        map2 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("2");
        map3 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("3");
        map4 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("4");
        mapInFront = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("inFront");
    }

    public void render() {
        oCam.zoom = Settings.zoom;
        oCam.position.x = oWorld.getPosCamara().x;
        oCam.position.y = oWorld.getPosCamara().y;

        oCam.update();

        drawTiled();

        batcher.setProjectionMatrix(oCam.combined);
        batcher.begin();
        batcher.enableBlending();
        drawFacingRight();
        drawMalos();
        drawBullets();
        batcher.end();

        drawTiledInfront();

    }

    private void drawTiledInfront() {

        tiledRender.setView(oCam);

        tiledRender.getBatch().begin();
        if (mapInFront != null)
            tiledRender.renderTileLayer(mapInFront);
        tiledRender.getBatch().end();
    }

    private void drawTiled() {
        tiledRender.setView(oCam);
        tiledRender.getBatch().begin();
        if (map1 != null)
            tiledRender.renderTileLayer(map1);
        if (map2 != null)
            tiledRender.renderTileLayer(map2);
        if (map3 != null)
            tiledRender.renderTileLayer(map3);
        if (map4 != null)
            tiledRender.renderTileLayer(map4);

        tiledRender.getBatch().end();
    }

    private void drawMalos() {

        for (BasePlayer obj : oWorld.getArrFacingLeft()) {

            AnimationSprite animWalk = null;
            AnimationSprite animAttack = null;
            AnimationSprite animDie = null;
            Sprite spriteHurt = null;

            float ajusteY = 0;
            float size = 0;

            if (obj instanceof ZombieKid) {
                animWalk = Assets.zombieKidWalk;
                animAttack = Assets.zombieKidAttack;
                animDie = Assets.zombieKidDie;
                spriteHurt = Assets.zombieKidHurt;
                ajusteY = -.033f;
                size = .8f;
            } else if (obj instanceof ZombieCuasy) {
                animWalk = Assets.zombieCuasyWalk;
                animAttack = Assets.zombieCuasyAttack;
                animDie = Assets.zombieCuasyDie;
                spriteHurt = Assets.zombieCuasyHurt;
                ajusteY = -.035f;
                size = .8f;
            } else if (obj instanceof ZombieMummy) {
                animWalk = Assets.zombieMummyWalk;
                animAttack = Assets.zombieMummyAttack;
                animDie = Assets.zombieMummyDie;
                spriteHurt = Assets.zombieMummyHurt;
                ajusteY = -.035f;
                size = .8f;
            } else if (obj instanceof ZombiePan) {
                animWalk = Assets.zombiePanWalk;
                animAttack = Assets.zombiePanAttack;
                animDie = Assets.zombiePanDie;
                spriteHurt = Assets.zombiePanHurt;
                ajusteY = -.038f;
                size = .8f;
            } else if (obj instanceof ZombieFrank) {
                animWalk = Assets.zombieFrankWalk;
                animAttack = Assets.zombieFrankAttack;
                animDie = Assets.zombieFrankDie;
                spriteHurt = Assets.zombieFrankHurt;
                ajusteY = -.033f;
                size = .8f;
            } else if (obj instanceof HeroForce) {
                animWalk = Assets.heroForceWalk;
                animAttack = Assets.heroForceShoot;
                animDie = Assets.heroForceDie;
                spriteHurt = Assets.heroForceHurt;
                size = .7f;
            }

            Sprite spriteFrame = null;

            if (obj.state == BasePlayer.STATE_NORMAL) {
                spriteFrame = animWalk.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == BasePlayer.STATE_ATTACK) {
                spriteFrame = animAttack.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == BasePlayer.STATE_DEAD) {
                spriteFrame = animDie.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == BasePlayer.STATE_HURT) {
                spriteFrame = spriteHurt;
            }

            // facing left
            spriteFrame.setPosition(obj.position.x + .29f, obj.position.y - .34f + ajusteY);
            spriteFrame.setSize(-size, size);
            spriteFrame.draw(batcher);
        }
    }

    private void drawFacingRight() {

        for (BasePlayer obj : oWorld.getArrFacingRight()) {

            AnimationSprite animWalk = null;
            AnimationSprite animAttack = null;
            AnimationSprite animDie = null;
            Sprite spriteHurt = null;

            float ajusteY = 0, ajusteX = 0;
            float sizeX = 0, sizeY = 0;

            if (obj instanceof HeroForce) {
                animWalk = Assets.heroForceWalk;
                animAttack = Assets.heroForceShoot;
                animDie = Assets.heroForceDie;
                spriteHurt = Assets.heroForceHurt;
                sizeX = sizeY = .7f;
            } else if (obj instanceof HeroFarmer) {
                animWalk = Assets.heroFarmerWalk;
                animAttack = Assets.heroFarmerShoot;
                animDie = Assets.heroFarmerDie;
                spriteHurt = Assets.heroFarmerHurt;
                sizeX = sizeY = .7f;
            } else if (obj instanceof HeroLumber) {
                animWalk = Assets.heroLumberWalk;
                animAttack = Assets.heroLumberShoot;
                animDie = Assets.heroLumberDie;
                spriteHurt = Assets.heroLumberHurt;
                sizeX = 1f;
                sizeY = .85f;
                ajusteX = -.15f;
            }

            Sprite spriteFrame = null;

            if (obj.state == BasePlayer.STATE_NORMAL) {
                spriteFrame = animWalk.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == BasePlayer.STATE_ATTACK) {
                spriteFrame = animAttack.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == BasePlayer.STATE_DEAD) {
                spriteFrame = animDie.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == BasePlayer.STATE_HURT) {
                spriteFrame = spriteHurt;
            }

            spriteFrame.setPosition(obj.position.x - .29f + ajusteX, obj.position.y - .34f + ajusteY);
            spriteFrame.setSize(sizeX, sizeY);
            spriteFrame.draw(batcher);
        }
    }

    private void drawBullets() {
        for (Bullet obj : oWorld.getArrBullets()) {
            if (!obj.isVisible)
                continue;

            AnimationSprite animBullet = Assets.bullet1;

            if (obj.state == Bullet.STATE_DESTROY)
                continue;

            // BALA
            {
                Sprite spriteFrame = animBullet.getKeyFrame(obj.stateTime, false);

                if (obj.isFacingLeft) {
                    spriteFrame.setPosition(obj.position.x + .1f, obj.position.y - .1f);
                    spriteFrame.setSize(-.2f, .2f);
                    spriteFrame.draw(batcher);
                } else {
                    spriteFrame.setPosition(obj.position.x - .1f, obj.position.y - .1f);
                    spriteFrame.setSize(.2f, .2f);
                    spriteFrame.draw(batcher);
                }
            }

            // MUZZLE FIRE
            if (obj.state == Bullet.STATE_MUZZLE) {
                Sprite spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false);
                if (obj.isFacingLeft) {
                    spriteFrame.setPosition(obj.oPerWhoFired.position.x + .1f - .42f, obj.oPerWhoFired.position.y - .1f - .14f);
                    spriteFrame.setSize(-.2f, .2f);
                } else {
                    spriteFrame.setPosition(obj.oPerWhoFired.position.x - .1f + .42f, obj.oPerWhoFired.position.y - .1f - .14f);
                    spriteFrame.setSize(.2f, .2f);
                }
                spriteFrame.draw(batcher);
            }

            // MUZZLE HIT
            if (obj.state == Bullet.STATE_HIT) {
                Sprite spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false);
                if (obj.isFacingLeft) { // Aqui es lo mismo que muzzle fire pero alreves
                    spriteFrame.setPosition(obj.position.x - .1f, obj.position.y - .1f);
                    spriteFrame.setSize(.2f, .2f);
                } else {
                    spriteFrame.setPosition(obj.position.x + .1f, obj.position.y - .1f);
                    spriteFrame.setSize(-.2f, .2f);
                }
                spriteFrame.draw(batcher);
            }
        }
    }
}
