package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.persons.Man;
import com.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a.getUserData().equals("legs") && b.getUserData().equals("Ground")){
            Man.setCanJump(true);
            Man.onGround = true;
        }
        if(b.getUserData().equals("legs") && a.getUserData().equals("Ground")){
            Man.onGround = true;
            Man.setCanJump(true);
        }
        if(a.getUserData().equals("legs") && b.getUserData().equals("flame")){
            GameScreen.destroy = true;
        }
        if(b.getUserData().equals("legs") && a.getUserData().equals("flame")){
            GameScreen.destroy = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a.getUserData().equals("legs") && b.getUserData().equals("Ground")){
            Man.onGround = false;
        }
        if(b.getUserData().equals("legs") && a.getUserData().equals("Ground")){
            Man.onGround = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a.getUserData().equals("Hero") && b.getUserData().equals("Coins")){
            GameScreen.bodeToDelete.add(b.getBody());
        }
        if(b.getUserData().equals("Hero") && a.getUserData().equals("Coins")){
            GameScreen.bodeToDelete.add(a.getBody());
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
