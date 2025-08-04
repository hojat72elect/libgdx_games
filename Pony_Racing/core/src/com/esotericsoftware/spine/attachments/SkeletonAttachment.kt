package com.esotericsoftware.spine.attachments

import com.esotericsoftware.spine.Skeleton

/**
 * Attachment that displays a skeleton.
 */
class SkeletonAttachment(name: String) : Attachment(name) {

    // It's possible that the skeleton of this attachment be null
    var skeleton: Skeleton? = null
}