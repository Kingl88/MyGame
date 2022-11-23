package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.persons.Man;
import com.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {
    public static boolean isDamage;

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData().equals("legs") && b.getUserData().equals("Ground")) {
            Man.setCanJump(true);
            Man.onGround = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("Ground")) {
            Man.onGround = true;
            Man.setCanJump(true);
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("flameSensor")) {
            isDamage = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("flameSensor")) {
            isDamage = true;
        }
        if (a.getUserData().equals("Hero") && b.getUserData().equals("CoinsSensor")) {
            GameScreen.bodyToDelete.add(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("CoinsSensor")) {
            GameScreen.bodyToDelete.add(a.getBody());
        }
        if (a.getUserData().equals("bullet") && b.getUserData().equals("stone")) {
            GameScreen.bodyToDelete.add(a.getBody());
        }
        if (b.getUserData().equals("bullet") && a.getUserData().equals("stone")) {
            GameScreen.bodyToDelete.add(b.getBody());
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData().equals("legs") && b.getUserData().equals("Ground")) {
            Man.onGround = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("Ground")) {
            Man.onGround = false;
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("flameSensor")) {
            isDamage = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("flameSensor")) {
            isDamage = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
