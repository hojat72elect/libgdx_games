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
import com.badlogic.gdx.utils.FloatArray;
import com.esotericsoftware.spine.attachments.Attachment;

public class Slot {
    final SlotData data;
    final Bone bone;
    final Color color;
    private final Skeleton skeleton;
    private final FloatArray attachmentVertices = new FloatArray();
    Attachment attachment;
    private float attachmentTime;

    Slot() {
        data = null;
        bone = null;
        skeleton = null;
        color = new Color(1, 1, 1, 1);
    }

    public Slot(SlotData data, Skeleton skeleton, Bone bone) {
        if (data == null) throw new IllegalArgumentException("data cannot be null.");
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
        if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
        this.data = data;
        this.skeleton = skeleton;
        this.bone = bone;
        color = new Color();
        setToSetupPose();
    }

    /**
     * Copy constructor.
     */
    public Slot(Slot slot, Skeleton skeleton, Bone bone) {
        if (slot == null) throw new IllegalArgumentException("slot cannot be null.");
        if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
        if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
        data = slot.data;
        this.skeleton = skeleton;
        this.bone = bone;
        color = new Color(slot.color);
        attachment = slot.attachment;
        attachmentTime = slot.attachmentTime;
    }

    public SlotData getData() {
        return data;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public Bone getBone() {
        return bone;
    }

    public Color getColor() {
        return color;
    }

    /**
     * @return May be null.
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * Sets the attachment, resets {@link #getAttachmentTime()}, and clears {@link #getAttachmentVertices()}.
     *
     * @param attachment May be null.
     */
    public void setAttachment(Attachment attachment) {
        if (this.attachment == attachment) return;
        this.attachment = attachment;
        attachmentTime = skeleton.time;
        attachmentVertices.clear();
    }

    /**
     * Returns the time since the attachment was set.
     */
    public float getAttachmentTime() {
        return skeleton.time - attachmentTime;
    }

    public void setAttachmentTime(float time) {
        attachmentTime = skeleton.time - time;
    }

    public FloatArray getAttachmentVertices() {
        return attachmentVertices;
    }

    void setToSetupPose(int slotIndex) {
        color.set(data.color);
        setAttachment(data.attachmentName == null ? null : skeleton.getAttachment(slotIndex, data.attachmentName));
        // BOZO - Set mesh to setup pose.
        // attachmentVertices.clear();
    }

    public void setToSetupPose() {
        setToSetupPose(skeleton.data.slots.indexOf(data, true));
    }

    public String toString() {
        return data.name;
    }
}
