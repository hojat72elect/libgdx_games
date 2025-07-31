package com.nopalsoft.zombiekiller.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.zombiekiller.AnimationSprite;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.game_objects.Bullet;
import com.nopalsoft.zombiekiller.game_objects.Crate;
import com.nopalsoft.zombiekiller.game_objects.Hero;
import com.nopalsoft.zombiekiller.game_objects.ItemGem;
import com.nopalsoft.zombiekiller.game_objects.ItemHeart;
import com.nopalsoft.zombiekiller.game_objects.ItemMeat;
import com.nopalsoft.zombiekiller.game_objects.ItemShield;
import com.nopalsoft.zombiekiller.game_objects.ItemSkull;
import com.nopalsoft.zombiekiller.game_objects.ItemStar;
import com.nopalsoft.zombiekiller.game_objects.Items;
import com.nopalsoft.zombiekiller.game_objects.Saw;
import com.nopalsoft.zombiekiller.game_objects.Zombie;
import com.nopalsoft.zombiekiller.screens.Screens;

public class WorldGameRenderer2 {

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch batch;
    WorldGame worldGame;
    ParallaxCamera camera;
    OrthogonalTiledMapRenderer tiledRenderer;

    Box2DDebugRenderer physicsDebugRenderer;

    float xMin, xMax, yMin, yMax;

    TiledMapTileLayer map1;
    TiledMapTileLayer map2;
    TiledMapTileLayer map3;
    TiledMapTileLayer map4;

    TiledMapTileLayer mapInFront;// In front of the monkey

    boolean showMoon;

    public WorldGameRenderer2(SpriteBatch batch, WorldGame worldGame) {

        this.camera = new ParallaxCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batch = batch;
        this.worldGame = worldGame;
        this.physicsDebugRenderer = new Box2DDebugRenderer();
        tiledRenderer = new OrthogonalTiledMapRenderer(Assets.map, worldGame.unitScale);

        // The smaller the number, the first they are rendered.
        map1 = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("1");
        map2 = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("2");
        map3 = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("3");
        map4 = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("4");
        mapInFront = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("inFront");

        xMin = 4.0f;// It starts at 4 because the camera is centered not at the origin
        xMax = worldGame.unitScale * worldGame.tiledWidth * 32 - 4;// Minus 4 because the camera is centered on the origin
        yMin = 2.4f;
        yMax = worldGame.unitScale * worldGame.tiledHeight * 32 - 1f;// Here I'm not going to subtract the -2.4, just -1f so that it has a little more freedom when going up.

        showMoon = MathUtils.randomBoolean();
    }

    public void render() {
        camera.position.x = worldGame.hero.position.x;
        camera.position.y = worldGame.hero.position.y;

        // I update the camera so that it doesn't go out of bounds
        if (camera.position.x < xMin)
            camera.position.x = xMin;
        else if (camera.position.x > xMax)
            camera.position.x = xMax;

        if (camera.position.y < yMin)
            camera.position.y = yMin;
        else if (camera.position.y > yMax)
            camera.position.y = yMax;

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.disableBlending();
        drawBackGround();
        batch.end();

        batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.5f, 1));
        batch.begin();
        drawParallaxBackground();
        batch.end();

        if (showMoon) {
            batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.25f, .8f));
            batch.begin();
            batch.enableBlending();
            drawMoon();
            batch.end();
        }

        drawTiled();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.enableBlending();
        drawItems();
        drawCrates();
        drawSaw();
        drawZombie();
        drawBullets();
        drawPlayer();

        batch.end();

        drawTiledInfront();

    }

    private void drawBackGround() {
        batch.draw(Assets.backBackground, camera.position.x - 4f, camera.position.y - 2.4f, 8.0f, 4.8f);
    }

    private void drawParallaxBackground() {
        for (int i = 0; i < 2; i += 1) {
            batch.draw(Assets.background, (-xMin / 2f) + (i * 16), 0, 8.0f, 4.8f);
            batch.draw(Assets.background, (-xMin / 2f) + ((i + 1) * 16), 0, -8.0f, 4.8f);
        }
    }

    private void drawMoon() {
        batch.draw(Assets.moon, 4, 2.3f, 3.5f, 2.55f);
    }

    private void drawTiledInfront() {

        tiledRenderer.setView(camera);

        tiledRenderer.getBatch().begin();
        if (mapInFront != null)
            tiledRenderer.renderTileLayer(mapInFront);
        tiledRenderer.getBatch().end();
    }

    private void drawTiled() {
        tiledRenderer.setView(camera);
        tiledRenderer.getBatch().begin();
        if (map1 != null)
            tiledRenderer.renderTileLayer(map1);
        if (map2 != null)
            tiledRenderer.renderTileLayer(map2);
        if (map3 != null)
            tiledRenderer.renderTileLayer(map3);
        if (map4 != null)
            tiledRenderer.renderTileLayer(map4);

        // tiledRender.render();
        tiledRenderer.getBatch().end();
    }

    private void drawCrates() {

        for (Crate obj : worldGame.crates) {
            float halfSize = obj.SIZE / 2f;
            batch.draw(Assets.crate, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE, obj.SIZE, 1, 1,
                    obj.angleDeg);
        }
    }

    private void drawSaw() {

        for (Saw obj : worldGame.saws) {
            float halfSize = (obj.SIZE + .2f) / 2f;
            batch.draw(Assets.saw, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE + .2f, obj.SIZE + .2f, 1, 1,
                    obj.angleDeg);
        }
    }

    private void drawItems() {
        TextureRegion keyframe = null;

        for (Items obj : worldGame.items) {
            if (obj instanceof ItemGem) {
                keyframe = Assets.itemGem;
            } else if (obj instanceof ItemHeart) {
                keyframe = Assets.itemHeart;
            } else if (obj instanceof ItemMeat) {
                keyframe = Assets.itemMeat;
            } else if (obj instanceof ItemSkull) {
                keyframe = Assets.itemSkull;
            } else if (obj instanceof ItemShield) {
                keyframe = Assets.itemShield;
            } else if (obj instanceof ItemStar) {
                keyframe = Assets.itemStar;
            }

            batch.draw(keyframe, obj.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT);
        }
    }

    private void drawZombie() {

        for (Zombie obj : worldGame.zombies) {

            AnimationSprite animWalk = null;
            AnimationSprite animIdle = null;
            AnimationSprite animRise = null;
            AnimationSprite animDie = null;
            Sprite zombieHurt = null;

            float ajusteY = 0;

            switch (obj.tipo) {
                case Zombie.TIPO_CUASY:
                    animWalk = Assets.zombieCuasyWalk;
                    animIdle = Assets.zombieCuasyIdle;
                    animRise = Assets.zombieCuasyRise;
                    animDie = Assets.zombieCuasyDie;
                    zombieHurt = Assets.zombieCuasyHurt;
                    ajusteY = -.035f;
                    break;

                case Zombie.TIPO_FRANK:
                    animWalk = Assets.zombieFrankWalk;
                    animIdle = Assets.zombieFrankIdle;
                    animRise = Assets.zombieFrankRise;
                    animDie = Assets.zombieFrankDie;
                    zombieHurt = Assets.zombieFrankHurt;
                    ajusteY = -.033f;
                    break;
                case Zombie.TIPO_KID:
                    animWalk = Assets.zombieKidWalk;
                    animIdle = Assets.zombieKidIdle;
                    animRise = Assets.zombieKidRise;
                    animDie = Assets.zombieKidDie;
                    zombieHurt = Assets.zombieKidHurt;
                    break;
                case Zombie.TIPO_MUMMY:
                    animWalk = Assets.zombieMummyWalk;
                    animIdle = Assets.zombieMummyIdle;
                    animRise = Assets.zombieMummyRise;
                    animDie = Assets.zombieMummyDie;
                    zombieHurt = Assets.zombieMummyHurt;
                    ajusteY = -.035f;
                    break;
                case Zombie.TIPO_PAN:
                    animWalk = Assets.zombiePanWalk;
                    animIdle = Assets.zombiePanIdle;
                    animRise = Assets.zombiePanRise;
                    animDie = Assets.zombiePanDie;
                    zombieHurt = Assets.zombiePanHurt;
                    ajusteY = -.038f;
                    break;
            }

            Sprite spriteFrame;

            if (obj.state == Zombie.STATE_NORMAL) {
                if (obj.isWalking)
                    spriteFrame = animWalk.getKeyFrame(obj.stateTime, true);
                else {
                    spriteFrame = animIdle.getKeyFrame(obj.stateTime, true);
                }
            } else if (obj.state == Zombie.STATE_RISE) {
                spriteFrame = animRise.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == Zombie.STATE_DEAD) {
                spriteFrame = animDie.getKeyFrame(obj.stateTime, false);
            } else if (obj.state == Zombie.STATE_HURT) {
                spriteFrame = zombieHurt;
            } else
                spriteFrame = null;

            if (obj.isFacingLeft) {
                spriteFrame.setPosition(obj.position.x + .29f, obj.position.y - .34f + ajusteY);
                spriteFrame.setSize(-.8f, .8f);
                spriteFrame.draw(batch);
            } else {
                spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f + ajusteY);
                spriteFrame.setSize(.8f, .8f);
                spriteFrame.draw(batch);
            }

            // Life bar
            if (obj.vidas > 0 && (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT))
                batch.draw(Assets.redBar, obj.position.x - .33f, obj.position.y + .36f, .65f * ((float) obj.vidas / obj.MAX_LIFE), .075f);
        }
    }

    private void drawBullets() {
        for (Bullet obj : worldGame.bullets) {
            AnimationSprite animBullet = null;

            switch (obj.tipo) {
                case Bullet.LEVEL_0:
                    animBullet = Assets.bullet1;
                    break;
                case Bullet.LEVEL_1:
                    animBullet = Assets.bullet2;
                    break;
                case Bullet.LEVEL_2:
                    animBullet = Assets.bullet3;
                    break;
                case Bullet.LEVEL_3:
                    animBullet = Assets.bullet4;
                    break;
                case Bullet.LEVEL_4_AND_UP:
                    animBullet = Assets.bullet5;
                    break;
            }

            if (obj.state == Bullet.STATE_DESTROY)
                continue;

            // BALA
            {
                Sprite spriteFrame = animBullet.getKeyFrame(obj.stateTime, false);

                if (obj.isFacingLeft) {
                    spriteFrame.setPosition(obj.position.x + .1f, obj.position.y - .1f);
                    spriteFrame.setSize(-.2f, .2f);
                    spriteFrame.draw(batch);
                } else {
                    spriteFrame.setPosition(obj.position.x - .1f, obj.position.y - .1f);
                    spriteFrame.setSize(.2f, .2f);
                    spriteFrame.draw(batch);
                }
            }

            // MUZZLE FIRE
            if (obj.state == Bullet.STATE_MUZZLE) {
                Sprite spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false);
                if (obj.isFacingLeft) {
                    spriteFrame.setPosition(worldGame.hero.position.x + .1f - .42f, worldGame.hero.position.y - .1f - .14f);
                    spriteFrame.setSize(-.2f, .2f);
                } else {
                    spriteFrame.setPosition(worldGame.hero.position.x - .1f + .42f, worldGame.hero.position.y - .1f - .14f);
                    spriteFrame.setSize(.2f, .2f);
                }
                spriteFrame.draw(batch);
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
                spriteFrame.draw(batch);
            }
        }
    }

    private void drawPlayer() {

        Hero obj = worldGame.hero;

        AnimationSprite heroClimb = null;
        AnimationSprite heroDie = null;
        Sprite heroHurt = null;
        Sprite heroIdle = null;
        AnimationSprite heroShoot = null;
        AnimationSprite heroWalk = null;

        switch (obj.type) {
            case Hero.TYPE_FORCE:
                heroClimb = Assets.heroForceClimb;
                heroDie = Assets.heroForceDie;
                heroHurt = Assets.heroForceHurt;
                heroIdle = Assets.heroForceIdle;
                heroShoot = Assets.heroForceShoot;
                heroWalk = Assets.heroForceWalk;
                break;

            case Hero.TYPE_RAMBO:
                heroClimb = Assets.heroRamboClimb;
                heroDie = Assets.heroRamboDie;
                heroHurt = Assets.heroRamboHurt;
                heroIdle = Assets.heroRamboIdle;
                heroShoot = Assets.heroRamboShoot;
                heroWalk = Assets.heroRamboWalk;
                break;
            case Hero.TYPE_SOLDIER:
                heroClimb = Assets.heroSoldierClimb;
                heroDie = Assets.heroSoldierDie;
                heroHurt = Assets.heroSoldierHurt;
                heroIdle = Assets.heroSoldierIdle;
                heroShoot = Assets.heroSoldierShoot;
                heroWalk = Assets.heroSoldierWalk;
                break;
            case Hero.TYPE_SWAT:
                heroClimb = Assets.heroSwatClimb;
                heroDie = Assets.heroSwatDie;
                heroHurt = Assets.heroSwatHurt;
                heroIdle = Assets.heroSwatIdle;
                heroShoot = Assets.heroSwatShoot;
                heroWalk = Assets.heroSwatWalk;
                break;
            case Hero.TYPE_VADER:
                heroClimb = Assets.heroVaderClimb;
                heroDie = Assets.heroVaderDie;
                heroHurt = Assets.heroVaderHurt;
                heroIdle = Assets.heroVaderIdle;
                heroShoot = Assets.heroVaderShoot;
                heroWalk = Assets.heroVaderWalk;
                break;
        }

        Sprite spriteFrame;

        if (obj.state == Hero.STATE_NORMAL) {
            if (obj.isClimbing) {
                spriteFrame = heroClimb.getKeyFrame(obj.stateTime, true);
            } else if (obj.isWalking)
                spriteFrame = heroWalk.getKeyFrame(obj.stateTime, true);
            else if (obj.isFiring) {
                spriteFrame = heroShoot.getKeyFrame(obj.stateTime, true);
            } else {
                spriteFrame = heroIdle;
            }
        } else if (obj.state == Hero.STATE_DEAD) {
            spriteFrame = heroDie.getKeyFrame(obj.stateTime, false);
        } else if (obj.state == Hero.STATE_HURT) {
            spriteFrame = heroHurt;
        } else
            spriteFrame = null;

        // If he is climbing I always draw him on the same side
        if (obj.isClimbing) {
            spriteFrame.setPosition(obj.position.x + .35f, obj.position.y - .34f);
            spriteFrame.setSize(-.7f, .77f);
            spriteFrame.draw(batch);
        } else if (obj.isFacingLeft) {
            spriteFrame.setPosition(obj.position.x + .29f, obj.position.y - .34f);
            spriteFrame.setSize(-.7f, .7f);
            spriteFrame.draw(batch);
        } else {
            spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f);
            spriteFrame.setSize(.7f, .7f);
            spriteFrame.draw(batch);
        }
    }

    static class ParallaxCamera extends OrthographicCamera {
        Matrix4 parallaxView = new Matrix4();
        Matrix4 parallaxCombined = new Matrix4();
        Vector3 tmp = new Vector3();
        Vector3 tmp2 = new Vector3();

        public ParallaxCamera(float viewportWidth, float viewportHeight) {
            super(viewportWidth, viewportHeight);
        }

        public Matrix4 calculateParallaxMatrix(float parallaxX, float parallaxY) {
            update();
            tmp.set(position);
            tmp.x *= parallaxX;
            tmp.y *= parallaxY;

            parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
            parallaxCombined.set(projection);
            Matrix4.mul(parallaxCombined.val, parallaxView.val);
            return parallaxCombined;
        }
    }
}
