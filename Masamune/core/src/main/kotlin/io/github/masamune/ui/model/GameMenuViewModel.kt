package io.github.masamune.ui.model

import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune
import io.github.masamune.audio.AudioService
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.Player
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.event.MenuEndEvent
import io.github.masamune.event.SaveEvent
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.screen.MainMenuScreen

class GameMenuViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val eventService: EventService,
    private val masamune: Masamune,
) : ViewModel(bundle, audioService) {

    private val playerEntities = world.family { all(Player) }
    var options: List<String> by propertyNotify(listOf())

    fun triggerOption(optionIdx: Int) {
        playSndMenuAccept()
        when (optionIdx) {
            // stats
            0 -> {
                // do not fire option event to avoid overlapping sound effect of two OptionTrigger events
                triggerClose(fireOptionEvent = false)
                eventService.fire(MenuBeginEvent(MenuType.STATS))
            }
            // inventory
            1 -> {
                triggerClose(fireOptionEvent = false)
                eventService.fire(MenuBeginEvent(MenuType.INVENTORY))
            }
            // quest log
            2 -> {
                triggerClose(fireOptionEvent = false)
                eventService.fire(MenuBeginEvent(MenuType.QUEST))
            }
            // monster book
            3 -> {
                if (options.size != 7) {
                    // no monster book -> save option
                    eventService.fire(SaveEvent(world))
                    return
                }
                triggerClose(fireOptionEvent = false)
                eventService.fire(MenuBeginEvent(MenuType.MONSTER_BOOK))
            }
            // save
            options.lastIndex - 2 -> {
                eventService.fire(SaveEvent(world))
            }
            // back to game
            options.lastIndex - 1 -> {
                options = emptyList()
                eventService.fire(MenuEndEvent)
            }
            // quit
            options.lastIndex -> {
                masamune.transitionScreen<MainMenuScreen>(
                    FadeTransitionType(1f, 1f, 0.1f),
                    FadeTransitionType(1f, 1f, 0.1f, delayInSeconds = 0.1f),
                )
                eventService.fire(MenuEndEvent)
            }
        }
    }

    fun triggerClose(fireOptionEvent: Boolean = true) {
        if (fireOptionEvent) {
            playSndMenuAccept()
        }
        options = emptyList()
        eventService.fire(MenuEndEvent)
    }

    override fun onEvent(event: Event) {
        if (event is MenuBeginEvent && event.type == MenuType.GAME) {
            playSndMenuAccept()
            with(world) {
                if (playerEntities.first() has MonsterBook) {
                    options = listOf(
                        bundle["menu.option.stats"],
                        bundle["menu.option.inventory"],
                        bundle["menu.option.quests"],
                        bundle["menu.option.monsterBook"],
                        bundle["menu.option.save"],
                        bundle["menu.option.back"],
                        bundle["menu.option.quit"],
                    )
                } else {
                    options = listOf(
                        bundle["menu.option.stats"],
                        bundle["menu.option.inventory"],
                        bundle["menu.option.quests"],
                        bundle["menu.option.save"],
                        bundle["menu.option.back"],
                        bundle["menu.option.quit"],
                    )
                }
            }
        }
    }
}
