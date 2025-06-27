package io.github.masamune.ui.model

import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.World
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.event.MenuEndEvent
import io.github.masamune.quest.Quest

class QuestViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val eventService: EventService,
) : ViewModel(bundle, audioService) {

    private val emptyQuest = QuestModel(i18nTxt(I18NKey.QUEST_NONE_NAME), i18nTxt(I18NKey.QUEST_NONE_DESCRIPTION))
    var completedQuests: List<QuestModel> = emptyList()

    var activeQuests: List<QuestModel> by propertyNotify(emptyList())

    private fun Quest.toQuestModel(): QuestModel {
        return QuestModel(bundle["${this.i18nKey}.name"], bundle["${this.i18nKey}.description"])
    }

    override fun onEvent(event: Event) {
        if (event !is MenuBeginEvent || event.type != MenuType.QUEST) {
            return
        }

        with(world) {
            val player = world.family { all(Player) }.first()
            val questPartition = player[QuestLog].quests.partition { it.isCompleted() }
            activeQuests = questPartition.second
                .map { it.toQuestModel() }
                .ifEmpty { listOf(emptyQuest) }
            completedQuests = questPartition.first
                .map { it.toQuestModel() }
                .ifEmpty { listOf(emptyQuest) }
        }
    }

    fun quit() {
        eventService.fire(MenuEndEvent)
        eventService.fire(MenuBeginEvent(MenuType.GAME))
        // clearing active quests will hide the quest view
        activeQuests = emptyList()
        completedQuests = emptyList()
    }

}
