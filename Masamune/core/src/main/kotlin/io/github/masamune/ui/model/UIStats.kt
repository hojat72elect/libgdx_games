package io.github.masamune.ui.model

enum class UIStats {
    AGILITY,
    ARCANE_STRIKE,
    ARMOR,
    CONSTITUTION,
    CRITICAL_STRIKE,
    DAMAGE,
    INTELLIGENCE,
    LIFE,
    LIFE_MAX,
    MAGICAL_EVADE,
    MANA,
    MANA_MAX,
    PHYSICAL_EVADE,
    RESISTANCE,
    STRENGTH,
    LEVEL,
    XP,
    XP_NEEDED,
    TALONS;

    val bundleKey = "stats.${name.toCamelCase()}"

    private fun String.toCamelCase(): String {
        return this.lowercase().replace("_[a-z]".toRegex()) { it.value.last().uppercase() }
    }
}
