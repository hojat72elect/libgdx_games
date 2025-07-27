package com.nopalsoft.ponyrace.objetos;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.WorldTiled;

public class PonyMalo extends Pony {

    private final Vector3 pastPosition;
    public boolean tocoBandera;
    public boolean hasToJump;
    float timeSamePosition;
    private float probababilidad = .5f;
    private float stateTiempoDisparo;

    public PonyMalo(float x, float y, String nombreSkin, WorldTiled oWorld) {
        super(x, y, nombreSkin, oWorld);
        hasToJump = false;

        switch (Settings.dificultadActual) {
            case Settings.DIFICULTAD_EASY:
                probababilidad = .35f;
                break;
            case Settings.DIFICULTAD_NORMAL:
                probababilidad = .5f;
                break;
            case Settings.DIFICULTAD_HARD:
                probababilidad = .7f;
                break;
            case Settings.DIFICULTAD_SUPERHARD:
                probababilidad = 1f;
                break;
        }
        pastPosition = new Vector3();
    }

    public void hitSimpleJump(int nuevaDireccion) {
        if (tocoBandera) {
            float pro = oRan.nextFloat();
            // Gdx.app.log("PROBABILIDAD", pro + "");
            if (pro < probababilidad) {
                hasToJump = true;
                state = nuevaDireccion;
            }
        } else {
            hasToJump = true;
            state = nuevaDireccion;
        }

        tocoBandera = false;
    }

    public void hitCaminarOtraDireccion(int nuevaDireccion) {
        if (tocoBandera) {
            float pro = oRan.nextFloat();
            // Gdx.app.log("PROBABILIDAD", pro + "");
            if (pro < probababilidad) {
                state = nuevaDireccion;
            }
        } else {
            state = nuevaDireccion;
        }
        tocoBandera = false;
    }

    @Override
    public void update(float delta, Body obj, float accelX) {
        stateTiempoDisparo += delta;

        // A veces disparan en medio de un salto y la banana queda en el objeto saltar, entonces los otros ponis q tocan el cuadrito tocan la banana y se atoran
        // por eso pongo !hasToJump || !isJumping.. aun asi el problema continuaaa =(
        float TIEMPO_DISPARO = 3f;
        if (stateTiempoDisparo >= TIEMPO_DISPARO && (!hasToJump || !isJumping)) {
            stateTiempoDisparo -= TIEMPO_DISPARO;
            if (oRan.nextInt(10) < 2 && !pasoLaMeta) {
                fireWood = true;
            }
        }

        // Checo si a estado mucho tiempo en la misma posicion en caso de que si lo pongo a saltar, esto es porque a veces se atoran
        if (pastPosition.x != position.x)
            pastPosition.x = position.x;
        else {
            timeSamePosition += delta;
            if (timeSamePosition >= 2.5f) {
                timeSamePosition = 0;
                hasToJump = true;
            }
        }

        super.update(delta, obj, accelX);
    }
}
