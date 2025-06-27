package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class CharacterStats(
    var agility: Float = 0f,
    var arcaneStrike: Float = 0f,
    var armor: Float = 0f,
    constitution: Float = 0f,
    var criticalStrike: Float = 0f,
    baseDamage: Float = 0f,
    var intelligence: Float = 0f,
    baseLife: Float = 0f,
    var magicalEvade: Float = 0f,
    baseMana: Float = 0f,
    var physicalEvade: Float = 0f,
    var resistance: Float = 0f,
    strength: Float = 0f,
) : Component<CharacterStats> {

    var life: Float = baseLife
    var lifeMax: Float = baseLife
        set(value) {
            if (field == value) {
                return
            }

            if (value <= 0f) {
                // maxLife set to zero or less -> also set life to zero
                life = 0f
                field = value
                return
            }
            if (field <= 0f) {
                // maxLife set to a value > zero but current maxLife is zero or less -> set life to max, if it was not set yet
                life = if (life == 0f) value else life
                field = value
                return
            }

            // otherwise, keep current life percentage
            val lifePerc = life / field
            life = (value * lifePerc).coerceAtMost(value)
            field = value
        }
    var constitution: Float = 0f
        set(value) {
            if (field == value) {
                return
            }

            lifeMax -= field * LIFE_PER_CONST
            lifeMax += value * LIFE_PER_CONST
            field = value
        }
    var damage: Float = baseDamage
    var strength: Float = 0f
        set(value) {
            if (field == value) {
                return
            }

            damage -= field * DAM_PER_STR
            damage += value * DAM_PER_STR
            field = value
        }
    var mana: Float = baseMana
    var manaMax: Float = baseMana
        set(value) {
            if (field == value) {
                return
            }

            if (value <= 0f) {
                // maxMana set to zero or less -> also set mana to zero
                mana = 0f
                field = value
                return
            }
            if (field <= 0f) {
                // maxMana set to a value > zero but current maxMana is zero or less -> set mana to max, if it was not set yet
                mana = if (mana == 0f) value else mana
                field = value
                return
            }

            // otherwise, keep current mana percentage
            val manaPerc = mana / field
            mana = (value * manaPerc).coerceAtMost(value)
            field = value
        }

    init {
        this.constitution = constitution
        this.strength = strength
    }

    override fun type() = CharacterStats

    operator fun plusAssign(other: ItemStats) {
        this.agility += other.agility
        this.arcaneStrike += other.arcaneStrike
        this.armor += other.armor
        this.criticalStrike += other.criticalStrike
        this.intelligence += other.intelligence
        this.magicalEvade += other.magicalEvade
        this.manaMax += other.manaMax
        this.physicalEvade += other.physicalEvade
        this.resistance += other.resistance
        this.constitution += other.constitution
        this.lifeMax += other.lifeMax
        this.strength += other.strength
        this.damage += other.damage
        // keep life/mana within its max values
        this.life = (this.life + other.life).coerceAtMost(this.lifeMax)
        this.mana = (this.mana + other.mana).coerceAtMost(this.manaMax)
    }

    operator fun minusAssign(other: ItemStats) {
        this.agility -= other.agility
        this.arcaneStrike -= other.arcaneStrike
        this.armor -= other.armor
        this.criticalStrike -= other.criticalStrike
        this.intelligence -= other.intelligence
        this.magicalEvade -= other.magicalEvade
        this.manaMax -= other.manaMax
        this.physicalEvade -= other.physicalEvade
        this.resistance -= other.resistance
        this.constitution -= other.constitution
        this.lifeMax -= other.lifeMax
        this.strength -= other.strength
        this.damage -= other.damage
        // keep life/mana within its max values
        this.life = (this.life - other.life).coerceAtLeast(1f)
        this.mana = (this.mana - other.mana).coerceAtLeast(1f)
    }

    fun copy(): CharacterStats {
        return CharacterStats().apply {
            agility = this@CharacterStats.agility
            arcaneStrike = this@CharacterStats.arcaneStrike
            armor = this@CharacterStats.armor
            criticalStrike = this@CharacterStats.criticalStrike
            intelligence = this@CharacterStats.intelligence
            magicalEvade = this@CharacterStats.magicalEvade
            physicalEvade = this@CharacterStats.physicalEvade
            resistance = this@CharacterStats.resistance
            mana = this@CharacterStats.mana
            manaMax = this@CharacterStats.manaMax
            // adjust lifeMax after constitution because constitution changes lifeMax value
            constitution = this@CharacterStats.constitution
            // adjust lifeMax before life because lifeMax changes life value
            lifeMax = this@CharacterStats.lifeMax
            life = this@CharacterStats.life
            // adjust damage after strength because strength changes damage values
            strength = this@CharacterStats.strength
            damage = this@CharacterStats.damage
        }
    }

    override fun toString(): String {
        return "CharacterStats(agility=$agility, arcaneStrike=$arcaneStrike, armor=$armor, criticalStrike=$criticalStrike, intelligence=$intelligence, magicalEvade=$magicalEvade, physicalEvade=$physicalEvade, resistance=$resistance, life=$life, lifeMax=$lifeMax, constitution=$constitution, damage=$damage, strength=$strength, mana=$mana, manaMax=$manaMax)"
    }

    companion object : ComponentType<CharacterStats>() {
        const val DAM_PER_STR = 1 / 2f
        const val MAG_DAM_PER_INT = 1 / 4f
        const val LIFE_PER_CONST = 10f

        fun CharacterStats.toItemStats() = ItemStats(
            agility = agility,
            arcaneStrike = arcaneStrike,
            armor = armor,
            constitution = constitution,
            criticalStrike = criticalStrike,
            damage = damage,
            intelligence = intelligence,
            life = life,
            lifeMax = lifeMax,
            magicalEvade = magicalEvade,
            mana = mana,
            manaMax = manaMax,
            physicalEvade = physicalEvade,
            resistance = resistance,
            strength = strength,
        )

        fun fromItemStats(itemStats: ItemStats): CharacterStats {
            return CharacterStats().apply {
                this.agility = itemStats.agility
                this.arcaneStrike = itemStats.arcaneStrike
                this.armor = itemStats.armor
                this.criticalStrike = itemStats.criticalStrike
                this.intelligence = itemStats.intelligence
                this.magicalEvade = itemStats.magicalEvade
                this.manaMax = itemStats.manaMax
                this.mana = itemStats.mana
                this.physicalEvade = itemStats.physicalEvade
                this.resistance = itemStats.resistance
                this.constitution = itemStats.constitution
                this.lifeMax = itemStats.lifeMax
                this.life = itemStats.life
                this.strength = itemStats.strength
                this.damage = itemStats.damage
            }
        }
    }
}
