package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private MyAtlasAnim run, stand, tmpA, jump;
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

    private float count;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {
        map = new TmxMapLoader().load("map/MarioWorld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        physX = new PhysX();

        BodyDef def = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        def.type = BodyDef.BodyType.StaticBody;
        fixtureDef.shape = shape;
        fixtureDef.density = 1; //плотность
        fixtureDef.friction = 0; // шершавость(трение)
        fixtureDef.restitution = 1; //отталкивание

        MapLayer env = map.getLayers().get("env");
        Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rect.size; i++) {
            float x = rect.get(i).getRectangle().x + rect.get(i).getRectangle().width / 2;
            float y = rect.get(i).getRectangle().y + rect.get(i).getRectangle().height / 2;
            float w = rect.get(i).getRectangle().width / 2;
            float h = rect.get(i).getRectangle().height / 2;
            def.position.set(x, y);
            shape.setAsBox(w, h);
            physX.world.createBody(def).createFixture(fixtureDef).setUserData("Cube");
        }


        def.gravityScale = 4;
        def.type = BodyDef.BodyType.DynamicBody;
        env = map.getLayers().get("dyn");
        rect = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rect.size; i++) {
            float x = rect.get(i).getRectangle().x + rect.get(i).getRectangle().width / 2;
            float y = rect.get(i).getRectangle().y + rect.get(i).getRectangle().height / 2;
            float w = rect.get(i).getRectangle().width / 2;
            float h = rect.get(i).getRectangle().height / 2;
            def.position.set(x, y);
            shape.setAsBox(w, h);

            fixtureDef.density = 1; //плотность
            fixtureDef.friction = 0; // шершавость(трение)
            fixtureDef.restitution = 1; //отталкивание
            physX.world.createBody(def).createFixture(fixtureDef).setUserData("Cube");
        }

        env = map.getLayers().get("hero");
        RectangleMapObject hero = (RectangleMapObject)env.getObjects().get("Hero");
        float x = hero.getRectangle().x + hero.getRectangle().width / 2;
        float y = hero.getRectangle().y + hero.getRectangle().height / 2;
        float w = hero.getRectangle().width / 2;
        float h = hero.getRectangle().height / 2;
        def.position.set(x, y);
        shape.setAsBox(w, h);
        fixtureDef.shape = shape;
        fixtureDef.density = 1; //плотность
        fixtureDef.friction = 0; // шершавость(трение)
        fixtureDef.restitution = 1; //отталкивание

        body = physX.world.createBody(def);
        body.createFixture(fixtureDef).setUserData("Cube");


        shape.dispose();

        inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        music = Gdx.audio.newMusic(Gdx.files.internal("81cebf7e45fdef7.mp3"));//Поиск файла внутри проекта, т.е. в папке "assets"
        music.setPan(0, 0.05f);//калибровка звука от -1 до 0 левый канал, от 0 до 1 правый канал.
        music.setLooping(true);//если трек заканчивается повторить сначала
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("reshitelnyiy-chetkiy-shag.mp3"));

        batch = new SpriteBatch();
        run = new MyAtlasAnim("atlas/MyPerson.atlas", "run", 7, Animation.PlayMode.LOOP);
        jump = new MyAtlasAnim("atlas/MyPerson.atlas", "jump", 7, Animation.PlayMode.LOOP);
        stand = new MyAtlasAnim("atlas/MyPerson.atlas", "stand", 7, Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.zoom = 1;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();
        tmpA = stand;
        dir = 0;

        count += Gdx.graphics.getDeltaTime();

        if (inputProcessor.getOutString().contains("A")) {
            if (count > 0.6) {
                sound.play();
                count = 0;
            }
            dir = -1;
            x--;
            tmpA = run;
            body.applyForceToCenter(new Vector2(-100000f, 0), true);
        }
        if (inputProcessor.getOutString().contains("D")) {
            if (count > 0.6) {
                sound.play();
                count = 0;
            }
            dir = 1;
            x++;
            tmpA = run;
            body.applyForceToCenter(new Vector2(100000f, 0), true);
        }
        if (inputProcessor.getOutString().contains("W")) y++;
        if (inputProcessor.getOutString().contains("S")) y--;
        if (inputProcessor.getOutString().contains("Space")) {
            body.applyForceToCenter(new Vector2(0, 100000f), true);
        }
        if (dir == -1) x -= step;
        if (dir == 1) x += step;
        tmpA.setTime(Gdx.graphics.getDeltaTime());
        if (!tmpA.draw().isFlipX() & dir == -1) tmpA.draw().flip(true, false);
        if (tmpA.draw().isFlipX() & dir == 1) tmpA.draw().flip(true, false);
        batch.setProjectionMatrix(camera.combined);
        float x = body.getPosition().x - 2.5f;
        float y = body.getPosition().y - 2.5f;
        batch.begin();
        batch.draw(tmpA.draw(), x, y);
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
    public void dispose() {
        batch.dispose();
        run.dispose();
        stand.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
