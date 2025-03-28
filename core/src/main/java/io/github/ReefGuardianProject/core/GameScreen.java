package io.github.ReefGuardianProject.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sprite sprite;
    private Box2DDebugRenderer box2dRender;
    private World world;
    private Texture texture;

    public GameScreen(OrthographicCamera orthographicCamera) {
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();

        //set gravity for x and y axis, I will research further about water world gravity
        this.world = new World(new Vector2(0, 0), false);
        this.box2dRender = new Box2DDebugRenderer();

    }
    @Override
    public void render(float delta) {
        this.update();

        //Clear the screen from previous frame
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


    }

    private void update() {
        //FPS = 60
        world.step(1 / 60f, 6, 2);
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);

        //If pressed ESC, exit the app
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
    //Update new position for the camera
    private void cameraUpdate() {

        camera.position.set(new Vector3(0,0,0));
        camera.update();
    }
    public World getWorld() {
        return world;
    }

}
