package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.GameScreen
import com.nopalsoft.ponyrace.menuobjetos.BotonNube
import java.util.Random

class WorldMapTiledScreen(game: PonyRacingGame) : BaseScreen(game), GestureListener {
    var tiledRender: OrthogonalTiledMapRenderer
    var unitScale: Float = 1 / 32f

    var arrMundos: Array<Mundos>
    var touchPoint: Vector3

    var CAM_MIN_X: Float
    var CAM_MIN_Y: Float
    var CAM_MAX_X: Float
    var CAM_MAX_Y: Float

    var btBack: BotonNube
    var btTienda: BotonNube

    var btDiffUp: Button?
    var btDiffDown: Button?

    var lblDificultadActual: Label

    var gestureDetector: GestureDetector?
    var oRan: Random = Random()

    var secretWorldBounds: Rectangle
    var secretWorld: Vector2

    init {
        tiledRender = OrthogonalTiledMapRenderer(game.assetsHandler!!.tiledWorldMap, unitScale)
        camera = OrthographicCamera(SCREEN_WIDTH * unitScale, SCREEN_HEIGHT * unitScale)
        camera!!.position.set(SCREEN_WIDTH * unitScale / 2f, SCREEN_HEIGHT * unitScale / 2f, 0f)

        CAM_MIN_X = SCREEN_WIDTH * unitScale / 2f
        CAM_MIN_Y = SCREEN_HEIGHT * unitScale / 2f

        CAM_MAX_X = game.assetsHandler!!.tiledWorldMap!!.properties!!.get<String?>("tamanoMapaX", String::class.java)!!.toInt().toFloat()
        CAM_MAX_X -= SCREEN_WIDTH * unitScale / 2f

        CAM_MAX_Y = game.assetsHandler!!.tiledWorldMap!!.properties.get<String?>("tamanoMapaY", String::class.java)!!.toInt().toFloat()
        CAM_MAX_Y -= SCREEN_HEIGHT * unitScale / 2f

        val x = (oRan.nextFloat() * SCREEN_WIDTH * unitScale - 2) + 2
        val y = (oRan.nextFloat() * SCREEN_HEIGHT * unitScale / 2) + SCREEN_HEIGHT * unitScale / 2 - 1f

        secretWorldBounds = Rectangle(x - 1f, y, 2f, 2f)
        secretWorld = Vector2(x, y)

        touchPoint = Vector3()
        arrMundos = Array<Mundos>()

        inicializarNiveles()

        btBack = BotonNube(assetsHandler.nube!!, "Back", assetsHandler.fontGde!!)
        btBack.setSize(150f, 100f)
        btBack.setPosition(645f, 5f)
        btBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btBack.wasSelected = true
                btBack.addAction(Actions.sequence(Actions.delay(.2f), btBack.accionInicial, Actions.run { game.setScreen(LoadingScreen(game, MainMenuScreen::class.java)) }))
            }
        })

        btTienda = BotonNube(assetsHandler.nube!!, "Shop", assetsHandler.fontGde!!)
        btTienda.setSize(150f, 100f)
        btTienda.setPosition(5f, 5f)

        btTienda.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btTienda.wasSelected = true
                btTienda.addAction(Actions.sequence(Actions.delay(.2f), btTienda.accionInicial, Actions.run { game.setScreen(LoadingScreen(game, ShopScreen::class.java)) }))
            }
        })

        btDiffUp = Button(assetsHandler.btDerUp, assetsHandler.btDerDown)
        btDiffUp!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeDificutad(1)
            }
        })

        btDiffDown = ImageButton(assetsHandler.btIzqUp, assetsHandler.btIzqDown)
        btDiffDown!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeDificutad(-1)
            }
        })

        val lblEstilo = LabelStyle(assetsHandler.fontChco, Color.WHITE)
        lblDificultadActual = Label("", lblEstilo)
        lblDificultadActual.setAlignment(Align.center)

        lblSetDificultad() // Mando llamar con cero para que en el lbl se ponga la dificultad actual;

        val contDif = Table()
        contDif.setPosition(SCREEN_WIDTH / 2f, 40f)

        contDif.add<Button?>(btDiffDown)
        contDif.add<Label?>(lblDificultadActual).width(180f).center()
        contDif.add<Button?>(btDiffUp)

        // contDif.debug();
        stage.addActor(btTienda)
        stage.addActor(btBack)

        stage.addActor(contDif)

        gestureDetector = GestureDetector(20f, 0.5f, 2f, 0.15f, this)
        val input = InputMultiplexer(stage, gestureDetector, this)
        Gdx.input.inputProcessor = input
    }

    private fun inicializarNiveles() {
        val layer = game!!.assetsHandler!!.tiledWorldMap!!.layers.get("animaciones")
        if (layer == null) {
            Gdx.app.log("", "layer animaciones no existe")
            return
        }

        val objects = layer.objects
        for (`object` in objects) {
            val properties = `object`.properties
            val level = properties.get("level", String::class.java)!!.toInt()

            val rectangle = (`object` as RectangleMapObject).rectangle
            val x = (rectangle.x + rectangle.width * 0.5f) * unitScale
            val y = (rectangle.y - rectangle.height * 0.5f) * unitScale

            arrMundos.add(Mundos(Vector2(x, y), Rectangle(x - .75f, y - .85f, 1.5f, 1.5f), level))
        }

        arrMundos.sort(object : Comparator<Mundos> {
            override fun compare(o1: Mundos, o2: Mundos): Int {
                if (o1.level > o2.level) return 1
                return -1
            }
        })
    }

    override fun update(delta: Float) {
    }

    /**
     * Cambia la dificultad si recibe +1 se increment la dificultad y en caso de llegar al final pues le da la vuelta y se regresa al facil. Si recibe un -1 se decrementa la difucltad y en caso de
     * llegar al inicio le da la vuelta y se pone en superHard
     */
    fun changeDificutad(cambio: Int) {
        if (Settings.difficultyLevel + cambio > Settings.DIFFICULTY_VERY_HARD) Settings.difficultyLevel = Settings.DIFFICULTY_EASY
        else if (Settings.difficultyLevel + cambio < Settings.DIFFICULTY_EASY) Settings.difficultyLevel = Settings.DIFFICULTY_VERY_HARD
        else Settings.difficultyLevel += cambio

        lblSetDificultad()
    }

    fun lblSetDificultad() {
        when (Settings.difficultyLevel) {
            Settings.DIFFICULTY_EASY -> {
                lblDificultadActual.setText("Easy")
                lblDificultadActual.style.fontColor = Color.GREEN
            }

            Settings.DIFFICULTY_NORMAL -> {
                lblDificultadActual.setText("Normal")
                lblDificultadActual.style.fontColor = Color.YELLOW
            }

            Settings.DIFFICULTY_HARD -> {
                lblDificultadActual.setText("Hard")
                lblDificultadActual.style.fontColor = Color.ORANGE
            }

            Settings.DIFFICULTY_VERY_HARD -> {
                lblDificultadActual.setText("20% Cooler")
                lblDificultadActual.style.fontColor = Color.RED
            }
        }
    }

    fun changeToGameTiledScreen(level: Int) {
        game!!.assetsHandler!!.unLoadMenus()
        game!!.setScreen(LoadingScreen(game!!, GameScreen::class.java, level))
    }

    override fun draw(delta: Float) {
        if (camera!!.position.x < CAM_MIN_X) camera!!.position.x = CAM_MIN_X
        if (camera!!.position.y < CAM_MIN_Y) camera!!.position.y = CAM_MIN_Y
        if (camera!!.position.x > CAM_MAX_X) camera!!.position.x = CAM_MAX_X
        if (camera!!.position.y > CAM_MAX_Y) camera!!.position.y = CAM_MAX_Y

        camera!!.update()
        tiledRender.setView(camera)
        tiledRender.render()

        batch!!.setProjectionMatrix(camera!!.combined)
        batch!!.enableBlending()
        batch!!.begin()

        renderRenderMap(delta)

        batch!!.end()

        if (AssetsHandler.drawDebugLines) renderShapes()

        stage.act(delta)
        stage.draw()
    }

    private fun renderRenderMap(delta: Float) {
        for (i in 0..<Settings.numberOfGameLevelsUnlocked) {
            val x = arrMundos.get(i).position.x
            val y = arrMundos.get(i).position.y

            assetsHandler.bolaAnim!!.apply(assetsHandler.bolaSkeleton, screenLastStateTime, ScreenStateTime, true, null)
            assetsHandler.bolaSkeleton!!.setX(x)
            assetsHandler.bolaSkeleton!!.setY(y - .5f)
            assetsHandler.bolaSkeleton!!.updateWorldTransform()
            assetsHandler.bolaSkeleton!!.update(delta)
            skeletonRenderer!!.draw(batch, assetsHandler.bolaSkeleton)

            assetsHandler.fontChco!!.getData().setScale(.0125f)
            assetsHandler.fontChco!!.draw(batch, arrMundos.get(i).level.toString() + "", x - .25f, y + .2f)
            assetsHandler.fontChco!!.getData().setScale(.6f)
        }
        if (Settings.isEnabledSecretWorld) {
            assetsHandler.rayoAnim!!.apply(assetsHandler.rayoSkeleton, screenLastStateTime, ScreenStateTime, true, null)
            assetsHandler.rayoSkeleton!!.setX(secretWorld.x)
            assetsHandler.rayoSkeleton!!.setY(secretWorld.y)
            assetsHandler.rayoSkeleton!!.updateWorldTransform()
            assetsHandler.rayoSkeleton!!.update(delta)
            skeletonRenderer!!.draw(batch, assetsHandler.rayoSkeleton)
        }

        assetsHandler.humoVolvanAnimation!!.apply(assetsHandler.humoVolcanSkeleton, screenLastStateTime, ScreenStateTime, true, null)
        assetsHandler.humoVolcanSkeleton!!.setX(15f)
        assetsHandler.humoVolcanSkeleton!!.setY(10.5f)
        assetsHandler.humoVolcanSkeleton!!.updateWorldTransform()
        assetsHandler.humoVolcanSkeleton!!.update(delta)
        skeletonRenderer!!.draw(batch, assetsHandler.humoVolcanSkeleton)
    }

    override fun show() {
        // TODO Auto-generated method stub
    }

    private fun renderShapes() {
        val render = ShapeRenderer()
        render.setProjectionMatrix(camera!!.combined) // testing propuses

        render.begin(ShapeType.Line)

        for (obj in arrMundos) {
            render.rect(obj.bounds.x, obj.bounds.y, obj.bounds.width, obj.bounds.height)
        }
        if (Settings.isEnabledSecretWorld) {
            render.rect(secretWorldBounds.x, secretWorldBounds.y, secretWorldBounds.width, secretWorldBounds.height)
        }

        render.end()

        render.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            this.game!!.setScreen(LoadingScreen(game!!, MainMenuScreen::class.java))

            return true
        }
        return false
    }

    // Este es el touchDown del gestureListener =)
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        camera!!.unproject(touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        Gdx.app.log("Touch", "X=" + touchPoint.x + " Y=" + touchPoint.y)

        for (obj in arrMundos) {
            if (obj.bounds.contains(touchPoint.x, touchPoint.y)) {
                if (Settings.numberOfGameLevelsUnlocked >= obj.level) changeToGameTiledScreen(obj.level)
                return true
            }
        }
        if (Settings.isEnabledSecretWorld && secretWorldBounds.contains(touchPoint.x, touchPoint.y)) {
            changeToGameTiledScreen(1000)
            Settings.isEnabledSecretWorld = false
            return true
        }
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        val speed = .035f
        camera!!.position.add(-deltaX * speed, deltaY * speed, 0f)
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun pinchStop() {
    }

    class Mundos(var position: Vector2, var bounds: Rectangle, var level: Int)
}
