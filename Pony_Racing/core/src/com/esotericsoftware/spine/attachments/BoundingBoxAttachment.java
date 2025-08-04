package com.esotericsoftware.spine.attachments;

import com.esotericsoftware.spine.Bone;

public class BoundingBoxAttachment extends Attachment {
    private float[] vertices;

    public BoundingBoxAttachment(String name) {
        super(name);
    }

    public void computeWorldVertices(float x, float y, Bone bone, float[] worldVertices) {
        x += bone.getWorldX();
        y += bone.getWorldY();
        float m00 = bone.getM00();
        float m01 = bone.getM01();
        float m10 = bone.getM10();
        float m11 = bone.getM11();
        float[] vertices = this.vertices;
        for (int i = 0, n = vertices.length; i < n; i += 2) {
            float px = vertices[i];
            float py = vertices[i + 1];
            worldVertices[i] = px * m00 + py * m01 + x;
            worldVertices[i + 1] = px * m10 + py * m11 + y;
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }
}
