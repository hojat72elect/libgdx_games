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

public class SlotData {
    final String name;
    final BoneData boneData;
    final Color color = new Color(1, 1, 1, 1);
    String attachmentName;
    boolean additiveBlending;

    SlotData() {
        name = null;
        boneData = null;
    }

    public SlotData(String name, BoneData boneData) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        if (boneData == null) throw new IllegalArgumentException("boneData cannot be null.");
        this.name = name;
        this.boneData = boneData;
    }

    public String getName() {
        return name;
    }

    public BoneData getBoneData() {
        return boneData;
    }

    public Color getColor() {
        return color;
    }

    /**
     * @return May be null.
     */
    public String getAttachmentName() {
        return attachmentName;
    }

    /**
     * @param attachmentName May be null.
     */
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public boolean getAdditiveBlending() {
        return additiveBlending;
    }

    public void setAdditiveBlending(boolean additiveBlending) {
        this.additiveBlending = additiveBlending;
    }

    public String toString() {
        return name;
    }
}
