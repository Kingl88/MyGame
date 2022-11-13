package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class MenuScreen implements Screen {
    Game game;
    Texture fon, sign;
    SpriteBatch spriteBatch;
    int x, y;
    Rectangle rectangle;
    public MenuScreen(Game game) {
        this.game = game;
        fon = new Texture("background.png");
        sign = new Texture("play.png");
        x = Gdx.graphics.getWidth()/2 - sign.getWidth()/2;
        y = Gdx.graphics.getHeight()/2;
        rectangle = new Rectangle(x, y, sign.getWidth(), sign.getHeight());
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.draw(sign, x, y);
        spriteBatch.end();
        if(Gdx.input.isTouched()){
            if(rectangle.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.fon.dispose();
        this.sign.dispose();
        this.spriteBatch.dispose();

    }
}
