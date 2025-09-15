package com.bitfire.uracer.utils

fun timeDilationToAudioPitch(pitchIn: Float, timeMultiplier: Float) = pitchIn - (1 - timeMultiplier) * 0.4F
