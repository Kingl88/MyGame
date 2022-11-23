package com.mygdx.game.persons;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.PhysX;

public class Bullet {
    private final Body body;
    private final static float SPD = 120;
    private float time;

    public Bullet(PhysX physX, float x, float y, int dir) {
        body = physX.addBullet(x, y);
        body.setBullet(true);
        body.setLinearVelocity(SPD * dir, 0);
        time = 1;
    }

    public Body update(float dTime) {
        time -= dTime;
        return (time <= 0) ? body : null;
    }
}
