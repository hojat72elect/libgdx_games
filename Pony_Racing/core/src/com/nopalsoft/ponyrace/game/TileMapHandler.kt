package com.nopalsoft.ponyrace.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.Settings.sumarMonedas
import com.nopalsoft.ponyrace.game_objects.Balloons
import com.nopalsoft.ponyrace.game_objects.BloodStone
import com.nopalsoft.ponyrace.game_objects.Bomb
import com.nopalsoft.ponyrace.game_objects.Bonfire
import com.nopalsoft.ponyrace.game_objects.Candy
import com.nopalsoft.ponyrace.game_objects.Chili
import com.nopalsoft.ponyrace.game_objects.Coin
import com.nopalsoft.ponyrace.game_objects.Flag
import com.nopalsoft.ponyrace.game_objects.OpponentPony
import com.nopalsoft.ponyrace.game_objects.Platform
import com.nopalsoft.ponyrace.game_objects.Pony
import com.nopalsoft.ponyrace.game_objects.PonyPlayer
import com.nopalsoft.ponyrace.game_objects.TiledMapManagerBox2d
import com.nopalsoft.ponyrace.game_objects.Wing
import com.nopalsoft.ponyrace.game_objects.Wood
import java.util.Random

class TileMapHandler(@JvmField var game: PonyRacingGame, var nivelTiled: Int) {
    var finJuego: Vector2? = null

    @JvmField
    var oWorldBox: World

    @JvmField
    var m_units: Float = 1 / 100.0f
    var arrBodys: Array<Body>

    @JvmField
    var oPony: PonyPlayer? = null

    @JvmField
    var arrFogatas: Array<Bonfire>

    @JvmField
    var arrPonysMalos: Array<OpponentPony?>

    @JvmField
    var arrPlumas: Array<Wing?>

    @JvmField
    var arrBloodStone: Array<BloodStone>

    @JvmField
    var arrBombas: Array<Bomb?>

    @JvmField
    var arrWoods: Array<Wood?>

    @JvmField
    var arrMonedas: Array<Coin?>

    @JvmField
    var arrChiles: Array<Chili?>

    @JvmField
    var arrGlobos: Array<Balloons?>

    @JvmField
    var arrDulces: Array<Candy?>
    var random: Random

    @JvmField
    var tamanoMapaX: Float = 0f

    @JvmField
    var tamanoMapaY: Float = 0f
    var tiempoLeft: Float = 0f // El tiempo que le queda.
    var tiempoLap: Float = 0f
    var gravedad: Vector2 = Vector2(0f, -9.5f)
    var arrPosiciones: Array<Pony> // Arreglo para ordenar los jugadores
    var state: State?
    var checadorPosiciones: Comparator<Pony> = object : Comparator<Pony> {
        override fun compare(arg0: Pony, arg1: Pony): Int {
            if (arg0.position.x < arg1.position.x) return 1
            return -1
        }
    }
    var impulso: Vector2 = Vector2()

    init {
        val sleep = true
        oWorldBox = World(gravedad, sleep)
        oWorldBox.setContactListener(plataformasContact())
        state = State.running
        random = Random()

        arrFogatas = Array<Bonfire>()
        arrPonysMalos = Array<OpponentPony?>()
        arrPlumas = Array<Wing?>()
        arrBloodStone = Array<BloodStone>()
        arrBombas = Array<Bomb?>()
        arrMonedas = Array<Coin?>()
        arrChiles = Array<Chili?>()
        arrGlobos = Array<Balloons?>()
        arrDulces = Array<Candy?>()
        arrWoods = Array<Wood?>()

        TiledMapManagerBox2d(this, m_units).createObjetosDesdeTiled(game.assetsHandler!!.tiledMap!!)

        arrBodys = Array<Body>()

        oWorldBox.getBodies(arrBodys)

        // ------
        arrPosiciones = Array<Pony>()
        for (obj in arrPonysMalos) {
            arrPosiciones.add(obj)
        }
        arrPosiciones.add(oPony)

        checarPosicionCarrera()
        inicializarVariablesDesdeMapas()
    }

    private fun checarPosicionCarrera() {
        arrPosiciones.sort(checadorPosiciones)

        var posicion = 1
        for (obj in arrPosiciones) {
            obj.lugarEnLaCarrera = posicion
            posicion++
        }
    }

    fun inicializarVariablesDesdeMapas() {
        tamanoMapaX = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tamanoMapaX", String::class.java).toInt().toFloat()
        tamanoMapaX = tamanoMapaX * 16 * m_units

        tamanoMapaY = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tamanoMapaY", String::class.java).toInt().toFloat()
        tamanoMapaY = tamanoMapaY * 16 * m_units

        when (Settings.difficultyLevel) {
            Settings.DIFFICULTY_EASY -> tiempoLeft = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tiempoEasy", String::class.java).toInt().toFloat()
            Settings.DIFFICULTY_NORMAL -> tiempoLeft = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tiempoNormal", String::class.java).toInt().toFloat()
            Settings.DIFFICULTY_HARD -> tiempoLeft = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tiempoHard", String::class.java).toInt().toFloat()
            Settings.DIFFICULTY_VERY_HARD -> tiempoLeft = game.assetsHandler!!.tiledMap!!.properties.get<String?>("tiempoSuperHard", String::class.java).toInt().toFloat()
        }
    }

    @JvmOverloads
    fun update(delta: Float, accelX: Float = 0f, jump: Boolean = false, fireBomb: Boolean = false, fireWood: Boolean = false) {
        oWorldBox.step(delta, 8, 4) // para hacer mas lento el juego 1/300f
        oWorldBox.clearForces()

        // Actualizo
        oPony!!.fireBomb = fireBomb
        oPony!!.fireWood = fireWood

        for (obj in arrBodys) {
            if (obj.userData != null && obj.userData is Pony) {
                updatePonys(delta, obj, accelX, jump)
            } else if (obj.userData != null && obj.userData is Wing) {
                val objWing = (obj.userData as Wing)
                objWing.update(delta)
                if (objWing.state == Wing.State.ACTIVE && !oWorldBox.isLocked) {
                    arrPlumas.removeValue(objWing, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Bomb) {
                val oBomb = (obj.userData as Bomb)
                oBomb.update(delta, obj)
                if (oBomb.state == Bomb.State.EXPLODE && !oWorldBox.isLocked && oBomb.stateTime >= Bomb.TIEMPO_EXPLOSION) {
                    arrBombas.removeValue(oBomb, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Wood) {
                val oWood = (obj.userData as Wood)
                oWood.update(delta, obj)
                if (oWood.state == Wood.State.HIT && !oWorldBox.isLocked && oWood.stateTime >= Bomb.TIEMPO_EXPLOSION) {
                    arrWoods.removeValue(oWood, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Coin) {
                val objMo = (obj.userData as Coin)
                objMo.update(delta)
                if (objMo.state == Coin.State.TAKEN && !oWorldBox.isLocked && objMo.stateTime >= Coin.TIEMPO_TOMADA) {
                    arrMonedas.removeValue(objMo, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Chili) {
                val objMo = (obj.userData as Chili)
                objMo.update(delta)
                if (objMo.state == Chili.State.TAKEN && !oWorldBox.isLocked && objMo.stateTime >= Chili.HIT_ANIMATION_DURATION) {
                    arrChiles.removeValue(objMo, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Balloons) {
                val objMo = (obj.userData as Balloons)
                objMo.update(delta)
                if (objMo.state == Balloons.State.TAKEN && !oWorldBox.isLocked && objMo.stateTime >= Balloons.TAKEN_ANIMATION_DURATION) {
                    arrGlobos.removeValue(objMo, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Candy) {
                val objMo = (obj.userData as Candy)
                objMo.update(delta)
                if (objMo.state == Candy.State.ACTIVE && !oWorldBox.isLocked && objMo.stateTime >= Candy.PICK_UP_DURATION) {
                    arrDulces.removeValue(objMo, true)
                    arrBodys.removeValue(obj, true)
                    oWorldBox.destroyBody(obj)
                }
            } else if (obj.userData != null && obj.userData is Flag) {
                val objBa = (obj.userData as Flag)
                if (objBa.state == Flag.State.ACTIVE && !oWorldBox.isLocked) {
                    oWorldBox.destroyBody(obj)
                }
            }
        }

        /*
         * Las cosas que se actualizan aqui no tienen Box2d para nada
         */
        updateFogatas(delta)
        updateBloodStone(delta)

        checarPosicionCarrera()
        checarGameOver(delta)
    }

    private fun updatePonys(delta: Float, obj: Body, accelX: Float, jump: Boolean) {
        var accelX = accelX
        var jump = jump
        val isMalo: Boolean
        isMalo = obj.userData is OpponentPony

        val ponyDataBody = obj.userData as Pony

        /*
         * PAra disparar
         */
        if (ponyDataBody.fireBomb) {
            lanzarBomba(ponyDataBody)
            ponyDataBody.fireBomb = false
        } else if (ponyDataBody.fireWood) {
            fireWood(ponyDataBody)
            ponyDataBody.fireWood = false
        }

        /*
         * En disparar
         */
        if (ponyDataBody.cayoEnHoyo) {
            obj.setTransform(ponyDataBody.regresoHoyo!!.x, ponyDataBody.regresoHoyo!!.y, 0f)
            ponyDataBody.cayoEnHoyo = false
        }

        if (isMalo || ponyDataBody.pasoLaMeta) { // Esto puede pasar si es malo y si es normal y paso la meta
            if (ponyDataBody.state == Pony.STATE_WALK_LEFT) accelX = -1f
            else if (ponyDataBody.state == Pony.STATE_WALK_RIGHT) accelX = 1f
            else accelX = 0f

            if (isMalo)  // /esto solo puede pasar si es malo
                jump = (ponyDataBody as OpponentPony).hasToJump

            if (!ponyDataBody.tocoElPisoDespuesCaerHoyo) accelX = 0f
        }

        if (ponyDataBody.isHurt) {
            accelX = 0f
            jump = false
        }

        ponyDataBody.update(delta, obj, accelX)

        if (!isMalo && (ponyDataBody.isChile || ponyDataBody.isDulce)) accelX *= Pony.VEL_RUN * 1.5f
        else accelX *= Pony.VEL_RUN

        val vel = obj.getLinearVelocity()

        var impulsoY = 0f
        if (jump && !ponyDataBody.isDoubleJump) {
            val velChange = Pony.VEL_JUMP - vel.y
            impulsoY = obj.mass * velChange // disregard time factor
            ponyDataBody.jump()

            if (isMalo) {
                (ponyDataBody as OpponentPony).hasToJump = false
                game.assetsHandler!!.playSound(game.assetsHandler!!.jump!!, .4f)
            } else game.assetsHandler!!.playSound(game.assetsHandler!!.jump!!)
        }

        val velChange = accelX - vel.x
        val impulsoX = obj.mass * velChange // disregard time factor

        impulso.set(impulsoX, impulsoY)
        obj.applyLinearImpulse(impulso, obj.getWorldCenter(), true)
    }

    private fun updateFogatas(delta: Float) {
        for (obj in arrFogatas) {
            obj.update(delta)
        }
    }

    private fun updateBloodStone(delta: Float) {
        for (obj in arrBloodStone) {
            obj.update(delta)
        }
    }

    private fun lanzarBomba(oPonyBomba: Pony) {
        if (oPonyBomba is PonyPlayer) {
            if (Settings.numeroBombas <= 0) return
            else Settings.numeroBombas--
        }

        val velX: Int
        if (oPonyBomba.state == Pony.STATE_WALK_LEFT) velX = -4
        else velX = 4

        val velocidad = Vector2(velX.toFloat(), 5f)
        val bd = BodyDef()
        bd.position.y = oPonyBomba.position.y + .4f
        bd.position.x = oPonyBomba.position.x + .3f
        bd.type = BodyDef.BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        // Nucleo que si puede chocar con el piso pero no con los corredores
        val circulo = CircleShape()
        circulo.radius = .05f
        val fixture = FixtureDef()
        fixture.shape = circulo
        fixture.density = .1f
        fixture.restitution = .4f
        fixture.friction = .25f
        fixture.filter.groupIndex = TiledMapManagerBox2d.CONTACT_CORREDORES
        oBody.createFixture(fixture)

        // Nucleo sensor que me dice si acabo de tocar un corredor para explotar la bomba
        circulo.radius = .1f
        circulo.position = Vector2(0f, 0f)
        fixture.shape = circulo
        fixture.isSensor = true
        fixture.filter.groupIndex = 0
        var sensorBomba = oBody.createFixture(fixture)
        sensorBomba.setUserData("nucleoBomba")
        oBody.createFixture(fixture)

        // sensor que dice el radio de explosion de la bomba
        circulo.radius = .5f
        circulo.position = Vector2(0f, 0f)
        fixture.shape = circulo
        fixture.isSensor = true
        fixture.filter.groupIndex = 0
        sensorBomba = oBody.createFixture(fixture)
        sensorBomba.setUserData("sensorBomba")
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.linearVelocity = velocidad

        val oBomb = Bomb(oPonyBomba.position.x, oPonyBomba.position.y, this)

        arrBombas.add(oBomb)
        arrBodys.add(oBody)
        oBody.userData = oBomb

        circulo.dispose()
    }

    private fun fireWood(oPonyWood: Pony) {
        if (oPonyWood is PonyPlayer) {
            if (Settings.numeroWoods <= 0) return
            else Settings.numeroWoods--
        }

        val pos: Float
        if (oPonyWood.state == Pony.STATE_WALK_LEFT) pos = .5f
        else pos = -.5f

        val bd = BodyDef()
        bd.position.y = oPonyWood.position.y
        bd.position.x = oPonyWood.position.x + pos
        bd.type = BodyDef.BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        // Nucleo que si puede chocar con el piso pero no con los corredores
        val circulo = CircleShape()
        circulo.radius = .05f
        val fixture = FixtureDef()
        fixture.shape = circulo
        fixture.density = .1f
        fixture.restitution = .4f
        fixture.friction = .25f
        fixture.filter.groupIndex = TiledMapManagerBox2d.CONTACT_CORREDORES
        oBody.createFixture(fixture)

        // Nucleo sensor que me dice si acabo de tocar un corredor para explotar la bomba
        val po = PolygonShape()
        po.setAsBox(.22f, .10f)
        fixture.shape = po
        fixture.isSensor = true
        fixture.filter.groupIndex = 0
        val sensorBomba = oBody.createFixture(fixture)
        sensorBomba.setUserData("nucleoWood")
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true

        val oWood = Wood(oPonyWood.position.x, oPonyWood.position.y, this)

        arrWoods.add(oWood)
        arrBodys.add(oBody)
        oBody.userData = oWood

        circulo.dispose()
    }

    private fun checarGameOver(delta: Float) {
        for (i in 0..<arrPosiciones.size) {
            val objPony = arrPosiciones.get(i)

            if (objPony.position.x > finJuego!!.x) {
                objPony.pasoLaMeta = true

                if (objPony.lugarEnLaCarrera == 1) {
                    if (objPony.position.x >= finJuego!!.x + 2.7f) objPony.state = Pony.STATE_STAND
                } else if (objPony.lugarEnLaCarrera == 2) {
                    if (objPony.position.x >= finJuego!!.x + 2.2) objPony.state = Pony.STATE_STAND
                } else if (objPony.lugarEnLaCarrera == 3) {
                    if (objPony.position.x >= finJuego!!.x + 1.6f) objPony.state = Pony.STATE_STAND
                } else if (objPony.lugarEnLaCarrera == 4) {
                    if (objPony.position.x >= finJuego!!.x + 1) objPony.state = Pony.STATE_STAND
                } else if (objPony.lugarEnLaCarrera == 5) {
                    if (objPony.position.x >= finJuego!!.x + .4f) objPony.state = Pony.STATE_STAND
                }
            }
        }

        if (!oPony!!.pasoLaMeta) {
            tiempoLeft -= delta
            tiempoLap += delta
            if (tiempoLeft <= 0 && state != State.timeUp) {
                state = State.timeUp
                oPony!!.die()
            }
        } else {
            if (oPony!!.lugarEnLaCarrera == 1) {
                if (state != State.nextLevel) { // Solo lo checa 1 vez
                    game.achievementsHandler!!.checkWorldComplete(nivelTiled)
                    game.achievementsHandler!!.checkVictoryMoreThan15Secs(nivelTiled, tiempoLeft)
                }
                state = State.nextLevel
            } else state = State.tryAgain
        }
    }

    // Next level significa que se paso el nivel.
    enum class State {
        running, timeUp, tryAgain, nextLevel
    }

    inner class plataformasContact : ContactListener {
        var arrContacs: Array<Contact?>?

        init {
            arrContacs = Array<Contact?>()
        }

        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            val Adata = a.body.userData
            val Bdata = b.body.userData

            if (Adata is Pony) beginContactPony(a, b)
            else if (Bdata is Pony) beginContactPony(b, a)
        }

        override fun endContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a == null || b == null) return

            val Adata = a.body.userData
            val Bdata = b.body.userData

            if (Adata is OpponentPony) endContactPonyMalo(a, b)
            else if (Bdata is OpponentPony) endContactPonyMalo(b, a)
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold?) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            val Adata = a.body.userData
            val Bdata = b.body.userData

            if (Adata is Pony) preSolveContactPony(a, b, contact)
            else if (Bdata is Pony) preSolveContactPony(b, a, contact)
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }

        /**
         * Checa las colisiones que hay en un pony, puede ser pony IA o pony Jugador
         */
        fun beginContactPony(fixPony: Fixture, fixOtraCosa: Fixture) {
            val ponyDataBody: Pony
            val isMalo: Boolean

            if (fixPony.body.userData is OpponentPony) {
                ponyDataBody = fixPony.body.userData as OpponentPony
                isMalo = true
            } else {
                ponyDataBody = fixPony.body.userData as PonyPlayer
                isMalo = false
            }

            val otraCosaDataBody = fixOtraCosa.body.userData

            if (fixPony.getUserData() != null && (fixPony.getUserData() == "sensorBottomIzq" || fixPony.getUserData() == "sensorBottomDer")) {
                if (otraCosaDataBody == "suelo") {
                    ponyDataBody.tocoElPiso()
                } else if (otraCosaDataBody == "sueloInclinado") {
                    ponyDataBody.tocoPisoInclinado()
                } else if (otraCosaDataBody == "hoyo") {
                    ponyDataBody.cayoEnHoyo()
                } else if (otraCosaDataBody is Platform) {
                    ponyDataBody.tocoElPiso()
                }
            } else if (otraCosaDataBody is Pony) {
                if (fixPony.getUserData() != null && fixOtraCosa.getUserData() != null) {
                    if (ponyDataBody.isChile) {
                        if (fixPony.getUserData() == "cuerpoSensor" && fixOtraCosa.getUserData() == "cuerpo") {
                            otraCosaDataBody.getHurt(Chili.HURT_DURATION)
                        }
                    } else if (fixOtraCosa.getUserData() == "cuerpoSensor" && (otraCosaDataBody).isChile) {
                        if (fixPony.getUserData() == "cuerpo") {
                            ponyDataBody.getHurt(Chili.HURT_DURATION)
                        }
                    }
                }
            } else if (fixPony.getUserData() != null && fixPony.getUserData() == "cuerpo") {
                if (otraCosaDataBody == "regresoHoyo") {
                    ponyDataBody.regresoHoyo!!.set(ponyDataBody.position.x, ponyDataBody.position.y)
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData() == "nucleoBomba") {
                    (otraCosaDataBody as Bomb).explode(fixOtraCosa.body)
                    ponyDataBody.getHurt(otraCosaDataBody.bombTimerSeconds)
                } else if (fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData() == "nucleoWood") {
                    val oWood = otraCosaDataBody as Wood
                    if (oWood.state == Wood.State.NORMAL) {
                        oWood.hitByPony(fixOtraCosa.body)
                        ponyDataBody.getHurt(otraCosaDataBody.TIEMPO_HURT)
                    }
                } else if (otraCosaDataBody is Coin && !isMalo) {
                    val oCoin = otraCosaDataBody
                    if (oCoin.state == Coin.State.IDLE) {
                        val valorMoneda: Int
                        if (random.nextBoolean()) {
                            when (Settings.coinLevel) {
                                1, 2 -> valorMoneda = 2
                                3, 4 -> valorMoneda = 3
                                5 -> valorMoneda = 5
                                0 -> valorMoneda = 1
                                else -> valorMoneda = 1
                            }
                        } else {
                            valorMoneda = 1
                        }
                        sumarMonedas(valorMoneda)
                        ponyDataBody.monedasRecolectadas += valorMoneda
                        game.assetsHandler!!.playSound(game.assetsHandler!!.pickCoin!!)
                        oCoin.hitPony()
                    }
                } else if (otraCosaDataBody is Chili) {
                    val oChili = otraCosaDataBody
                    if (oChili.state == Chili.State.IDLE) {
                        oChili.hitPony()
                        ponyDataBody.tocoChile()
                        ponyDataBody.chilesRecolectados++
                    }
                } else if (otraCosaDataBody is Balloons) {
                    val oBalloons = otraCosaDataBody
                    if (oBalloons.state == Balloons.State.IDLE) {
                        oBalloons.hitPony()
                        if (!isMalo) {
                            when (Settings.chocolateLevel) {
                                1 -> tiempoLeft += 7.5f
                                2 -> tiempoLeft += 10.5f
                                3 -> tiempoLeft += 14f
                                4 -> tiempoLeft += 15f
                                5 -> tiempoLeft += 18f
                                0 -> tiempoLeft += 5f
                                else -> tiempoLeft += 5f
                            }
                            ponyDataBody.globosRecolectados++
                        }
                    }
                } else if (otraCosaDataBody is Candy) {
                    val oCandy = otraCosaDataBody
                    if (oCandy.state == Candy.State.NORMAL) {
                        oCandy.hitPony()
                        ponyDataBody.tocoDulce()
                        ponyDataBody.dulcesRecolectados++
                    }
                }
                /*
                 * COSAS PONYS MALOS SOLAMENTE
                 */
                run {
                    if (isMalo) {
                        if (otraCosaDataBody == "saltoDerecha") {
                            (ponyDataBody as OpponentPony).hitSimpleJump(Pony.STATE_WALK_RIGHT)
                        } else if (otraCosaDataBody == "saltoIzquierda") {
                            (ponyDataBody as OpponentPony).hitSimpleJump(Pony.STATE_WALK_LEFT)
                        } else if (otraCosaDataBody == "salto") {
                            (ponyDataBody as OpponentPony).hitSimpleJump(Pony.STATE_STAND)
                        } else if (otraCosaDataBody == "caminarIzquierda") {
                            (ponyDataBody as OpponentPony).hitCaminarOtraDireccion(Pony.STATE_WALK_LEFT)
                        } else if (otraCosaDataBody == "caminarDerecha") {
                            (ponyDataBody as OpponentPony).hitCaminarOtraDireccion(Pony.STATE_WALK_RIGHT)
                        } else if (otraCosaDataBody == "caer") {
                            (ponyDataBody as OpponentPony).hitCaminarOtraDireccion(Pony.STATE_STAND)
                        } else if (otraCosaDataBody == "bandera") {
                            (ponyDataBody as OpponentPony).didTouchFlag = true
                        } else if (otraCosaDataBody is Flag) {
                            val oBan = otraCosaDataBody
                            val oPon = ponyDataBody as OpponentPony

                            if (oBan.permitirSalto()) {
                                if (oBan.actionType == Flag.ActionType.JUMP_LEFT) oPon.hitSimpleJump(Pony.STATE_WALK_LEFT)
                                else if (oBan.actionType == Flag.ActionType.JUMP_RIGHT) oPon.hitSimpleJump(Pony.STATE_WALK_RIGHT)
                                else if (oBan.actionType == Flag.ActionType.JUMP) oPon.hitSimpleJump(Pony.STATE_STAND)
                            }
                        }
                    }
                }
                /*
                 * FIN COSAS PONYS MALOS
                 */
            }
        }

        fun preSolveContactPony(fixPony: Fixture, fixOtraCosa: Fixture, contact: Contact) {
            val otraCosaDataBody = fixOtraCosa.body.userData

            if (otraCosaDataBody is Platform) {
                // Si el pony su centro - la mitad de su altura y el piso su centro mas la mitad de su altura
                // Si ponyY es menor significa q esta por abajo.
                val oPis = otraCosaDataBody
                val ponyY = fixPony.body.getPosition().y - .25f
                val pisY = oPis.position.y + oPis.alto / 2f

                if (ponyY < pisY) contact.isEnabled = false
            }
        }

        fun endContactPonyMalo(fixPony: Fixture, fixOtraCosa: Fixture) {
            val otraCosaData = fixOtraCosa.body.userData
            val oOpponentPony = fixPony.body.userData as OpponentPony

            if (fixPony.getUserData() != null && fixPony.getUserData() == "cuerpo" && fixOtraCosa.getUserData() != null && fixOtraCosa.getUserData() == "sensorBomba") {
                if ((otraCosaData as Bomb).state == Bomb.State.EXPLODE) {
                    oOpponentPony.getHurt(otraCosaData.bombTimerSeconds)
                }
            }
        }
    }
}
