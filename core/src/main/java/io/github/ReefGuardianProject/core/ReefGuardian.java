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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.ReefGuardianProject.objects.*;
import io.github.ReefGuardianProject.objects.enemy.WaterBottle;
import io.github.ReefGuardianProject.objects.finalBoss.boss.FinalBoss;
import io.github.ReefGuardianProject.objects.player.Honu;
import io.github.ReefGuardianProject.objects.projectile.Projectile;
import com.badlogic.gdx.audio.Sound;
import io.github.ReefGuardianProject.objects.ui.HonuHealthBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ReefGuardian implements ApplicationListener {
    private OrthographicCamera camera;
    //Menu sprite
    private Sprite menuGameOver;
    private Sprite buttonRetry;
    private Sprite buttonQuit;
    private Sprite menuCongrats;
    private Sprite buttonNextLevel;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture texture;
    private Honu honu;
    private HonuHealthBar honuHealthBar;
    private Sound collectLifeSound, honuDmgSound;
    private ArrayList<GameObjects> gameObjectsList = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int level = 2;
    private Texture backgroundLevel;
    /**
     * State of the game: 1. Main menu; 2. Main Game; 3. Next Level; 4. Game Over
     */
    private int gameState = 2;
    @Override
    public void create() {
        camera = new OrthographicCamera();

        viewport = new FitViewport(1280, 1024, camera);
        viewport.apply();

        //camera.position.set(640, 412, 0);
        camera.setToOrtho(false, 1280, 1024);

        batch = new SpriteBatch();

        //Honu healthbar
        honuHealthBar = new HonuHealthBar();

        //Load sfx of collecting live
        collectLifeSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\CollectLive_SFX.mp3"));

        //Load Honu damge sound
        honuDmgSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\Pixel_Honu_Dmg_SFX.mp3"));

        //Call the load level
        if (level == 1) {
            honu = new Honu();
            honu.setPosition(0, 128);
            loadLevel("map\\level1.txt");
        }
        if (level == 2) {
            honu = new Honu();
            honu.setPosition(0, 128);
            loadLevel("map\\level2.txt");
        }
    }

    @Override
    public void resize(int width, int height) {
        // Makes sure screen of the game doesn't stretch when the window changes
        viewport.update(width, height);
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
        float camHalfWidth = camera.viewportWidth / 2f;
        float honuX = honu.getHitBox().x;

        float deadZoneLeft = camera.position.x - 150;
        float deadZoneRight = camera.position.x + 150;

        // Move camera right if Honu goes past right dead zone
        if (honuX > deadZoneRight) {
            camera.position.x = honuX - 150;
        }
        // Move camera left if Honu goes past left dead zone
        else if (honuX < deadZoneLeft) {
            camera.position.x = honuX + 150;
        }
        // prevent camera from going before 0
        if (camera.position.x < camHalfWidth) {
            camera.position.x = camHalfWidth;
        }
        camera.update();
    }
    //Load the level
    public void loadLevel(String level) {
        //Clear the list before loading the level
        gameObjectsList.clear();
        //Loading level .txt files
        FileHandle file = Gdx.files.internal(level);
        String[] lines = file.readString().split("\\r?\\n"); // Split by new lines

        for (String line : lines) {
            line = line.trim();

            // Skip empty or comment lines
            if (line.isEmpty() || line.startsWith("#")) continue;

            StringTokenizer tokens = new StringTokenizer(line);
            String type = tokens.nextToken();

            switch (type) {
                //Background
                case "Level1_Background":
                    String bgPath1 = tokens.nextToken();
                    if (backgroundLevel != null) backgroundLevel.dispose();
                    backgroundLevel = new Texture(Gdx.files.internal(bgPath1));
                    break;
                case "Level2_Background":
                    String bgPath2;
                    bgPath2 = tokens.nextToken();
                    if (backgroundLevel != null) backgroundLevel.dispose();
                    backgroundLevel = new Texture(Gdx.files.internal(bgPath2));
                    break;
                case "RockBlock":
                    gameObjectsList.add(new RockBlock(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                    //Collectible
                case "Checkpoint":
                    gameObjectsList.add(new Checkpoint(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "Live":
                    gameObjectsList.add(new LiveCollectible(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                    //Enemy
                case "WaterBottle":
                    gameObjectsList.add(new WaterBottle(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "FinalBoss":
                    gameObjectsList.add(new FinalBoss(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
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
            // 1. Background FIRST (fills the screen)
            if (backgroundLevel != null) {
                //Scroll the background based on the camera position
                float bgX = camera.position.x - camera.viewportWidth / 2f;
                // match the image size
                batch.draw(backgroundLevel, bgX, 0, 1280, 1024);
            }
            // 2. Game objects (Honu + obstacles)
             honu.draw(batch);
            for (GameObjects o : gameObjectsList) {
                o.draw(batch);
            }

            // 3. Honu Healthbar UI
            honuHealthBar.update(honu.getLives());
            honuHealthBar.draw(batch, camera);

            // 4. Honu shoot waterball
            for (Projectile p : projectiles) {
                p.draw(batch);
            }
            // 5. FinalBoss
            for (GameObjects o : gameObjectsList) {
                o.update(Gdx.graphics.getDeltaTime());
            }
        batch.end();

        //Updates

        //Honu
        honu.update(Gdx.graphics.getDeltaTime());

        // Waterball of Honu
        for (Projectile p : projectiles) {
            p.update(Gdx.graphics.getDeltaTime());
        }
        // Detect WaterBall hitting enemy objects
        Iterator<Projectile> projectileIter = projectiles.iterator();
        while (projectileIter.hasNext()) {
            Projectile p = projectileIter.next();
            Iterator<GameObjects> objIter = gameObjectsList.iterator();

            while (objIter.hasNext()) {
                GameObjects obj = objIter.next();
                if (obj instanceof WaterBottle && p.getHitBox().overlaps(obj.getHitBox())) {
                    // Remove projectile and enemy on collision
                    projectileIter.remove();
                    objIter.remove();
                    break; // Only one object can be hit at a time by one projectile
                }
            }
        }
        //If waterball doesn't hit anything, delete it
        projectiles.removeIf(p -> !p.isActive());

        //Check for collision
        boolean changeLevel = false; //Changing level check

        //Create iterator for gameObjectList
        Iterator<GameObjects> iterator = gameObjectsList.iterator();
        while (iterator.hasNext()) {
            GameObjects o = iterator.next();;
            int honuCollision = honu.hit(o.getHitBox());
            int collisionType = o.hitAction(); // 1 = normal block, 2 = receive dmg, 3 = collectible, 4 = checkpoint

            // Handle object type behavior
            switch (collisionType) {
                case 1:
                    switch (honuCollision) {
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
                            honu.action(4, honu.getHitBox().x, o.getHitBox().y - honu.getHitBox().height);
                            break;
                    }
                    break;
                case 2: // Character receives damage
                    if (honuCollision != -1) {
                        honu.loseLife();
                        //Configurations
                            //Play sound effect
                            if (honuDmgSound != null) honuDmgSound.play();
                            // Strong knockback based on direction
                            float knockBackDist = 40f;

                        // Knock Honu back depending on collision side
                        switch (honuCollision) {
                            case 1: honu.knockBack(0, knockBackDist); break;  // Hit top → push down
                            case 2: honu.knockBack(knockBackDist, 0); break;  // Hit right → push left
                            case 3: honu.knockBack(-knockBackDist, 0); break; // Hit left → push right
                            case 4: honu.knockBack(0, -knockBackDist); break; // Hit bottom → push up
                        }
                    }
                    break;
                case 3: // Collect item
                    if (honuCollision != -1) {
                        // Remove the collectible if touched
                        iterator.remove();   // Delete collectible after collecting
                        honu.gainLife();     // restore one life
                        // Play sfx
                        if (collectLifeSound != null) collectLifeSound.play();
                    }
                    break;
                case 4: // Checkpoint
                    if (honuCollision != -1) {
                        changeLevel = true;
                    }
                    break;
            }
        }

        //Change level test
        if (changeLevel) {
            level++;
            if (level == 2) {
                honu.setPosition(0, 128);
                loadLevel("map\\level2.txt");
            }
            if (level == 3) {
                honu.setPosition(0, 128);
                loadLevel("map\\levelBoss.txt");
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            projectiles.add(honu.shoot());                  //shoot = SPACE
        }

    }
    public void nextLevel() {

    }
    public void gameOver() {
        Gdx.gl.glClearColor(1, 1, 1, 0.7f); //White with some opacity
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Center of the screen
        float centerX = camera.position.x;
        float centerY = camera.position.y / 2;
        menuGameOver.setPosition(centerX - menuGameOver.getWidth() / 2, centerY + 100);
        menuGameOver.draw(batch);

        //GAME OVER TITLE
        //RETRY? button
        //QUIT button


        batch.end();
        //Input handling
        if (Gdx.input.justTouched()) {
            // Convert screen coordinates to world coordinates
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Converts to world coordinates
            camera.unproject(touch);

            // Retry clicked
            if (buttonRetry.getBoundingRectangle().contains(touch.x, touch.y)) {
                resetGame(); // Reset game logic
            }
            // Quit clicked
            else if (buttonQuit.getBoundingRectangle().contains(touch.x, touch.y)) {
                gameState = 1; // Back to main menu (to be implemented)
            }
        }

    }

    private void resetGame() {
        //TODO: check for game progress
        level = 1;
        honu = new Honu();
        honu.setPosition(0, 128);
        loadLevel("map/level1.txt");
        gameState = 2;
    }

    @Override
    public void dispose() {
        //Dispose life collecting sond
        if (collectLifeSound != null) collectLifeSound.dispose();
    }
}
