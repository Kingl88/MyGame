package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private MyAnim anim;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("flame.jpg");
		anim = new MyAnim("flame.jpg", 2, 8, 15, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		anim.setTime(Gdx.graphics.getDeltaTime());

		float x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - (Gdx.input.getY() + anim.draw().getRegionHeight()/2);
		batch.begin();
		batch.draw(anim.draw(), x, y);
//		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		anim.dispose();
	}
}
