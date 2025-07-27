package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    public static final int MONEDAS_REGALO_FACEBOOK = 3500;
    public static final int MONEDAS_REGALO_SHARE_FACEBOOK = 1000;

    /**
     * No use enums porque con la forma implementada para cambiar la dificultad era mas facil usar enteros
     */
    public static final int DIFICULTAD_EASY = 0;
    public static final int DIFICULTAD_NORMAL = 1;
    public static final int DIFICULTAD_HARD = 2;
    public static final int DIFICULTAD_SUPERHARD = 3;
    private final static Preferences pref = Gdx.app
            .getPreferences("com.nopalsoft.ponyRace.settings");
    public static boolean isEnabledSecretWorld = false; // Esta variable siempre es falsa al inicio y no se guarda
    public static boolean isSonidoON = false;
    public static boolean isMusicaON = false;
    public static int mundosDesbloqueados;
    public static boolean seDioLike = false;
    public static boolean seCalificoApp = false;
    //
    public static String skinSeleccionada;
    public static int numeroBombas = 0;// Bombas para usar in game
    public static int numeroWoods = 0;// Troncos disponibles para usar in game
    public static int numeroMonedasActual = 0;// Monedas disponibles para gastar
    public static int statMonedasTotal;// Estadisticas de las monedas totales.
    public static int statTimesPlayed;
    public static int dificultadActual = DIFICULTAD_NORMAL;
    public static int nivelChocolate = 0;
    public static int nivelBallon = 0;
    public static int nivelChili = 0;
    public static int nivelBomb = 0;
    public static int nivelWood = 0;
    public static int nivelCoin = 0;
    public static int nivelTime = 0;
    public static boolean isBackGroundEnabled = true;

    public static void cargar() {
        isSonidoON = pref.getBoolean("isSonidoON", true);
        isMusicaON = pref.getBoolean("isMusicaON", true);

        seDioLike = pref.getBoolean("seDioLike", false);
        seCalificoApp = pref.getBoolean("seCalificoApp", false);

        skinSeleccionada = pref.getString("skinSeleccionada", "Cloud");
        mundosDesbloqueados = pref.getInteger("mundosDesbloqueados", 1);

        // Itemes usables en juego Mondeas bombas
        numeroBombas = pref.getInteger("numeroBombas", 25);
        numeroWoods = pref.getInteger("numeroWoods", 25);
        numeroMonedasActual = pref.getInteger("numeroMonedasActual", 500);

        // Estadistias de las monedas
        statMonedasTotal = pref.getInteger("statMonedasTotal", 0);
        statTimesPlayed = pref.getInteger("statTimesPlayed", 0);

        // Niveles de las cosas
        nivelChocolate = pref.getInteger("nivelChocolate", 0);
        nivelBallon = pref.getInteger("nivelBallon", 0);
        nivelChili = pref.getInteger("nivelChili", 0);
        nivelBomb = pref.getInteger("nivelBomb", 0);
        nivelWood = pref.getInteger("nivelWood", 0);
        nivelCoin = pref.getInteger("nivelCoin", 0);
        nivelTime = pref.getInteger("nivelTime", 0);

        dificultadActual = pref.getInteger("dificultadActual",
                DIFICULTAD_NORMAL);
    }

    public static void guardar() {
        pref.putBoolean("isSonidoON", isSonidoON);
        pref.putBoolean("isMusicaON", isMusicaON);

        pref.putBoolean("seDioLike", seDioLike);
        pref.putBoolean("seCalificoApp", seCalificoApp);

        pref.putString("skinSeleccionada", skinSeleccionada);
        pref.putInteger("mundosDesbloqueados", mundosDesbloqueados);

        pref.putInteger("numeroBombas", numeroBombas);
        pref.putInteger("numeroWoods", numeroWoods);
        pref.putInteger("numeroMonedasActual", numeroMonedasActual);

        pref.putInteger("statMonedasTotal", statMonedasTotal);
        pref.putInteger("statTimesPlayed", statTimesPlayed);

        pref.putInteger("nivelChocolate", nivelChocolate);
        pref.putInteger("nivelBallon", nivelBallon);
        pref.putInteger("nivelChili", nivelChili);
        pref.putInteger("nivelBomb", nivelBomb);
        pref.putInteger("nivelWood", nivelWood);
        pref.putInteger("nivelCoin", nivelCoin);
        pref.putInteger("nivelTime", nivelTime);

        pref.putInteger("dificultadActual", dificultadActual);
        pref.flush();
    }

    public static void borrarDatosGuardados() {
        pref.clear();
        pref.flush();
        cargar();
    }

    /**
     * Suma numMonedas al total de monedas para gastar y a las estadisticas de las monedas recolectadas para los achivments
     *
     * @param numMonedas = Numero de monedas a incrementar.
     */
    public static void sumarMonedas(int numMonedas) {
        numeroMonedasActual += numMonedas;
        // Estas se incrementan de 1 en 1 sin importar que se alla tomado una moneda que vale x2 o x3. Para que sea mas facil implementar
        // Los google game services.
        statMonedasTotal += numMonedas;
    }
}
