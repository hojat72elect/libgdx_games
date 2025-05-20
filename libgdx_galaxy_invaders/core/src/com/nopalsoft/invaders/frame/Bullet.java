package com.nopalsoft.invaders.frame;

public class Bullet extends DynamicGameObject {

    public static final float WIDTH = 2.1f;
    public static final float HEIGHT = 1.5f;

    public final float SPEED = 30;
    public final static int STATE_DISPARADO = 0;
    public final static int STATE_EXPLOTANDO = 1;

    public int level = 1;
    public float stateTime;
    public int state;

    /**
     * Space ship bullet
     */
    public Bullet(float x, float y, int boostLevel) {
        super(x, y, WIDTH, HEIGHT);
        state = STATE_DISPARADO;
        stateTime = 0;
        velocity.set(0, SPEED);
        this.level += boostLevel;
    }

    /**
     * Alien Bullet
     */
    public Bullet(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        state = STATE_DISPARADO;
        stateTime = 0;
        velocity.set(0, -SPEED);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsRectangle.x = position.x - WIDTH / 2;
        boundsRectangle.y = position.y - HEIGHT / 2;
        stateTime += deltaTime;
    }

    public void hitTarget(int vidaTarget) {
        level -= vidaTarget;
        if (level <= 0) {
            velocity.set(0, 0);
            stateTime = 0;
            state = STATE_EXPLOTANDO;
        }
    }

    /**
     * En caso de que la bala se salga de la pantalla World.Height pues mando llamar este metodo para que se remueva del arreglo
     */
    public void destruirBala() {
        velocity.set(0, 0);
        stateTime = 0;
        state = STATE_EXPLOTANDO;
    }

}
