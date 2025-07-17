package com.salvai.centrum.utils

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector

fun circleHitsCircle(circle1: Circle, circle2: Circle) = Intersector.overlaps(circle1, circle2)
