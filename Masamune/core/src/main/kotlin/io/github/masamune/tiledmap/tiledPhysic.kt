package io.github.masamune.tiledmap

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune.Companion.UNIT_SCALE
import io.github.masamune.PhysicWorld
import ktx.app.gdxError
import ktx.box2d.FixtureDefinition
import ktx.box2d.body
import ktx.box2d.box
import ktx.collections.GdxArray
import ktx.math.component1
import ktx.math.component2
import ktx.math.component3
import ktx.math.component4
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.isEmpty
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y

private val TMP_RELATIVE_TO = vec2()

fun TiledMap.spawnBoundaryBody(world: World): Body {
    // create four boxes for the map boundary (left, right, bottom and top edge)
    val physicWorld = world.inject<PhysicWorld>()
    val mapW = width.toFloat()
    val mapH = height.toFloat()
    val halfW = width * 0.5f
    val halfH = height * 0.5f
    val boxThickness = 1f

    return physicWorld.body(BodyType.StaticBody) {
        position.set(0f, 0f)

        // left edge
        box(boxThickness, mapH, vec2(-boxThickness * 0.5f, halfH)) { friction = 0f }
        // right edge
        box(boxThickness, mapH, vec2(mapW + boxThickness * 0.5f, halfH)) { friction = 0f }

        // bottom edge
        box(mapW, boxThickness, vec2(halfW, -boxThickness * 0.5f)) { friction = 0f }
        // top edge
        box(mapW, boxThickness, vec2(halfW, mapH + boxThickness * 0.5f)) { friction = 0f }
    }
}

fun TiledMapTile.toBody(
    world: World,
    x: Float,
    y: Float,
    bodyType: BodyType,
    data: Any? = null
): Body {
    if (objects.isEmpty()) {
        gdxError("Trying to create a physic body without fixtures for tile ${this.id}")
    }

    val physicWorld = world.inject<PhysicWorld>()
    return physicWorld.body(bodyType) {
        position.set(x, y)
        fixedRotation = true
        userData = data
        fixtureDefinitions.addAll(toFixtureDefs())
    }
}

fun MapObject.toBody(
    world: World,
    x: Float,
    y: Float,
    data: Any? = null,
): Body {
    val physicWorld = world.inject<PhysicWorld>()
    return physicWorld.body(BodyType.StaticBody) {
        position.set(x, y)
        fixedRotation = true
        userData = data
        fixtureDefinitions.add(this@toBody.toFixtureDef(TMP_RELATIVE_TO.set(x, y)))
    }
}

fun TiledMapTile.toFixtureDefs(): GdxArray<FixtureDefinition> {
    val result = GdxArray<FixtureDefinition>()
    objects.forEach { mapObject ->
        result.add(mapObject.toFixtureDef())
    }
    return result
}

// relativeTo is necessary for map objects that are directly placed on a layer because
// their x/y is equal to the position of the object, but we need it relative to 0,0 like it
// is in the collision editor of a tile.
fun MapObject.toFixtureDef(relativeTo: Vector2 = Vector2.Zero): FixtureDefinition {
    return when (this) {
        is RectangleMapObject -> rectangleFixtureDef(this, relativeTo)
        is EllipseMapObject -> ellipseFixtureDef(this, relativeTo)
        is PolygonMapObject -> polygonFixtureDef(this, this.polygon.vertices, relativeTo)
        is PolylineMapObject -> polygonFixtureDef(this, this.polyline.vertices, relativeTo)
        else -> gdxError("Unsupported MapObject $this")
    }
}

// box is centered around body position in Box2D, but we want to have it aligned in a way
// that the body position is the bottom left corner of the box.
// That's why we use a 'boxOffset' below.
private fun rectangleFixtureDef(mapObject: RectangleMapObject, relativeTo: Vector2): FixtureDefinition {
    val (rectX, rectY, rectW, rectH) = mapObject.rectangle
    val boxX = rectX * UNIT_SCALE - relativeTo.x
    val boxY = rectY * UNIT_SCALE - relativeTo.y

    val boxW = rectW * UNIT_SCALE * 0.5f
    val boxH = rectH * UNIT_SCALE * 0.5f
    return FixtureDefinition().apply {
        shape = PolygonShape().apply {
            setAsBox(boxW, boxH, vec2(boxX + boxW, boxY + boxH), 0f)
        }
        friction = mapObject.friction
        restitution = mapObject.restitution
        density = mapObject.density
        isSensor = mapObject.isSensor
        userData = mapObject.userData
    }
}

private fun ellipseFixtureDef(mapObject: EllipseMapObject, relativeTo: Vector2): FixtureDefinition {
    val (x, y, w, h) = mapObject.ellipse
    val ellipseX = x * UNIT_SCALE - relativeTo.x
    val ellipseY = y * UNIT_SCALE - relativeTo.y
    val ellipseW = w * UNIT_SCALE / 2f
    val ellipseH = h * UNIT_SCALE / 2f

    return if (MathUtils.isEqual(ellipseW, ellipseH, 0.1f)) {
        // width and height are equal -> return a circle shape
        FixtureDefinition().apply {
            shape = CircleShape().apply {
                position = vec2(ellipseX + ellipseW, ellipseY + ellipseH)
                radius = ellipseW
            }
            friction = mapObject.friction
            restitution = mapObject.restitution
            density = mapObject.density
            isSensor = mapObject.isSensor
            userData = mapObject.userData
        }
    } else {
        // width and height are not equal -> return an ellipse shape (=polygon with 'numVertices' vertices)
        // PolygonShape only supports 8 vertices
        // ChainShape supports more but does not properly collide in some scenarios
        val numVertices = 8
        val angleStep = MathUtils.PI2 / numVertices
        val vertices = Array(numVertices) { vertexIdx ->
            val angle = vertexIdx * angleStep
            val offsetX = ellipseW * MathUtils.cos(angle)
            val offsetY = ellipseH * MathUtils.sin(angle)
            vec2(ellipseX + ellipseW + offsetX, ellipseY + ellipseH + offsetY)
        }

        FixtureDefinition().apply {
            shape = PolygonShape().apply {
                set(vertices)
            }
            friction = mapObject.friction
            restitution = mapObject.restitution
            density = mapObject.density
            isSensor = mapObject.isSensor
            userData = mapObject.userData
        }
    }
}

private fun polygonFixtureDef(
    mapObject: MapObject,
    polyVertices: FloatArray,
    relativeTo: Vector2,
): FixtureDefinition {
    val x = mapObject.x * UNIT_SCALE - relativeTo.x
    val y = mapObject.y * UNIT_SCALE - relativeTo.y
    val vertices = FloatArray(polyVertices.size) { vertexIdx ->
        if (vertexIdx % 2 == 0) {
            x + polyVertices[vertexIdx] * UNIT_SCALE
        } else {
            y + polyVertices[vertexIdx] * UNIT_SCALE
        }
    }

    return FixtureDefinition().apply {
        shape = ChainShape().apply {
            if (mapObject is PolygonMapObject) {
                createLoop(vertices)
            } else {
                createChain(vertices)
            }
        }
        friction = mapObject.friction
        restitution = mapObject.restitution
        density = mapObject.density
        isSensor = mapObject.isSensor
        userData = mapObject.userData
    }
}
