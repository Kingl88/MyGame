package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnim {
    private Texture img;
    private Animation<TextureRegion> anm;
    private float time;

    public MyAnim(String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        img = new Texture(name);
        TextureRegion reg = new TextureRegion(img);
        TextureRegion[][] regions = reg.split(img.getWidth() / col, img.getHeight() / row);
        TextureRegion[] temp = new TextureRegion[regions.length * regions[0].length];
        int index = 0;
        for (TextureRegion[] region : regions) {
            for (TextureRegion textureRegion : region) {
                temp[index] = textureRegion;
                index++;
            }
        }
        anm = new Animation<>(1 / fps, temp);
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return anm.getKeyFrame(time);
    }

    public void setTime(float dT) {
        time += dT;
    }
    public void dispose(){
        this.img.dispose();
    }
}
