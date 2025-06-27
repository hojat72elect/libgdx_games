package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.quest.Quest
import kotlinx.serialization.Serializable

@Serializable
data class QuestLog(val quests: MutableList<Quest> = mutableListOf()) : Component<QuestLog> {
    override fun type() = QuestLog

    inline fun <reified T : Quest> getOrNull(): T? = quests.filterIsInstance<T>().firstOrNull()

    inline fun <reified T : Quest> get(): T = quests.filterIsInstance<T>().first()

    companion object : ComponentType<QuestLog>()
}
