package com.nopalsoft.ponyrace.objetos;

import com.nopalsoft.ponyrace.game.WorldTiled;

import java.util.Random;

public class Bandera1 {
    public State state;
    public TipoAccion tipoAccion;
    Random oRan;

    public Bandera1(WorldTiled oWorld, TipoAccion tipoAccion) {
        oRan = oWorld.oRan;
        state = State.normal;
        this.tipoAccion = tipoAccion;
    }

    public boolean permitirSalto() {
        if (state == State.normal && oRan.nextBoolean()) {
            state = State.tomada;
            return true;
        }
        return false;
    }

    public enum State {
        normal,
        tomada
    }

    public enum TipoAccion {
        saltoIzq,
        saltoDer,
        salto
    }
}
