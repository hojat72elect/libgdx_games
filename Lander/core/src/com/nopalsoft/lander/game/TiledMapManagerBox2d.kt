package com.nopalsoft.lander.game

import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Logger
import com.nopalsoft.lander.game.objetos.Bomba
import com.nopalsoft.lander.game.objetos.Estrella
import com.nopalsoft.lander.game.objetos.Gas
import com.nopalsoft.lander.game.objetos.Laser
import com.nopalsoft.lander.game.objetos.Nave
import com.nopalsoft.lander.game.objetos.Plataforma

class TiledMapManagerBox2d(private val oWorld: WorldGame, private val m_units: Float) {
    private val oWorldBox = oWorld.oWorldBox
    private val logger = Logger("MapBodyManager", 1)
    private val defaultFixture = FixtureDef()

    init {
        defaultFixture.density = 1.0f
        defaultFixture.friction = .5f
        defaultFixture.restitution = 0.0f
    }

    fun createObjetosDesdeTiled(map: Map) {
        crearFisicos(map)
        crearInteaccion(map)
    }

    private fun crearInteaccion(map: Map) {
        val layer = map.layers.get("interaccion")

        if (layer == null) {
            logger.error("layer " + "interaccion" + " no existe")
            return
        }

        val mapLayerObjects = layer.objects
        for (layerObject in mapLayerObjects) {
            if (layerObject is TextureMapObject) {
                continue
            }

            val properties = layerObject.properties
            val tipo = properties.get("type") as String
            when (tipo) {
                "estrella" -> createStar(layerObject)
                "gas" -> createGas(layerObject)
                "laser" -> createLaser(layerObject)
                "bomba" -> createBomb(layerObject)
            }
        }
    }

    private fun crearFisicos(map: Map) {
        val layer = map.layers.get("fisicos")

        if (layer == null) {
            logger.error("layer " + "fisicos" + " no existe")
            return
        }

        val mapLayerObjects = layer.objects

        for (layerObject in mapLayerObjects) {
            if (layerObject is TextureMapObject) {
                continue
            }

            val properties = layerObject.properties
            val tipo = properties.get("type") as String?
            if (tipo == null) continue
            else if (tipo == "inicio" || tipo == "fin") {
                creatPlataformas(layerObject, tipo)
            }
            val shape = if (layerObject is RectangleMapObject) {
                getRectangle(layerObject)
            } else if (layerObject is PolygonMapObject) {
                getPolygon(layerObject)
            } else if (layerObject is PolylineMapObject) {
                getPolyline(layerObject)
            } else if (layerObject is CircleMapObject) {
                getCircle(layerObject)
            } else {
                logger.error("non suported shape $layerObject")
                continue
            }

            defaultFixture.shape = shape

            val bodyDef = BodyDef()
            bodyDef.type = BodyType.StaticBody

            val body = oWorldBox.createBody(bodyDef)
            body.createFixture(defaultFixture)

            body.userData = tipo

            defaultFixture.shape = null
            defaultFixture.isSensor = false
            defaultFixture.filter.groupIndex = 0
            shape.dispose()
        }
    }

    private fun creatPlataformas(platformMapObject: MapObject, tipo: String) {
        val rectangle = (platformMapObject as RectangleMapObject).rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f)
        defaultFixture.shape = polygon

        val bodyDef = BodyDef()
        bodyDef.type = BodyType.StaticBody
        val body = oWorldBox.createBody(bodyDef)
        body.createFixture(defaultFixture)

        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val height = (rectangle.height * m_units * 0.5f)
        val width = (rectangle.width * m_units * 0.5f)

        val plataforma = Plataforma(x, y, width, height)

        body.userData = plataforma
        oWorld.arrPlataformas.add(plataforma)

        if (tipo == "inicio") crearNave(plataforma)
        else if (tipo == "fin") plataforma.isFinal = true
    }

    private fun crearNave(plataforma: Plataforma) {
        val oNave = Nave(plataforma.position.x, plataforma.position.y + plataforma.size.y / 2)

        val bd = BodyDef()
        bd.position.x = oNave.position.x
        bd.position.y = oNave.position.y
        bd.type = BodyType.DynamicBody
        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Nave.WIDTH / 2f, Nave.HEIGHT / 2f) // Lo puse mejor con vertices pa que no tuviera esquinas picudas y tratar de minimizar

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = Nave.DENSIDAD_INICIAL
        fixture.restitution = 0f
        fixture.friction = .5f
        oBody.createFixture(fixture)

        oBody.userData = oNave
        oBody.isBullet = true

        shape.dispose()

        oWorld.oNave = oNave
    }

    private fun createStar(starMapObject: MapObject) {
        val rectangle = (starMapObject as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Estrella(x, y)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.1f, .1f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj

        oWorld.arrEstrellas.add(obj)
        pies.dispose()
    }

    private fun createGas(gasMapObject: MapObject) {
        val rectangle = (gasMapObject as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Gas(x, y)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(.1f, .1f)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj

        oWorld.arrGas.add(obj)
        pies.dispose()
    }

    private fun createLaser(laserMapObject: MapObject) {
        val rectangle = (laserMapObject as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val direccion = laserMapObject.properties.get("direccion").toString()

        val timeOFF: Float = laserMapObject.properties.get("tiempoApagado").toString().toFloat()
        val timeOffActual: Float = laserMapObject.properties.get("tiempoApagadoActual").toString().toFloat()


        val obj = Laser(x, y, rectangle.getWidth() * m_units, rectangle.height * m_units, timeOFF, timeOffActual, direccion)
        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.StaticBody

        val pies = PolygonShape()
        pies.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units)

        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = .5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj

        oWorld.arrLaser.add(obj)
        pies.dispose()
    }

    private fun createBomb(bombMapObject: MapObject) {
        val rectangle = (bombMapObject as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val obj = Bomba(x, y, bombMapObject.properties.get("direccion").toString())

        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.DynamicBody

        val pies = CircleShape()
        pies.radius = .2f
        val fixture = FixtureDef()
        fixture.shape = pies
        fixture.density = 15f
        fixture.restitution = 0f
        fixture.friction = 0f

        val oBody = oWorldBox.createBody(bd)
        oBody.gravityScale = 0f
        oBody.createFixture(fixture)

        oBody.userData = obj
        oWorld.arrBombas.add(obj)

        pies.dispose()
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
}
