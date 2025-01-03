package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Logger

class TiledMapManagerBox2d(oWorld: WorldGame, private val unitScale: Float) {
    private val oWorldBox = oWorld.oWorldBox
    private val logger = Logger("MapBodyManager", 1)
    private val defaultFixture = FixtureDef()

    init {
        defaultFixture.density = 1.0f
        defaultFixture.friction = .5f
        defaultFixture.restitution = 0.0f
    }

    fun createTiledObjects(map: Map) {
        createPhysics(map)
    }

    private fun createPhysics(map: Map) {
        val layer = map.layers["fisicos"]

        if (layer == null) {
            logger.error("Physical layer does not exist")
            return
        }

        val objects = layer.objects

        // Normally if not none is the floor
        for (gameObject in objects) {
            if (gameObject is TextureMapObject) {
                continue
            }

            val properties = gameObject.properties
            val type = properties["type"] as String

            // Normally if not none is the floor
            val shape = if (gameObject is RectangleMapObject) {
                getRectangle(gameObject)
            } else if (gameObject is PolygonMapObject) {
                getPolygon(gameObject)
            } else if (gameObject is PolylineMapObject) {
                getPolyline(gameObject)
            } else if (gameObject is CircleMapObject) {
                getCircle(gameObject)
            } else {
                logger.error("Non supported shape $gameObject")
                continue
            }

            defaultFixture.shape = shape

            val bodyDef = BodyDef()
            bodyDef.type = BodyDef.BodyType.StaticBody

            val body = oWorldBox.createBody(bodyDef)
            body.createFixture(defaultFixture)
            body.userData = type

            defaultFixture.shape = null
            defaultFixture.isSensor = false
            defaultFixture.filter.groupIndex = 0
            shape.dispose()
        }
    }

    private fun getRectangle(rectangleObject: RectangleMapObject): Shape {
        val rectangle = rectangleObject.rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * unitScale, (rectangle.y + rectangle.height * 0.5f) * unitScale)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * unitScale, rectangle.height * 0.5f * unitScale, size, 0.0f)
        return polygon
    }

    private fun getCircle(circleObject: CircleMapObject): Shape {
        val circle = circleObject.circle
        val circleShape = CircleShape()
        circleShape.radius = circle.radius * unitScale
        circleShape.position = Vector2(circle.x * unitScale, circle.y * unitScale)
        return circleShape
    }

    private fun getPolygon(polygonObject: PolygonMapObject): Shape {
        val polygon = PolygonShape()
        val vertices = polygonObject.polygon.vertices
        val worldVertices = FloatArray(vertices.size)
        val yLost = polygonObject.polygon.y * unitScale
        val xLost = polygonObject.polygon.x * unitScale

        for (i in vertices.indices) {
            if (i % 2 == 0) worldVertices[i] = vertices[i] * unitScale + xLost
            else worldVertices[i] = vertices[i] * unitScale + yLost
        }
        polygon.set(worldVertices)

        return polygon
    }

    private fun getPolyline(polylineObject: PolylineMapObject): Shape {
        val vertices = polylineObject.polyline.vertices

        val worldVertices = arrayOfNulls<Vector2>(vertices.size / 2)
        val yLost = polylineObject.polyline.y * unitScale
        val xLost = polylineObject.polyline.x * unitScale
        for (i in 0 until vertices.size / 2) {
            worldVertices[i] = Vector2()
            worldVertices[i]?.x = vertices[i * 2] * unitScale + xLost
            worldVertices[i]?.y = vertices[i * 2 + 1] * unitScale + yLost
        }
        val chain = ChainShape()
        chain.createChain(worldVertices)
        return chain
    }
}
