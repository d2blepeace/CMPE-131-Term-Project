package io.github.ReefGuardianProject.core;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import io.github.ReefGuardianProject.objects.projectile.WaterBall;
import io.github.ReefGuardianProject.objects.ui.HonuHealthBar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ReefGuardian implements ApplicationListener {
    private static ReefGuardian instance;
    List<GameObjects> objectsToAdd = new ArrayList<>();
    private OrthographicCamera camera;
    //Game Over Assets
    private Sprite gameOver, gameOverOverlay;
    private Sprite buttonRetry, buttonQuit;
    private Sprite buttonRetryHighlight, buttonQuitHighlight;
    private Sound gameOverSfx;
    private boolean gameOverPlayed = false; // only play the sound once
    //Main Menu Assets
    private Sprite menuBackground, menuTitle;
    private Sprite buttonStart, buttonOptions, buttonExit;
    private Sprite buttonStartHover, buttonOptionsHover, buttonExitHover;
    private Music menuMusic;
    //Pause Screen Asset
    private Sprite pauseText;
    private Sprite returnButton, returnButtonHover;
    private Sprite mainMenuButton, mainMenuButtonHover;
    private Sprite pauseStuckButton, pauseStuckButtonHover;
    //How to play Asset
    private Sprite howToWASD, howToSpace, howToESC, howToArrows;
    private float howToTimer = 0f;
    private final float FADE_IN_TIME    = 1.0f;
    private final float DISPLAY_TIME    = 2.0f;
    private final float FADE_OUT_TIME   = 1.0f;
    private final float HOWTO_TOTAL     = FADE_IN_TIME + DISPLAY_TIME + FADE_OUT_TIME;
    // Ending screen asset
    private Sprite endingScreen;
    private Music endingMusic;
    private boolean endingPlayed = false;
    private float endingTimer = 0f;
    //Game background music
    private Music bgMusic;
    private Music bossDefeatMusic;
    private boolean bossDefeatedPlayed = false;
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
    //Next Level Screen
    private int nextLevelToLoad;
    private Music transitionMusic;
    private boolean transitionPlayed = false;
    private boolean transitionStarted = false;

    /**
     * StateS of the game: 1. Main menu; 2. Main Game; 3. Next Level; 4. Game Over
     */
    private int gameState;
    private boolean isBossLevel = false;
    private static final int STATE_MENU = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_NEXT_LEVEL = 3;
    private static final int STATE_GAME_OVER = 4;
    private static final int STATE_PAUSED = 5;
    private static final int STATE_HOWTO = 6;
    private static final int STATE_ENDING = 7;

    // Font of the game
    private BitmapFont fontWhite, fontBlack;


    @Override
    public void create() {
        instance = this;
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 1024, camera);
        viewport.apply();
        camera.setToOrtho(false, 1280, 1024);
        batch = new SpriteBatch();

        //Game font
        fontBlack = new BitmapFont(Gdx.files.internal("font\\fontBlack.fnt"),
            Gdx.files.internal("font\\fontBlack.png"), false);
        fontBlack.getData().setScale(1.5f);
        fontWhite = new BitmapFont(Gdx.files.internal("font\\fontWhite.fnt"),
            Gdx.files.internal("font\\fontWhite.png"), false);
        fontWhite.getData().setScale(1.5f);

        //Honu healthbar
        honuHealthBar = new HonuHealthBar();

        //Load Sounds:
            //Load sfx of collecting live
            collectLifeSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\CollectLive_SFX.mp3"));

            //Load Honu damge sound
            honuDmgSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\Pixel_Honu_Dmg_SFX.mp3"));

        //Game Over Screen
        gameOver = new Sprite(new Texture("gameUI\\GameOver.png"));
        gameOverOverlay = new Sprite(new Texture("gameUI\\1280x1024_grey_background.jpg"));
        gameOverOverlay.setAlpha(0.045f);
        gameOverSfx = Gdx.audio.newSound(Gdx.files.internal("sfx/GameOver_Sfx.mp3"));

        buttonRetry = new Sprite(new Texture("gameUI\\Retry.png"));
        buttonQuit = new Sprite(new Texture("gameUI\\Quit.png"));
        buttonRetryHighlight = new Sprite(new Texture("gameUI\\Retry_2.png"));
        buttonQuitHighlight = new Sprite(new Texture("gameUI\\Quit_2.png"));

        //Main Menu Screen
        menuBackground = new Sprite(new Texture("gameUI\\mainMenu\\1280x1024_MainMenu.png"));
        menuTitle = new Sprite(new Texture("gameUI\\mainMenu\\760x145_Reef_Guardian.png"));
        buttonStart = new Sprite(new Texture("gameUI\\mainMenu\\420x180_Start.png"));
        buttonStartHover = new Sprite(new Texture("gameUI\\mainMenu\\420x180_StartHighlighted.png"));
        buttonOptions = new Sprite(new Texture("gameUI\\mainMenu\\540x145_Options.png"));
        buttonOptionsHover = new Sprite(new Texture("gameUI\\mainMenu\\540x145_OptionsHighlighted.png"));
        buttonExit = new Sprite(new Texture("gameUI\\mainMenu\\420x180_Exit.png"));
        buttonExitHover = new Sprite(new Texture("gameUI\\mainMenu\\420x180_ExitHighlighted.png"));

        //Main Menu Music
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\MainMenuTheme.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();

        //Pause Screen
        pauseText = new Sprite(new Texture("gameUI\\pause\\600x280_PauseText.png"));
        returnButton = new Sprite(new Texture("gameUI\\pause\\540x145_ReturnPause.png"));
        returnButtonHover = new Sprite(new Texture("gameUI\\pause\\540x145_ReturnPauseHighlighted.png"));
        mainMenuButton = new Sprite(new Texture("gameUI\\pause\\700x145_MainMenuPause.png"));
        mainMenuButtonHover = new Sprite(new Texture("gameUI\\pause\\700x145_MainMenuPauseHighlighted.png"));
        pauseStuckButton = new Sprite(new Texture("gameUI\\pause\\700x145_Stuck.png"));
        pauseStuckButtonHover = new Sprite(new Texture("gameUI\\Pause\\700x145_StuckHighlighted.png"));

        //How to play Screen
        howToWASD  = new Sprite(new Texture("gameUI\\howToPlay\\430x265_WASD.png"));
        howToSpace = new Sprite(new Texture("gameUI\\howToPlay\\285x55_Spacebar.png"));
        howToESC   = new Sprite(new Texture("gameUI\\howToPlay\\430x265_ESC.png"));
        howToArrows = new Sprite(new Texture("gameUI\\howToPlay\\430x265_arrow_key.png"));

        //Boss Defeat cue audio
        bossDefeatMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\BossDefeat_Cue.mp3"));
        bossDefeatMusic.setLooping(false);

        //Ending screen
        endingScreen = new Sprite(new Texture("gameUI\\ending\\EndingScreen.png"));
        endingMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\EndingTheme.mp3"));
        endingMusic.setLooping(false);

        //Next Level
        transitionMusic = Gdx.audio.newMusic(
            Gdx.files.internal("music\\LevelComplete_Cue.mp3")
        );
        transitionMusic.setLooping(false);
        //Set game state to menu
        gameState = STATE_MENU;
    }

    @Override
    public void resize(int width, int height) {
        // Makes sure screen of the game doesn't stretch when the window changes
        viewport.update(width, height);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameState == STATE_PLAYING) {
                gameState = STATE_PAUSED;
            } else if (gameState == STATE_PAUSED) {
                gameState = STATE_PLAYING;
            }
        }
        switch (this.gameState) {
            case STATE_MENU: //1
                this.mainMenu();
                break;
            case STATE_PLAYING: //2
                this.mainGame();
                break;
            case STATE_NEXT_LEVEL: //3
                this.nextLevel();
                break;
            case STATE_GAME_OVER: //4
                this.gameOver();
                break;
            case STATE_PAUSED:  //5
                this.pauseScreen();
                break;
            case STATE_HOWTO:   //6
                this.howToScreen();
                break;
            case STATE_ENDING:  //7
                this.showEnding();
                break;
        }
    }


    //When user minimize window or set gameState = STATE_PAUSED, paused the game
    @Override
    public void pause() {
        if (gameState == STATE_PLAYING) {
            gameState = STATE_PAUSED;
        }
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
        // Determine if this is the boss level
        isBossLevel = level.toLowerCase().endsWith("level3.txt");

        // If it is level 1, show how to play
        if (level.equals("map\\level1.txt")) {
            gameState  = STATE_HOWTO;
            howToTimer = 0f;
        }

        // Clear out the old objects
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
                //BGM
                case "LevelMusic":
                    String musicPath = tokens.nextToken();
                    if (bgMusic != null) {
                        bgMusic.stop();
                        bgMusic.dispose();
                    }
                    bgMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
                    bgMusic.setLooping(true); // Make it loop automatically
                    bgMusic.play();
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
                case "BlueClam":
                    gameObjectsList.add(new BlueClam(
                        Integer.parseInt(tokens.nextToken()),
                        Integer.parseInt(tokens.nextToken())
                    ));
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

        // Activation of Boss
        final float ACTIVATION_DISTANCE = 500f;
        for (GameObjects obj : gameObjectsList) {
            if (obj instanceof FinalBoss) {
                FinalBoss boss = (FinalBoss)obj;

                // if not yet activated and Honu is within range…
                if (!boss.isActive() &&
                    Math.abs(honu.getHitBox().x - boss.getHitBox().x) < ACTIVATION_DISTANCE) {

                    // 1) flip the boss on
                    boss.activate();

                    // 2) start your level-3 music
                    if (bgMusic != null && !bgMusic.isPlaying()) {
                        bgMusic.play();
                    }

                    // only need to do this once
                    break;
                }
            }
        }

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
        gameObjectsList.removeIf(o -> o.getHitBox() == null);
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
                                honu.action(1, honu.getHitBox().x, o.getHitBox().y + o.getHitBox().height);
                                break;
                            case 2:
                                //Collide right
                                honu.action(2, o.getHitBox().x + o.getHitBox().width + 1, honu.getHitBox().y);
                                break;
                            case 3:
                                //Collide left
                                honu.action(3, o.getHitBox().x - honu.getHitBox().width - 1, honu.getHitBox().y);
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
                            float knockBackDist = 30f;

                            // Knock Honu back depending on collision side
                            switch (honuCollision) {
                                case 1: honu.knockBack(0, 0); break;  // Hit top
                                case 2: honu.knockBack(knockBackDist, 0); break;  // Hit right → push left
                                case 3: honu.knockBack(-knockBackDist, 0); break; // Hit left → push right
                                case 4: honu.knockBack(0, 0); break; // Hit bottom
                            }
                            //Delete the enemy object when it hit Honu
                            iterator.remove();

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
                            // if it's a BlueClam...
                            if (o instanceof BlueClam) {
                                ((BlueClam)o).playCollectSfx();
                                honu.activateShield();      // new method on Honu
                            } else {
                                honu.gainLife();
                                if (collectLifeSound != null) collectLifeSound.play();
                            }
                            iterator.remove();
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
                            if (isBossLevel) {
                                gameState = STATE_ENDING;
                            } else {
                                nextLevelToLoad   = level + 1;   // stash which level to load
                                transitionStarted = false;       // reset your flag
                                gameState         = STATE_NEXT_LEVEL;
                            }
                            return;  // bail out of mainGame() immediately
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
            // play BossDefeat_Cue music once
            if (!bossDefeatedPlayed) {
                bossDefeatMusic.play();
                bossDefeatedPlayed = true;
            }
            // stop the level-3 music
            if (bgMusic != null && bgMusic.isPlaying()) {
                bgMusic.stop();
            }
        }

        updateCamera();

        // Check if Honu's defeat animation has finished to show Game Over screen
        if (honu.isDefeated() && honu.isDefeatAnimationFinished()) {
            gameState = STATE_GAME_OVER;
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

        if (honu.canMove()) {
            float dt = Gdx.graphics.getDeltaTime();

            // Up: W or ↑
            if (Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.UP)) {
                honu.moveUp(dt);
            }
            // Down: S or ↓
            if (Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                honu.moveDown(dt);
            }
            // Left: A or ←
            if (Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                honu.moveLeft(dt);
            }
            // Right: D or →
            if (Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                honu.moveRight(dt);
            }
            // Spacebar: Shooting
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                WaterBall w = honu.shoot();
                if (w != null) projectiles.add(w);
            }
        }
    }
    //Main menu Screen
    public void mainMenu() {
        // reset the camera to show the menu in its entirety
        camera.position.set(viewport.getWorldWidth()/2f,
            viewport.getWorldHeight()/2f,
            0);
        camera.update();

        // now clear & draw full-screen menu
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        menuBackground.setPosition(0, 0);
        menuBackground.draw(batch);

        // Draw background and title
        float titleCenterX = (1280 - menuTitle.getWidth()) / 2f;
        float titleCenterY = (1024 - menuTitle.getHeight()) / 2f + 300;
        menuBackground.setPosition(0, 0);
        menuBackground.draw(batch);
        menuTitle.setPosition(titleCenterX, titleCenterY);
        menuTitle.draw(batch);

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        // Start button
        float startX = (1280 - buttonStart.getWidth()) / 2f;
        float startY =  (1280 - buttonStart.getHeight()) / 2f - 50;
        Sprite startToDraw = buttonStart.getBoundingRectangle().contains(mouse.x, mouse.y)
            ? buttonStartHover : buttonStart;
        startToDraw.setPosition(startX, startY);
        startToDraw.draw(batch);
/*
        // Options button
        float optionsX = (1280 - buttonStart.getWidth()) / 2f;
        float optionsY =  (1280 - buttonStart.getHeight()) / 2f - 150;
        Sprite optionsToDraw = buttonOptions.getBoundingRectangle().contains(mouse.x, mouse.y)
            ? buttonOptionsHover : buttonOptions;
        optionsToDraw.setPosition(optionsX, optionsY);
        optionsToDraw.draw(batch);
 */

        // Quit button
        float exitX = (1280 - buttonStart.getWidth()) / 2f;
        float exitY =  (1280 - buttonStart.getHeight()) / 2f - 200;
        Sprite quitToDraw = buttonExit.getBoundingRectangle().contains(mouse.x, mouse.y)
            ? buttonExitHover : buttonExit;
        quitToDraw.setPosition(exitX, exitY);
        quitToDraw.draw(batch);


        // Version text
        String version = "v1.0.0";
        GlyphLayout layout = new GlyphLayout(fontBlack, version);
        float x = (viewport.getWorldWidth() - layout.width) / 2f;
        // 20px up from the very bottom
        float y = 100f;
        fontBlack.draw(batch, layout, x, y);

        batch.end();
        // Handle click
        if (Gdx.input.justTouched()) {
            if (buttonStart.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                menuMusic.stop(); // Stop menu music
                honu = new Honu();
                honu.setPosition(0, 128);
                loadLevel("map\\level1.txt");
                gameState = STATE_HOWTO ; // Start game with How to Play
            } else if (buttonOptions.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                // Implement later
            } else if (buttonExit.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                Gdx.app.exit();
            }
        }
    }
    public void nextLevel() {
        // on first frame, stop old BGM and start your transition cue:
        if (!transitionStarted) {
            transitionStarted = true;
            if (bgMusic != null && bgMusic.isPlaying()) {
                bgMusic.stop();
            }
            // assume you loaded `transitionMusic` in create()
            transitionMusic.play();
        }

        // clear & draw your “Level X Complete!” text
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(viewport.getWorldWidth()/2f,
            viewport.getWorldHeight()/2f,
            0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        String msg = "Level " + level + " Complete!";
        // center it:
        GlyphLayout layout = new GlyphLayout(fontWhite, msg);
        float x = (viewport.getWorldWidth() - layout.width) / 2f;
        float y = viewport.getWorldHeight()/2f + 50;
        fontWhite.draw(batch, layout, x, y);
        batch.end();

        // when your cue finishes, actually load the next map:
        if (!transitionMusic.isPlaying()) {
            // bump the level index
            level = nextLevelToLoad;

            // reset Honu
            honu = new Honu();
            honu.setPosition(0, 128);

            // load the new txt
            loadLevel("map\\level" + level + ".txt");

            // go back to either HOWTO or PLAYING
            gameState = (level == 1 ? STATE_HOWTO : STATE_PLAYING);
        }
    }
    public void gameOver() {
        // --- play the jingle once on entry ---
        if (!gameOverPlayed) {
            gameOverSfx.play();
            gameOverPlayed = true;
            // also stop any level music
            if (bgMusic != null && bgMusic.isPlaying()) {
                bgMusic.stop();
            }
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Center of the screen
        float centerX = camera.position.x;
        float centerY = camera.position.y / 2 + 200;
        float retryX  = centerX - 500;
        float quitX   = centerX + 100;
        float buttonY = centerY - 50;

        gameOverOverlay.setPosition(
            camera.position.x - camera.viewportWidth / 2f,
            camera.position.y - camera.viewportHeight / 2f
        );
        gameOverOverlay.draw(batch);

        gameOver.setPosition(
            centerX - gameOver.getWidth() / 2,
            centerY + 100
        );
        gameOver.draw(batch);

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        // Retry
        if (buttonRetry.getBoundingRectangle().contains(mouse.x, mouse.y)) {
            buttonRetryHighlight.setPosition(retryX, buttonY);
            buttonRetryHighlight.draw(batch);
        } else {
            buttonRetry.setPosition(retryX, buttonY);
            buttonRetry.draw(batch);
        }

        // Quit
        if (buttonQuit.getBoundingRectangle().contains(mouse.x, mouse.y)) {
            buttonQuitHighlight.setPosition(quitX, buttonY);
            buttonQuitHighlight.draw(batch);
        } else {
            buttonQuit.setPosition(quitX, buttonY);
            buttonQuit.draw(batch);
        }
        batch.end();

        if (Gdx.input.justTouched()) {
            if (buttonRetry.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                // reset the flag so it can play again next time
                gameOverPlayed = false;
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
        if (menuMusic != null) menuMusic.dispose();
        if (bossDefeatMusic != null) bossDefeatMusic.dispose();
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
    public void pauseScreen() {
        //Background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //Center
        float centerX = camera.position.x;
        float centerY = camera.position.y;

        pauseText.setPosition(centerX - pauseText.getWidth() / 2, centerY + 200);
        pauseText.draw(batch);

        // Display healthBar
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        honuHealthBar.draw(batch, camera);

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        // Buttons
        drawPauseButton(batch, returnButton, returnButtonHover, centerX - 270, centerY - 50, mouse);
        drawPauseButton(batch, mainMenuButton, mainMenuButtonHover, centerX - 350, centerY - 200, mouse);
        drawPauseButton(batch, pauseStuckButton, pauseStuckButtonHover, centerX - 350, centerY - 360, mouse);

        batch.end();

        //Handling input
        if (Gdx.input.justTouched()) {
            if (returnButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                gameState = STATE_PLAYING;
            } else if (mainMenuButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                // Stop and dispose level background music
                if (bgMusic != null) {
                    bgMusic.stop();
                    bgMusic.dispose();
                    bgMusic = null;
                }
                // Play main menu music if not already playing
                if (menuMusic != null && !menuMusic.isPlaying()) {
                    menuMusic.play();
                }
                gameState = STATE_MENU;
            } else if (pauseStuckButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                honu.setPosition(lastCheckpointX, lastCheckpointY);
                gameState = STATE_PLAYING;
            }
        }
    }
    //Helper method
    private void drawPauseButton(SpriteBatch batch, Sprite normal, Sprite hover,
                                 float x, float y, Vector3 mouse) {
        if (normal.getBoundingRectangle().contains(mouse.x, mouse.y)) {
            hover.setPosition(x, y);
            hover.draw(batch);
        } else {
            normal.setPosition(x, y);
            normal.draw(batch);
        }
    }
    //How to play screen, appear before playing level 1
    private void howToScreen() {
        float delta = Gdx.graphics.getDeltaTime();
        howToTimer += delta;

        //Compute alpha
        float alpha;
        if (howToTimer < FADE_IN_TIME) {
            alpha = howToTimer / FADE_IN_TIME;
        } else if (howToTimer < FADE_IN_TIME + DISPLAY_TIME) {
            alpha = 1f;
        } else if (howToTimer < HOWTO_TOTAL) {
            alpha = 1f - (howToTimer - (FADE_IN_TIME + DISPLAY_TIME)) / FADE_OUT_TIME;
        } else {
            // done -> go play
            gameState = STATE_PLAYING;
            howToTimer = 0f;
            return;
        }

        // Clear screen & begin
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

            // Set global tint
            batch.setColor(1,1,1, alpha);

            float cx = camera.viewportWidth/2f, cy = camera.viewportHeight/2f;

            // draw title
            fontWhite.getData().setScale(3);
            fontWhite.draw(batch, "How to Play", cx - 250, cy + 400);

            // draw the three controls underneath
            howToWASD.setPosition(cx - howToWASD.getWidth()/2 - 350 , cy);
            howToWASD.draw(batch);
            howToArrows.setPosition(cx - howToWASD.getWidth()/2 + 350, cy);
            howToArrows.draw(batch);
            fontWhite.getData().setScale(1.5f);
            fontWhite.draw(batch, "- Move -", cx - 100, cy + 150);

            howToSpace.setPosition(cx - 300, cy - 120);
            howToSpace.draw(batch);
            fontWhite.getData().setScale(1.5f);
            fontWhite.draw(batch, "- Shoot", cx + 100 , cy - 75);

            howToESC.setPosition(cx - 360, cy - 450);
            howToESC.draw(batch);
            fontWhite.getData().setScale(1.5f);
            fontWhite.draw(batch, "- Pause Game", cx + 100 , cy - 245);

            // reset tint
            batch.setColor(1,1,1,1);
        batch.end();
    }
    // Ending Screen
    private void showEnding() {
        //grab the frame‐delta
        float delta = Gdx.graphics.getDeltaTime();

        //on the very first call, start the music & zero the timer
        if (!endingPlayed) {
            endingMusic.play();
            endingPlayed  = true;
            endingTimer   = 0f;
        }

        // advance clock
        endingTimer += delta;

        // draw the ending screen
        camera.position.set(viewport.getWorldWidth()/2f,
            viewport.getWorldHeight()/2f,
            0);
        camera.update();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        endingScreen.setPosition(
            camera.position.x - endingScreen.getWidth()/2f,
            camera.position.y - endingScreen.getHeight()/2f
        );
        endingScreen.draw(batch);
        batch.end();

        // only allow skipping after 15 seconds have elapsed
        boolean canSkip = endingTimer >= 15f;

        // if the music ran out, or (once you're past 15s and the player taps/presses a key),
        //    tear everything down and go back to the menu
        if (!endingMusic.isPlaying()
            || (canSkip && (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)
            || Gdx.input.justTouched()))) {

            endingMusic.stop();
            endingPlayed = false;

            gameObjectsList.clear();
            projectiles.clear();

            if (menuMusic != null && !menuMusic.isPlaying()) {
                menuMusic.play();
            }

            gameState = STATE_MENU;
        }
    }

}
