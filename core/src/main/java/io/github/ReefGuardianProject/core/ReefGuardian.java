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
import io.github.ReefGuardianProject.objects.enemy.*;
import io.github.ReefGuardianProject.objects.environment.KelpBlock;
import io.github.ReefGuardianProject.objects.environment.RockBlock;
import io.github.ReefGuardianProject.objects.finalBoss.BossBarrier;
import io.github.ReefGuardianProject.objects.finalBoss.FinalBoss;
import io.github.ReefGuardianProject.objects.player.Honu;
import io.github.ReefGuardianProject.objects.projectile.EnemyProjectile;
import io.github.ReefGuardianProject.objects.projectile.Projectile;
import com.badlogic.gdx.audio.Sound;
import io.github.ReefGuardianProject.objects.ui.HonuHealthBar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ReefGuardian implements ApplicationListener {
    private static ReefGuardian instance;
    List<GameObjects> objectsToAdd = new ArrayList<>();
    private OrthographicCamera camera;
    //Game Over sprite
    private Sprite gameOver, gameOverOverlay;
    private Sprite buttonRetry, buttonQuit;
    private Sprite buttonRetryHighlight, buttonQuitHighlight;

    private Sprite menuCongrats;
    private Sprite buttonNextLevel;

    private Viewport viewport;
    private SpriteBatch batch;
    //private Texture texture;
    private Honu honu;
    private HonuHealthBar honuHealthBar;
    private Sound collectLifeSound, honuDmgSound;
    private float lastCheckpointX = 0, lastCheckpointY = 128; //handle Checkpoint
    private ArrayList<GameObjects> gameObjectsList = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int level = 1;
    private Texture backgroundLevel;
    /**
     * State of the game: 1. Main menu; 2. Main Game; 3. Next Level; 4. Game Over
     */
    private int gameState = 2;
    @Override
    public void create() {
        instance = this;
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

        //Game Over Screen
        gameOver = new Sprite(new Texture("gameUI/GameOver.png"));
        gameOverOverlay = new Sprite(new Texture("gameUI/1280x1024_grey_background.jpg"));
        gameOverOverlay.setAlpha(0.045f);

        buttonRetry = new Sprite(new Texture("gameUI/Retry.png"));
        buttonQuit = new Sprite(new Texture("gameUI/Quit.png"));
        buttonRetryHighlight = new Sprite(new Texture("gameUI/Retry_2.png"));
        buttonQuitHighlight = new Sprite(new Texture("gameUI/Quit_2.png"));

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
                // Environment Block
                case "KelpBlock" :
                    gameObjectsList.add(new KelpBlock(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "RockBlock":
                    gameObjectsList.add(new RockBlock(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "NextLevelDoor":
                    gameObjectsList.add(new NextLevelDoor(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "BossBarrier": //Level 3 (boss) only
                    gameObjectsList.add(new BossBarrier(
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
                case "BeerBottle":
                    gameObjectsList.add(new BeerBottle(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "OilBarrel":
                    gameObjectsList.add(new OilBarrel(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "NuclearWaste":
                    gameObjectsList.add(new NuclearWaste(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "TrashBag":
                    gameObjectsList.add(new TrashBag(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())));
                    break;
                case "FinalBoss":
                    gameObjectsList.add(new FinalBoss(
                        Integer.parseInt(tokens.nextToken()), // x
                        Integer.parseInt(tokens.nextToken()), // y
                        Integer.parseInt(tokens.nextToken())  // startingHealth
                    ));
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
            if (!honu.isDefeated()) {
                honu.draw(batch); // normal rendering if alive
            } else {
                honu.drawDefeatAnimation(batch); // special method to draw animation
            }
            for (GameObjects o : gameObjectsList) {
                o.draw(batch);
            }

            // 3. Honu's Healthbar UI
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

        // Waterball of Honu and Boss Projectiles
        for (Projectile p : projectiles) {
            p.update(Gdx.graphics.getDeltaTime());
        }

        // Detect WaterBall hitting enemy objects and wall (RockBlock)
        List<GameObjects> objectsToRemove = new ArrayList<>();
        Iterator<Projectile> projectileIter = projectiles.iterator();
        while (projectileIter.hasNext()) {
            Projectile p = projectileIter.next();
            for (GameObjects obj : gameObjectsList) {

                if (p.getHitBox().overlaps(obj.getHitBox())) {
                    if (obj instanceof BossBarrier) {
                        // Skip barrier when checking projectiles (allow waterball to pass)
                        continue;
                    }
                    if (obj.isEnemy() || obj.hitAction() == 1) {
                        projectileIter.remove(); // remove projectile

                        if (obj instanceof FinalBoss) {
                            ((FinalBoss) obj).receiveDamage(); // Boss takes damage
                        } else if (obj.isEnemy()) {
                            objectsToRemove.add(obj); // Normal enemy gets marked for deletion
                        }

                        break;
                    }
                }
            }
        }

        // After iteration, remove all marked objects
        gameObjectsList.removeAll(objectsToRemove);
        gameObjectsList.addAll(objectsToAdd);
        objectsToAdd.clear();

        //If waterball doesn't hit anything, delete it
        projectiles.removeIf(p -> !p.isActive());

        //Check for collision
        boolean changeLevel = false; //Changing level check

        //Create iterator for gameObjectList
        Iterator<GameObjects> iterator = gameObjectsList.iterator();
        while (iterator.hasNext()) {
            GameObjects o = iterator.next();;
            if (o.getHitBox() != null) {
                int honuCollision = honu.hit(o.getHitBox());

                //1 = normal block, 2 = receive dmg, 3 = collectible, 4 = Checkpoint, 5 = Next Level, 6 = Collide to EnemyProjectile
                int collisionType = o.hitAction();

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

                            if (honu.getLives() == 0) {
                                if (honu.isDefeatAnimationFinished()) {
                                    gameState = 4; //4. game over screen
                                    return;
                                }
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
                    case 4: // Checkpoint logic
                        Checkpoint checkpoint = (Checkpoint) o;
                        if (honu.getHitBox().overlaps(checkpoint.getHitBox()) && !checkpoint.isActivated()) {
                            if (!checkpoint.isActivated()) {
                                checkpoint.activateCheckpoint();
                                lastCheckpointX = checkpoint.getHitBox().x;
                                lastCheckpointY = checkpoint.getHitBox().y;
                            }
                        }
                        break;
                    case 5: //Next Level when reach nextLevelDoor
                        if (honuCollision != -1) {
                            changeLevel = true;
                        }
                        break;
                    case 6:
                        if (honuCollision != -1) {
                            honu.loseLife();
                            honu.knockBack(-40f, 0f);
                            if (honuDmgSound != null) honuDmgSound.play();
                            if (o instanceof EnemyProjectile) {
                                ((EnemyProjectile) o).delete();
                            }
                            if (honu.getLives() == 0 && honu.isDefeatAnimationFinished()) {
                                gameState = 4;
                                return;
                            }
                        }
                        break;
                    case 10: // Invisible Barrier - block Honu but not waterBall
                        if (honuCollision != -1) {
                            switch (honuCollision) {
                                case 1:
                                    honu.action(1, 0, o.getHitBox().y + o.getHitBox().height);
                                    break;
                                case 2:
                                    honu.action(2, o.getHitBox().x + o.getHitBox().width + 1, 0);
                                    break;
                                case 3:
                                    honu.action(3, o.getHitBox().x - honu.getHitBox().width - 1, 0);
                                    break;
                                case 4:
                                    honu.action(4, honu.getHitBox().x, o.getHitBox().y - honu.getHitBox().height);
                                    break;
                            }
                        }
                }
            }
        }
        // Remove dead enemy projectiles after hitting Honu
        gameObjectsList.removeIf(obj -> (obj instanceof EnemyProjectile) && !((EnemyProjectile)obj).isActive());
        gameObjectsList.removeIf(obj -> obj instanceof TrashBag && (obj).getHitBox() == null);


        // Check if the Boss is defeated
        boolean bossDefeated = false;
        Iterator<GameObjects> bossIter = gameObjectsList.iterator();
        while (bossIter.hasNext()) {
            GameObjects obj = bossIter.next();
            if (obj instanceof FinalBoss) {
                FinalBoss boss = (FinalBoss) obj;
                if (boss.isDefeated()) {
                    bossDefeated = true;
                    bossIter.remove();
                    break;
                }
            }
        }

        // Disable the boss barrier
        if (bossDefeated) {
            for (GameObjects obj : gameObjectsList) {
                if (obj instanceof BossBarrier) {
                    ((BossBarrier) obj).deactivate();
                }
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
                //BOSS level
                honu.setPosition(0, 128);
                loadLevel("map\\level3.txt");
            }
        }

        updateCamera();

        // Check if Honu's defeat animation has finished to show Game Over screen
        if (honu.isDefeated() && honu.isDefeatAnimationFinished()) {
            gameState = 4;
            return;
        }



        // Handle collisions between Enemy Projectiles (SubmarineProjectile) and Honu
        for (GameObjects obj : gameObjectsList) {
            if (obj instanceof EnemyProjectile) {
                EnemyProjectile ep = (EnemyProjectile) obj;

                if (ep.getHitBox().overlaps(honu.getHitBox()) && ep.isActive()) {
                    honu.loseLife();
                    honu.knockBack(-40f, 0f); // knock Honu back to left (enemy is coming from right)

                    if (honuDmgSound != null) honuDmgSound.play();

                    ep.delete(); // Mark the projectile for deletion

                    if (honu.getLives() == 0) {
                        if (honu.isDefeatAnimationFinished()) {
                            gameState = 4; // Game Over
                            return;
                        }
                    }
                }
            }
        }
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
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
            //Center of the screen
            float centerX = camera.position.x;
            float centerY = camera.position.y / 2 + 200;
            float retryX = centerX - 500;
            float quitX = centerX + 100;
            float buttonY = centerY - 50;

            //Game over screen overlay
            gameOverOverlay.setPosition(camera.position.x - camera.viewportWidth / 2f,
                camera.position.y - camera.viewportHeight / 2f);
            gameOverOverlay.draw(batch);

            //GAME OVER TITLE
            gameOver.setPosition(centerX - gameOver.getWidth() / 2, centerY + 100);
            gameOver.draw(batch);

            // Get mouse position
            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse);

            // Draw Retry button
            if (buttonRetry.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                buttonRetryHighlight.setPosition(retryX, buttonY);
                buttonRetry.getBoundingRectangle().setPosition(retryX, buttonY);
                buttonRetryHighlight.draw(batch);
            } else {
                buttonRetry.setPosition(retryX, buttonY);
                buttonRetry.draw(batch);
            }
            // Draw Quit button
            if (buttonQuit.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                buttonQuitHighlight.setPosition(quitX, buttonY);
                buttonQuit.getBoundingRectangle().setPosition(quitX, buttonY);
                buttonQuitHighlight.draw(batch);
            } else {
                buttonQuit.setPosition(quitX, buttonY);
                buttonQuit.draw(batch);
            }
        batch.end();

        //Input handling
        if (Gdx.input.justTouched()) {
            if (buttonRetry.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                resetGame();
            } else if (buttonQuit.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                Gdx.app.exit();
            }
        }

    }

    private void resetGame() {
        //TODO: check for game progress
        honu = new Honu();
        honu.setPosition(lastCheckpointX, lastCheckpointY);
        honu.setLives(2); // Set to 2 lives
        loadLevel("map\\level" + level + ".txt");
        gameState = 2;
    }

    @Override
    public void dispose() {
        //Dispose life collecting sond
        if (collectLifeSound != null) collectLifeSound.dispose();
        if (honuDmgSound != null) honuDmgSound.dispose();
        for (GameObjects obj : gameObjectsList) {
            obj.dispose();
        }
    }
    public static ReefGuardian getInstance() {
        return instance;
    }
    public void scheduleGameObjectAdd(GameObjects obj) {
        objectsToAdd.add(obj);
    }

    public Honu getHonu() {
        return honu;
    }
}
