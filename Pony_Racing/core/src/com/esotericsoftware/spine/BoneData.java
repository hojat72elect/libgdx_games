/******************************************************************************
 * Spine Runtimes Software License
 * Version 2
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software, you may not (a) modify, translate, adapt or
 * otherwise create derivative works, improvements of the Software or develop
 * new applications using the Software or (b) remove, delete, alter or obscure
 * any trademarks or any copyright, trademark, patent or other intellectual
 * property or proprietary rights notices on or in the Software, including
 * any copy thereof. Redistributions in binary or source form must include
 * this license and terms. THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.Color;

public class BoneData {
    final BoneData parent;
    final String name;
    // Nonessential.
    final Color color = new Color(1, 1, 1, 1);
    float length;
    float x, y;
    float rotation;
    float scaleX = 1, scaleY = 1;
    boolean inheritScale = true, inheritRotation = true;

    /**
     * @param parent May be null.
     */
    public BoneData(String name, BoneData parent) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        this.name = name;
        this.parent = parent;
    }

    /**
     * Copy constructor.
     *
     * @param parent May be null.
     */
    public BoneData(BoneData bone, BoneData parent) {
        if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
        this.parent = parent;
        name = bone.name;
        length = bone.length;
        x = bone.x;
        y = bone.y;
        rotation = bone.rotation;
        scaleX = bone.scaleX;
        scaleY = bone.scaleY;
    }

    /**
     * @return May be null.
     */
    public BoneData getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public boolean getInheritScale() {
        return inheritScale;
    }

    public void setInheritScale(boolean inheritScale) {
        this.inheritScale = inheritScale;
    }

    public boolean getInheritRotation() {
        return inheritRotation;
    }

    public void setInheritRotation(boolean inheritRotation) {
        this.inheritRotation = inheritRotation;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return name;
    }
}
