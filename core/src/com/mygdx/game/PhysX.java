package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class PhysX {
    final World world;
    private final Box2DDebugRenderer dDebugRenderer;

    public PhysX() {
        this.world = new World(new Vector2(0, -9.81f), true);
        this.dDebugRenderer = new Box2DDebugRenderer();
    }
    public void debugDraw(Camera camera){
        dDebugRenderer.render(world, camera.combined);
    }
    public void step(){world.step(1/60f, 3, 3);}
    public void dispose(){
        world.dispose();
        dDebugRenderer.dispose();
    }
}
