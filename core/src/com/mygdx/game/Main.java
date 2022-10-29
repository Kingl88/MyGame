package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.MenuScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
