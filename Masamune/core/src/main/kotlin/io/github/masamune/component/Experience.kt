package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    var level: Int = 1,
    var current: Int = 0,
    var forLevelUp: Int = 0,
) : Component<Experience> {

    init {
        forLevelUp = calcLevelUpXp(level)
    }

    override fun type() = Experience

    fun gainXp(xpGained: Int) {
        current += xpGained
        while (current >= forLevelUp) {
            forLevelUp = calcLevelUpXp(++level)
        }
    }

    companion object : ComponentType<Experience>() {
        fun calcLevelUpXp(forLevel: Int): Int = (80f + ((forLevel - 1) * 120) * 1.2f).toInt()

        fun calcLevelUps(level: Int, currentXp: Int, xpGain: Int): Int {
            var tmpLvl = level
            val tmpXp = currentXp + xpGain
            var tmpNeededXp = calcLevelUpXp(level)
            var levelUps = 0
            while (tmpXp >= tmpNeededXp) {
                ++levelUps
                tmpNeededXp = calcLevelUpXp(++tmpLvl)
            }
            return levelUps
        }

        fun ItemStats.levelUpStats(forLevel: Int) {
            strength += if (forLevel % 4 == 0) 3f else 2f
            intelligence += if (forLevel % 5 == 0) 4f else 1f
            manaMax += if (forLevel % 5 == 0) 5f else 0f
            constitution += if (forLevel % 3 == 0) 2f else 1f
            agility += if (forLevel % 5 == 0 || forLevel == 3) 2f else 0f
        }
    }
}
