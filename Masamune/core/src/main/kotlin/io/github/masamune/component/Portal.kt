package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.asset.TiledMapAsset

data class Portal(
    val toMapAsset: TiledMapAsset,
    val toPortalId: Int,
) : Component<Portal> {
    override fun type() = Portal

    companion object : ComponentType<Portal>()
}
