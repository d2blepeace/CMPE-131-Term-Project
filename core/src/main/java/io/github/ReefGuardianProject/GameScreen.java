package io.github.ReefGuardianProject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import static io.github.ReefGuardianProject.helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera orthographicCamera;
    private SpriteBatch batch;
    private Box2DDebugRenderer box2dRender;
    private World world;
    public GameScreen(OrthographicCamera orthographicCamera) {
        this.orthographicCamera = orthographicCamera;
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

        batch.begin();  //Start rendering objects
            //Render objects in here
        batch.end();    //End rendering objects
        box2dRender.render(world, orthographicCamera.combined.scl(PPM)); //refer to Constant
    }

    private void update() {
        //FPS = 60
        world.step(1 / 60f, 6, 2);

        batch.setProjectionMatrix(orthographicCamera.combined);

        //If pressed ESC, exit the app
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    //Update new position for the camera
    private void cameraUpdate() {
        orthographicCamera.position.set(new Vector3(0,0,0));
    }

}
