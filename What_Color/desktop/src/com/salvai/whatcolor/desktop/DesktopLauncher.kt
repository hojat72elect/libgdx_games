package com.salvai.whatcolor.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.salvai.whatcolor.WhatColor

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    Lwjgl3Application(WhatColor(), config)
}

