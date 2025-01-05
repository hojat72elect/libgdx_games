package dev.ian.snakeboi.asset;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Created by: Ian Parcon
 * Date created: Aug 27, 2018
 * Time created: 9:32 PM
 */
public class SoundPlayer {

    private static HashMap<String, Music> musics = new HashMap<String, Music>();
    private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();

    private SoundPlayer() {
    }

    public static void init() {
        musics.put(Asset.MEMO_SOUND, (Music) Asset.instance().get(Asset.MEMO_SOUND));
        musics.put(Asset.GAME_OVER_SOUND, (Music) Asset.instance().get(Asset.GAME_OVER_SOUND));
        sounds.put(Asset.EAT_FOOD_SOUND, (Sound) Asset.instance().get(Asset.EAT_FOOD_SOUND));
        sounds.put(Asset.CRASH_SOUND, (Sound) Asset.instance().get(Asset.CRASH_SOUND));
    }

    public static Sound playSound(String name, boolean looping) {
        Sound sound = sounds.get(name);
        sound.setLooping(sound.play(.5f), looping);

        return sound;
    }

    public static Music playMusic(String name, boolean looping) {
        Music music = musics.get(name);
        music.setLooping(looping);
        if (!music.isPlaying() && !music.isLooping()) music.play();
        return music;
    }

    public static void stopSound(String id) {
        sounds.get(id).stop();
    }

    public static void stopMusic(String id) {
        musics.get(id).stop();
    }
}
