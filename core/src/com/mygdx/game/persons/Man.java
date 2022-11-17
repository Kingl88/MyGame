package com.mygdx.game.persons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.PhysX;
import com.mygdx.game.enums.Actions;

import java.util.HashMap;

public class Man {
    private HashMap<Actions, Animation<TextureRegion>> manAssets = new HashMap<>();
    private final float FPS = 1/7f;
    private float time;
    public static boolean canJump;
    public static boolean onGround;
    private Animation<TextureRegion> baseAnm;
    private boolean loop;
    private TextureAtlas atl;
    private Body body;
    private Dir dir;
    private static float dScale = 1.4f;
    public enum Dir{LEFT, RIGHT}

    public Man(Body body) {
        this.body = body;
        atl = new TextureAtlas("atlas/MyPerson.atlas");
        manAssets.put(Actions.STAND,new Animation<TextureRegion>(FPS, atl.findRegions("stand")));
        manAssets.put(Actions.RUN,new Animation<TextureRegion>(FPS, atl.findRegions("run")));
        manAssets.put(Actions.JUMP,new Animation<TextureRegion>(FPS, atl.findRegions("jump")));
        baseAnm = manAssets.get(Actions.STAND);
        loop = true;
        dir = Dir.LEFT;
    }
    public boolean isCanJump(){return canJump;}
    public static void setCanJump(boolean isJump){canJump = isJump;}
    public void setDir(Dir dir){this.dir = dir;}
    public void setLoop(boolean loop){this.loop = loop;}
    public void setFPS(Vector2 vector){
        if(vector.x > 0.1f) setDir(Dir.RIGHT);
        if(vector.x < 0.1f) setDir(Dir.LEFT);
        float tmp = (float) (Math.sqrt(vector.x * vector.x + vector.y * vector.y)) * 10;
        setState(Actions.STAND);
        if(Math.abs(vector.x) > 0.25f && Math.abs(vector.y) < 10 && onGround){
            setState(Actions.RUN);
            baseAnm.setFrameDuration(1/tmp);
        }
        if(Math.abs(vector.y) > 1 && !onGround){
            setState(Actions.JUMP);
            baseAnm.setFrameDuration(FPS);
        }
    }
    public float setTime(float deltaTime){
        time += deltaTime;
        return time;
    }
    public void setState(Actions state){
        baseAnm = manAssets.get(state);
        switch (state){
            case STAND -> {
                loop = true;
                baseAnm.setFrameDuration(FPS);
            }
            case JUMP -> loop = false;
            default -> loop = true;
        }
    }
    public TextureRegion getFrame(){
        if(time > baseAnm.getAnimationDuration() && loop) {time = 0;}
        if(time > baseAnm.getAnimationDuration()) {time = 0;}
        TextureRegion tr = baseAnm.getKeyFrame(time);
        if(!tr.isFlipX() && dir == Dir.LEFT) tr.flip(true, false);
        if(tr.isFlipX() && dir == Dir.RIGHT) tr.flip(true, false);
        return tr;
    }
    public Rectangle getRect(OrthographicCamera camera, TextureRegion region){
        TextureRegion tr = baseAnm.getKeyFrame(time);
        float cx = body.getPosition().x * PhysX.PPM - tr.getRegionWidth()/ 2f / dScale;
        float cy = body.getPosition().y * PhysX.PPM - tr.getRegionHeight()/ 2f / dScale;
        float cW = tr.getRegionWidth() / PhysX.PPM / dScale;
        float cH = tr.getRegionHeight() / PhysX.PPM / dScale;
        return new Rectangle(cx,cy, cW, cH);
    }
    public void dispose(){
        atl.dispose();
        this.manAssets.clear();
    }
}
