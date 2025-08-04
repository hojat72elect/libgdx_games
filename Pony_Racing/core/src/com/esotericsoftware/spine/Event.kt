package com.esotericsoftware.spine

class Event(val data: EventData) {
    var intValue = 0
    var floatValue = 0f
    var stringValue = ""

    override fun toString(): String = data.name
}
