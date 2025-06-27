package io.github.masamune.ui.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.model.CombatViewModel
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.ItemCombatModel
import io.github.masamune.ui.model.MagicModel
import io.github.masamune.ui.widget.Bar
import io.github.masamune.ui.widget.ItemCombatTable
import io.github.masamune.ui.widget.MagicTable
import io.github.masamune.ui.widget.TurnOrderTable
import io.github.masamune.ui.widget.bar
import io.github.masamune.ui.widget.itemCombatTable
import io.github.masamune.ui.widget.magicTable
import io.github.masamune.ui.widget.turnOrderTable
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.actors.txt
import ktx.scene2d.KButtonTable
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.buttonGroup
import ktx.scene2d.defaultStyle
import ktx.scene2d.imageButton
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table
import kotlin.math.max

private enum class UiAction {
    UNDEFINED, ATTACK, MAGIC, ITEM
}

private enum class UiCombatState {
    SELECT_ACTION, SELECT_TARGET, SELECT_MAGIC, SELECT_ITEM, VICTORY_DEFEAT
}

/**
 * CombatView is a group of standalone widgets that get positioned according to the player position
 * in the game world. It contains following widgets:
 * - player info table (name, life + mana of player entity)
 * - action table to choose which action to perform (attack, magic, item)
 * - magic table to choose the magic to perform
 * - item table to choose the item to use
 */
@Scene2dDsl
class CombatView(
    model: CombatViewModel,
    skin: Skin,
) : View<CombatViewModel>(skin, model), KGroup {

    private val playerInfoTable: Table
    private val playerNameLabel: Label
    private val playerLifeLabel: Label
    private val lifeProgressBar: ProgressBar
    private val playerManaLabel: Label
    private val manaProgressBar: ProgressBar

    private val actionTable: KButtonTable
    private val attackBtn: ImageButton
    private val magicBtn: ImageButton
    private val itemBtn: ImageButton

    private val magicTable: MagicTable
    private var magicModels: List<MagicModel> = listOf()

    private val itemTable: ItemCombatTable
    private var itemModels: List<ItemCombatModel> = listOf()

    private val actionDescriptionTable: Table
    private val actionDescriptionLabel: TypingLabel

    private var uiAction: UiAction = UiAction.UNDEFINED
    private var uiState = UiCombatState.SELECT_ACTION

    private val enemyHealthBars = mutableMapOf<Int, Stack>()

    private val turnOrderTable: TurnOrderTable

    private val actionInfoLabel: Label

    init {
        playerInfoTable = table(skin) {
            background = skin.getDrawable("nine_path_bg_2")
            pad(10f)

            this@CombatView.playerNameLabel = label("", defaultStyle, skin) {
                this.setAlignment(Align.left)
                it.padBottom(10f).row()
            }

            table(skin) { tblCell ->
                this@CombatView.lifeProgressBar = progressBar(0f, 1f, 0.01f, false, "green", skin) {
                    this.value = 1f
                    this.setAnimateDuration(1f)
                    this.setAnimateInterpolation(Interpolation.pow4Out)
                    it.padRight(15f)
                }
                this@CombatView.playerLifeLabel = label("", defaultStyle, skin) {
                    it.minWidth(130f)
                }

                tblCell.growX().row()
            }

            table(skin) { tblCell ->
                this@CombatView.manaProgressBar = progressBar(0f, 1f, 0.01f, false, "blue", skin) {
                    this.value = 1f
                    this.setAnimateDuration(1f)
                    this.setAnimateInterpolation(Interpolation.pow4Out)
                    it.padRight(15f)
                }
                this@CombatView.playerManaLabel = label("", defaultStyle, skin) {
                    it.minWidth(130f)
                }

                tblCell.growX().row()
            }

            pack()
        }

        actionTable = buttonGroup(minCheckedCount = 0, maxCheckedCount = 1, skin) {
            this@CombatView.attackBtn = imageButton("imgBtnSword", skin) {
                image.setScaling(Scaling.fit)
                imageCell.size(ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD, ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD)
                this.isChecked = false
                it.colspan(2).center().size(ACTION_BTN_SIZE, ACTION_BTN_SIZE).row()
            }
            this@CombatView.magicBtn = imageButton("imgBtnMagic", skin) {
                image.setScaling(Scaling.fit)
                imageCell.size(ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD, ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD)
                this.isChecked = false
                it.padRight(ACTION_BTN_SIZE * 0.5f).size(ACTION_BTN_SIZE, ACTION_BTN_SIZE)
            }
            this@CombatView.itemBtn = imageButton("imgBtnItems", skin) {
                image.setScaling(Scaling.fit)
                imageCell.size(ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD, ACTION_BTN_SIZE - ACTION_BTN_IMG_PAD)
                this.isChecked = false
                it.padLeft(ACTION_BTN_SIZE * 0.5f).size(ACTION_BTN_SIZE, ACTION_BTN_SIZE)
            }

            pack()
        }

        magicTable = magicTable(skin) { this.isVisible = false }

        itemTable = itemCombatTable(skin) { this.isVisible = false }

        actionDescriptionTable = table(skin) {
            this.background = skin.getDrawable("nine_path_bg_2")
            this.isVisible = false
            this@CombatView.actionDescriptionLabel = typingLabel("", "dialog_content", skin) {
                this.setAlignment(Align.topLeft)
                this.wrap = true
                it.pad(5f).grow()
            }
            this.pack()
            this.setSize(MIN_MAGIC_ITEM_TABLE_WIDTH + 100f, 150f)
        }

        turnOrderTable = turnOrderTable(skin) { }

        actionInfoLabel = label("", "dialog_option", skin) {
            this.setSize(200f, 40f)
            this.setAlignment(Align.center)
            this.alpha = 0f
        }

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        // player related property events
        viewModel.onPropertyChange(CombatViewModel::playerName) {
            playerNameLabel.txt = it
            playerInfoTable.pack()

            // player name also gets set in case of a combat restart -> reset view states
            uiAction = UiAction.UNDEFINED
            uiState = UiCombatState.SELECT_ACTION
        }
        viewModel.onPropertyChange(CombatViewModel::playerLife) { (current, max) ->
            playerLifeLabel.txt = "${current.coerceAtLeast(0f).toInt()}/${max.toInt()}"
            lifeProgressBar.value = (current / max).coerceIn(0f, 1f)
            if (viewModel.combatStart) {
                lifeProgressBar.updateVisualValue()
            }
            playerInfoTable.pack()
        }
        viewModel.onPropertyChange(CombatViewModel::playerMana) { (current, max) ->
            playerManaLabel.txt = "${current.coerceAtLeast(0f).toInt()}/${max.toInt()}"
            manaProgressBar.value = (current / max).coerceIn(0f, 1f)
            if (viewModel.combatStart) {
                manaProgressBar.updateVisualValue()
            }
            playerInfoTable.pack()
        }
        viewModel.onPropertyChange(CombatViewModel::playerMagic) { magicList ->
            magicModels = magicList
            magicTable.clearEntries()
            magicList.forEach { (_, name, _, targetDescriptor, mana, canPerform) ->
                magicTable.magic(name, targetDescriptor, mana, canPerform)
            }
            magicTable.pack()
            magicTable.height = MAGIC_ITEM_TABLE_HEIGHT
            magicTable.width = max(magicTable.width, MIN_MAGIC_ITEM_TABLE_WIDTH)
            updateActionDescription(magicList.firstOrNull()?.description ?: "")
        }
        viewModel.onPropertyChange(CombatViewModel::playerItems) { itemList ->
            itemModels = itemList
            itemTable.clearEntries()
            itemList.forEach { (_, name, _, targetDescriptor, amount) ->
                itemTable.item(name, targetDescriptor, amount)
            }
            itemTable.pack()
            itemTable.height = MAGIC_ITEM_TABLE_HEIGHT
            itemTable.width = max(itemTable.width, MIN_MAGIC_ITEM_TABLE_WIDTH)
            updateActionDescription(itemList.firstOrNull()?.description ?: "")
        }

        // ui position update
        viewModel.onPropertyChange(CombatViewModel::playerPosition) { position ->
            val infoW = playerInfoTable.width
            playerInfoTable.setPosition(position.x - infoW, 5f)
            playerInfoTable.toFront()
            val actionW = actionTable.width

            // actionTable resizing depends on if it is currently shown or hidden to the user.
            // Also, we need to clear its actions in case it is in the middle of the transition
            // from shown <-> hidden.
            val actionTableY = if (actionTable.userObject == "show") {
                playerInfoTable.height + 10f
            } else {
                playerInfoTable.height - actionTable.height * 0.7f
            }
            actionTable.clearActions()
            actionTable.setPosition(
                position.x - infoW * 0.5f - actionW * 0.5f,
                actionTableY
            )

            magicTable.setPosition(
                max(10f, position.x - infoW * 0.5f - magicTable.width * 0.5f),
                playerInfoTable.height + 20f + actionTable.height
            )
            itemTable.setPosition(
                max(10f, position.x - infoW * 0.5f - itemTable.width * 0.5f),
                playerInfoTable.height + 20f + actionTable.height
            )
            actionDescriptionTable.setPosition(
                max(10f, position.x - infoW * 0.5f - actionDescriptionTable.width * 0.5f),
                playerInfoTable.height + 30f + actionTable.height + magicTable.height
            )

            // center turn order table vertically
            turnOrderTable.setPosition(0f, stage.height * 0.5f - turnOrderTable.height * 0.5f)

            // center action info table at the top
            actionInfoLabel.setPosition(
                stage.width * 0.5f - actionInfoLabel.width * 0.5f,
                stage.height - actionInfoLabel.height
            )
        }

        // action table fade in effect
        viewModel.onPropertyChange(CombatViewModel::combatTurn) {
            playerInfoTable.height + 10f
            actionTable.clearActions()
            actionTable += Actions.moveBy(0f, actionTable.height * 0.7f + 10f, 1f, Interpolation.bounceIn)
            actionTable.buttonGroup.uncheckAll()
            actionTable.userObject = "show"
            uiAction = UiAction.UNDEFINED
        }

        // damage/heal/mana indicators
        viewModel.onPropertyChange(CombatViewModel::combatDamage) { (position, amount, critical) ->
            val effect: String
            val color: Color
            if (critical) {
                effect = "{RAINBOW=1;5;1;1}{JUMP=2;5;1;1}"
                color = skin.getColor("white")
            } else {
                effect = "{JUMP=1.5;5;1;1}"
                color = skin.getColor("red")
            }
            combatTxt(amount, effect, color, position, 2.5f)
        }
        viewModel.onPropertyChange(CombatViewModel::combatHeal) { (position, amount) ->
            combatTxt(amount, "{JUMP=0.5;5;1;1}", skin.getColor("green"), position, 2f)
        }
        viewModel.onPropertyChange(CombatViewModel::combatMana) { (position, amount) ->
            combatTxt(amount, "{JUMP=0.5;5;1;1}", skin.getColor("blue"), position, 2f)
        }
        viewModel.onPropertyChange(CombatViewModel::combatMiss) { position ->
            combatTxt(i18nTxt(I18NKey.COMBAT_MISS), "{JUMP=0.5;5;1;1}", skin.getColor("white"), position, 2.5f)
        }

        // victory / defeat
        viewModel.onPropertyChange(CombatViewModel::combatDone) { isDone ->
            if (isDone) {
                uiState = UiCombatState.VICTORY_DEFEAT
            }
        }

        viewModel.onPropertyChange(CombatViewModel::enemyPosAndLifes) { enemyPosAndLifes ->
            enemyHealthBars.values.forEach { it.remove() }
            enemyHealthBars.clear()
            enemyPosAndLifes.forEach { (entityId, values) ->
                val (position, size, lifeAndMax) = values
                healthBar(entityId, lifeAndMax.x, lifeAndMax.y, position, size)
            }
        }
        viewModel.onPropertyChange(CombatViewModel::enemyDamage) { (entityId, lifeAndMax) ->
            if (lifeAndMax == Vector2.Zero) {
                // entity is dead
                enemyHealthBars[entityId]?.remove()
                return@onPropertyChange
            }

            val stack = enemyHealthBars[entityId]
            if (stack != null) {
                val lifePerc = lifeAndMax.x / lifeAndMax.y
                val bar = stack.children.first() as Bar
                val label = stack.children.last() as Label
                label.txt = "${lifeAndMax.x.toInt()}"
                bar.value = lifePerc
            } else {
                val (position, size) = viewModel.getEnemyPositionAndSize(entityId)
                healthBar(entityId, lifeAndMax.x, lifeAndMax.y, position, size)
            }
        }

        // turn order table related properties
        viewModel.onPropertyChange(CombatViewModel::turnEntities) { entityDrawables ->
            turnOrderTable.drawablesOfRound(entityDrawables)
        }
        viewModel.onPropertyChange(CombatViewModel::actionFinishedEntityId) {
            turnOrderTable.removeDrawable(it)
        }

        // action info text
        viewModel.onPropertyChange(CombatViewModel::currentAction) { name ->
            actionInfoLabel.clearActions()
            actionInfoLabel += alpha(0f) then fadeIn(0.25f) then delay(2f) then fadeOut(0.5f)
            actionInfoLabel.txt = name
        }
    }

    private fun healthBar(entityId: Int, life: Float, lifeMax: Float, position: Vector2, size: Vector2) =
        scene2d.stack {
            val skin = this@CombatView.skin

            val lifePerc = life / lifeMax
            bar(skin, lifePerc, 0f, 1f, 0.01f, skin.getColor("red"))
            label("${life.toInt()}", "bar_content", skin) {
                this.setAlignment(Align.top, Align.center)
            }

            this.setPosition(position.x, position.y - 25f)
            this.setSize(size.x, 20f)
            this.isVisible = this@CombatView.viewModel.isEnemyKnown(entityId)

            this@CombatView.enemyHealthBars[entityId] = this
            this@CombatView.stage.addActor(this)
            this.toBack()
        }

    private fun updateActionDescription(description: String) {
        actionDescriptionLabel.txt = description
        actionDescriptionLabel.skipToTheEnd()
    }

    private fun combatTxt(amount: Int, effect: String, color: Color, position: Vector2, duration: Float) {
        val label = scene2d.typingLabel("$effect$amount", "combat_number", skin) {
            this.color = color
            this.setPosition(position.x, position.y)
        }
        val transparentColor = Color(color.r, color.g, color.b, 0f)
        label += Actions.color(transparentColor, duration, Interpolation.pow3OutInverse) then Actions.removeActor()
        stage.addActor(label)
    }

    private fun combatTxt(text: String, effect: String, color: Color, position: Vector2, duration: Float) {
        val labelText = if (text.length > 4) {
            "$effect${text.substring(0, 4)}."
        } else {
            "$effect$text"
        }
        val label = scene2d.typingLabel(labelText, "combat_number", skin) {
            this.color = color
            this.setPosition(position.x, position.y)
        }
        val transparentColor = Color(color.r, color.g, color.b, 0f)
        label += Actions.color(transparentColor, duration, Interpolation.pow3OutInverse) then Actions.removeActor()
        stage.addActor(label)
    }

    override fun onUpPressed() {
        when (uiState) {
            UiCombatState.SELECT_ACTION -> {
                uiAction = UiAction.ATTACK
                attackBtn.isChecked = true
                viewModel.playSndMenuClick()
            }

            UiCombatState.SELECT_MAGIC -> {
                if (magicTable.hasNoEntries()) {
                    return
                }
                magicTable.prevEntry(magicTable.entriesPerRow)
                viewModel.playSndMenuClick()
                updateActionDescription(magicModels[magicTable.selectedEntryIdx].description)
            }

            UiCombatState.SELECT_ITEM -> {
                if (itemTable.hasNoEntries()) {
                    return
                }
                itemTable.prevEntry(itemTable.entriesPerRow)
                viewModel.playSndMenuClick()
                updateActionDescription(itemModels[itemTable.selectedEntryIdx].description)
            }

            else -> Unit
        }
    }

    override fun onLeftPressed() {
        when (uiState) {
            UiCombatState.SELECT_ACTION -> {
                uiAction = UiAction.MAGIC
                magicBtn.isChecked = true
                viewModel.playSndMenuClick()
            }

            UiCombatState.SELECT_TARGET -> viewModel.selectPrevTarget()
            UiCombatState.SELECT_MAGIC -> {
                if (magicTable.hasNoEntries()) {
                    return
                }
                magicTable.prevEntry()
                viewModel.playSndMenuClick()
                updateActionDescription(magicModels[magicTable.selectedEntryIdx].description)
            }
            UiCombatState.SELECT_ITEM -> {
                if (itemTable.hasNoEntries()) {
                    return
                }
                itemTable.prevEntry()
                viewModel.playSndMenuClick()
                updateActionDescription(itemModels[itemTable.selectedEntryIdx].description)
            }
            UiCombatState.VICTORY_DEFEAT -> Unit
        }
    }

    override fun onRightPressed() {
        when (uiState) {
            UiCombatState.SELECT_ACTION -> {
                uiAction = UiAction.ITEM
                itemBtn.isChecked = true
                viewModel.playSndMenuClick()
            }

            UiCombatState.SELECT_TARGET -> viewModel.selectNextTarget()
            UiCombatState.SELECT_MAGIC -> {
                if (magicTable.hasNoEntries()) {
                    return
                }
                magicTable.nextEntry()
                viewModel.playSndMenuClick()
                updateActionDescription(magicModels[magicTable.selectedEntryIdx].description)
            }
            UiCombatState.SELECT_ITEM -> {
                if (itemTable.hasNoEntries()) {
                    return
                }
                itemTable.nextEntry()
                viewModel.playSndMenuClick()
                updateActionDescription(itemModels[itemTable.selectedEntryIdx].description)
            }
            UiCombatState.VICTORY_DEFEAT -> Unit
        }
    }

    override fun onDownPressed() {
        when (uiState) {
            UiCombatState.SELECT_MAGIC -> {
                if (magicTable.hasNoEntries()) {
                    return
                }
                magicTable.nextEntry(magicTable.entriesPerRow)
                viewModel.playSndMenuClick()
                updateActionDescription(magicModels[magicTable.selectedEntryIdx].description)
            }

            UiCombatState.SELECT_ITEM -> {
                if (itemTable.hasNoEntries()) {
                    return
                }
                itemTable.nextEntry(itemTable.entriesPerRow)
                viewModel.playSndMenuClick()
                updateActionDescription(itemModels[itemTable.selectedEntryIdx].description)
            }

            else -> Unit
        }
    }

    override fun onBackPressed() {
        when (uiState) {
            UiCombatState.SELECT_TARGET -> {
                if (viewModel.stopOrRevertSelection()) {
                    uiState = UiCombatState.SELECT_ACTION
                }
            }

            UiCombatState.SELECT_MAGIC -> {
                magicTable.isVisible = false
                actionDescriptionTable.isVisible = false
                uiState = UiCombatState.SELECT_ACTION
                viewModel.playSndMenuAbort()
            }

            UiCombatState.SELECT_ITEM -> {
                itemTable.isVisible = false
                actionDescriptionTable.isVisible = false
                uiState = UiCombatState.SELECT_ACTION
                viewModel.playSndMenuAbort()
            }

            UiCombatState.SELECT_ACTION -> Unit
            UiCombatState.VICTORY_DEFEAT -> Unit
        }
    }

    override fun onSelectPressed() {
        when (uiState) {
            UiCombatState.SELECT_ACTION -> when (uiAction) {
                UiAction.ATTACK -> {
                    uiState = UiCombatState.SELECT_TARGET
                    viewModel.selectAttack()
                }

                UiAction.MAGIC -> {
                    uiState = UiCombatState.SELECT_MAGIC
                    magicTable.isVisible = true
                    magicTable.selectFirstEntry()
                    if (magicTable.hasEntries()) {
                        actionDescriptionTable.isVisible = true
                        updateActionDescription(magicModels[magicTable.selectedEntryIdx].description)
                    }
                    viewModel.playSndMenuAccept()
                }

                UiAction.ITEM -> {
                    uiState = UiCombatState.SELECT_ITEM
                    itemTable.isVisible = true
                    itemTable.selectFirstEntry()
                    if (itemTable.hasEntries()) {
                        actionDescriptionTable.isVisible = true
                        updateActionDescription(itemModels[itemTable.selectedEntryIdx].description)
                    }
                    viewModel.playSndMenuAccept()
                }

                else -> Unit
            }

            UiCombatState.SELECT_TARGET -> {
                if (viewModel.confirmTargetSelection()) {
                    uiState = UiCombatState.SELECT_ACTION
                    uiAction = UiAction.UNDEFINED
                    actionTable.buttonGroup.uncheckAll()
                    // hide action table again but first complete any remaining UI effect actions
                    actionTable.act(10f)
                    actionTable += Actions.moveBy(0f, -actionTable.height * 0.7f - 10f, 1f, Interpolation.bounceOut)
                    actionTable.userObject = null
                }
            }

            UiCombatState.SELECT_MAGIC -> {
                if (magicTable.hasNoEntries()) {
                    // cannot perform any magic -> do nothing
                    return
                }

                uiState = UiCombatState.SELECT_TARGET
                viewModel.selectMagic(magicModels[magicTable.selectedEntryIdx])
                magicTable.isVisible = false
                actionDescriptionTable.isVisible = false
            }

            UiCombatState.SELECT_ITEM -> {
                if (itemTable.hasNoEntries()) {
                    // no items to use -> do nothing
                    return
                }

                uiState = UiCombatState.SELECT_TARGET
                viewModel.selectItem(itemModels[itemTable.selectedEntryIdx])
                itemTable.isVisible = false
                actionDescriptionTable.isVisible = false
            }
            UiCombatState.VICTORY_DEFEAT -> Unit
        }
    }

    companion object {
        private const val ACTION_BTN_SIZE = 30f
        private const val ACTION_BTN_IMG_PAD = 10f
        private const val MAGIC_ITEM_TABLE_HEIGHT = 100f
        private const val MIN_MAGIC_ITEM_TABLE_WIDTH = 400f
    }
}

@Scene2dDsl
fun <S> KWidget<S>.combatView(
    model: CombatViewModel,
    skin: Skin,
    init: (@Scene2dDsl CombatView).(S) -> Unit = {},
): CombatView = actor(CombatView(model, skin), init)
