package io.github.masamune.component

import io.github.masamune.component.CharacterStats.Companion.DAM_PER_STR
import io.github.masamune.component.CharacterStats.Companion.LIFE_PER_CONST
import io.kotest.matchers.floats.shouldBeExactly
import kotlin.test.Test

class CharacterStatsTest {

    @Test
    fun `test constructor life and damage`() {
        val stats = CharacterStats(
            constitution = 10f,
            baseLife = 20f,
            strength = 15f,
            baseDamage = 40f,
        )

        stats.constitution shouldBeExactly 10f
        stats.lifeMax shouldBeExactly 20f + 10f * LIFE_PER_CONST
        stats.life shouldBeExactly stats.lifeMax
        stats.strength shouldBeExactly 15f
        stats.damage shouldBeExactly 40f + 15f * DAM_PER_STR
    }

    @Test
    fun `test copy`() {
        val stats = CharacterStats(
            agility = 1f,
            arcaneStrike = 2f,
            armor = 3f,
            constitution = 4f,
            criticalStrike = 5f,
            baseDamage = 6f,
            intelligence = 7f,
            baseLife = 8f,
            magicalEvade = 9f,
            baseMana = 10f,
            physicalEvade = 11f,
            resistance = 12f,
            strength = 13f,
        )
        stats.lifeMax = 100f
        stats.life = 1f
        stats.damage = 300f
        stats.manaMax = 200f
        stats.mana = 2f

        val copied = stats.copy()

        copied.agility shouldBeExactly 1f
        copied.arcaneStrike shouldBeExactly 2f
        copied.armor shouldBeExactly 3f
        copied.constitution shouldBeExactly 4f
        copied.criticalStrike shouldBeExactly 5f
        copied.damage shouldBeExactly 300f
        copied.intelligence shouldBeExactly 7f
        copied.lifeMax shouldBeExactly 100f
        copied.life shouldBeExactly 1f
        copied.magicalEvade shouldBeExactly 9f
        copied.mana shouldBeExactly 2f
        copied.manaMax shouldBeExactly 200f
        copied.physicalEvade shouldBeExactly 11f
        copied.resistance shouldBeExactly 12f
        copied.strength shouldBeExactly 13f
    }

    @Test
    fun `test bonus strength and damage`() {
        val stats = CharacterStats()
        stats.damage shouldBeExactly 0f
        stats.strength shouldBeExactly 0f

        stats.damage += 1f
        stats.damage shouldBeExactly 1f
        stats.strength shouldBeExactly 0f

        stats.strength += 10f
        stats.damage shouldBeExactly 1f + 10f * DAM_PER_STR
        stats.strength shouldBeExactly 10f

        stats.strength -= 5f
        stats.damage shouldBeExactly 1f + 5f * DAM_PER_STR
        stats.strength shouldBeExactly 5f
    }

    @Test
    fun `test bonus constitution and lifeMax`() {
        val stats = CharacterStats()
        stats.constitution shouldBeExactly 0f
        stats.life shouldBeExactly 0f
        stats.lifeMax shouldBeExactly 0f

        stats.lifeMax += 5f
        stats.constitution shouldBeExactly 0f
        stats.life shouldBeExactly 5f
        stats.lifeMax shouldBeExactly 5f

        stats.constitution += 10f
        stats.constitution shouldBeExactly 10f
        stats.life shouldBeExactly 5f + 10f * LIFE_PER_CONST
        stats.lifeMax shouldBeExactly 5f + 10f * LIFE_PER_CONST

        stats.constitution -= 5f
        stats.constitution shouldBeExactly 5f
        stats.life shouldBeExactly 5f + 5f * LIFE_PER_CONST
        stats.lifeMax shouldBeExactly 5f + 5f * LIFE_PER_CONST

        stats.lifeMax -= 3f
        stats.constitution shouldBeExactly 5f
        stats.life shouldBeExactly 2f + 5f * LIFE_PER_CONST
        stats.lifeMax shouldBeExactly 2f + 5f * LIFE_PER_CONST
    }

    @Test
    fun `test life update percentage when lifeMax changes`() {
        val stats = CharacterStats()

        stats.life = 10f
        stats.lifeMax = 100f
        stats.life shouldBeExactly 10f
        stats.lifeMax shouldBeExactly 100f

        stats.lifeMax = 200f
        stats.life shouldBeExactly 20f
        stats.lifeMax shouldBeExactly 200f

        stats.lifeMax = 100f
        stats.life shouldBeExactly 10f
        stats.lifeMax shouldBeExactly 100f

        stats.life = 100f
        stats.life shouldBeExactly 100f
        stats.lifeMax shouldBeExactly 100f

        stats.lifeMax = 50f
        stats.life shouldBeExactly 50f
        stats.lifeMax shouldBeExactly 50f
    }

}
