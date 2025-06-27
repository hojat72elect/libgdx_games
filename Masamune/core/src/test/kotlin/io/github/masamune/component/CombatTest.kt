package io.github.masamune.component

import io.github.masamune.component.Combat.Companion.andEquipment
import io.github.masamune.tiledmap.ActionType
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.ints.shouldBeExactly
import kotlin.test.Test

class CombatTest {

    @Test
    fun `test andEquipment`() {
        val actionTypes = listOf(ActionType.ATTACK_SINGLE)
        val equipmentTypes = listOf(ActionType.UNDEFINED, ActionType.ATTACK_SINGLE, ActionType.FIREBALL)

        val actual = actionTypes andEquipment equipmentTypes

        actual.size shouldBeExactly 2
        actual shouldContainAll listOf(ActionType.ATTACK_SINGLE, ActionType.FIREBALL)
    }
    
}
