package io.github.masamune.quest

import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import kotlinx.serialization.Serializable

@Serializable
sealed interface Quest : EventListener {
    val i18nKey: String

    fun isCompleted(): Boolean

    fun complete()

    override fun onEvent(event: Event) = Unit
}

@Serializable
data class MainQuest(var progress: Int = 0) : Quest {
    override val i18nKey = "quest.main"

    override fun isCompleted(): Boolean = progress == 100

    override fun complete() {
        progress = 100
    }
}

@Serializable
data class FlowerGirlQuest(var progress: Int = 0) : Quest {
    override val i18nKey = "quest.flower_girl"

    override fun isCompleted(): Boolean = progress == 100

    override fun complete() {
        progress = 100
    }
}

@Serializable
data class MonsterBookQuest(var completed: Boolean = false) : Quest {
    override val i18nKey = "quest.monster_book"

    override fun isCompleted(): Boolean = completed

    override fun complete() {
        completed = true
    }
}

@Serializable
data class SpiritQuest(var progress: Int = 0) : Quest {
    override val i18nKey = "quest.spirit"

    override fun isCompleted(): Boolean = progress == 100

    override fun complete() {
        progress = 100
    }
}
