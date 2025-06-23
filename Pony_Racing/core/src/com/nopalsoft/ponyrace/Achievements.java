package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.ponyrace.handlers.GameServicesHandler;
import com.nopalsoft.ponyrace.handlers.GoogleGameServicesHandler;

public class Achievements {

    GameServicesHandler gameServicesHandler;

    private final Preferences prefAchiv = Gdx.app.getPreferences("com.nopalsoft.ponyracing.achievements");

    boolean isGoogleGameServices = false;
    final String EASY;
    final String _18plus;
    final String BIG_LEAGES;
    final String _20_COOLER;
    final String FASTER_THAN_THE_MAKER;
    final String I_WORK_OUT;

    MundosCompletados[] arrMundos;

    public Achievements(MainPonyRace game) {
        this.gameServicesHandler = game.gameServiceHandler;

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

        if (gameServicesHandler instanceof GoogleGameServicesHandler) {
            isGoogleGameServices = true;
            EASY = "CgkIv7KCocYXEAIQFQ";
            _18plus = "CgkIv7KCocYXEAIQFg";
            BIG_LEAGES = "CgkIv7KCocYXEAIQFw";
            _20_COOLER = "CgkIv7KCocYXEAIQGA";
            FASTER_THAN_THE_MAKER = "CgkIv7KCocYXEAIQGQ";
            I_WORK_OUT = "CgkIv7KCocYXEAIQHQ";

        } else {
            EASY = "easy";
            _18plus = "18plus";
            BIG_LEAGES = "bigleagues";
            _20_COOLER = "20Cooler";
            FASTER_THAN_THE_MAKER = "iworkout";
            I_WORK_OUT = "fasterThanTheMaker";
        }

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
            gameServicesHandler.unlockAchievement(EASY);
            Gdx.app.log("ACHIEVEMENT", "EASY");
        }

        if (normalComplete) {
            gameServicesHandler.unlockAchievement(_18plus);
            Gdx.app.log("ACHIEVEMENT", "18+");
        }

        if (hardComplete) {
            gameServicesHandler.unlockAchievement(BIG_LEAGES);
            Gdx.app.log("ACHIEVEMENT", "BIG LEAGUES");
        }

        if (superHardComplete) {
            gameServicesHandler.unlockAchievement(_20_COOLER);
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

            if (!obj.did15Sec)
                gotIt = false;
        }

        if (gotIt) {
            gameServicesHandler.unlockAchievement(FASTER_THAN_THE_MAKER);// 29 Faster than the maker!
            Gdx.app.log("ACHIEVEMENT", "FASTER THAN THE MAKER");
        }
    }

    public void checkMonedasAchievements(int monedasTomadasLevel) {
        if (!isGoogleGameServices)
            return;

        if (monedasTomadasLevel <= 0)
            return;

        if (Settings.statMonedasTotal < 1000)
            gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQGg", monedasTomadasLevel);// 21 You got any spare change?

        if (Settings.statMonedasTotal < 2000)
            gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQGw", monedasTomadasLevel);// 22 Got my mind on my money

        if (Settings.statMonedasTotal < 12000)
            gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQHA", monedasTomadasLevel);// 23 My name is not Filthy Rich

    }

    public void checkUpgradesAchivmentes() {
        if (Settings.nivelChocolate >= 5 && Settings.nivelChili >= 5 && Settings.nivelBallon >= 5) {
            gameServicesHandler.unlockAchievement(I_WORK_OUT);// 30 I work out!
        }
    }

    // ###################

    public void checkHitBombAchievements() {
        if (!isGoogleGameServices)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQHg", 1);// Did someone said bombs?
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQHw", 1);// Rain Boom Pony
    }

    public void checkHitSpikeAchievements() {
        if (!isGoogleGameServices)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQIA", 1);// Hold On!
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQIQ", 1);// Trap Master
    }

    public void checkEatChocolateAchievements(int duclesTomadosLevel) {
        if (!isGoogleGameServices)
            return;
        if (duclesTomadosLevel <= 0)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQJQ", duclesTomadosLevel);// Chocolate Taster
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQJg", duclesTomadosLevel);// Sugar Rush
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQJw", duclesTomadosLevel);// i might need a diet
    }

    public void checkWinFirstPlaceAchievements() {
        if (!isGoogleGameServices)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQIg", 1);// Need for Speed
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQIw", 1);// Speed Demon
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQJA", 1);// Racing Blood
    }

    public void checkEatChiliAchievements(int chilesTomadosLevel) {
        if (!isGoogleGameServices)
            return;
        if (chilesTomadosLevel <= 0)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQKA", chilesTomadosLevel);// 18 Spicy
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQKQ", chilesTomadosLevel);// 19 Breath of fire
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQKg", chilesTomadosLevel);// 20 Iron pony
    }

    public void checkGetBallonsAchievements(int globosTomadosLevel) {
        if (!isGoogleGameServices)
            return;
        if (globosTomadosLevel <= 0)
            return;
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQKw", globosTomadosLevel);// 26 Time Seeker
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQLA", globosTomadosLevel);// 27 Time to Spare
        gameServicesHandler.unlockIncrementalAchievement("CgkIv7KCocYXEAIQLQ", globosTomadosLevel);// 28 Time Agent
    }

    class MundosCompletados {
        final int nivel;
        public boolean easy, normal, hard, superHard, did15Sec;

        public MundosCompletados(int nivel) {
            this.nivel = nivel;
        }

    }

}
