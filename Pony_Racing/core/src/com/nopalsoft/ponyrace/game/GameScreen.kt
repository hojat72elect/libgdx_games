package com.nopalsoft.ponyrace.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.Settings.sumarMonedas
import com.nopalsoft.ponyrace.menuobjetos.BotonNube
import com.nopalsoft.ponyrace.scene2d.Ventana
import com.nopalsoft.ponyrace.scene2d.VentanaNextLevel
import com.nopalsoft.ponyrace.scene2d.VentanaPause
import com.nopalsoft.ponyrace.scene2d.VentanaRate
import com.nopalsoft.ponyrace.scene2d.VentanaTimesUp
import com.nopalsoft.ponyrace.scene2d.VentanaTryAgain
import com.nopalsoft.ponyrace.screens.BaseScreen
import com.nopalsoft.ponyrace.screens.LoadingScreen
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen

class GameScreen(game: PonyRacingGame, nivelTiled: Int) : BaseScreen(game) {
    // Variables to deduct coins 1 by 1 when you finish the race and get coins
    val GET_COIN_FOR_TIME_LEFT: Float = .065f
    val COIN_MULTIPLIER_TIME_LEFT: Int
    var time_left_coin: Float = 0f
    var world: TileMapHandler?

    // finish
    var nivelTiled: Int

    var stringMonedasRecolectadas: StringBuilder
    var stringTiempoLeft: StringBuilder
    var lapTime: StringBuilder
    var renderer: WorldTiledRenderer?
    var touchPoint: Vector3
    var state: State
    var jump: Boolean
    var fireBomb: Boolean
    var fireWood: Boolean
    var btIzq: Button
    var btDer: Button
    var btJump: Button
    var btFireBomb: TextButton
    var btFireWood: TextButton
    var btPausa: Button
    var stringMundoActual: String? = null
    var accelX: Float = 0f
    var drawStatsEndRace: Boolean
    var vtRate: VentanaRate
    var vtPause: VentanaPause
    var tamanoBoton: Int = 105
    var btMainMenu: BotonNube? = null
    var btContinue: BotonNube? = null
    var btTryAgain: BotonNube? = null
    var btNextLevel: BotonNube? = null
    var btSonido: ImageButton? = null
    var btMusica: ImageButton? = null

    init {
        Settings.statTimesPlayed++

        world = TileMapHandler(game, nivelTiled)
        state = State.ready
        this.nivelTiled = nivelTiled
        renderer = WorldTiledRenderer(batch!!, world!!)
        touchPoint = Vector3()

        fireWood = false
        jump = fireWood
        fireBomb = jump

        btDer = Button(assetsHandler.padDer)
        btIzq = Button(assetsHandler.padIzq)
        btJump = Button(assetsHandler.btJumpUp, assetsHandler.btJumpDown)

        btPausa = Button(assetsHandler.btPauseUp)

        val txButtonStyleFireBombs = TextButtonStyle(assetsHandler.btBombaUp, assetsHandler.btBombaDown, null, assetsHandler.fontChco)
        val txButtonStyleFireWoods = TextButtonStyle(assetsHandler.btTroncoUp, assetsHandler.btTroncoDown, null, assetsHandler.fontChco)

        btFireBomb = TextButton(Settings.numeroBombas.toString() + "", txButtonStyleFireBombs)
        btFireWood = TextButton(Settings.numeroWoods.toString() + "", txButtonStyleFireWoods)

        setBotonesInterfaz()
        inicializarBotonesMenusInGame()

        lapTime = StringBuilder()

        stringMonedasRecolectadas = StringBuilder()
        stringMonedasRecolectadas.append("0")

        stringTiempoLeft = StringBuilder()
        stringTiempoLeft.append(world!!.tiempoLeft.toInt())

        if (nivelTiled == 1000) {
            stringMundoActual = "Secret world"
        } else {
            stringMundoActual = "World " + nivelTiled
        }
        drawStatsEndRace = false

        when (Settings.timeLevel) {
            1 -> COIN_MULTIPLIER_TIME_LEFT = 1
            2 -> COIN_MULTIPLIER_TIME_LEFT = 2
            3 -> COIN_MULTIPLIER_TIME_LEFT = 3
            4 -> COIN_MULTIPLIER_TIME_LEFT = 4
            5 -> COIN_MULTIPLIER_TIME_LEFT = 5
            0 -> COIN_MULTIPLIER_TIME_LEFT = 0
            else -> COIN_MULTIPLIER_TIME_LEFT = 0
        }

        vtPause = VentanaPause(this)
        vtRate = VentanaRate(this)

        if (!Settings.wasAppRated && Settings.statTimesPlayed % 5 == 0) {
            vtRate.show(stage)
        }
    }

    fun setBotonesInterfaz() {
        stage.clear()

        btJump.setPosition(692f, 10f)
        btJump.setSize(tamanoBoton.toFloat(), tamanoBoton.toFloat())

        btFireBomb.setPosition(584f, 10f)
        btFireBomb.setSize(tamanoBoton.toFloat(), tamanoBoton.toFloat())

        btFireWood.setPosition(692f, 123f)
        btFireWood.setSize(tamanoBoton.toFloat(), tamanoBoton.toFloat())

        btIzq.setPosition(5f, 5f)
        btIzq.setSize(120f, 120f)

        btDer.setPosition(130f, 5f)
        btDer.setSize(120f, 120f)

        btPausa.setSize(45f, 45f)
        btPausa.setPosition(750f, 430f)

        btJump.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                jump = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        btFireBomb.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                fireBomb = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        btFireWood.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                fireWood = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        btDer.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                accelX = 1f
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                accelX = 0f
            }
        })
        btIzq.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                accelX = -1f
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                accelX = 0f
            }
        })

        btPausa.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                setPause()
                super.clicked(event, x, y)
            }
        })

        //        if (Gdx.app.getType() != ApplicationType.Desktop) {
        stage.addActor(btDer)
        stage.addActor(btIzq)
        stage.addActor(btJump)
        if (Settings.numeroBombas > 0) stage.addActor(btFireBomb)
        if (Settings.numeroWoods > 0) stage.addActor(btFireWood)

        //        }
        stage.addActor(btPausa)
    }

    override fun update(delta: Float) {
        if (world == null) return

        when (state) {
            State.running -> updateRunning(delta)
            State.timeUp -> updateTimeUp(delta)
            State.paused -> {}
            State.nextLevel -> updateNextLevel(delta)
            State.tryAgain -> updateTryAgain(delta)
            State.ready -> updateReady()
            else -> updateReady()
        }
    }

    private fun updateReady() {
        if (Gdx.input.isTouched && !vtRate.isVisible) state = State.running
    }

    private fun updateRunning(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) accelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) accelX = 1f

        world!!.update(delta, accelX, jump, fireBomb, fireWood)

        if (world!!.state == TileMapHandler.State.timeUp) {
            setTimeUp()
        } else if (world!!.state == TileMapHandler.State.nextLevel) { // Solo se pone cuando ganas en primer lugar
            setNextLevel()

            if (nivelTiled == Settings.numberOfGameLevelsUnlocked) Settings.numberOfGameLevelsUnlocked++

            if (Settings.numberOfGameLevelsUnlocked > AssetsHandler.mundoMaximo) Settings.numberOfGameLevelsUnlocked = AssetsHandler.mundoMaximo
        } else if (world!!.state == TileMapHandler.State.tryAgain) {
            setTryAgain()
        }

        stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length)
        stringMonedasRecolectadas.append(world!!.oPony!!.monedasRecolectadas)

        stringTiempoLeft.delete(0, stringTiempoLeft.length)
        stringTiempoLeft.append(world!!.tiempoLeft.toInt())

        lapTime.delete(0, lapTime.length)
        lapTime.append("Lap ").append(game!!.formatter!!.format("%.2f", world!!.tiempoLap))

        if (fireBomb) btFireBomb.setText(Settings.numeroBombas.toString() + "")

        if (fireWood) btFireWood.setText(Settings.numeroWoods.toString() + "")
        fireWood = false
        jump = fireWood
        fireBomb = jump

        if (world!!.state == TileMapHandler.State.nextLevel || world!!.state == TileMapHandler.State.tryAgain) {
            if ((world!!.tiempoLeft.toInt()) % 2 == 0 && world!!.oPony!!.monedasRecolectadas % 2 == 0 && (world!!.tiempoLap.toInt() % 2) == 0) Settings.isEnabledSecretWorld = true
        }
    }

    private fun updateTimeUp(delta: Float) {
        world!!.update(delta)
    }

    private fun updateNextLevel(delta: Float) {
        world!!.update(delta)

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true
            giveCoinsAfterfinish(delta)
        }
    }

    private fun updateTryAgain(delta: Float) {
        world!!.update(delta)

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true
            giveCoinsAfterfinish(delta)
        }
    }

    private fun giveCoinsAfterfinish(delta: Float) {
        time_left_coin += delta
        if (Settings.timeLevel > 0 && Settings.difficultyLevel >= Settings.DIFFICULTY_NORMAL && world!!.tiempoLeft > 0 && time_left_coin >= GET_COIN_FOR_TIME_LEFT) {
            time_left_coin -= GET_COIN_FOR_TIME_LEFT
            world!!.tiempoLeft--
            world!!.oPony!!.monedasRecolectadas += COIN_MULTIPLIER_TIME_LEFT

            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length)
            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length)
            stringMonedasRecolectadas.append(world!!.oPony!!.monedasRecolectadas)

            stringTiempoLeft.delete(0, stringTiempoLeft.length)
            stringTiempoLeft.append(world!!.tiempoLeft.toInt())

            sumarMonedas(COIN_MULTIPLIER_TIME_LEFT)
            game!!.assetsHandler!!.playSound(game!!.assetsHandler!!.pickCoin!!)
        }
    }

    override fun draw(delta: Float) {
        var delta = delta
        Gdx.gl.glClearColor(.38f, .77f, .87f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (state == State.paused) delta = 0f

        if (renderer == null) return

        renderer!!.render(delta)
        camera!!.update()
        batch!!.setProjectionMatrix(camera!!.combined)

        batch!!.enableBlending()
        batch!!.begin()
        when (state) {
            State.ready -> presentReady()
            State.running -> presentRunning()
            State.tryAgain, State.paused, State.timeUp, State.nextLevel -> {}
        }

        batch!!.end()

        stage.act()
        stage.draw()
    }

    private fun presentReady() {
        glyphLayout!!.setText(assetsHandler.fontGde, "Touch the screen to start")
        assetsHandler.fontGde!!.draw(batch, "Touch the screen to start", SCREEN_WIDTH / 2f - glyphLayout!!.width / 2f, SCREEN_HEIGHT / 2f - glyphLayout!!.height / 2f)
    }

    private fun presentRunning() {
        val alturaIndicador = 440
        batch!!.draw(assetsHandler.indicador, 150f, alturaIndicador.toFloat(), 500f, 15f)
        batch!!.draw(assetsHandler.lugaresMarco, 0f, 250f, 75f, 164f) // Barra del lado izq que muestra los primeros 3 lugares

        // Para dibujar los lugares donde estan los ponyes en la barra superior
        for (i in 0..<world!!.arrPosiciones.size) {
            val oPony = world!!.arrPosiciones.get(i)

            val textura: AtlasRegion?
            val perfil: AtlasRegion?

            when (oPony.nombreSkin) {
                "Cloud" -> {
                    textura = assetsHandler.indicadorCloud
                    perfil = assetsHandler.perfilRegionCloud
                }

                "Natylol" -> {
                    textura = assetsHandler.indicadorNatylol
                    perfil = assetsHandler.perfilRegionNatylol
                }

                "Ignis" -> {
                    textura = assetsHandler.indicadorIgnis
                    perfil = assetsHandler.perfilRegionIgnis
                }

                "cientifico" -> {
                    textura = assetsHandler.indicadorCientifico
                    perfil = assetsHandler.perfilRegionCientifico
                }

                "LAlba" -> {
                    textura = assetsHandler.indicadorLighthingAlba
                    perfil = assetsHandler.perfilRegionLAlba
                }

                else -> {
                    textura = assetsHandler.indicadorMinion
                    perfil = assetsHandler.perfilRegionEnemigo
                }
            }

            val posocion = 500 / world!!.tamanoMapaX * oPony.position.x + 140
            batch!!.draw(textura, posocion, alturaIndicador.toFloat(), 25f, 25f)

            if (i == 0) {
                batch!!.draw(perfil, 26f, 368f, 45f, 45f)
            }
            if (i == 1) {
                batch!!.draw(perfil, 26f, 310f, 45f, 45f)
            }
            if (i == 2) {
                batch!!.draw(perfil, 26f, 252f, 45f, 45f)
            }
        }

        // Fin

        // Dibujar Monedas
        batch!!.draw(assetsHandler.moneda, 5f, 445f, 30f, 30f)
        assetsHandler.fontChco!!.draw(batch, stringMonedasRecolectadas, 38f, 472f)

        // El mundo actual
        glyphLayout!!.setText(assetsHandler.fontChco, stringMundoActual)
        assetsHandler.fontChco!!.draw(batch, stringMundoActual, SCREEN_WIDTH / 2f - glyphLayout!!.width / 2, (alturaIndicador - 5).toFloat())

        // El tiempo que queda
        glyphLayout!!.setText(assetsHandler.fontChco, stringTiempoLeft)
        assetsHandler.fontChco!!.draw(batch, stringTiempoLeft, SCREEN_WIDTH / 2f - glyphLayout!!.width / 2, (alturaIndicador - 32).toFloat())

        // fin
        assetsHandler.fontChco!!.draw(batch, lapTime, 0f, 225f)

        if (AssetsHandler.drawDebugLines) assetsHandler.fontChco!!.draw(batch, "FPS=" + Gdx.graphics.framesPerSecond, 0f, 190f)
    }

    private fun setTimeUp() {
        state = State.timeUp
        ScreenStateTime = 0f
        stage.clear()
        VentanaTimesUp(this).show(stage)
    }

    fun setNextLevel() {
        state = State.nextLevel
        ScreenStateTime = 0f
        stage.clear()

        btTryAgain!!.setPosition(5f, 5f)
        btNextLevel!!.setPosition(645f, 5f)

        VentanaNextLevel(this).show(stage)

        stage.addActor(btNextLevel)

        if (nivelTiled != 1000)  // Si es el mundo secreto no agrego el try again
            stage.addActor(btTryAgain)
    }

    fun setTryAgain() {
        state = State.tryAgain
        ScreenStateTime = 0f
        stage.clear()

        btTryAgain!!.setPosition(5f, 5f)
        btMainMenu!!.setPosition(645f, 5f)

        VentanaTryAgain(this).show(stage)

        stage.addActor(btMainMenu)
        if (nivelTiled != 1000)  // Si es el mundo secreto no agrego el try again
            stage.addActor(btTryAgain)
    }

    fun setRunning() {
        setBotonesInterfaz()
        state = State.running
    }

    fun setPause() {
        state = State.paused
        stage.clear()
        vtPause.show(stage)
    }

    private fun inicializarBotonesMenusInGame() {
        btMainMenu = BotonNube(assetsHandler.nube!!, "Menu", assetsHandler.fontChco!!)
        btMainMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btMainMenu!!.wasSelected = true
                btMainMenu!!.addAction(Actions.sequence(Actions.delay(.2f), btMainMenu!!.accionInicial, Actions.run(object : Runnable {
                    override fun run() {
                        this@GameScreen.game!!.setScreen(LoadingScreen(game!!, WorldMapTiledScreen::class.java))
                        dispose()
                    }
                })))
            }
        })

        btContinue = BotonNube(assetsHandler.nube!!, "Continue", assetsHandler.fontChco!!)
        btContinue!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btContinue!!.wasSelected = true
                btContinue!!.addAction(Actions.sequence(Actions.delay(.2f), btContinue!!.accionInicial, Actions.run(object : Runnable {
                    override fun run() {
                        setRunning()
                    }
                })))
            }
        })

        btTryAgain = BotonNube(assetsHandler.nube!!, "Try again", assetsHandler.fontChco!!)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btTryAgain!!.wasSelected = true
                btTryAgain!!.addAction(Actions.sequence(Actions.delay(.2f), btTryAgain!!.accionInicial, Actions.run(object : Runnable {
                    override fun run() {
                        this@GameScreen.game!!.setScreen(LoadingScreen(game!!, GameScreen::class.java, nivelTiled))
                        if ((Settings.difficultyLevel == Settings.DIFFICULTY_VERY_HARD) and (state == State.nextLevel)) sumarMonedas((COIN_MULTIPLIER_TIME_LEFT * world!!.tiempoLeft).toInt())
                    }
                })))
            }
        })

        btNextLevel = BotonNube(assetsHandler.nube!!, "Next", assetsHandler.fontChco!!)
        btNextLevel!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btNextLevel!!.wasSelected = true
                btNextLevel!!.addAction(Actions.sequence(Actions.delay(.2f), btNextLevel!!.accionInicial, Actions.run(object : Runnable {
                    override fun run() {
                        this@GameScreen.game!!.setScreen(LoadingScreen(game!!, WorldMapTiledScreen::class.java))
                        if (Settings.difficultyLevel == Settings.DIFFICULTY_VERY_HARD) sumarMonedas((COIN_MULTIPLIER_TIME_LEFT * world!!.tiempoLeft).toInt())

                        dispose()
                    }
                })))
            }
        })

        btSonido = ImageButton(assetsHandler.btSonidoOff, null, assetsHandler.btSonidoON)
        btSonido!!.setChecked(Settings.isSoundOn)
        btSonido!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                super.clicked(event, x, y)
            }
        })

        btMusica = ImageButton(assetsHandler.btMusicaOff, null, assetsHandler.btMusicaON)
        btMusica!!.setChecked(Settings.isMusicOn)
        btMusica!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                assetsHandler.platMusicInGame()
                super.clicked(event, x, y)
            }
        })

        btTryAgain!!.setSize(150f, 100f)
        btMainMenu!!.setSize(150f, 100f)
        btContinue!!.setSize(150f, 100f)
        btNextLevel!!.setSize(150f, 100f)
    }

    override fun show() {
    }

    override fun dispose() {
        super.dispose()

        // Pongo esta condicion porque algunas veces el usuario presiona 2 veces el boton y se llama 2 veces este metodo;
        if (world == null || renderer == null) return

        world!!.oWorldBox.dispose()
        renderer!!.renderBox.dispose()
        assetsHandler.tiledMap!!.dispose()
        renderer!!.tiledRender.dispose()
        renderer = null
        world = null
        game!!.assetsHandler!!.unloadGameScreenTiled()
    }

    override fun hide() {
        super.hide()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.DPAD_DOWN == keycode) renderer!!.OrthoCam.position.y -= 0.1f
        else if (Input.Keys.DPAD_UP == keycode) {
            renderer!!.OrthoCam.position.y += 0.1f
            jump = true
        } else if (Input.Keys.DPAD_LEFT == keycode) renderer!!.OrthoCam.position.x -= 3f
        else if (Input.Keys.DPAD_RIGHT == keycode) renderer!!.OrthoCam.position.x += 3f
        else if (Input.Keys.K == keycode) renderer!!.OrthoCam.position.x -= .1f
        else if (Input.Keys.L == keycode) renderer!!.OrthoCam.position.x += .1f
        else if (Input.Keys.SPACE == keycode) jump = true
        else if (Input.Keys.B == keycode) fireBomb = true
        else if (Input.Keys.N == keycode) fireWood = true
        else if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (state == State.running) setPause()
            else {
                game!!.setScreen(LoadingScreen(game!!, WorldMapTiledScreen::class.java))
                dispose()
            }
            return true
        }

        return super.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.A || keycode == Input.Keys.D) accelX = 0f
        return super.keyUp(keycode)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        this.camera!!.unproject(touchPoint.set(screenX.toFloat(), screenY.toFloat(), 0f))
        Gdx.app.log("X", touchPoint.x.toString() + "")
        Gdx.app.log("Y", touchPoint.y.toString() + "")

        return false
    }

    override fun pause() {
        setPause()
        super.pause()
    }

    enum class State {
        ready, running, paused, timeUp, nextLevel, tryAgain
    }
}
