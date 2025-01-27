package com.mygdx.game

object Settings {
    const val CAMERA_START_PITCH = 20F // Default Starting pitch
    const val CAMERA_MIN_PITCH = CAMERA_START_PITCH - 20 // Min Pitch
    const val CAMERA_MAX_PITCH = CAMERA_START_PITCH + 20 // Max Pitch
    const val CAMERA_PITCH_FACTOR = 0.3F
    const val CAMERA_ZOOM_LEVEL_FACTOR = 0.5F // Our zoom multiplier (speed)
    const val CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2F // Rotation around player speed
    const val CAMERA_MIN_DISTANCE_FROM_PLAYER = 4F // Min zoom distance
}