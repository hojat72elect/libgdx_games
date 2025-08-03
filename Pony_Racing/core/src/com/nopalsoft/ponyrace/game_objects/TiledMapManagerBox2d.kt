package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Logger
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler

class TiledMapManagerBox2d(oWorld: TileMapHandler, unitsPerPixel: Float) {
    private val oWorld: TileMapHandler?
    private val oWorldBox: World
    private val m_units: Float
    private val logger: Logger
    private val defaultFixture: FixtureDef
    private val nombrePonys: LinkedHashMap<Int?, String>
    var contadorPonisCreados: Int = 0

    init {
        this.oWorld = oWorld
        oWorldBox = oWorld.oWorldBox
        m_units = unitsPerPixel
        logger = Logger("MapBodyManager", 1)
        nombrePonys = oWorld.game.assetsHandler.nombrePonys

        defaultFixture = FixtureDef()
        defaultFixture.density = 1.0f
        defaultFixture.friction = .5f
        defaultFixture.restitution = 0.0f
    }

    fun createObjetosDesdeTiled(map: Map) {
        crearFisicos(map, "fisicos")
        crearInteaccion(map, "interaccion")
    }

    private fun crearInteaccion(map: Map, layerName: String?) {
        val layer = map.layers.get(layerName)

        if (layer == null) {
            logger.error("layer " + layerName + " no existe")
            return
        }

        val objects = layer.objects
        for (`object` in objects) {
            if (`object` is TextureMapObject) {
                continue
            }

            val properties = `object`.properties
            val tipo = properties.get("type") as String?

            if (tipo != null) {
                when (tipo) {
                    "moneda" -> crearMoneda(`object`)
                    "dulce" -> crearDulce(`object`)
                    "chile" -> crearChile(`object`)
                    "globo" -> crearGlobo(`object`)
                }
            }
        }
    }

    fun crearFisicos(map: Map, layerName: String?) {
        val layer = map.layers.get(layerName)

        if (layer == null) {
            logger.error("layer " + layerName + " no existe")
            return
        }

        val objects = layer.objects

        for (`object` in objects) {
            if (`object` is TextureMapObject) {
                continue
            }

            val properties = `object`.properties
            val tipo = properties.get("type") as String?
            if (tipo == null) continue
            else if (tipo == "pony") {
                crearPony(`object`, tipo)
                continue
            } else if (tipo == "malos") {
                crearPony(`object`, tipo)
                continue
            } else if (tipo == "plumas") {
                crearPlumas(`object`)
                continue
            } else if (tipo == "fogata") {
                crearFogata(`object`)
                continue
            } else if (tipo == "pisable") {
                if (`object` is RectangleMapObject) {
                    crearPisable(`object`)
                    continue
                }
            } else if (tipo == "fin") {
                val rectangle = (`object` as RectangleMapObject).rectangle
                val x = (rectangle.x + rectangle.width * 0.5f) * m_units
                val y = (rectangle.y + rectangle.height * 0.5f) * m_units
                oWorld!!.finJuego = Vector2(x, y)
                continue
            } else if (tipo == "gemaChica") {
                val rectangle = (`object` as RectangleMapObject).rectangle
                val x = (rectangle.x + rectangle.width * 0.5f) * m_units
                val y = (rectangle.y + rectangle.height * 0.5f) * m_units
                oWorld!!.arrBloodStone.add(BloodStone(x, y, BloodStone.Type.SMALL, oWorld.random))
                continue
            } else if (tipo == "gemaMediana") {
                val rectangle = (`object` as RectangleMapObject).rectangle
                val x = (rectangle.x + rectangle.width * 0.5f) * m_units
                val y = (rectangle.y + rectangle.height * 0.5f) * m_units
                oWorld!!.arrBloodStone.add(BloodStone(x, y, BloodStone.Type.MEDIUM, oWorld.random))
                continue
            } else if (tipo == "gemaGrande") {
                val rectangle = (`object` as RectangleMapObject).rectangle
                val x = (rectangle.x + rectangle.width * 0.5f) * m_units
                val y = (rectangle.y + rectangle.height * 0.5f) * m_units
                oWorld!!.arrBloodStone.add(BloodStone(x, y, BloodStone.Type.LARGE, oWorld.random))
                continue
            }

            val shape: Shape?
            if (`object` is RectangleMapObject) {
                shape = getRectangle(`object`)
            } else if (`object` is PolygonMapObject) {
                shape = getPolygon(`object`)
            } else if (`object` is PolylineMapObject) {
                shape = getPolyline(`object`)
            } else if (`object` is CircleMapObject) {
                shape = getCircle(`object`)
            } else {
                logger.error("non suported shape " + `object`)
                continue
            }

            defaultFixture.shape = shape

            val bodyDef = BodyDef()
            bodyDef.type = BodyDef.BodyType.StaticBody

            if (tipo == "bandera1" || tipo == "caer" || tipo == "saltoDerecha" || tipo == "bandera" || tipo == "regresoHoyo"
                || tipo == "caminarIzquierda" || tipo == "caminarDerecha" || tipo == "saltoIzquierda" || tipo == "salto"
            ) {
                defaultFixture.isSensor = true
            }

            val body = oWorldBox.createBody(bodyDef)
            body.createFixture(defaultFixture)

            if (tipo == "bandera1") {
                if (properties.get("jump") == "left") body.userData = Flag(
                    oWorld!!,
                    Flag.ActionType.JUMP_LEFT
                )
                else if (properties.get("jump") == "right") body.userData = Flag(
                    oWorld!!,
                    Flag.ActionType.JUMP_RIGHT
                )
                else if (properties.get("jump") == "stand") body.userData = Flag(oWorld!!, Flag.ActionType.JUMP)
            } else body.userData = tipo

            defaultFixture.shape = null
            defaultFixture.isSensor = false
            defaultFixture.filter.groupIndex = 0
            shape.dispose()
        }
    }

    private fun crearPisable(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f)
        defaultFixture.shape = polygon

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        val body = oWorldBox.createBody(bodyDef)
        body.createFixture(defaultFixture)

        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units
        val height = (rectangle.height * m_units * 0.5f)
        val width = (rectangle.width * m_units * 0.5f)
        body.userData = Platform(x, y, width, height)
    }

    private fun getRectangle(rectangleObject: RectangleMapObject): Shape {
        val rectangle = rectangleObject.rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f)
        return polygon
    }

    private fun getCircle(circleObject: CircleMapObject): Shape {
        val circle = circleObject.circle
        val circleShape = CircleShape()
        circleShape.radius = circle.radius * m_units
        circleShape.position = Vector2(circle.x * m_units, circle.y * m_units)
        return circleShape
    }

    private fun getPolygon(polygonObject: PolygonMapObject): Shape {
        val polygon = PolygonShape()
        val vertices = polygonObject.polygon.vertices
        val worldVertices = FloatArray(vertices.size)
        val yLost = polygonObject.polygon.y * m_units
        val xLost = polygonObject.polygon.x * m_units

        for (i in vertices.indices) {
            if (i % 2 == 0) worldVertices[i] = vertices[i] * m_units + xLost
            else worldVertices[i] = vertices[i] * m_units + yLost
        }
        polygon.set(worldVertices)

        return polygon
    }

    private fun getPolyline(polylineObject: PolylineMapObject): Shape {
        val vertices = polylineObject.polyline.vertices

        val worldVertices = arrayOfNulls<Vector2>(vertices.size / 2)
        val yLost = polylineObject.polyline.y * m_units
        val xLost = polylineObject.polyline.x * m_units
        for (i in 0..<vertices.size / 2) {
            worldVertices[i] = Vector2()
            worldVertices[i]!!.x = vertices[i * 2] * m_units + xLost
            worldVertices[i]!!.y = vertices[i * 2 + 1] * m_units + yLost
        }
        val chain = ChainShape()
        chain.createChain(worldVertices)
        return chain
    }

    private fun crearPony(`object`: MapObject, tipo: String) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val oPony: Pony?

        val nombreSkin: String

        if (tipo == "pony") {
            nombreSkin = Settings.selectedSkin
            oPony = PonyPlayer(x, y, nombreSkin, oWorld!!)
        } else { // Ponis malos

            if (Settings.selectedSkin == nombrePonys.get(contadorPonisCreados)) contadorPonisCreados++
            nombreSkin = nombrePonys.get(contadorPonisCreados)!!

            oPony = OpponentPony(x, y, nombreSkin, oWorld!!)
        }
        contadorPonisCreados++ // Se comenta esta linea si se quieren poner muchos ponys. PAra debugear

        val bd = BodyDef()
        bd.position.y = oPony.position.y
        bd.position.x = oPony.position.x
        bd.type = BodyDef.BodyType.DynamicBody
        val oBody = oWorldBox.createBody(bd)

        val pies = PolygonShape()

        // pies.setAsBox(.2f, .23f);//Lo puse mejor con vertices pa que no tuviera esquinas picudas y tratar de minimizar
        // que los ponis se caian al vacio
        val vert = floatArrayOf(-.2f, -.2f, -.18f, -.23f, .18f, -.23f, .2f, -.2f, .2f, .23f, -.2f, .23f)
        pies.set(vert)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.filter.groupIndex = CONTACT_CORREDORES
        var cuerpo = oBody.createFixture(fixture)
        cuerpo.setUserData("cuerpo")
        oBody.createFixture(fixture)

        // Sensor pa cuando tenga el chile pueda danar a los enemigos
        // pies.setAsBox(.2f, .23f, new Vector2(.1f, .2f), 0);
        fixture.shape = pies
        fixture.density = .1f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        fixture.filter.groupIndex = 0
        cuerpo = oBody.createFixture(fixture)
        cuerpo.setUserData("cuerpoSensor")
        oBody.createFixture(fixture)

        var sensorBottomIzq = CircleShape()
        sensorBottomIzq.radius = .03f
        sensorBottomIzq.position = Vector2(-.14f, -.23f)
        fixture.shape = sensorBottomIzq
        fixture.density = 03f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        fixture.filter.groupIndex = CONTACT_CORREDORES
        var sensorBottomIzqFixture = oBody.createFixture(fixture)
        sensorBottomIzqFixture.setUserData("sensorBottomIzq")
        oBody.createFixture(fixture)

        sensorBottomIzq = CircleShape()
        sensorBottomIzq.radius = .03f
        sensorBottomIzq.position = Vector2(.14f, -.23f)
        fixture.shape = sensorBottomIzq
        fixture.density = 03f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        fixture.filter.groupIndex = CONTACT_CORREDORES
        sensorBottomIzqFixture = oBody.createFixture(fixture)
        sensorBottomIzqFixture.setUserData("sensorBottomDer")
        oBody.createFixture(fixture)

        // oBody.setFixedRotation(true);
        oBody.userData = oPony
        oBody.isBullet = true

        if (tipo == "pony") {
            oWorld.oPony = oPony as PonyPlayer
        } else { // ponis malos
            oWorld.arrPonysMalos.add(oPony as OpponentPony)
        }

        pies.dispose()
        sensorBottomIzq.dispose()
    }

    private fun crearFogata(`object`: MapObject?) {
        val polygon = PolygonShape()
        val polygonObject = `object` as PolygonMapObject
        val vertices = polygonObject.polygon.vertices
        val worldVertices = FloatArray(vertices.size)
        val yLost = polygonObject.polygon.y * m_units
        val xLost = polygonObject.polygon.x * m_units

        for (i in vertices.indices) {
            if (i % 2 == 0) worldVertices[i] = vertices[i] * m_units + xLost
            else worldVertices[i] = vertices[i] * m_units + yLost
        }

        val `as` = Polygon(vertices)
        val oBonfire: Bonfire?
        oBonfire = Bonfire(
            `as`.getBoundingRectangle().width / 2f * m_units + `as`.getBoundingRectangle().x * m_units + xLost,
            `as`.getBoundingRectangle().height / 2f * m_units + `as`.getBoundingRectangle().y * m_units + yLost - .15f, oWorld!!.random
        )

        polygon.set(worldVertices)

        val fixture = FixtureDef()
        fixture.density = 1.0f
        fixture.friction = 1f
        fixture.restitution = 0.0f
        fixture.shape = polygon

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody

        val body = oWorldBox.createBody(bodyDef)
        body.createFixture(fixture)
        body.userData = oBonfire

        oWorld.arrFogatas.add(oBonfire)
        polygon.dispose()
    }

    private fun crearPlumas(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Wing(x, y, oWorld!!.random)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyDef.BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.1f, .1f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        fixture.filter.groupIndex = CONTACT_CORREDORES

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrPlumas.add(obj)
        pies.dispose()
    }

    private fun crearMoneda(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Coin(x, y, oWorld!!)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyDef.BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.1f, .1f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        // fixture.filter.groupIndex = CONTACT_CORREDORES;
        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrMonedas.add(obj)
        pies.dispose()
    }

    private fun crearChile(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Chili(x, y, oWorld!!)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyDef.BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.25f, .15f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        // fixture.filter.groupIndex = CONTACT_CORREDORES;
        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrChiles.add(obj)
        pies.dispose()
    }

    private fun crearGlobo(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Balloons(x, y, oWorld!!)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyDef.BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.15f, .4f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        // fixture.filter.groupIndex = CONTACT_CORREDORES;
        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrGlobos.add(obj)
        pies.dispose()
    }

    private fun crearDulce(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Candy(x, y, oWorld!!)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyDef.BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.15f, .25f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        // fixture.filter.groupIndex = CONTACT_CORREDORES;
        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrDulces.add(obj)
        pies.dispose()
    }

    companion object {
        @JvmField
        val CONTACT_CORREDORES: Short = -1
    }
}
