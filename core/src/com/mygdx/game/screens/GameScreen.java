package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyAtlasAnim;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.PhysX;
import com.mygdx.game.enums.Actions;

import java.awt.*;
import java.util.HashMap;

public class GameScreen implements Screen {
    Game game;
    Actions actions;
    private SpriteBatch batch;
    private HashMap<Actions, MyAtlasAnim> manAssets;
    private Music music;
    private Sound sound;
    private MyInputProcessor inputProcessor;
    private OrthographicCamera camera;
    private float x;
    private float y;

    private int dir = 0, step = 1;

    private Rectangle rectangle, window;
    private PhysX physX;
    private Body body;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(Game game) {
        this.game = game;
        map = new TmxMapLoader().load("map/MarioWorld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        physX = new PhysX();

        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }

        body = physX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);

        inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        music = Gdx.audio.newMusic(Gdx.files.internal("81cebf7e45fdef7.mp3"));//Поиск файла внутри проекта, т.е. в папке "assets"
        music.setPan(0, 0.05f);//калибровка звука от -1 до 0 левый канал, от 0 до 1 правый канал.
        music.setLooping(true);//если трек заканчивается повторить сначала
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("reshitelnyiy-chetkiy-shag.mp3"));

        batch = new SpriteBatch();
        manAssets = new HashMap<>();
        manAssets.put(Actions.STAND, new MyAtlasAnim("atlas/MyPerson.atlas", "stand", 7, Animation.PlayMode.LOOP));
        manAssets.put(Actions.RUN,  new MyAtlasAnim("atlas/MyPerson.atlas", "run", 7, Animation.PlayMode.LOOP));
        manAssets.put(Actions.JUMP, new MyAtlasAnim("atlas/MyPerson.atlas", "jump", 7, Animation.PlayMode.LOOP));
        actions = Actions.STAND;
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

        mapRenderer.setView(camera);
        mapRenderer.render();
        body.applyForceToCenter(inputProcessor.getVector(), true);

        if (body.getLinearVelocity().len() < 0.6f) actions = Actions.STAND;
        else if (Math.abs(body.getLinearVelocity().x) > 0.6f) {
            actions = Actions.RUN;
        }
        manAssets.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssets.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {
            manAssets.get(actions).draw().flip(true, false);
        }
        if (manAssets.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {
            manAssets.get(actions).draw().flip(true, false);
        }
        float x = body.getPosition().x * physX.PPM - (float) manAssets.get(actions).draw().getRegionWidth()/2;
        float y = body.getPosition().y * physX.PPM - (float) manAssets.get(actions).draw().getRegionHeight()/2;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(manAssets.get(actions).draw(), x, y);
        batch.end();
        physX.step();
        physX.debugDraw(camera);

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
    }
}
