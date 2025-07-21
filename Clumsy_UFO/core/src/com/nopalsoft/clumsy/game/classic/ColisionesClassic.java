package com.nopalsoft.clumsy.game.classic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.objects.ScoreKeeper;
import com.nopalsoft.clumsy.objects.Ufo;

public class ColisionesClassic implements ContactListener {

    WorldGameClassic oWorld;

    public ColisionesClassic(WorldGameClassic oWorld) {
        this.oWorld = oWorld;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getBody().getUserData() instanceof Ufo)
            beginContactBirdOtraCosa(a, b);
        else if (b.getBody().getUserData() instanceof Ufo)
            beginContactBirdOtraCosa(b, a);
    }

    private void beginContactBirdOtraCosa(Fixture bird, Fixture otraCosa) {
        Ufo oUfo = (Ufo) bird.getBody().getUserData();
        Object oOtraCosa = otraCosa.getBody().getUserData();

        if (oOtraCosa instanceof ScoreKeeper) {
            ScoreKeeper obj = (ScoreKeeper) oOtraCosa;
            if (obj.state == ScoreKeeper.STATE_NORMAL) {
                obj.state = ScoreKeeper.STATE_DESTROY;
                oWorld.score++;
                Assets.playSound(Assets.point);
            }
        } else {
            if (oUfo.state == Ufo.STATE_NORMAL) {
                oUfo.getHurt();
                Assets.playSound(Assets.hit);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }
}
