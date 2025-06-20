package com.mygdx.game.terrains

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.GdxRuntimeException
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * This is a test class, showing how one could implement a height field. See also HeightMapTest. Do not expect this to be
 * a fully supported and implemented height field class.
 *
 *
 * Represents a HeightField, which is an evenly spaced grid of values, where each value defines the height on that position of the
 * grid, so forming a 3D shape. Typically used for (relatively simple) terrains and such. See
 * [wikipedia](http://en.wikipedia.org/wiki/Heightmap) for more information.
 *
 *
 * A height field has a width and height, specifying the width and height of the grid. Points on this grid are specified using
 * integer values, named "x" and "y". Do not confuse these with the x, y and z floating point values representing coordinates in
 * world space.
 *
 *
 * The values of the heightfield are normalized. Meaning that they typically range from 0 to 1 (but they can be negative or more
 * than one). The plane of the heightfield can be specified using the [.corner00], [.corner01], [.corner10] and
 * [.corner11] members. Where `corner00` is the location on the grid at x:0, y;0, `corner01` at x:0, y:height-1, `corner10`
 * at x:width-1, y:0 and `corner11` the location on the grid at x:width-1, y:height-1.
 *
 *
 * The height and direction of the field can be set using the [.magnitude] vector. Typically this should be the vector
 * perpendicular to the heightfield. E.g. if the field is on the XZ plane, then the magnitude is typically pointing on the Y axis.
 * The length of the `magnitude` specifies the height of the height field. In other words, the word coordinate of a point on the
 * grid is specified as:
 *
 *
 * base[y * width + x] + magnitude * value[y * width + x]
 *
 *
 * Use the [.getPositionAt] method to get the coordinate of a specific point on the grid.
 *
 *
 * You can set this heightfield using the constructor or one of the `set` methods. E.g. by specifying an array of values or a
 * [Pixmap]. The latter can be used to load a HeightMap, which is an image loaded from disc of which each texel is used to
 * specify the value for each point on the field. Be aware that the total number of vertices cannot exceed 32k. Using a large
 * height map will result in unpredicted results.
 *
 *
 * You can also manually modify the heightfield by directly accessing the [.data] member. The index within this array can be
 * calculates as: `y * width + x`. E.g. `field.data[y * field.width + x] = value;`. When you modify the data then you can update
 * the [.mesh] using the [.update] method.
 *
 *
 * The [.mesh] member can be used to render the height field. The vertex attributes this mesh contains are specified in the
 * constructor. There are two ways for generating the mesh: smooth and sharp.
 *
 *
 * Smooth can be forced by specifying `true` for the `smooth` argument of the constructor. Otherwise it will be based on whether
 * the specified vertex attributes contains a normal attribute. If there is no normal attribute then the mesh will always be
 * smooth (even when you specify `false` in the constructor). In this case the number of vertices is the same as the amount of
 * grid points. Causing vertices to be shared amongst multiple faces.
 *
 *
 * Sharp will be used if the vertex attributes contains a normal attribute and you didnt specify `true` for the `smooth` argument
 * of the constructor. This will cause the number of vertices to be around four times the amount grid points and each normal is
 * estimated for each face instead of each point.
 */
class HeightField(isStatic: Boolean, width: Int, height: Int, smooth: Boolean, attributes: VertexAttributes) : Disposable {
    private val uvOffset: Vector2 = Vector2(0f, 0f)
    private val uvScale: Vector2 = Vector2(1f, 1f)
    private val color00: Color = Color(Color.WHITE)
    private val color10: Color = Color(Color.WHITE)
    private val color01: Color = Color(Color.WHITE)
    private val color11: Color = Color(Color.WHITE)
    val corner00: Vector3 = Vector3(0f, 0f, 0f)
    val corner10: Vector3 = Vector3(1f, 0f, 0f)
    val corner01: Vector3 = Vector3(0f, 0f, 1f)
    val corner11: Vector3 = Vector3(1f, 0f, 1f)
    val magnitude: Vector3 = Vector3(0f, 1f, 0f)

    val data: FloatArray
    private val width: Int
    private val height: Int
    private val smooth: Boolean
    val mesh: Mesh

    private val vertices: FloatArray
    private val stride: Int

    private val posPos: Int
    private val norPos: Int
    private val uvPos: Int
    private val colPos: Int

    private val vertex00 = VertexInfo()
    private val vertex10 = VertexInfo()
    private val vertex01 = VertexInfo()
    private val vertex11 = VertexInfo()

    private val tmpV1 = Vector3()
    private val tmpV2 = Vector3()
    private val tmpV3 = Vector3()
    private val tmpV4 = Vector3()
    private val tmpV5 = Vector3()
    private val tmpV6 = Vector3()
    private val tmpV7 = Vector3()
    private val tmpV8 = Vector3()
    private val tmpC = Color()

    constructor(isStatic: Boolean, map: Pixmap, smooth: Boolean, attributes: Int) : this(isStatic, map.width, map.height, smooth, attributes) {
        set(map)
    }

    constructor(isStatic: Boolean, width: Int, height: Int, smooth: Boolean, attributes: Int) : this(isStatic, width, height, smooth, MeshBuilder.createAttributes(attributes.toLong()))

    init {
        var smooth = smooth
        this.posPos = attributes.getOffset(VertexAttributes.Usage.Position, -1)
        this.norPos = attributes.getOffset(VertexAttributes.Usage.Normal, -1)
        this.uvPos = attributes.getOffset(VertexAttributes.Usage.TextureCoordinates, -1)
        this.colPos = attributes.getOffset(VertexAttributes.Usage.ColorUnpacked, -1)
        smooth = smooth || (norPos < 0) // cant have sharp edges without normals

        this.width = width
        this.height = height
        this.smooth = smooth
        this.data = FloatArray(width * height)

        this.stride = attributes.vertexSize / 4

        val numVertices = if (smooth) width * height else (width - 1) * (height - 1) * 4
        val numIndices = (width - 1) * (height - 1) * 6

        this.mesh = Mesh(isStatic, numVertices, numIndices, attributes)
        this.vertices = FloatArray(numVertices * stride)

        setIndices()
    }

    private fun setIndices() {
        val w = width - 1
        val h = height - 1
        val indices = ShortArray(w * h * 6)
        var i = -1
        for (y in 0..<h) {
            for (x in 0..<w) {
                val c00 = if (smooth) (y * width + x) else (y * 2 * w + x * 2)
                val c10 = c00 + 1
                val c01 = c00 + (if (smooth) width else w * 2)
                val c11 = c10 + (if (smooth) width else w * 2)
                indices[++i] = c11.toShort()
                indices[++i] = c10.toShort()
                indices[++i] = c00.toShort()
                indices[++i] = c00.toShort()
                indices[++i] = c01.toShort()
                indices[++i] = c11.toShort()
            }
        }
        mesh.setIndices(indices)
    }

    fun update() {
        if (smooth) {
            if (norPos < 0) updateSimple()
            else updateSmooth()
        } else updateSharp()
    }

    private fun updateSmooth() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                val v = getVertexAt(vertex00, x, y)
                getWeightedNormalAt(v.normal, x, y)
                setVertex(y * width + x, v)
            }
        }
        mesh.setVertices(vertices)
    }

    private fun updateSimple() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                setVertex(y * width + x, getVertexAt(vertex00, x, y))
            }
        }
        mesh.setVertices(vertices)
    }

    private fun updateSharp() {
        val w = width - 1
        val h = height - 1
        for (y in 0..<h) {
            for (x in 0..<w) {
                val c00 = (y * 2 * w + x * 2)
                val c10 = c00 + 1
                val c01 = c00 + w * 2
                val c11 = c10 + w * 2
                val v00 = getVertexAt(vertex00, x, y)
                val v10 = getVertexAt(vertex10, x + 1, y)
                val v01 = getVertexAt(vertex01, x, y + 1)
                val v11 = getVertexAt(vertex11, x + 1, y + 1)
                v01.normal.set(v01.position).sub(v00.position).nor().crs(tmpV1.set(v11.position).sub(v01.position).nor())
                v10.normal.set(v10.position).sub(v11.position).nor().crs(tmpV1.set(v00.position).sub(v10.position).nor())
                v00.normal.set(v01.normal).lerp(v10.normal, .5f)
                v11.normal.set(v00.normal)

                setVertex(c00, v00)
                setVertex(c10, v10)
                setVertex(c01, v01)
                setVertex(c11, v11)
            }
        }
        mesh.setVertices(vertices)
    }

    /**
     * Does not set the normal member!
     */
    private fun getVertexAt(out: VertexInfo, x: Int, y: Int): VertexInfo {
        val dx = x.toFloat() / (width - 1).toFloat()
        val dy = y.toFloat() / (height - 1).toFloat()
        val a = data[y * width + x]
        out.position.set(corner00).lerp(corner10, dx).lerp(tmpV1.set(corner01).lerp(corner11, dx), dy)
        out.position.add(tmpV1.set(magnitude).scl(a))
        out.color.set(color00).lerp(color10, dx).lerp(tmpC.set(color01).lerp(color11, dx), dy)
        out.uv.set(dx, dy).scl(uvScale).add(uvOffset)
        return out
    }

    private fun getPositionAt(out: Vector3, x: Int, y: Int): Vector3 {
        val dx = x.toFloat() / (width - 1).toFloat()
        val dy = y.toFloat() / (height - 1).toFloat()
        val a = data[y * width + x]
        out.set(corner00).lerp(corner10, dx).lerp(tmpV1.set(corner01).lerp(corner11, dx), dy)
        out.add(tmpV1.set(magnitude).scl(a))
        return out
    }

    private fun getWeightedNormalAt(out: Vector3, x: Int, y: Int) {
        var faces = 0
        out[0f, 0f] = 0f

        val center = getPositionAt(tmpV2, x, y)
        val left = if (x > 0) getPositionAt(tmpV3, x - 1, y) else null
        val right = if (x < (width - 1)) getPositionAt(tmpV4, x + 1, y) else null
        val bottom = if (y > 0) getPositionAt(tmpV5, x, y - 1) else null
        val top = if (y < (height - 1)) getPositionAt(tmpV6, x, y + 1) else null
        if (top != null && left != null) {
            out.add(tmpV7.set(top).sub(center).nor().crs(tmpV8.set(center).sub(left).nor()).nor())
            faces++
        }
        if (left != null && bottom != null) {
            out.add(tmpV7.set(left).sub(center).nor().crs(tmpV8.set(center).sub(bottom).nor()).nor())
            faces++
        }
        if (bottom != null && right != null) {
            out.add(tmpV7.set(bottom).sub(center).nor().crs(tmpV8.set(center).sub(right).nor()).nor())
            faces++
        }
        if (right != null && top != null) {
            out.add(tmpV7.set(right).sub(center).nor().crs(tmpV8.set(center).sub(top).nor()).nor())
            faces++
        }
        if (faces != 0) out.scl(1f / faces.toFloat())
        else out.set(magnitude).nor()
    }

    private fun setVertex(index: Int, info: VertexInfo) {
        var index = index
        index *= stride
        if (posPos >= 0) {
            vertices[index + posPos] = info.position.x
            vertices[index + posPos + 1] = info.position.y
            vertices[index + posPos + 2] = info.position.z
        }
        if (norPos >= 0) {
            vertices[index + norPos] = info.normal.x
            vertices[index + norPos + 1] = info.normal.y
            vertices[index + norPos + 2] = info.normal.z
        }
        if (uvPos >= 0) {
            vertices[index + uvPos] = info.uv.x
            vertices[index + uvPos + 1] = info.uv.y
        }
        if (colPos >= 0) {
            vertices[index + colPos] = info.color.r
            vertices[index + colPos + 1] = info.color.g
            vertices[index + colPos + 2] = info.color.b
            vertices[index + colPos + 3] = info.color.a
        }
    }

    fun set(map: Pixmap) {
        if (map.width != width || map.height != height) throw GdxRuntimeException("Incorrect map size")
        set(map.pixels, map.format)
    }

    fun set(colorData: ByteBuffer, format: Pixmap.Format) {
        set(heightColorsToMap(colorData, format, width, height))
    }

    @JvmOverloads
    fun set(data: FloatArray, offset: Int = 0) {
        if (this.data.size > (data.size - offset)) throw GdxRuntimeException("Incorrect data size")
        System.arraycopy(data, offset, this.data, 0, this.data.size)
        update()
    }

    override fun dispose() {
        mesh.dispose()
    }

    companion object {
        /**
         * Simply creates an array containing only all the red components of the data.
         */
        fun heightColorsToMap(data: ByteBuffer, format: Pixmap.Format, width: Int, height: Int): FloatArray {
            val bytesPerColor = (if (format == Pixmap.Format.RGB888) 3 else (if (format == Pixmap.Format.RGBA8888) 4 else 0))
            if (bytesPerColor == 0) throw GdxRuntimeException("Unsupported format, should be either RGB8 or RGBA8")
            if (data.remaining() < (width * height * bytesPerColor)) throw GdxRuntimeException("Incorrect map size")

            val startPos = data.position()
            val source: ByteArray
            var sourceOffset = 0
            if (data.hasArray() && !data.isReadOnly) {
                source = data.array()
                sourceOffset = data.arrayOffset() + startPos
            } else {
                source = ByteArray(width * height * bytesPerColor)
                data[source]
                (data as Buffer).position(startPos)
            }

            val dest = FloatArray(width * height)
            for (i in dest.indices) {
                var v = source[sourceOffset + i * bytesPerColor].toInt()
                v = if (v < 0) 256 + v else v
                dest[i] = v.toFloat() / 255f
            }

            return dest
        }
    }
}
