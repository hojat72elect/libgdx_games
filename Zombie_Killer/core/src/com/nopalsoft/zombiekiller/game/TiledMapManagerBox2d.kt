package com.nopalsoft.zombiekiller.game

import com.badlogic.gdx.Gdx
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
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Logger
import com.nopalsoft.zombiekiller.game_objects.Crate
import com.nopalsoft.zombiekiller.game_objects.ItemGem
import com.nopalsoft.zombiekiller.game_objects.ItemHeart
import com.nopalsoft.zombiekiller.game_objects.ItemMeat
import com.nopalsoft.zombiekiller.game_objects.ItemShield
import com.nopalsoft.zombiekiller.game_objects.ItemSkull
import com.nopalsoft.zombiekiller.game_objects.ItemStar
import com.nopalsoft.zombiekiller.game_objects.Items
import com.nopalsoft.zombiekiller.game_objects.Platform
import com.nopalsoft.zombiekiller.game_objects.Saw
import com.nopalsoft.zombiekiller.game_objects.Zombie

class TiledMapManagerBox2d(worldGame: WorldGame, unitScale: Float) {
    private val worldGame: WorldGame?
    private val world: World
    private val m_units: Float
    private val logger: Logger
    private val defaultFixtureDefinition: FixtureDef

    init {
        this.worldGame = worldGame
        world = worldGame.world
        m_units = unitScale
        logger = Logger("MapBodyManager", 1)

        defaultFixtureDefinition = FixtureDef()
        defaultFixtureDefinition.density = 1.0f
        defaultFixtureDefinition.friction = .5f
        defaultFixtureDefinition.restitution = 0.0f
    }

    fun createObjectsFromTiled(map: Map) {
        createPhysics(map)
        createEnemies(map)
    }

    private fun createPhysics(map: Map) {
        val layer = map.layers.get("fisicos")

        if (layer == null) {
            logger.error("layer " + "fisicos" + " no existe")
            return
        }

        val objects = layer.objects
        val objectIterator = objects.iterator()

        var skulls = 0
        while (objectIterator.hasNext()) {
            val `object` = objectIterator.next()

            if (`object` is TextureMapObject) {
                continue
            }

            val properties = `object`.properties
            val type = properties.get("type") as String?
            if (type == null) continue
            else if (type == "pisable") {
                if (`object` is RectangleMapObject) {
                    createPlatform(`object`)
                    continue
                }
            } else if (type == "ladder") {
                if (`object` is RectangleMapObject) {
                    createLadder(`object`, type)
                    continue
                }
            } else if (type == "crate") {
                if (`object` is RectangleMapObject) {
                    createCrate(`object`)
                    continue
                }
            } else if (type == "saw") {
                if (`object` is RectangleMapObject) {
                    createSaw(`object`)
                    continue
                }
            } else if (type == "gem" || type == "heart" || type == "star" || type == "skull" || type == "meat"
                || type == "shield"
            ) {
                if (`object` is RectangleMapObject) {
                    if (type == "skull") skulls++
                    createItem(`object`, type)
                    continue
                }
            }

            /*
             * Normally if not none is the floor.
             */
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

            defaultFixtureDefinition.shape = shape

            val bodyDefinition = BodyDef()
            bodyDefinition.type = BodyType.StaticBody

            val body = world.createBody(bodyDefinition)
            body.createFixture(defaultFixtureDefinition)
            body.userData = type

            defaultFixtureDefinition.shape = null
            defaultFixtureDefinition.isSensor = false
            defaultFixtureDefinition.filter.groupIndex = 0
            shape.dispose()
        }
        if (skulls != 3) throw GdxRuntimeException("#### DEBE HABER 3 SKULLS ####")
    }

    private fun createEnemies(map: Map) {
        val layer = map.layers.get("malos")

        if (layer == null) {
            logger.error("layer " + "malos" + " no existe")
            return
        }

        val objects = layer.objects

        for (`object` in objects) {
            if (`object` is TextureMapObject) {
                continue
            }

            val properties = `object`.properties
            val type = properties.get("type") as String
            if (type == "zombieCuasy" || type == "zombieFrank" || type == "zombieMummy" || type == "zombieKid"
                || type == "zombiePan"
            ) {
                if (`object` is RectangleMapObject) {
                    createZombie(`object`, type)
                    worldGame!!.TOTAL_ZOMBIES_LEVEL++
                }
            } else {
                throw GdxRuntimeException("Error en layer " + "malos" + ", objeto:" + type)
            }
        }
    }

    private fun createPlatform(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f)
        defaultFixtureDefinition.shape = polygon

        val bodyDef = BodyDef()
        bodyDef.type = BodyType.StaticBody
        val body = world.createBody(bodyDef)
        body.createFixture(defaultFixtureDefinition)

        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units
        val height = (rectangle.height * m_units * 0.5f)
        val width = (rectangle.width * m_units * 0.5f)
        body.userData = Platform(x, y, width, height)
    }

    private fun createLadder(`object`: MapObject, tipo: String?) {
        val rectangle = (`object` as RectangleMapObject).rectangle
        val polygon = PolygonShape()
        val size = Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units)
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f)
        defaultFixtureDefinition.shape = polygon

        val bodyDef = BodyDef()
        bodyDef.type = BodyType.StaticBody
        val body = world.createBody(bodyDef)

        defaultFixtureDefinition.isSensor = true
        body.createFixture(defaultFixtureDefinition)
        body.userData = tipo

        defaultFixtureDefinition.isSensor = false
    }

    private fun createCrate(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle

        val polygon = PolygonShape()
        val width = (rectangle.width * m_units)
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        polygon.setAsBox(width / 2f, width / 2f)

        val fixDef = FixtureDef()
        fixDef.density = 12f
        fixDef.friction = .7f
        fixDef.restitution = 0.0f
        fixDef.shape = polygon

        val obj = Crate(x, y, width)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = obj.position.x
        bodyDefinition.position.y = obj.position.y
        bodyDefinition.type = BodyType.DynamicBody
        val body = world.createBody(bodyDefinition)

        body.createFixture(fixDef)

        worldGame!!.crates!!.add(obj)
        body.userData = obj
    }

    private fun createSaw(`object`: MapObject) {
        val rectangle = (`object` as RectangleMapObject).rectangle

        val width = (rectangle.width * m_units)
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        val shape = CircleShape()
        shape.radius = width / 2f

        defaultFixtureDefinition.shape = shape

        val saw = Saw(x, y, width)

        val bodyDef = BodyDef()
        bodyDef.position.x = saw.position.x
        bodyDef.position.y = saw.position.y
        bodyDef.type = BodyType.KinematicBody
        val body = world.createBody(bodyDef)

        body.createFixture(defaultFixtureDefinition)

        worldGame!!.saws!!.add(saw)
        body.userData = saw
        body.angularVelocity = Math.toRadians(360.0).toFloat()

        shape.dispose()
    }

    private fun createItem(`object`: MapObject, type: String) {
        var obj: Items? = null

        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        when (type) {
            "gem" -> obj = ItemGem(x, y)
            "heart" -> {
                obj = ItemHeart(x, y)
                Gdx.app.log("Heart", "")
            }

            "skull" -> obj = ItemSkull(x, y)
            "meat" -> obj = ItemMeat(x, y)
            "shield" -> obj = ItemShield(x, y)
            "star" -> obj = ItemStar(x, y)
        }

        val bodyDefinition = BodyDef()
        bodyDefinition.position.y = obj!!.position.y
        bodyDefinition.position.x = obj.position.x
        bodyDefinition.type = BodyType.StaticBody

        val shape = CircleShape()
        shape.radius = .15f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        val body = world.createBody(bodyDefinition)
        body.createFixture(fixtureDefinition)

        body.userData = obj
        worldGame!!.items.add(obj)
        shape.dispose()
    }

    private fun createZombie(`object`: MapObject, type: String) {
        var obj: Zombie? = null

        val rectangle = (`object` as RectangleMapObject).rectangle
        val x = (rectangle.x + rectangle.width * 0.5f) * m_units
        val y = (rectangle.y + rectangle.height * 0.5f) * m_units

        when (type) {
            "zombieCuasy" -> obj = Zombie(x, y, Zombie.TYPE_CUASY)
            "zombieFrank" -> obj = Zombie(x, y, Zombie.TYPE_FRANK)
            "zombieKid" -> obj = Zombie(x, y, Zombie.TYPE_KID)
            "zombieMummy" -> obj = Zombie(x, y, Zombie.TYPE_MUMMY)
            "zombiePan" -> obj = Zombie(x, y, Zombie.TYPE_PAN)
        }

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = obj!!.position.x
        bodyDefinition.position.y = obj.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = world.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.17f, .32f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 8f
        fixtureDefinition.friction = 0f
        fixtureDefinition.filter.groupIndex = -1
        body.createFixture(fixtureDefinition)

        body.isFixedRotation = true
        body.userData = obj
        worldGame!!.zombies.add(obj)

        shape.dispose()
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
