package com.nGame.utils.ng;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by oli on 17.04.2014.
 */
class Conf {
    public static void loadConf() {
        Preferences prefs = Gdx.app.getPreferences("samegameconf");

        System.LOGLEVEL = prefs.getInteger(Name.SYS_LOGLEVEL, System.LOGLEVEL);

        Video.FULLSCREEN = prefs.getBoolean(Name.V_FULLSCREEN, Video.FULLSCREEN);
        Video.VSYNC = prefs.getBoolean(Name.V_VSYNC, Video.VSYNC);
        Video.MIPMAPS = prefs.getBoolean(Name.V_MIPMAPS, Video.MIPMAPS);
        Video.WIDTH = prefs.getInteger(Name.V_WIDTH, Video.WIDTH);
        Video.HEIGHT = prefs.getInteger(Name.V_HEIGHT, Video.HEIGHT);

        Audio.MUSIC = prefs.getBoolean(Name.S_MUSIC, Audio.MUSIC);
        Audio.MUSIC_VOL = prefs.getFloat(Name.S_MUSIC_VOL, Audio.MUSIC_VOL);
        Audio.SOUND = prefs.getBoolean(Name.S_SOUND, Audio.SOUND);
        Audio.SOUND_VOL = prefs.getFloat(Name.S_SOUND_VOL, Audio.SOUND_VOL);

        Visual.SCREENTRANSITIONTIME = prefs.getFloat(Name.VIS_SCRTRANSTIME, Visual.SCREENTRANSITIONTIME);
        Visual.PAGERFLINGTIME = prefs.getFloat(Name.VIS_PAGERTIME, Visual.PAGERFLINGTIME);

    }

    public static void saveConf() {
        Preferences prefs = Gdx.app.getPreferences("samegameconf");

        prefs.putInteger(Name.SYS_LOGLEVEL, System.LOGLEVEL);

        prefs.putBoolean(Name.V_FULLSCREEN, Video.FULLSCREEN);
        prefs.putBoolean(Name.V_VSYNC, Video.VSYNC);
        prefs.putBoolean(Name.V_MIPMAPS, Video.MIPMAPS);
        prefs.putInteger(Name.V_WIDTH, Video.WIDTH);
        prefs.putInteger(Name.V_HEIGHT, Video.WIDTH);

        prefs.putBoolean(Name.S_MUSIC, Audio.MUSIC);
        prefs.putFloat(Name.S_MUSIC_VOL, Audio.MUSIC_VOL);
        prefs.putBoolean(Name.S_SOUND, Audio.SOUND);
        prefs.putFloat(Name.S_SOUND_VOL, Audio.SOUND_VOL);


        prefs.putFloat(Name.VIS_SCRTRANSTIME, Visual.SCREENTRANSITIONTIME);
        prefs.putFloat(Name.VIS_PAGERTIME, Visual.PAGERFLINGTIME);

        prefs.flush();
    }

    public static class Video {
        public static boolean FULLSCREEN = false;
        public static boolean VSYNC = true;
        public static boolean MIPMAPS = true;
        public static int WIDTH = 1280;
        public static int HEIGHT = 720;

        public static void setVsync(boolean active) {
            Gdx.graphics.setVSync(active);
        }
    }

    public static class Visual {
        public static GameNG.Transition SCREENTRANSITION = GameNG.Transition.MOVE_LEFT;
        public static GameNG.Transition SCREENTRANSITIONBACK = GameNG.Transition.MOVE_RIGHT;
        public static float SCREENTRANSITIONTIME = .3f;
        public static float PAGERFLINGTIME = .2f;
        public static int PAGERSPACING = 0;
        public static String THEME = "default";
    }

    public static class Audio {
        public static boolean SOUND = true;
        public static boolean MUSIC = true;
        public static float SOUND_VOL = 1f;
        public static float MUSIC_VOL = .8f;
    }

    public static class System {
        public static int LOGLEVEL = Application.LOG_DEBUG;
        public static String SAVEFILE = "samegamesave";
        public static String CFGFILE = "samegameconf";
    }

    public static class GameScreen {
        public static int GS_HEADERHEIGHT = 200;
    }


    private static class Name {
        public static String SYS_LOGLEVEL = "sys_loglevel";

        public static String V_FULLSCREEN = "vid_fullscreen";
        public static String V_VSYNC = "vid_csync";
        public static String V_MIPMAPS = "vid_mipmaps";
        public static String V_WIDTH = "vid_width";
        public static String V_HEIGHT = "vid_height";


        public static String S_MUSIC = "snd_music";
        public static String S_MUSIC_VOL = "snd_music_vol";
        public static String S_SOUND = "snd_sound";
        public static String S_SOUND_VOL = "snd_sound_vol";

        public static String VIS_SCRTRANSTIME = "vis_screentranstime";


        public static String VIS_PAGERTIME = "vis_pagertime";

        public static String VIS_THEME = "vis_theme";

    }

}
