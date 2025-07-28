package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.ponyrace.AssetsHandler;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.GameScreen;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;

import java.util.Comparator;
import java.util.Random;

public class WorldMapTiledScreen extends BaseScreen implements GestureListener {

    OrthogonalTiledMapRenderer tiledRender;
    float unitScale = 1 / 32f;

    Array<Mundos> arrMundos;
    Vector3 touchPoint;

    float CAM_MIN_X;
    float CAM_MIN_Y;
    float CAM_MAX_X;
    float CAM_MAX_Y;

    BotonNube btBack;
    BotonNube btTienda;

    Button btDiffUp;
    Button btDiffDown;

    Label lblDificultadActual;

    GestureDetector gestureDetector;
    Random oRan;

    Rectangle secretWorldBounds;
    Vector2 secretWorld;

    public WorldMapTiledScreen(final PonyRacingGame game) {
        super(game);
        oRan = new Random();
        tiledRender = new OrthogonalTiledMapRenderer(game.assetsHandler.tiledWorldMap, unitScale);
        camera = new OrthographicCamera(SCREEN_WIDTH * unitScale, SCREEN_HEIGHT * unitScale);
        camera.position.set(SCREEN_WIDTH * unitScale / 2f, SCREEN_HEIGHT * unitScale / 2f, 0);

        CAM_MIN_X = SCREEN_WIDTH * unitScale / 2f;
        CAM_MIN_Y = SCREEN_HEIGHT * unitScale / 2f;

        CAM_MAX_X = Integer.parseInt(game.assetsHandler.tiledWorldMap.getProperties().get("tamanoMapaX", String.class));
        CAM_MAX_X -= SCREEN_WIDTH * unitScale / 2f;

        CAM_MAX_Y = Integer.parseInt(game.assetsHandler.tiledWorldMap.getProperties().get("tamanoMapaY", String.class));
        CAM_MAX_Y -= SCREEN_HEIGHT * unitScale / 2f;

        float x = (oRan.nextFloat() * SCREEN_WIDTH * unitScale - 2) + 2;
        float y = (oRan.nextFloat() * SCREEN_HEIGHT * unitScale / 2) + SCREEN_HEIGHT * unitScale / 2 - 1f;

        secretWorldBounds = new Rectangle(x - 1f, y, 2f, 2f);
        secretWorld = new Vector2(x, y);

        touchPoint = new Vector3();
        arrMundos = new Array<>();

        inicializarNiveles();

        btBack = new BotonNube(assetsHandler.nube, "Back", assetsHandler.fontGde);
        btBack.setSize(150, 100);
        btBack.setPosition(645, 5);
        btBack.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btBack.wasSelected = true;
                btBack.addAction(Actions.sequence(Actions.delay(.2f), btBack.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new LoadingScreen(game, MainMenuScreen.class));
                    }
                })));
            }
        });

        btTienda = new BotonNube(assetsHandler.nube, "Shop", assetsHandler.fontGde);
        btTienda.setSize(150, 100);
        btTienda.setPosition(5, 5);

        btTienda.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btTienda.wasSelected = true;
                btTienda.addAction(Actions.sequence(Actions.delay(.2f), btTienda.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new LoadingScreen(game, ShopScreen.class));
                    }
                })));
            }
        });

        btDiffUp = new Button(assetsHandler.btDerUp, assetsHandler.btDerDown);
        btDiffUp.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                changeDificutad(1);
            }
        });

        btDiffDown = new ImageButton(assetsHandler.btIzqUp, assetsHandler.btIzqDown);
        btDiffDown.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                changeDificutad(-1);
            }
        });

        LabelStyle lblEstilo = new LabelStyle(assetsHandler.fontChco, Color.WHITE);
        lblDificultadActual = new Label("", lblEstilo);
        lblDificultadActual.setAlignment(Align.center);

        lblSetDificultad();// Mando llamar con cero para que en el lbl se ponga la dificultad actual;

        Table contDif = new Table();
        contDif.setPosition(SCREEN_WIDTH / 2f, 40);

        contDif.add(btDiffDown);
        contDif.add(lblDificultadActual).width(180).center();
        contDif.add(btDiffUp);

        // contDif.debug();

        stage.addActor(btTienda);
        stage.addActor(btBack);

        stage.addActor(contDif);

        gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, this);
        InputMultiplexer input = new InputMultiplexer(stage, gestureDetector, this);
        Gdx.input.setInputProcessor(input);
    }

    private void inicializarNiveles() {
        MapLayer layer = game.assetsHandler.tiledWorldMap.getLayers().get("animaciones");
        if (layer == null) {
            Gdx.app.log("", "layer animaciones no existe");
            return;
        }

        MapObjects objects = layer.getObjects();
        for (MapObject object : objects) {
            MapProperties properties = object.getProperties();
            int level = Integer.parseInt(properties.get("level", String.class));

            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            float x = (rectangle.x + rectangle.width * 0.5f) * unitScale;
            float y = (rectangle.y - rectangle.height * 0.5f) * unitScale;

            arrMundos.add(new Mundos(new Vector2(x, y), new Rectangle(x - .75f, y - .85f, 1.5f, 1.5f), level));
        }

        arrMundos.sort(new Comparator<Mundos>() {

            @Override
            public int compare(Mundos o1, Mundos o2) {
                if (o1.level > o2.level)
                    return 1;
                return -1;
            }
        });
    }

    @Override
    public void update(float delta) {
    }

    /**
     * Cambia la dificultad si recibe +1 se increment la dificultad y en caso de llegar al final pues le da la vuelta y se regresa al facil. Si recibe un -1 se decrementa la difucltad y en caso de
     * llegar al inicio le da la vuelta y se pone en superHard
     */
    public void changeDificutad(int cambio) {
        if (Settings.difficultyLevel + cambio > Settings.DIFFICULTY_VERY_HARD)
            Settings.difficultyLevel = Settings.DIFFICULTY_EASY;
        else if (Settings.difficultyLevel + cambio < Settings.DIFFICULTY_EASY)
            Settings.difficultyLevel = Settings.DIFFICULTY_VERY_HARD;
        else
            Settings.difficultyLevel += cambio;

        lblSetDificultad();
    }

    public void lblSetDificultad() {
        switch (Settings.difficultyLevel) {
            case Settings.DIFFICULTY_EASY:
                lblDificultadActual.setText("Easy");
                lblDificultadActual.getStyle().fontColor = Color.GREEN;
                break;
            case Settings.DIFFICULTY_NORMAL:
                lblDificultadActual.setText("Normal");
                lblDificultadActual.getStyle().fontColor = Color.YELLOW;
                break;
            case Settings.DIFFICULTY_HARD:
                lblDificultadActual.setText("Hard");
                lblDificultadActual.getStyle().fontColor = Color.ORANGE;
                break;
            case Settings.DIFFICULTY_VERY_HARD:
                lblDificultadActual.setText("20% Cooler");
                lblDificultadActual.getStyle().fontColor = Color.RED;
                break;
        }
    }

    public void changeToGameTiledScreen(int level) {
        game.assetsHandler.unLoadMenus();
        game.setScreen(new LoadingScreen(game, GameScreen.class, level));
    }

    @Override
    public void draw(float delta) {

        if (camera.position.x < CAM_MIN_X)
            camera.position.x = CAM_MIN_X;
        if (camera.position.y < CAM_MIN_Y)
            camera.position.y = CAM_MIN_Y;
        if (camera.position.x > CAM_MAX_X)
            camera.position.x = CAM_MAX_X;
        if (camera.position.y > CAM_MAX_Y)
            camera.position.y = CAM_MAX_Y;

        camera.update();
        tiledRender.setView(camera);
        tiledRender.render();

        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();

        renderRenderMap(delta);

        batch.end();

        if (AssetsHandler.drawDebugLines)
            renderShapes();

        stage.act(delta);
        stage.draw();
    }

    private void renderRenderMap(float delta) {

        for (int i = 0; i < Settings.numberOfGameLevelsUnlocked; i++) {
            float x = arrMundos.get(i).position.x;
            float y = arrMundos.get(i).position.y;

            assetsHandler.bolaAnim.apply(assetsHandler.bolaSkeleton, screenLastStateTime, ScreenStateTime, true, null);
            assetsHandler.bolaSkeleton.setX(x);
            assetsHandler.bolaSkeleton.setY(y - .5f);
            assetsHandler.bolaSkeleton.updateWorldTransform();
            assetsHandler.bolaSkeleton.update(delta);
            skeletonRenderer.draw(batch, assetsHandler.bolaSkeleton);

            assetsHandler.fontChco.getData().setScale(.0125f);
            assetsHandler.fontChco.draw(batch, arrMundos.get(i).level + "", x - .25f, y + .2f);
            assetsHandler.fontChco.getData().setScale(.6f);
        }
        if (Settings.isEnabledSecretWorld) {
            assetsHandler.rayoAnim.apply(assetsHandler.rayoSkeleton, screenLastStateTime, ScreenStateTime, true, null);
            assetsHandler.rayoSkeleton.setX(secretWorld.x);
            assetsHandler.rayoSkeleton.setY(secretWorld.y);
            assetsHandler.rayoSkeleton.updateWorldTransform();
            assetsHandler.rayoSkeleton.update(delta);
            skeletonRenderer.draw(batch, assetsHandler.rayoSkeleton);
        }

        assetsHandler.humoVolvanAnimation.apply(assetsHandler.humoVolcanSkeleton, screenLastStateTime, ScreenStateTime, true, null);
        assetsHandler.humoVolcanSkeleton.setX(15);
        assetsHandler.humoVolcanSkeleton.setY(10.5f);
        assetsHandler.humoVolcanSkeleton.updateWorldTransform();
        assetsHandler.humoVolcanSkeleton.update(delta);
        skeletonRenderer.draw(batch, assetsHandler.humoVolcanSkeleton);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    private void renderShapes() {
        ShapeRenderer render = new ShapeRenderer();
        render.setProjectionMatrix(camera.combined);// testing propuses

        render.begin(ShapeType.Line);

        for (Mundos obj : arrMundos) {
            render.rect(obj.bounds.x, obj.bounds.y, obj.bounds.width, obj.bounds.height);
        }
        if (Settings.isEnabledSecretWorld) {
            render.rect(secretWorldBounds.x, secretWorldBounds.y, secretWorldBounds.width, secretWorldBounds.height);
        }

        render.end();

        render.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            this.game.setScreen(new LoadingScreen(game, MainMenuScreen.class));

            return true;
        }
        return false;
    }

    @Override
    // Este es el touchDown del gestureListener =)
    public boolean touchDown(float x, float y, int pointer, int button) {
        camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Gdx.app.log("Touch", "X=" + touchPoint.x + " Y=" + touchPoint.y);

        for (Mundos obj : arrMundos) {
            if (obj.bounds.contains(touchPoint.x, touchPoint.y)) {
                if (Settings.numberOfGameLevelsUnlocked >= obj.level)
                    changeToGameTiledScreen(obj.level);
                return true;
            }
        }
        if (Settings.isEnabledSecretWorld && secretWorldBounds.contains(touchPoint.x, touchPoint.y)) {

            changeToGameTiledScreen(1000);
            Settings.isEnabledSecretWorld = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float speed = .035f;
        camera.position.add(-deltaX * speed, deltaY * speed, 0);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void pinchStop() {

    }

    protected static class Mundos {
        public Vector2 position;
        public Rectangle bounds;
        public int level;

        public Mundos(Vector2 position, Rectangle bounds, int level) {
            super();
            this.position = position;
            this.bounds = bounds;
            this.level = level;
        }
    }
}
