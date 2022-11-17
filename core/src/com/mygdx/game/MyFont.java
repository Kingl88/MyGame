package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MyFont {
    private BitmapFont font;

    public MyFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Airstrikeplat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.BLACK;
        parameter.characters = "1234567890+-QWERTYUIOP{}|ASDFGHJKL:ZXCVBNM<>?qqwertyuiop[]\\asfghjkl;'zxvbnm,.\'";
        font = generator.generateFont(parameter);
    }
    public void draw(SpriteBatch batch, String text, int x, int y){font.draw(batch, text, x, y + font.getLineHeight());}
    public void dispose(){
        font.dispose();
    }
}
