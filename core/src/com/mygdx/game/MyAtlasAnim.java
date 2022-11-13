package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnim {
    private TextureAtlas atlas;
    private Animation<TextureAtlas.AtlasRegion> anm;
    private float time;

    public MyAtlasAnim(String atlasName, String name, float fps, Animation.PlayMode playMode) {
        time = 0;
        this.atlas = new TextureAtlas(atlasName);
        anm = new Animation<>(1 / fps, this.atlas.findRegions(name));
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return anm.getKeyFrame(time);
    }

    public void setTime(float dT) {
        time += dT;
    }
    public void putTime(float time){this.time = time;}
    public void dispose(){
        this.atlas.dispose();
    }
}
