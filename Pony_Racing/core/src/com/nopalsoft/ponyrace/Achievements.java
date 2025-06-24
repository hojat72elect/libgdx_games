package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Achievements {

    private final Preferences prefAchiv = Gdx.app.getPreferences("com.nopalsoft.ponyracing.achievements");

    final String EASY;
    final String _18plus;
    final String BIG_LEAGES;
    final String _20_COOLER;
    final String FASTER_THAN_THE_MAKER;
    final String I_WORK_OUT;

    MundosCompletados[] arrMundos;

    public Achievements(MainPonyRace game) {

        int len = Assets.mundoMaximo;
        arrMundos = new MundosCompletados[len];

        for (int i = 0; i < len; i++) {
            arrMundos[i] = new MundosCompletados(i + 1);
            MundosCompletados obj = arrMundos[i];
            obj.easy = prefAchiv.getBoolean("world_easy" + obj.nivel, false);
            obj.normal = prefAchiv.getBoolean("world_normal" + obj.nivel, false);
            obj.hard = prefAchiv.getBoolean("world_hard" + obj.nivel, false);
            obj.superHard = prefAchiv.getBoolean("world_superhard" + obj.nivel, false);
            obj.did15Sec = prefAchiv.getBoolean("world_15Secs" + obj.nivel, false);
        }


        EASY = "easy";
            _18plus = "18plus";
            BIG_LEAGES = "bigleagues";
            _20_COOLER = "20Cooler";
            FASTER_THAN_THE_MAKER = "iworkout";
            I_WORK_OUT = "fasterThanTheMaker";
    }

    public void checkWorldComplete(int nivelTiled) {

        MundosCompletados mundoCompletado = null;
        int len = arrMundos.length;
        for (int i = 0; i < len; i++) {
            MundosCompletados obj = arrMundos[i];
            if (obj.nivel == nivelTiled)
                mundoCompletado = obj;
        }
        switch (Settings.dificultadActual) {
            case Settings.DIFICULTAD_EASY:
                mundoCompletado.easy = true;
                break;
            case Settings.DIFICULTAD_NORMAL:
                mundoCompletado.normal = true;
                break;
            case Settings.DIFICULTAD_HARD:
                mundoCompletado.hard = true;
                break;
            case Settings.DIFICULTAD_SUPERHARD:
                mundoCompletado.superHard = true;
                break;
        }

        for (int i = 0; i < len; i++) {
            MundosCompletados obj = arrMundos[i];
            prefAchiv.putBoolean("world_easy" + obj.nivel, obj.easy);
            prefAchiv.putBoolean("world_normal" + obj.nivel, obj.normal);
            prefAchiv.putBoolean("world_hard" + obj.nivel, obj.hard);
            prefAchiv.putBoolean("world_superhard" + obj.nivel, obj.superHard);
        }
        prefAchiv.flush();

        boolean easyComplete, normalComplete, hardComplete, superHardComplete;
        easyComplete = normalComplete = hardComplete = superHardComplete = true;

        for (int i = 0; i < len; i++) {
            MundosCompletados obj = arrMundos[i];

            if (!obj.easy)
                easyComplete = false;
            if (!obj.normal)
                normalComplete = false;
            if (!obj.hard)
                hardComplete = false;
            if (!obj.superHard)
                superHardComplete = false;
        }

        if (easyComplete) {
            Gdx.app.log("ACHIEVEMENT", "EASY");
        }

        if (normalComplete) {
            Gdx.app.log("ACHIEVEMENT", "18+");
        }

        if (hardComplete) {
            Gdx.app.log("ACHIEVEMENT", "BIG LEAGUES");
        }

        if (superHardComplete) {
            Gdx.app.log("ACHIEVEMENT", "20% cooler");
        }
    }

    public void checkVictoryMoreThan15Secs(int nivelTiled, float timeLeft) {

        MundosCompletados mundoCompletado = null;
        int len = arrMundos.length;
        for (int i = 0; i < len; i++) {
            MundosCompletados obj = arrMundos[i];
            if (obj.nivel == nivelTiled)
                mundoCompletado = obj;
        }

        if (timeLeft >= 15 && (Settings.dificultadActual == Settings.DIFICULTAD_HARD || Settings.dificultadActual == Settings.DIFICULTAD_SUPERHARD)) {
            mundoCompletado.did15Sec = true;
        }

        prefAchiv.putBoolean("world_15Secs" + mundoCompletado.nivel, mundoCompletado.did15Sec);
        prefAchiv.flush();

        boolean gotIt = true;

        for (int i = 0; i < len; i++) {
            MundosCompletados obj = arrMundos[i];

            if (!obj.did15Sec) {
                gotIt = false;
                break;
            }
        }

        if (gotIt) {
            Gdx.app.log("ACHIEVEMENT", "FASTER THAN THE MAKER");
        }
    }


    class MundosCompletados {
        final int nivel;
        public boolean easy, normal, hard, superHard, did15Sec;

        public MundosCompletados(int nivel) {
            this.nivel = nivel;
        }
    }
}
