package io.github.masamune.ui.model

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.World
import io.github.masamune.asset.CachingAtlas
import io.github.masamune.audio.AudioService
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.Player
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.event.MenuEndEvent
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.tiledmap.TiledService

class MonsterBookViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val monsterAtlas: CachingAtlas,
    private val eventService: EventService,
    private val tiledService: TiledService,
) : ViewModel(bundle, audioService) {

    var monsterModels: List<MonsterModel> by propertyNotify(emptyList())

    override fun onEvent(event: Event) {
        if (event !is MenuBeginEvent || event.type != MenuType.MONSTER_BOOK) {
            return
        }

        with(world) {
            val player = world.family { all(Player) }.first()
            val knownTypes = player[MonsterBook].knownTypes
            monsterModels = TiledObjectType.entries
                .filter { it.isEnemy }
                .map {
                    if (it in knownTypes) {
                        it.toMonsterModel()
                    } else {
                        MonsterModel.UNKNOWN
                    }
                }
        }
    }

    private fun TiledObjectType.toMonsterModel(): MonsterModel {
        val monsterKey = this.name.lowercase()
        val statsXpAndTalons = tiledService.loadEnemyStats(this)
        return MonsterModel(
            name = bundle["enemy.${monsterKey}.name"],
            description = description("enemy.${monsterKey}.description"),
            drawable = TextureRegionDrawable(monsterAtlas.findRegions("$monsterKey/idle_down").first()),
            stats = statsXpAndTalons.toUiStatsMap(),
        )
    }

    fun quit() {
        eventService.fire(MenuEndEvent)
        eventService.fire(MenuBeginEvent(MenuType.GAME))
        // clearing knownMonsters will hide the view
        monsterModels = emptyList()
    }

}
