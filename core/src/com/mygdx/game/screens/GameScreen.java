package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.*;
import com.mygdx.game.persons.Bullet;
import com.mygdx.game.persons.Man;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private final Game game;
    public static boolean destroy;
    private SpriteBatch batch;
    private Music music;
    private Sound sound;
    private MyInputProcessor inputProcessor;
    private OrthographicCamera camera;
    private PhysX physX;
    private Body body;
    private int countCoinsForWin = 0;
    private int countCoins = 0;
    private final MyAnim coinAnm;
    private TiledMap map;
    private final MyFont font;

    private OrthogonalTiledMapRenderer mapRenderer;

    private int[] layers;
    private final Man man;
    public static int bulletsCount;
    public static List<Body> bodyToDelete = new ArrayList<>();;
    public static List<Bullet> bulletToDelete= new ArrayList<>();;

    public GameScreen(Game game) {
        bulletsCount = 100;
        coinAnm = new MyAnim("Full Coins.png", 1, 8, 12, Animation.PlayMode.LOOP);
        this.game = game;
        font = new MyFont(30);
        map = new TmxMapLoader().load("map/MarioWorld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        layers = new int[2];
        layers[0] = map.getLayers().getIndex("front");
        layers[1] = map.getLayers().getIndex("back");

        physX = new PhysX();
        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            body = physX.addObject(objects.get(i));
        }

        body = physX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
        man = new Man(body);

        inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        music = Gdx.audio.newMusic(Gdx.files.internal("81cebf7e45fdef7.mp3"));//Поиск файла внутри проекта, т.е. в папке "assets"
        music.setPan(0, 0.05f);//калибровка звука от -1 до 0 левый канал, от 0 до 1 правый канал.
        music.setLooping(true);//если трек заканчивается повторить сначала
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("reshitelnyiy-chetkiy-shag.mp3"));
        countCoinsForWin = physX.getBodies("Coins").size;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        camera.position.x = body.getPosition().x * physX.PPM;
        camera.position.y = body.getPosition().y * physX.PPM;
        camera.zoom = 1;
        camera.update();

        if (MyContactListener.isDamage) {
            man.getHit(1);
        }

        mapRenderer.setView(camera);
        mapRenderer.render();

        man.setTime(delta);
        body.applyForceToCenter(inputProcessor.getVector(), true);
        Body tmpBody = man.setFPS(body.getLinearVelocity());
        if(tmpBody != null && bulletsCount >= 0){
            bulletsCount--;
            bulletToDelete.add(new Bullet(physX, tmpBody.getPosition().x, tmpBody.getPosition().y, man.getDir()));
        }
        ArrayList<Bullet> bulletTemp = new ArrayList<>();
        for(Bullet b: bulletToDelete){
            Body tBody = b.update(delta);
            if (tBody != null) {
                bodyToDelete.add(tBody);
                bulletTemp.add(b);
            }
        }
        bulletToDelete.removeAll(bulletTemp);
        Rectangle tmp = man.getRect(camera, man.getFrame());
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(tmp.width / 2, tmp.height / 2);
        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(tmp.width / 3, tmp.height / 10, new Vector2(0, -tmp.height / 2), 0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(man.getFrame(), tmp.x, tmp.y, tmp.width * PhysX.PPM, tmp.height * PhysX.PPM);

        Array<Body> bodies = physX.getBodies("Coins");
        coinAnm.setTime(delta);
        TextureRegion tr = coinAnm.draw();
        float dScale = 1.0f;
        for (Body body : bodies) {
            float cx = body.getPosition().x * PhysX.PPM - tr.getRegionWidth() / 2f / dScale;
            float cy = body.getPosition().y * PhysX.PPM - tr.getRegionHeight() / 2f / dScale;
            float cW = tr.getRegionWidth() / PhysX.PPM / dScale;
            float cH = tr.getRegionHeight() / PhysX.PPM / dScale;
            body.setFixedRotation(true);
            ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(cW / 2, cH / 2);
            ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(cW / 1.3f, cH / 1.3f);
            batch.draw(coinAnm.draw(), cx, cy, cW * physX.PPM, cH * physX.PPM);
        }
        font.draw(batch, "HP: " + (int) man.getHit(0) + " Coins: " + countCoins, (int) tmp.x, (int) (tmp.y + 2 * tmp.height * PhysX.PPM));
        batch.end();
        for (Body body : bodyToDelete) {
            if(body.getUserData() != null && body.getUserData().equals("Coins")){
                countCoins++;
            }
            physX.removeBody(body);
        }
        bodyToDelete.clear();
        physX.step();
        physX.debugDraw(camera);
        if (countCoins == countCoinsForWin) {
            dispose();
            game.setScreen(new WinScreen(game));
        }
        if (man.getHit(0) < 0) {
            dispose();
            game.setScreen(new GameOverScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
        batch.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
        physX.dispose();
        man.dispose();
        coinAnm.dispose();


    }
}
