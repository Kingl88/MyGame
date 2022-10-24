package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private MyAtlasAnim run, stand, tmpA;
	private Music music;
	private Sound sound;
	private MyInputProcessor inputProcessor;
	float x;
	float y;

	int dir = 0, step = 1;

	float count;
	
	@Override
	public void create () {
		inputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
		music = Gdx.audio.newMusic(Gdx.files.internal("81cebf7e45fdef7.mp3"));//Поиск файла внутри проекта, т.е. в папке "assets"
		music.setPan(0, 0.05f);//калибровка звука от -1 до 0 левый канал, от 0 до 1 правый канал.
		music.setLooping(true);//если трек заканчивается повторить сначала
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("reshitelnyiy-chetkiy-shag.mp3"));

		batch = new SpriteBatch();
		run = new MyAtlasAnim("atlas/standAndRun.atlas", "run", 7, Animation.PlayMode.LOOP);
		stand = new MyAtlasAnim("atlas/standAndRun.atlas", "stand", 7, Animation.PlayMode.LOOP);
		//tmpA = stand;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		tmpA = stand;
		dir = 0;

		count += Gdx.graphics.getDeltaTime();

		if(inputProcessor.getOutString().contains("A")) {
			if(count > 0.6){
				sound.play();
				count = 0;
			}
			dir = -1;
			x--;
			tmpA = run;
		}
		if(inputProcessor.getOutString().contains("D")) {
			if(count > 0.6){
				sound.play();
				count = 0;
			}
			dir = 1;
			x++;
			tmpA = run;
		}
		if(inputProcessor.getOutString().contains("W")) y++;
		if(inputProcessor.getOutString().contains("S")) y--;
		if(inputProcessor.getOutString().contains("Space")){
			x = Gdx.graphics.getWidth()/2;
			y = Gdx.graphics.getHeight()/2;
		}
		if(dir == -1) x -= step;
		if(dir == 1) x += step;
		tmpA.setTime(Gdx.graphics.getDeltaTime());
		if(!tmpA.draw().isFlipX() & dir == -1) tmpA.draw().flip(true, false);
		if(tmpA.draw().isFlipX() & dir == 1) tmpA.draw().flip(true, false);

		batch.begin();
		batch.draw(tmpA.draw(), x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		run.dispose();
		stand.dispose();
		music.dispose();
		sound.dispose();
	}
}
