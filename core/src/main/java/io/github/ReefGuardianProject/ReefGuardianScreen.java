package io.github.ReefGuardianProject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ReefGuardianScreen implements Screen {
    ReefGuardian reefGuardian;
    OrthographicCamera camera;
    SpriteBatch batch;
    //Constructor
    public ReefGuardianScreen(ReefGuardian reefGuardian) {
        this.reefGuardian = reefGuardian;

        //Set up virtual camera size: 1920 * 1080
        //If the game screen's size is adjusted, camera render quality remain
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 1920, 1080);

        batch = new SpriteBatch();
    }

    /**
     * It is called, when set this RGScreen as the Games Screen with setScreen(reefGuardianScreen)
     */
    @Override
    public void show() {
        if(!Assets.menuMusic.isPlaying()) {
            //Start music when menu load
            Assets.menuMusic.play();
        }
    }

    /**
     * Called every gameloop (if enabled), used to update the objects in game and then draw them to the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Get texture dimensions
        float spriteWidth = Assets.spriteBack.getWidth();
        float spriteHeight = Assets.spriteBack.getHeight();

        // Calculate center position
        float x = (1920 - spriteWidth) / 2;
        float y = (1080 - spriteHeight) / 2;

        //Clear screen to white
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Resize every sprite to screen size
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.spriteBack, x,y);
        batch.end();


        camera.update();
    }

    /**
     *  Pause game when you minimize window or when the game lost the focus.
     */
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     *  It is called whenever we transition from one screen to another,
     *  for example from your main menu screen to the options screen.
     */
    @Override
    public void hide() {
        //Stop music when leaving menu screen
        Assets.menuMusic.stop();
    }

    /**
     * Called when ReefGuardian game is closed
     */
    @Override
    public void dispose() {

    }
    @Override
    public void resize(int width, int height) {

    }
}
