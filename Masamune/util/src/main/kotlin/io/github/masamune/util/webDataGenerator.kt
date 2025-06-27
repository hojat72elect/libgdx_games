package io.github.masamune.util

import io.github.masamune.combat.action.ActionTargetType
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.util.web.ActionInfo
import io.github.masamune.util.web.EnemyInfo
import io.github.masamune.util.web.ItemInfo
import kotlinx.serialization.json.Json
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val json = Json {
        ignoreUnknownKeys = true
    }

    val bundleFile = File("../assets/ui/messages.properties")
    val resourceBundle = PropertyResourceBundle(FileInputStream(bundleFile))

    val actionInfo: List<ActionInfo> = actionInfo(resourceBundle)
    val actionFile = File("../web/actions.json")
    actionFile.createNewFile()
    actionFile.writeText(json.encodeToString(actionInfo))

    val itemInfo: List<ItemInfo> = itemInfo(resourceBundle)
    val itemFile = File("../web/items.json")
    itemFile.createNewFile()
    itemFile.writeText(json.encodeToString(itemInfo))

    val enemyInfo: List<EnemyInfo> = enemyInfo(resourceBundle)
    val enemyFile = File("../web/enemies.json")
    enemyFile.createNewFile()
    enemyFile.writeText(json.encodeToString(enemyInfo))
}

private fun actionInfo(resourceBundle: PropertyResourceBundle): List<ActionInfo> {
    fun actionCategory(targetType: ActionTargetType, defensive: Boolean): String {
        return when {
            targetType == ActionTargetType.NONE -> "Passive"
            defensive -> "Active (Defensive)"
            else -> "Active (Offensive)"
        }
    }

    fun actionTargetType(targetType: ActionTargetType): String {
        return targetType.name.first() + targetType.name.substring(1).lowercase()
    }

    fun actionDescription(actionName: String): String {
        // Get the raw description from resources
        val rawDescription = resourceBundle.getString("magic.${actionName.lowercase()}.description")

        // Replace all strings surrounded by curly braces with empty string
        // This pattern matches {anything} including {COLOR=XYZ} and {CLEARCOLOR}
        return rawDescription.replace(Regex("\\{[^}]*}"), "")
            .replace("\n", " ")
    }

    val actionsToIgnore = listOf(
        ActionType.ITEM_HEALTH_RESTORE,
        ActionType.ITEM_MANA_RESTORE,
        ActionType.USE_ITEM,
        ActionType.TRANSFORM,
        ActionType.UNDEFINED,
    )
    return ActionType.entries
        .filter { it !in actionsToIgnore }
        .map {
            val action = it()

            ActionInfo(
                resourceBundle.getString("magic.${it.name.lowercase()}.name"),
                actionDescription(it.name),
                actionCategory(action.targetType, action.defensive),
                actionTargetType(action.targetType),
                action.manaCost,
            )
        }
}

fun itemInfo(resourceBundle: PropertyResourceBundle): List<ItemInfo> {
    fun itemCategory(category: String): String {
        return category.first() + category.substring(1).lowercase()
    }

    fun itemAction(action: String): String {
        val actionsToIgnore = listOf("ITEM_MANA_RESTORE", "ITEM_HEALTH_RESTORE")
        if (action in actionsToIgnore) {
            return ""
        }

        return resourceBundle.getString("magic.${action.lowercase()}.name")
    }

    val doc = getObjectsTsxDoc()

    // Get all tile elements
    val tileList = doc.getElementsByTagName("tile")

    // Filter for ItemObject tiles and extract information
    val itemInfoList = mutableListOf<ItemInfo>()

    for (i in 0 until tileList.length) {
        val tileElement = tileList.item(i) as Element
        val tileType = tileElement.getAttribute("type")
        // Only process ItemObject tiles
        if (tileType != "ItemObject") {
            continue
        }

        val properties = tileElement.getElementsByTagName("properties").item(0) as Element
        val propertyList = properties.getElementsByTagName("property")

        var itemTypeName = ""
        var category = "Other"
        var action = ""
        var speed = 0f
        val stats = mutableMapOf<String, Float>()
        var cost = 0

        // Extract properties
        for (j in 0 until propertyList.length) {
            val property = propertyList.item(j) as Element
            val propertyName = property.getAttribute("name")
            val propertyValue = property.getAttribute("value")

            when (propertyName) {
                "itemType" -> itemTypeName = propertyValue
                "category" -> category = itemCategory(propertyValue)
                "action" -> action = itemAction(propertyValue)
                "speed" -> speed = propertyValue.toFloatOrNull() ?: 0f
                "stats" -> stats.parseStats(property.getElementsByTagName("properties").item(0))
                "cost" -> cost = propertyValue.toIntOrNull() ?: 0
            }
        }

        // Get the image URL from the image element
        val imageElement = tileElement.getElementsByTagName("image").item(0) as Element
        val imageSource = imageElement.getAttribute("source")
        val imageUrl = "assets/maps/$imageSource"

        // Get the item name from the resource bundle
        val itemName = resourceBundle.getString("item.${itemTypeName.lowercase()}.name")

        // Create ItemInfo object and add to the list
        itemInfoList.add(
            ItemInfo(
                imageUrl,
                itemName,
                category,
                stats,
                speed,
                action,
                cost
            )
        )
    }

    return itemInfoList
}

private fun MutableMap<String, Float>.parseStats(statsProperties: Node?) {
    if (statsProperties !is Element) {
        return
    }

    val statPropertyList = statsProperties.getElementsByTagName("property")
    for (k in 0 until statPropertyList.length) {
        val statProperty = statPropertyList.item(k) as Element
        val statName = statProperty.getAttribute("name")
        val statValue = statProperty.getAttribute("value").toFloatOrNull() ?: 0f
        this[statName] = statValue
    }
}

fun enemyInfo(resourceBundle: PropertyResourceBundle): List<EnemyInfo> {
    val doc = getObjectsTsxDoc()

    // Get all tile elements
    val tileList = doc.getElementsByTagName("tile")

    // Filter for EnemyObject tiles and extract information
    val enemyInfoList = mutableListOf<EnemyInfo>()

    for (i in 0 until tileList.length) {
        val tileElement = tileList.item(i) as Element
        val tileType = tileElement.getAttribute("type")

        // Only process EnemyObject tiles
        if (tileType != "EnemyObject") {
            continue
        }

        val properties = tileElement.getElementsByTagName("properties").item(0) as Element
        val propertyList = properties.getElementsByTagName("property")

        var objTypeName = ""
        var xp = 0
        var talons = 0
        var level = 1
        var combatActions = listOf<String>()
        val stats = mutableMapOf<String, Float>()

        // Extract properties
        for (j in 0 until propertyList.length) {
            val property = propertyList.item(j) as Element
            val propertyName = property.getAttribute("name")
            val propertyValue = property.getAttribute("value")

            when (propertyName) {
                "objType" -> objTypeName = propertyValue
                "xp" -> xp = propertyValue.toIntOrNull() ?: 0
                "talons" -> talons = propertyValue.toIntOrNull() ?: 0
                "level" -> level = propertyValue.toIntOrNull() ?: 1
                "stats" -> stats.parseStats(property.getElementsByTagName("properties").item(0))
                "combatActions" -> combatActions = propertyValue
                    .split(",")
                    .map { resourceBundle.getString("magic.${it.lowercase()}.name") }
            }
        }

        // Get the image URL from the image element
        val imageElement = tileElement.getElementsByTagName("image").item(0) as Element
        val imageSource = imageElement.getAttribute("source")
        val imageUrl = "assets/maps/$imageSource"

        // Get the enemy name from the resource bundle or use objType as fallback
        val enemyName = resourceBundle.getString("enemy.${objTypeName.lowercase()}.name")

        // Create EnemyInfo object and add to the list
        enemyInfoList.add(
            EnemyInfo(
                imageUrl,
                enemyName,
                xp,
                talons,
                stats,
                level,
                combatActions
            )
        )
    }

    return enemyInfoList
}

private fun getObjectsTsxDoc(): Document {
    val xmlFile = File("../assets/maps/objects.tsx")
    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val doc = dBuilder.parse(xmlFile)
    doc.documentElement.normalize()
    return doc
}
