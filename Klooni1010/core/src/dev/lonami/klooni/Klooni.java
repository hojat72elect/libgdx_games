package dev.lonami.klooni;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dev.lonami.klooni.effects.EvaporateEffectFactory;
import dev.lonami.klooni.effects.ExplodeEffectFactory;
import dev.lonami.klooni.effects.SpinEffectFactory;
import dev.lonami.klooni.effects.VanishEffectFactory;
import dev.lonami.klooni.effects.WaterdropEffectFactory;
import dev.lonami.klooni.interfaces.IEffectFactory;
import dev.lonami.klooni.screens.MainMenuScreen;
import dev.lonami.klooni.screens.TransitionScreen;
import java.util.HashMap;
import java.util.Map;

public class Klooni extends Game {

    // ordered list of effects. index 0 will get default if VanishEffectFactory is removed from list
    public final static IEffectFactory[] EFFECTS = {
            new VanishEffectFactory(),
            new WaterdropEffectFactory(),
            new EvaporateEffectFactory(),
            new SpinEffectFactory(),
            new ExplodeEffectFactory(),
    };
    public static final int GAME_HEIGHT = 680;
    public static final int GAME_WIDTH = 408;
    private final static float SCORE_TO_MONEY = 1f / 100f;
    // FIXME theme should NOT be static as it might load textures which will expose it to the race condition iff GDX got initialized before or not
    public static Theme theme;
    public static boolean onDesktop;
    private static Preferences prefs;
    public final ShareChallenge shareChallenge;
    public IEffectFactory effect;
    public Skin skin;

    private Map<String, Sound> effectSounds;

    // TODO Possibly implement a 'ShareChallenge'
    //      for other platforms instead passing null
    public Klooni(final ShareChallenge shareChallenge) {
        this.shareChallenge = shareChallenge;
    }

    public static int getMaxScore() {
        return prefs.getInteger("maxScore", 0);
    }

    public static void setMaxScore(int score) {
        prefs.putInteger("maxScore", score).flush();
    }

    public static int getMaxTimeScore() {
        return prefs.getInteger("maxTimeScore", 0);
    }

    public static void setMaxTimeScore(int maxTimeScore) {
        prefs.putInteger("maxTimeScore", maxTimeScore).flush();
    }

    public static boolean soundsEnabled() {
        return !prefs.getBoolean("muteSound", false);
    }


    public static boolean toggleSound() {
        final boolean result = soundsEnabled();
        prefs.putBoolean("muteSound", result).flush();
        return !result;
    }

    public static boolean shouldSnapToGrid() {
        return prefs.getBoolean("snapToGrid", false);
    }

    public static boolean toggleSnapToGrid() {
        final boolean result = !shouldSnapToGrid();
        prefs.putBoolean("snapToGrid", result).flush();
        return result;
    }

    // Themes related
    public static boolean isThemeBought(Theme theme) {
        if (theme.getPrice() == 0)
            return true;

        String[] themes = prefs.getString("boughtThemes", "").split(":");
        for (String t : themes)
            if (t.equals(theme.getName()))
                return true;

        return false;
    }

    public static boolean buyTheme(Theme theme) {
        final float money = getRealMoney();
        if (theme.getPrice() > money)
            return false;

        setMoney(money - theme.getPrice());

        String bought = prefs.getString("boughtThemes", "");
        if (bought.equals(""))
            bought = theme.getName();
        else
            bought += ":" + theme.getName();

        prefs.putString("boughtThemes", bought);

        return true;
    }

    public static void updateTheme(Theme newTheme) {
        prefs.putString("themeName", newTheme.getName()).flush();
        theme.update(newTheme.getName());
    }

    public static boolean isEffectBought(IEffectFactory effect) {
        if (effect.getPrice() == 0)
            return true;

        String[] effects = prefs.getString("boughtEffects", "").split(":");
        for (String e : effects)
            if (e.equals(effect.getName()))
                return true;

        return false;
    }

    public static boolean buyEffect(IEffectFactory effect) {
        final float money = getRealMoney();
        if (effect.getPrice() > money)
            return false;

        setMoney(money - effect.getPrice());

        String bought = prefs.getString("boughtEffects", "");
        if (bought.equals(""))
            bought = effect.getName();
        else
            bought += ":" + effect.getName();

        prefs.putString("boughtEffects", bought);

        return true;
    }

    // Money related
    public static void addMoneyFromScore(int score) {
        setMoney(getRealMoney() + score * SCORE_TO_MONEY);
    }

    public static int getMoney() {
        return (int) getRealMoney();
    }

    private static void setMoney(float money) {
        prefs.putFloat("money", money).flush();
    }

    private static float getRealMoney() {
        return prefs.getFloat("money");
    }

    @Override
    public void create() {
        onDesktop = Gdx.app.getType().equals(Application.ApplicationType.Desktop);
        prefs = Gdx.app.getPreferences("dev.lonami.klooni.game");

        // Load the best match for the skin (depending on the device screen dimensions)
        skin = SkinLoader.loadSkin();

        // Use only one instance for the theme, so anyone using it uses the most up-to-date
        Theme.skin = skin; // Not the best idea
        final String themeName = prefs.getString("themeName", "default");
        if (Theme.exists(themeName))
            theme = Theme.getTheme(themeName);
        else
            theme = Theme.getTheme("default");

        Gdx.input.setCatchBackKey(true); // To show the pause menu
        setScreen(new MainMenuScreen(this));
        String effectName = prefs.getString("effectName", "vanish");
        effectSounds = new HashMap<String, Sound>(EFFECTS.length);
        effect = EFFECTS[0];
        for (IEffectFactory e : EFFECTS) {
            loadEffectSound(e.getName());
            if (e.getName().equals(effectName)) {
                effect = e;
            }
        }
    }

    // TransitionScreen will also dispose by default the previous screen
    public void transitionTo(Screen screen) {
        transitionTo(screen, true);
    }

    public void transitionTo(Screen screen, boolean disposeAfter) {
        setScreen(new TransitionScreen(this, getScreen(), screen, disposeAfter));
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        theme.dispose();
        if (effectSounds != null) {
            for (Sound s : effectSounds.values()) {
                s.dispose();
            }
            effectSounds = null;
        }
    }

    private void loadEffectSound(final String effectName) {
        FileHandle soundFile = Gdx.files.internal("sound/effect_" + effectName + ".mp3");
        if (!soundFile.exists())
            soundFile = Gdx.files.internal("sound/effect_vanish.mp3");

        effectSounds.put(effectName, Gdx.audio.newSound(soundFile));
    }

    public void playEffectSound() {
        effectSounds.get(effect.getName())
                .play(MathUtils.random(0.7f, 1f), MathUtils.random(0.8f, 1.2f), 0);
    }

    public void updateEffect(IEffectFactory newEffect) {
        prefs.putString("effectName", newEffect.getName()).flush();
        // Create a new effect, since the one passed through the parameter may dispose later
        effect = newEffect;
    }
}
