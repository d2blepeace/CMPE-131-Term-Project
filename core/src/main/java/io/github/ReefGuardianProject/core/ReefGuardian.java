package io.github.ReefGuardianProject.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ReefGuardianProject.objects.BrickBlock;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.player.Honu;
import com.badlogic.gdx.math.Rectangle;
import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReefGuardian implements ApplicationListener {
    private OrthographicCamera camera;
    private Sprite sprite;
    private SpriteBatch batch;
    private Texture texture;
    private Honu honu;
    private ArrayList<GameObjects> gameObjectsList = new ArrayList<GameObjects>();
    private int level = 1;
    /**
     * State of the game: 1. Main menu; 2. Main Game; 3. Next Level; 4. Game Over
     */
    private int gameState = 2;
    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        //Create Honu
        honu = new Honu();
        honu.setPosition(0, 200);

        //Add brick blocks
        gameObjectsList.add(new BrickBlock(0,0));
        gameObjectsList.add(new BrickBlock(64,0));
        gameObjectsList.add(new BrickBlock(128,0));
        gameObjectsList.add(new BrickBlock(400, 300));
        gameObjectsList.add(new BrickBlock(600,300));
        gameObjectsList.add(new BrickBlock(900,300));

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        switch (this.gameState) {
            case 1:
                this.mainMenu();
                break;
            case 2:
                this.mainGame();
                break;
            case 3:
                this.nextLevel();
                break;
            case 4:
                this.gameOver();
                break;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void updateCamera() {


        camera.position.x = honu.getHitBox().x;
        camera.update();;
    }
    //Load the level
    public void loadLevel(String level) {
        //Clear the list before loading the level
        gameObjectsList.clear();
        //Loading level .txt files
        FileHandle file = Gdx.files.internal("map"); //"map\\level1.txt"
        StringTokenizer tokens = new StringTokenizer(file.readString());
        while (tokens.hasMoreTokens()) {
            String type = tokens.nextToken();

            //Render the map from txt files:
            if (type.equals("BrickBlock")) {
                gameObjectsList.add(new BrickBlock(
                    Integer.parseInt(tokens.nextToken()), //x value
                    Integer.parseInt(tokens.nextToken()))); //y value
            } else if (type.equals("Enemy1")) {
                return;
            } else if (type.equals("Coral1")) {
                return;
            }
        }
    }
    public void mainMenu() {

    }
    public void mainGame() {
        //Clear screen before rendering
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        //Begin the rendering
        batch.begin();
        //Update render here
        honu.draw(batch);
        //object
        for (GameObjects o : gameObjectsList) {
            o.draw(batch);
        }
        batch.end();

        //Updates

        //Two Honus
        honu.update(Gdx.graphics.getDeltaTime());

        //Check for collision
        boolean changeLevel = false; //Changing level check
        for (GameObjects o : gameObjectsList) {
            switch (honu.hit(o.getHitBox())) {
                case 1:
                    //Collide top
                    honu.action(1, 0, o.getHitBox().y + o.getHitBox().height);
                    break;
                case 2:
                    //Collide right
                    honu.action(2, o.getHitBox().x + o.getHitBox().width + 1, 0);
                    break;
                case 3:
                    //Collide left
                    honu.action(3, o.getHitBox().x - honu.getHitBox().width - 1, 0);
                    break;
                case 4:
                    //Collide Bottom
                    honu.action(4, 0, o.getHitBox().y - honu.getHitBox().height);
                    break;
                case 5:
                    //Hit the checkpoint, proceed to next level
                    level++;
                    break;
            }
        }
        updateCamera();

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
        //Call the load level
        //loadLevel("level1");
    }
    public void nextLevel() {

    }
    public void gameOver() {

    }
    @Override
    public void dispose() {

    }
}
