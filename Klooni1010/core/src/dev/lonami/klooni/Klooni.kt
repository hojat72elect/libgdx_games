package dev.lonami.klooni

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import dev.lonami.klooni.SkinLoader.loadSkin
import dev.lonami.klooni.Theme.Companion.exists
import dev.lonami.klooni.Theme.Companion.getTheme
import dev.lonami.klooni.effects.EvaporateEffectFactory
import dev.lonami.klooni.effects.ExplodeEffectFactory
import dev.lonami.klooni.effects.SpinEffectFactory
import dev.lonami.klooni.effects.VanishEffectFactory
import dev.lonami.klooni.effects.WaterdropEffectFactory
import dev.lonami.klooni.interfaces.EffectFactory
import dev.lonami.klooni.screens.MainMenuScreen
import dev.lonami.klooni.screens.TransitionScreen

class Klooni(@JvmField val shareChallenge: ShareChallenge?) : Game() {
    @JvmField
    var effect: EffectFactory? = null

    @JvmField
    var skin: Skin? = null

    private var effectSounds: MutableMap<String?, Sound>? = null

    override fun create() {
        onDesktop = Gdx.app.type == Application.ApplicationType.Desktop
        prefs = Gdx.app.getPreferences("dev.lonami.klooni.game")

        // Load the best match for the skin (depending on the device screen dimensions)
        skin = loadSkin()

        // Use only one instance for the theme, so anyone using it uses the most up-to-date
        Theme.skin = skin // Not the best idea
        val themeName: String = prefs!!.getString("themeName", "default")
        theme = if (exists(themeName)) getTheme(themeName)
        else getTheme("default")

        Gdx.input.isCatchBackKey = true // To show the pause menu
        setScreen(MainMenuScreen(this))
        val effectName: String = prefs!!.getString("effectName", "vanish")
        effectSounds = HashMap(EFFECTS.size)
        effect = EFFECTS[0]
        for (e in EFFECTS) {
            loadEffectSound(e.name)
            if (e.name == effectName) {
                effect = e
            }
        }
    }

    // TransitionScreen will also dispose by default the previous screen
    @JvmOverloads
    fun transitionTo(screen: Screen?, disposeAfter: Boolean = true) {
        setScreen(TransitionScreen(this, getScreen(), screen!!, disposeAfter))
    }

    override fun dispose() {
        super.dispose()
        skin!!.dispose()
        theme!!.dispose()
        if (effectSounds != null) {
            for (s in effectSounds!!.values) {
                s.dispose()
            }
            effectSounds = null
        }
    }

    private fun loadEffectSound(effectName: String?) {
        var soundFile = Gdx.files.internal("sound/effect_$effectName.mp3")
        if (!soundFile.exists()) soundFile = Gdx.files.internal("sound/effect_vanish.mp3")

        effectSounds!!.put(effectName, Gdx.audio.newSound(soundFile))
    }

    fun playEffectSound() {
        effectSounds!![effect!!.name]!!
            .play(MathUtils.random(0.7f, 1f), MathUtils.random(0.8f, 1.2f), 0f)
    }

    fun updateEffect(newEffect: EffectFactory) {
        prefs!!.putString("effectName", newEffect.name).flush()
        // Create a new effect, since the one passed through the parameter may dispose later
        effect = newEffect
    }

    companion object {
        // ordered list of effects. index 0 will get default if VanishEffectFactory is removed from list
        @JvmField
        val EFFECTS: Array<EffectFactory> = arrayOf<EffectFactory>(
            VanishEffectFactory(),
            WaterdropEffectFactory(),
            EvaporateEffectFactory(),
            SpinEffectFactory(),
            ExplodeEffectFactory(),
        )
        const val GAME_HEIGHT: Int = 680
        const val GAME_WIDTH: Int = 408
        private const val SCORE_TO_MONEY = 1f / 100f

        // FIXME theme should NOT be static as it might load textures which will expose it to the race condition iff GDX got initialized before or not
        @JvmField
        var theme: Theme? = null

        @JvmField
        var onDesktop: Boolean = false
        private var prefs: Preferences? = null

        fun getMaxScore(): Int {
            return prefs!!.getInteger("maxScore", 0)
        }

        fun setMaxScore(score: Int) {
            prefs!!.putInteger("maxScore", score).flush()
        }

        @JvmStatic
        fun getMaxTimeScore(): Int {
            return prefs!!.getInteger("maxTimeScore", 0)
        }

        @JvmStatic
        fun setMaxTimeScore(maxTimeScore: Int) {
            prefs!!.putInteger("maxTimeScore", maxTimeScore).flush()
        }

        @JvmStatic
        fun soundsEnabled(): Boolean {
            return !prefs!!.getBoolean("muteSound", false)
        }

        @JvmStatic
        fun toggleSound(): Boolean {
            val result: Boolean = soundsEnabled()
            prefs!!.putBoolean("muteSound", result).flush()
            return !result
        }

        @JvmStatic
        fun shouldSnapToGrid(): Boolean {
            return prefs!!.getBoolean("snapToGrid", false)
        }

        @JvmStatic
        fun toggleSnapToGrid(): Boolean {
            val result: Boolean = !shouldSnapToGrid()
            prefs!!.putBoolean("snapToGrid", result).flush()
            return result
        }

        @JvmStatic
        fun isThemeBought(theme: Theme): Boolean {
            if (theme.price == 0) return true

            val themes = prefs!!.getString("boughtThemes", "").split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (t in themes) if (t == theme.name) return true

            return false
        }

        @JvmStatic
        fun buyTheme(theme: Theme): Boolean {
            val money: Float = getRealMoney()
            if (theme.price > money) return false

            setMoney(money - theme.price)

            var bought: String? = prefs!!.getString("boughtThemes", "")
            if (bought!!.isEmpty()) bought = theme.name
            else bought += ":" + theme.name

            prefs!!.putString("boughtThemes", bought)

            return true
        }

        @JvmStatic
        fun updateTheme(newTheme: Theme) {
            prefs!!.putString("themeName", newTheme.name).flush()
            theme!!.update(newTheme.name)
        }

        @JvmStatic
        fun isEffectBought(effect: EffectFactory): Boolean {
            if (effect.price == 0) return true

            val effects = prefs!!.getString("boughtEffects", "").split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (e in effects) if (e == effect.name) return true

            return false
        }

        @JvmStatic
        fun buyEffect(effect: EffectFactory): Boolean {
            val money: Float = getRealMoney()
            if (effect.price > money) return false

            setMoney(money - effect.price)

            var bought: String = prefs!!.getString("boughtEffects", "")
            if (bought.isEmpty()) bought = effect.name
            else bought += ":" + effect.name

            prefs!!.putString("boughtEffects", bought)

            return true
        }

        @JvmStatic
        fun addMoneyFromScore(score: Int) {
            setMoney(getRealMoney() + score * SCORE_TO_MONEY)
        }

        @JvmStatic
        fun getMoney(): Int {
            return getRealMoney().toInt()
        }

        private fun setMoney(money: Float) {
            prefs!!.putFloat("money", money).flush()
        }

        private fun getRealMoney(): Float {
            return prefs!!.getFloat("money")
        }
    }
}
