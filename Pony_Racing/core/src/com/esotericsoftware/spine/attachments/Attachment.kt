package com.esotericsoftware.spine.attachments

abstract class Attachment(val name: String) {

    init {
        requireNotNull(name) { "name cannot be null." }
    }

    override fun toString(): String {
        return name
    }
}