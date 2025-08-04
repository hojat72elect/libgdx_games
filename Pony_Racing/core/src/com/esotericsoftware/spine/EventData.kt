package com.esotericsoftware.spine

class EventData(val name: String) {
    var intValue = 0
    var floatValue = 0f
    var stringValue = ""

    override fun toString(): String = name
}