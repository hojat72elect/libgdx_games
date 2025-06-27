package io.github.masamune.component

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class Tag : EntityTags by entityTagOf() {
    CAMERA_FOCUS, // an entity with this tag gets the camera focused on its center
    EXECUTE_TRIGGER, // temporary tag to execute a trigger entity
    MAP_TRANSITION, // entities with this tag are not removed in TiledService when a map gets unloaded
    PAUSE,
}
