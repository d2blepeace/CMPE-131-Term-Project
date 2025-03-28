package io.github.ReefGuardianProject.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.player.Honu;

public class ReefGuardian implements ApplicationListener {
    private OrthographicCamera camera;
    private Sprite sprite;
    private SpriteBatch batch;
    private Texture texture;
    private Honu honu, honu2;
    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        honu = new Honu();
        honu.setPosition(300, 300);

        honu2 = new Honu();
        honu2.setPosition(500, 300);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        //Clear screen before rendering
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        //Begin the rendering
        batch.begin();
            //Update render here
            honu.draw(batch);
            honu2.draw(batch);
        batch.end();
        //Updates
        honu.update(Gdx.graphics.getDeltaTime());
        honu2.update(Gdx.graphics.getDeltaTime());
        // Check collision between Honu objects

        //Handling Input Controls
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            honu.moveUp(Gdx.graphics.getDeltaTime());    //move Up = W
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            honu.moveLeft(Gdx.graphics.getDeltaTime());    //move left = A
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            honu.moveDown(Gdx.graphics.getDeltaTime());    //move down = S
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            honu.moveRight(Gdx.graphics.getDeltaTime());    //move right = D
        }
    }

    @Override
    public void pause() {

    }



    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
