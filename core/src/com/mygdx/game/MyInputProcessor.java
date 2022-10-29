package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class MyInputProcessor implements InputProcessor {
    private Vector2 outForce;

    public Vector2 getVector() {
        return outForce;
    }

    public MyInputProcessor() {
        this.outForce = new Vector2();
    }

    @Override
    public boolean keyDown(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();
        switch (inKey) {
            case "LEFT", "A" -> outForce.add(-0.5f, 0);
            case "RIGHT", "D" -> outForce.add(0.5f, 0);
            case "DOWN", "S" -> outForce.add(0, -0.1f);
            case "SPACE" -> outForce.add(0, 2.5f);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();
        switch (inKey) {
            case "LEFT", "A" -> outForce.add(0, outForce.y);
            case "RIGHT", "D" -> outForce.add(0, outForce.y);
            case "DOWN", "S" -> outForce.add(outForce.x, 0);
            case "SPACE" -> outForce.add(outForce.x, 0);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
