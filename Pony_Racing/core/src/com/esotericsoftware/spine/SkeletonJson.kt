package com.esotericsoftware.spine

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.SerializationException
import com.esotericsoftware.spine.Animation.AttachmentTimeline
import com.esotericsoftware.spine.Animation.ColorTimeline
import com.esotericsoftware.spine.Animation.CurveTimeline
import com.esotericsoftware.spine.Animation.DrawOrderTimeline
import com.esotericsoftware.spine.Animation.EventTimeline
import com.esotericsoftware.spine.Animation.FfdTimeline
import com.esotericsoftware.spine.Animation.RotateTimeline
import com.esotericsoftware.spine.Animation.ScaleTimeline
import com.esotericsoftware.spine.Animation.TranslateTimeline
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader
import com.esotericsoftware.spine.attachments.Attachment
import com.esotericsoftware.spine.attachments.AttachmentType
import com.esotericsoftware.spine.attachments.MeshAttachment
import kotlin.math.max

class SkeletonJson(atlas: TextureAtlas) {
    private val attachmentLoader = AtlasAttachmentLoader(atlas)
    private var scale = 1f


    /**
     * Scales the bones, images, and animations as they are loaded.
     */
    fun setScale(scale: Float) {
        this.scale = scale
    }

    fun readSkeletonData(file: FileHandle): SkeletonData {
        requireNotNull(file) { "file cannot be null." }

        val scale = this.scale

        val skeletonData = SkeletonData()
        skeletonData.name = file.nameWithoutExtension()

        val root = JsonReader().parse(file)

        // Bones.
        var boneMap = root.getChild("bones")
        while (boneMap != null) {
            var parent: BoneData? = null
            val parentName = boneMap.getString("parent", null)
            if (parentName != null) {
                parent = skeletonData.findBone(parentName)
                if (parent == null) throw SerializationException("Parent bone not found: " + parentName)
            }
            val boneData = BoneData(boneMap.getString("name"), parent)
            boneData.length = boneMap.getFloat("length", 0f) * scale
            boneData.x = boneMap.getFloat("x", 0f) * scale
            boneData.y = boneMap.getFloat("y", 0f) * scale
            boneData.rotation = boneMap.getFloat("rotation", 0f)
            boneData.scaleX = boneMap.getFloat("scaleX", 1f)
            boneData.scaleY = boneMap.getFloat("scaleY", 1f)
            boneData.inheritScale = boneMap.getBoolean("inheritScale", true)
            boneData.inheritRotation = boneMap.getBoolean("inheritRotation", true)

            val color = boneMap.getString("color", null)
            if (color != null) boneData.color.set(Color.valueOf(color))

            skeletonData.addBone(boneData)
            boneMap = boneMap.next
        }

        // Slots.
        var slotMap = root.getChild("slots")
        while (slotMap != null) {
            val slotName = slotMap.getString("name")
            val boneName = slotMap.getString("bone")
            val boneData = skeletonData.findBone(boneName)
            if (boneData == null) throw SerializationException("Slot bone not found: " + boneName)
            val slotData = SlotData(slotName, boneData)

            val color = slotMap.getString("color", null)
            if (color != null) slotData.color.set(Color.valueOf(color))

            slotData.attachmentName = slotMap.getString("attachment", null)

            slotData.additiveBlending = slotMap.getBoolean("additive", false)

            skeletonData.addSlot(slotData)
            slotMap = slotMap.next
        }

        // Skins.
        var skinMap = root.getChild("skins")
        while (skinMap != null) {
            val skin = Skin(skinMap.name)
            var slotEntry = skinMap.child
            while (slotEntry != null) {
                val slotIndex = skeletonData.findSlotIndex(slotEntry.name)
                if (slotIndex == -1) throw SerializationException("Slot not found: " + slotEntry.name)
                var entry = slotEntry.child
                while (entry != null) {
                    val attachment = readAttachment(skin, entry.name, entry)
                    if (attachment != null) skin.addAttachment(slotIndex, entry.name, attachment)
                    entry = entry.next
                }
                slotEntry = slotEntry.next
            }
            skeletonData.addSkin(skin)
            if (skin.name == "default") skeletonData.defaultSkin = skin
            skinMap = skinMap.next
        }

        // Events.
        var eventMap = root.getChild("events")
        while (eventMap != null) {
            val eventData = EventData(eventMap.name)
            eventData.intValue = eventMap.getInt("int", 0)
            eventData.floatValue = eventMap.getFloat("float", 0f)
            eventData.stringValue = eventMap.getString("string", null)
            skeletonData.addEvent(eventData)
            eventMap = eventMap.next
        }

        // Animations.
        var animationMap = root.getChild("animations")
        while (animationMap != null) {
            readAnimation(animationMap.name, animationMap, skeletonData)
            animationMap = animationMap.next
        }

        skeletonData.bones.shrink()
        skeletonData.slots.shrink()
        skeletonData.skins.shrink()
        skeletonData.animations.shrink()
        return skeletonData
    }

    private fun readAttachment(skin: Skin, name: String, map: JsonValue): Attachment? {
        var name = name
        val scale = this.scale
        name = map.getString("name", name)

        when (AttachmentType.valueOf(map.getString("type", AttachmentType.REGION.name))) {
            AttachmentType.REGION -> {
                val region = attachmentLoader.newRegionAttachment(skin, name, map.getString("path", name))
                region!!.x = map.getFloat("x", 0f) * scale
                region.y = map.getFloat("y", 0f) * scale
                region.scaleX = map.getFloat("scaleX", 1f)
                region.scaleY = map.getFloat("scaleY", 1f)
                region.rotation = map.getFloat("rotation", 0f)
                region.width = map.getFloat("width") * scale
                region.height = map.getFloat("height") * scale

                val color = map.getString("color", null)
                if (color != null) region.color.set(Color.valueOf(color))

                region.updateOffset()
                return region
            }

            AttachmentType.BOUNDING_BOX -> {
                val box = attachmentLoader.newBoundingBoxAttachment(skin, name)
                val vertices = map.require("vertices").asFloatArray()
                if (scale != 1f) {
                    var i = 0
                    val n = vertices.size
                    while (i < n) {
                        vertices[i] *= scale
                        i++
                    }
                }
                box!!.vertices = vertices
                return box
            }

            AttachmentType.MESH -> {
                val mesh = attachmentLoader.newMeshAttachment(skin, name, map.getString("path", name))
                val vertices = map.require("vertices").asFloatArray()
                if (scale != 1f) {
                    var i = 0
                    val n = vertices.size
                    while (i < n) {
                        vertices[i] *= scale
                        i++
                    }
                }
                val triangles = map.require("triangles").asShortArray()
                val uvs = map.require("uvs").asFloatArray()
                mesh!!.setMesh(vertices, triangles, uvs)
                mesh.width = map.getFloat("width", 0f) * scale
                mesh.height = map.getFloat("height", 0f) * scale
                return mesh
            }
        }

    }

    private fun readAnimation(name: String?, map: JsonValue, skeletonData: SkeletonData) {
        val timelines = Array<Animation.Timeline?>()
        var duration = 0f

        // Slot timelines.
        var slotMap = map.getChild("slots")
        while (slotMap != null) {
            val slotIndex = skeletonData.findSlotIndex(slotMap.name)
            if (slotIndex == -1) throw SerializationException("Slot not found: " + slotMap.name)

            var timelineMap = slotMap.child
            while (timelineMap != null) {
                val timelineName = timelineMap.name
                if (timelineName == "color") {
                    val timeline = ColorTimeline(timelineMap.size)
                    timeline.slotIndex = slotIndex

                    var frameIndex = 0
                    var valueMap = timelineMap.child
                    while (valueMap != null) {
                        val color = Color.valueOf(valueMap.getString("color"))
                        timeline.setFrame(frameIndex, valueMap.getFloat("time"), color.r, color.g, color.b, color.a)
                        readCurve(timeline, frameIndex, valueMap)
                        frameIndex++
                        valueMap = valueMap.next
                    }
                    timelines.add(timeline)
                    duration = max(duration, timeline.frames[timeline.frameCount * 5 - 5])
                } else if (timelineName == "attachment") {
                    val timeline = AttachmentTimeline(timelineMap.size)
                    timeline.slotIndex = slotIndex

                    var frameIndex = 0
                    var valueMap = timelineMap.child
                    while (valueMap != null) {
                        timeline.setFrame(frameIndex++, valueMap.getFloat("time"), valueMap.getString("name"))
                        valueMap = valueMap.next
                    }
                    timelines.add(timeline)
                    duration = max(duration, timeline.frames[timeline.frameCount - 1])
                } else throw RuntimeException("Invalid timeline type for a slot: " + timelineName + " (" + slotMap.name + ")")
                timelineMap = timelineMap.next
            }
            slotMap = slotMap.next
        }

        // Bone timelines.
        var boneMap = map.getChild("bones")
        while (boneMap != null) {
            val boneIndex = skeletonData.findBoneIndex(boneMap.name)
            if (boneIndex == -1) throw SerializationException("Bone not found: " + boneMap.name)

            var timelineMap = boneMap.child
            while (timelineMap != null) {
                val timelineName = timelineMap.name
                if (timelineName == "rotate") {
                    val timeline = RotateTimeline(timelineMap.size)
                    timeline.boneIndex = boneIndex

                    var frameIndex = 0
                    var valueMap = timelineMap.child
                    while (valueMap != null) {
                        timeline.setFrame(frameIndex, valueMap.getFloat("time"), valueMap.getFloat("angle"))
                        readCurve(timeline, frameIndex, valueMap)
                        frameIndex++
                        valueMap = valueMap.next
                    }
                    timelines.add(timeline)
                    duration = max(duration, timeline.frames[timeline.frameCount * 2 - 2])
                } else if (timelineName == "translate" || timelineName == "scale") {
                    val timeline: TranslateTimeline?
                    var timelineScale = 1f
                    if (timelineName == "scale") timeline = ScaleTimeline(timelineMap.size)
                    else {
                        timeline = TranslateTimeline(timelineMap.size)
                        timelineScale = scale
                    }
                    timeline.boneIndex = boneIndex

                    var frameIndex = 0
                    var valueMap = timelineMap.child
                    while (valueMap != null) {
                        val x = valueMap.getFloat("x", 0f)
                        val y = valueMap.getFloat("y", 0f)
                        timeline.setFrame(frameIndex, valueMap.getFloat("time"), x * timelineScale, y * timelineScale)
                        readCurve(timeline, frameIndex, valueMap)
                        frameIndex++
                        valueMap = valueMap.next
                    }
                    timelines.add(timeline)
                    duration = max(duration, timeline.getFrames()[timeline.frameCount * 3 - 3])
                } else throw RuntimeException("Invalid timeline type for a bone: " + timelineName + " (" + boneMap.name + ")")
                timelineMap = timelineMap.next
            }
            boneMap = boneMap.next
        }

        // FFD timelines.
        var ffdMap = map.getChild("ffd")
        while (ffdMap != null) {
            val skin = skeletonData.findSkin(ffdMap.name)
            if (skin == null) throw SerializationException("Skin not found: " + ffdMap.name)
            var slotMap = ffdMap.child
            while (slotMap != null) {
                val slotIndex = skeletonData.findSlotIndex(slotMap.name)
                if (slotIndex == -1) throw SerializationException("Slot not found: " + slotMap.name)
                var meshMap = slotMap.child
                while (meshMap != null) {
                    val timeline = FfdTimeline(meshMap.size)
                    val mesh = skin.getAttachment(slotIndex, meshMap.name) as MeshAttachment?
                    if (mesh == null) throw SerializationException("Mesh attachment not found: " + meshMap.name)
                    timeline.slotIndex = slotIndex
                    timeline.meshAttachment = mesh

                    var frameIndex = 0
                    var valueMap = meshMap.child
                    while (valueMap != null) {
                        val vertices: FloatArray
                        val verticesValue = valueMap.get("vertices")
                        if (verticesValue == null) vertices = mesh.vertices
                        else {
                            vertices = verticesValue.asFloatArray()
                            if (scale != 1f) {
                                var i = 0
                                val n = vertices.size
                                while (i < n) {
                                    vertices[i] *= scale
                                    i++
                                }
                            }
                        }
                        timeline.setFrame(frameIndex, valueMap.getFloat("time"), vertices)
                        readCurve(timeline, frameIndex, valueMap)
                        frameIndex++
                        valueMap = valueMap.next
                    }
                    timelines.add(timeline)
                    duration = max(duration, timeline.frames[timeline.frameCount - 1])
                    meshMap = meshMap.next
                }
                slotMap = slotMap.next
            }
            ffdMap = ffdMap.next
        }

        // Draw order timeline.
        val drawOrdersMap = map.get("draworder")
        if (drawOrdersMap != null) {
            val timeline = DrawOrderTimeline(drawOrdersMap.size)
            val slotCount = skeletonData.slots.size
            var frameIndex = 0
            var drawOrderMap = drawOrdersMap.child
            while (drawOrderMap != null) {
                var drawOrder: IntArray? = null
                val offsets = drawOrderMap.get("offsets")
                if (offsets != null) {
                    drawOrder = IntArray(slotCount)
                    for (i in slotCount - 1 downTo 0) drawOrder[i] = -1
                    val unchanged = IntArray(slotCount - offsets.size)
                    var originalIndex = 0
                    var unchangedIndex = 0
                    var offsetMap = offsets.child
                    while (offsetMap != null) {
                        val slotIndex = skeletonData.findSlotIndex(offsetMap.getString("slot"))
                        if (slotIndex == -1) throw SerializationException("Slot not found: " + offsetMap.getString("slot"))
                        // Collect unchanged items.
                        while (originalIndex != slotIndex) unchanged[unchangedIndex++] = originalIndex++
                        // Set changed items.
                        drawOrder[originalIndex + offsetMap.getInt("offset")] = originalIndex++
                        offsetMap = offsetMap.next
                    }
                    // Collect remaining unchanged items.
                    while (originalIndex < slotCount) unchanged[unchangedIndex++] = originalIndex++
                    // Fill in unchanged items.
                    for (i in slotCount - 1 downTo 0) if (drawOrder[i] == -1) drawOrder[i] = unchanged[--unchangedIndex]
                }
                timeline.setFrame(frameIndex++, drawOrderMap.getFloat("time"), drawOrder)
                drawOrderMap = drawOrderMap.next
            }
            timelines.add(timeline)
            duration = max(duration, timeline.frames[timeline.frameCount - 1])
        }

        // Event timeline.
        val eventsMap = map.get("events")
        if (eventsMap != null) {
            val timeline = EventTimeline(eventsMap.size)
            var frameIndex = 0
            var eventMap = eventsMap.child
            while (eventMap != null) {
                val eventData = skeletonData.findEvent(eventMap.getString("name"))
                if (eventData == null) throw SerializationException("Event not found: " + eventMap.getString("name"))
                val event = Event(eventData)
                event.intValue = eventMap.getInt("int", eventData.intValue)
                event.floatValue = eventMap.getFloat("float", eventData.floatValue)
                event.stringValue = eventMap.getString("string", eventData.stringValue)
                timeline.setFrame(frameIndex++, eventMap.getFloat("time"), event)
                eventMap = eventMap.next
            }
            timelines.add(timeline)
            duration = max(duration, timeline.frames[timeline.frameCount - 1])
        }

        timelines.shrink()
        skeletonData.addAnimation(Animation(name, timelines, duration))
    }

    fun readCurve(timeline: CurveTimeline, frameIndex: Int, valueMap: JsonValue) {
        val curve = valueMap.get("curve")
        if (curve == null) return
        if (curve.isString && curve.asString() == "stepped") timeline.setStepped(frameIndex)
        else if (curve.isArray) {
            timeline.setCurve(frameIndex, curve.getFloat(0), curve.getFloat(1), curve.getFloat(2), curve.getFloat(3))
        }
    }
}
