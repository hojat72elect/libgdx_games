package io.github.masamune.ui.model

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.quillraven.fleks.collection.mutableEntityBagOf
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Equipment
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.Name
import io.github.masamune.component.Player
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.tiledmap.ConsumableType
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.ui.view.zeroIfMissing
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test

class InventoryViewModelTest {

    @BeforeTest
    fun mockLogging() {
        val appMock = mockk<Application>()
        Gdx.app = appMock
        every { appMock.logLevel } returns Application.LOG_DEBUG
        every { appMock.debug(any(), any()) } returns Unit
    }

    @Test
    fun testEquipWithFullLife() {
        val world = configureWorld { }
        world.entity {
            it += Player()
            it += CharacterStats(baseLife = 30f)
            it += Inventory(
                items = mutableEntityBagOf(
                    world.entity { item ->
                        item += ItemStats(strength = 10f, constitution = 5f, lifeMax = 100f)
                        item += Item(ItemType.ELDER_SWORD, 0, ItemCategory.WEAPON, "", ActionType.UNDEFINED, ConsumableType.UNDEFINED)
                        item += Name("")
                    }
                )
            )
            it += Equipment()
        }

        val model = InventoryViewModel(mockk(relaxed = true), mockk(), world, mockk())

        // test equip
        model.equip(ItemModel(ItemType.ELDER_SWORD, emptyMap(), "", 0, "", ItemCategory.WEAPON, null, false))
        model.playerStats.zeroIfMissing(UIStats.STRENGTH) shouldBe "10"
        model.playerStats.zeroIfMissing(UIStats.CONSTITUTION) shouldBe "5"
        model.playerStats.zeroIfMissing(UIStats.LIFE) shouldBe "180"
        model.playerStats.zeroIfMissing(UIStats.LIFE_MAX) shouldBe "180"

        // test remove equip
        model.equip(emptyItemModel(ItemCategory.WEAPON, ""))
        model.playerStats.zeroIfMissing(UIStats.STRENGTH) shouldBe "0"
        model.playerStats.zeroIfMissing(UIStats.CONSTITUTION) shouldBe "0"
        model.playerStats.zeroIfMissing(UIStats.LIFE) shouldBe "30"
        model.playerStats.zeroIfMissing(UIStats.LIFE_MAX) shouldBe "30"
    }

    @Test
    fun testEquipWithHalfLife() {
        val world = configureWorld { }
        world.entity {
            it += Player()
            it += CharacterStats(baseLife = 30f).apply { life = 15f }
            it += Inventory(
                items = mutableEntityBagOf(
                    world.entity { item ->
                        item += ItemStats(strength = 10f, constitution = 5f, lifeMax = 100f)
                        item += Item(ItemType.ELDER_SWORD, 0, ItemCategory.WEAPON, "", ActionType.UNDEFINED, ConsumableType.UNDEFINED)
                        item += Name("")
                    }
                )
            )
            it += Equipment()
        }

        val model = InventoryViewModel(mockk(relaxed = true), mockk(), world, mockk())

        // test equip
        model.equip(ItemModel(ItemType.ELDER_SWORD, emptyMap(), "", 0, "", ItemCategory.WEAPON, null, false))
        model.playerStats.zeroIfMissing(UIStats.STRENGTH) shouldBe "10"
        model.playerStats.zeroIfMissing(UIStats.CONSTITUTION) shouldBe "5"
        model.playerStats.zeroIfMissing(UIStats.LIFE) shouldBe "90"
        model.playerStats.zeroIfMissing(UIStats.LIFE_MAX) shouldBe "180"

        // test remove equip
        model.equip(emptyItemModel(ItemCategory.WEAPON, ""))
        model.playerStats.zeroIfMissing(UIStats.STRENGTH) shouldBe "0"
        model.playerStats.zeroIfMissing(UIStats.CONSTITUTION) shouldBe "0"
        model.playerStats.zeroIfMissing(UIStats.LIFE) shouldBe "15"
        model.playerStats.zeroIfMissing(UIStats.LIFE_MAX) shouldBe "30"
    }
}
