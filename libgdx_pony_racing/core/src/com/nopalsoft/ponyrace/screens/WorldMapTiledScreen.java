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
import com.nopalsoft.ponyrace.Assets;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.GameScreenTileds;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class WorldMapTiledScreen extends Screens implements GestureListener {

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

    public WorldMapTiledScreen(final MainPonyRace game) {
        super(game);
        oRan = new Random();
        tiledRender = new OrthogonalTiledMapRenderer(game.oAssets.tiledWorldMap, unitScale);
        guiCam = new OrthographicCamera(SCREEN_WIDTH * unitScale, SCREEN_HEIGHT * unitScale);
        guiCam.position.set(SCREEN_WIDTH * unitScale / 2f, SCREEN_HEIGHT * unitScale / 2f, 0);

        CAM_MIN_X = SCREEN_WIDTH * unitScale / 2f;
        CAM_MIN_Y = SCREEN_HEIGHT * unitScale / 2f;

        CAM_MAX_X = Integer.valueOf(game.oAssets.tiledWorldMap.getProperties().get("tamanoMapaX", String.class));
        CAM_MAX_X -= SCREEN_WIDTH * unitScale / 2f;

        CAM_MAX_Y = Integer.valueOf(game.oAssets.tiledWorldMap.getProperties().get("tamanoMapaY", String.class));
        CAM_MAX_Y -= SCREEN_HEIGHT * unitScale / 2f;

        float x = (oRan.nextFloat() * SCREEN_WIDTH * unitScale - 2) + 2;
        float y = (oRan.nextFloat() * SCREEN_HEIGHT * unitScale / 2) + SCREEN_HEIGHT * unitScale / 2 - 1f;

        // Settings.isEnabledSecretWorld = true;
        secretWorldBounds = new Rectangle(x - 1f, y, 2f, 2f);
        secretWorld = new Vector2(x, y);

        touchPoint = new Vector3();
        arrMundos = new Array<Mundos>();

        inicializarNiveles();

        btBack = new BotonNube(oAssets.nube, "Back", oAssets.fontGde);
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

        btTienda = new BotonNube(oAssets.nube, "Shop", oAssets.fontGde);
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

        btDiffUp = new Button(oAssets.btDerUp, oAssets.btDerDown);
        btDiffUp.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                changeDificutad(1);
            }
        });

        btDiffDown = new ImageButton(oAssets.btIzqUp, oAssets.btIzqDown);
        btDiffDown.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                changeDificutad(-1);
            }
        });

        LabelStyle lblEstilo = new LabelStyle(oAssets.fontChco, Color.WHITE);
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
        MapLayer layer = game.oAssets.tiledWorldMap.getLayers().get("animaciones");
        if (layer == null) {
            Gdx.app.log("", "layer animaciones no existe");
            return;
        }

        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();
        while (objectIt.hasNext()) {
            MapObject object = objectIt.next();
            MapProperties properties = object.getProperties();
            int level = Integer.valueOf(properties.get("level", String.class));

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
     *
     * @param cambio
     */
    public void changeDificutad(int cambio) {
        if (Settings.dificultadActual + cambio > Settings.DIFICULTAD_SUPERHARD)
            Settings.dificultadActual = Settings.DIFICULTAD_EASY;
        else if (Settings.dificultadActual + cambio < Settings.DIFICULTAD_EASY)
            Settings.dificultadActual = Settings.DIFICULTAD_SUPERHARD;
        else
            Settings.dificultadActual += cambio;

        lblSetDificultad();
    }

    public void lblSetDificultad() {
        switch (Settings.dificultadActual) {
            case Settings.DIFICULTAD_EASY:
                lblDificultadActual.setText("Easy");
                lblDificultadActual.getStyle().fontColor = Color.GREEN;
                break;
            case Settings.DIFICULTAD_NORMAL:
                lblDificultadActual.setText("Normal");
                lblDificultadActual.getStyle().fontColor = Color.YELLOW;
                break;
            case Settings.DIFICULTAD_HARD:
                lblDificultadActual.setText("Hard");
                lblDificultadActual.getStyle().fontColor = Color.ORANGE;
                break;
            case Settings.DIFICULTAD_SUPERHARD:
                lblDificultadActual.setText("20% Cooler");
                lblDificultadActual.getStyle().fontColor = Color.RED;
                break;
        }
    }

    public void changeToGameTiledScreen(int level) {
        game.oAssets.unLoadMenus();
        game.setScreen(new LoadingScreen(game, GameScreenTileds.class, level));
    }

    @Override
    public void draw(float delta) {

        if (guiCam.position.x < CAM_MIN_X)
            guiCam.position.x = CAM_MIN_X;
        if (guiCam.position.y < CAM_MIN_Y)
            guiCam.position.y = CAM_MIN_Y;
        if (guiCam.position.x > CAM_MAX_X)
            guiCam.position.x = CAM_MAX_X;
        if (guiCam.position.y > CAM_MAX_Y)
            guiCam.position.y = CAM_MAX_Y;

        guiCam.update();
        tiledRender.setView(guiCam);
        tiledRender.render();

        batcher.setProjectionMatrix(guiCam.combined);
        batcher.enableBlending();
        batcher.begin();

        renderRenderMap(delta);

        batcher.end();

        if (Assets.drawDebugLines)
            renderShapes();

        stage.act(delta);
        stage.draw();
    }

    private void renderRenderMap(float delta) {

        for (int i = 0; i < Settings.mundosDesbloqueados; i++) {
            float x = arrMundos.get(i).position.x;
            float y = arrMundos.get(i).position.y;

            oAssets.bolaAnim.apply(oAssets.bolaSkeleton, ScreenlastStatetime, ScreenStateTime, true, null);
            oAssets.bolaSkeleton.setX(x);
            oAssets.bolaSkeleton.setY(y - .5f);
            oAssets.bolaSkeleton.updateWorldTransform();
            oAssets.bolaSkeleton.update(delta);
            skelrender.draw(batcher, oAssets.bolaSkeleton);

            oAssets.fontChco.getData().setScale(.0125f);
            oAssets.fontChco.draw(batcher, arrMundos.get(i).level + "", x - .25f, y + .2f);
            oAssets.fontChco.getData().setScale(.6f);
        }
        if (Settings.isEnabledSecretWorld) {
            oAssets.rayoAnim.apply(oAssets.rayoSkeleton, ScreenlastStatetime, ScreenStateTime, true, null);
            oAssets.rayoSkeleton.setX(secretWorld.x);
            oAssets.rayoSkeleton.setY(secretWorld.y);
            oAssets.rayoSkeleton.updateWorldTransform();
            oAssets.rayoSkeleton.update(delta);
            skelrender.draw(batcher, oAssets.rayoSkeleton);
        }

        oAssets.humoVolvanAnimation.apply(oAssets.humoVolcanSkeleton, ScreenlastStatetime, ScreenStateTime, true, null);
        oAssets.humoVolcanSkeleton.setX(15);
        oAssets.humoVolcanSkeleton.setY(10.5f);
        oAssets.humoVolcanSkeleton.updateWorldTransform();
        oAssets.humoVolcanSkeleton.update(delta);
        skelrender.draw(batcher, oAssets.humoVolcanSkeleton);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    private void renderShapes() {
        ShapeRenderer render = new ShapeRenderer();
        render.setProjectionMatrix(guiCam.combined);// testing propuses

        render.begin(ShapeType.Line);

        Iterator<Mundos> it = arrMundos.iterator();
        while (it.hasNext()) {
            Mundos obj = it.next();
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

    protected class Mundos {
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

    @Override
    // Este es el touchDown del gestureListener =)
    public boolean touchDown(float x, float y, int pointer, int button) {
        guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Gdx.app.log("Touch", "X=" + touchPoint.x + " Y=" + touchPoint.y);

        Iterator<Mundos> it = arrMundos.iterator();
        while (it.hasNext()) {
            Mundos obj = it.next();

            if (obj.bounds.contains(touchPoint.x, touchPoint.y)) {
                if (Settings.mundosDesbloqueados >= obj.level)
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
        guiCam.position.add(-deltaX * speed, deltaY * speed, 0);
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
}
