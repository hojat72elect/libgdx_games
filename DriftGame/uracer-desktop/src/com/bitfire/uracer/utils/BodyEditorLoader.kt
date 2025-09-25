package com.bitfire.uracer.utils

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * Loads the collision fixtures defined with the Physics Body Editor application. You only need to give it a body and the
 * corresponding fixture name, and it will attach these fixtures to your body.
 */
class BodyEditorLoader(file: FileHandle) {

    private val model = readJson(file.readString())
    private val vectorPool: MutableList<Vector2?> = ArrayList()
    private val polygonShape = PolygonShape()
    private val circleShape = CircleShape()
    private val vec = Vector2()

    /**
     * Creates and applies the fixtures defined in the editor. The name parameter is used to retrieve the right fixture from the
     * loaded file.
     *
     * The body reference point (the red cross in the tool) is by default located at the bottom left corner of the image. This
     * reference point will be put over the BodyDef position point. Therefore, you should place this reference point
     * carefully to let you place your body in your world easily with its BodyDef.position point.
     *
     * Also, saved shapes are normalized. As shown in the tool, the width of the image is considered to be always 1 meter. Thus,
     * you need to provide a scale factor so the polygons get resized according to your needs (not every body is 1 meter large in
     * your game, I guess).
     *
     * @param body  The Box2D body you want to attach the fixture to.
     * @param name  The name of the fixture you want to load.
     * @param fd    The fixture parameters to apply to the created body fixture.
     * @param scale The desired scale of the body. The default width is 1.
     */
    fun attachFixture(body: Body, name: String, fd: FixtureDef, scale: Float, scaleX: Float, scaleY: Float) {

        val rbModel = model.rigidBodies[name]!!
        val origin = vec.set(rbModel.origin).scl(scale)

        for (polygon in rbModel.polygons) {
            val vertices = polygon.buffer

            for (i in vertices!!.indices) {
                vertices[i] = newVec()!!.set(polygon.vertices[i]).scl(scale)
                vertices[i]!!.sub(origin)
                vertices[i]!!.x *= scaleX
                vertices[i]!!.y *= scaleY
            }

            polygonShape.set(vertices)
            fd.shape = polygonShape
            body.createFixture(fd)

            for (vertex in vertices) {
                free(vertex)
            }
        }

        var i = 0
        val n = rbModel.circles.size
        while (i < n) {
            val circle = rbModel.circles[i]
            val center = newVec()!!.set(circle.center).scl(scale)
            val radius = circle.radius * scale

            circleShape.position = center
            circleShape.radius = radius
            fd.shape = circleShape
            body.createFixture(fd)

            free(center)
            i++
        }
    }

    private fun readJson(str: String): Model {
        val m = Model()
        val root = JsonReader().parse(str)
        val bodies = root.get("rigidBodies")

        var body = bodies.child()
        while (body != null) {
            val rbModel = readRigidBody(body)
            m.rigidBodies[rbModel.name] = rbModel
            body = body.next()
        }

        return m
    }

    private fun readRigidBody(bodyElem: JsonValue): RigidBodyModel {
        val rbModel = RigidBodyModel()
        rbModel.name = bodyElem.getString("name")
        rbModel.imagePath = bodyElem.getString("imagePath")
        val origin = bodyElem.get("origin")
        rbModel.origin.x = origin.getFloat("x")
        rbModel.origin.y = origin.getFloat("y")
        val polygons = bodyElem.get("polygons")

        var vertices = polygons.child()
        while (vertices != null) {
            val polygon = PolygonModel()
            rbModel.polygons.add(polygon)

            var vertex = vertices.child()
            while (vertex != null) {
                polygon.vertices.add(Vector2(vertex.getFloat("x"), vertex.getFloat("y")))
                vertex = vertex.next()
            }

            polygon.buffer = arrayOfNulls(polygon.vertices.size)
            vertices = vertices.next()
        }

        val circles = bodyElem.get("circles")

        var circle = circles.child()
        while (circle != null) {
            val c = CircleModel()
            rbModel.circles.add(c)

            c.center.x = circle.getFloat("cx")
            c.center.y = circle.getFloat("cy")
            c.radius = circle.getFloat("r")
            circle = circle.next()
        }

        return rbModel
    }

    private fun newVec(): Vector2? {
        return if (vectorPool.isEmpty()) Vector2() else vectorPool.removeAt(0)
    }

    private fun free(v: Vector2?) {
        vectorPool.add(v)
    }

    class Model {
        val rigidBodies: MutableMap<String, RigidBodyModel> = HashMap()
    }

    class RigidBodyModel {
        val origin: Vector2 = Vector2()
        val polygons: MutableList<PolygonModel> = ArrayList()
        val circles: MutableList<CircleModel> = ArrayList()
        var name = ""
        var imagePath: String? = null
    }

    class PolygonModel {
        val vertices: MutableList<Vector2> = ArrayList()
        var buffer: Array<Vector2?>? = null // used to avoid allocation in attachFixture()
    }

    class CircleModel {
        val center: Vector2 = Vector2()
        var radius: Float = 0f
    }
}
