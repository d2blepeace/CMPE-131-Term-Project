package io.github.ReefGuardianProject.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.projectile.WaterBall;
import com.badlogic.gdx.audio.Sound;

public class Honu extends GameObjects {
    Rectangle bottom, top, right, left, full;
    Sprite sprite;
    Texture texture;
    int lives = 1; //Max lives of Honu
    int waterBallSpeed = 450; //Speed of waterBall
    private final int MAX_LIVES = 3;
    // Defeat animation of Honu
    private boolean isDefeated = false;
    private Animation<TextureRegion> defeatAnimation;
    private float defeatTimer = 0f;


    int body, cosmetic;

    // Stores the animation sprite
    private Texture[] armTextures;
    private Texture[] headTextures;

    float gameTime = 0f;
    boolean shoot = false;
    private Sound shootSound;

    Animation<TextureRegion> armAnimation, headAnimation;
    TextureRegion headIdle = new TextureRegion(new Texture("sprite\\head\\head1.png"));
    float velocityX;
    float velocityY;
    //Entry points of Honu
    public Honu() {
        //Hitbox
        //TODO: Check hitbox after updating the asset files, especially bottom
        full = new Rectangle(0, 0, 64, 64);
        bottom = new Rectangle(0, 0, 64, 8);
        left = new Rectangle(0, 8, 32, 48);
        right = new Rectangle(32, 8, 32, 48);
        top = new Rectangle(0, 56, 64, 8);

        //TODO: check later
        Texture bodyTexture = new Texture(bodyType(0));

        //Call animation set up
        setArmAnimation(0.1f);
        setHeadAnimation(0.1f);
        setDefeatAnimation(0.2f);

        texture = new Texture(Gdx.files.internal(bodyType(0)));
        sprite = new Sprite(texture, 0, 0, 64, 64);
        this.setPosition(0,0);

        //Setup gravity
        velocityY = 0;

        //Load shooting sound
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\pixelShoot.wav"));
    }
    // The defeat animation frame of Honu
    private void setDefeatAnimation(float time) {
        TextureRegion[] defeatFrames = new TextureRegion[] {
            new TextureRegion(new Texture("sprite\\honudefeat\\HonuDefeat_1.png")),
            new TextureRegion(new Texture("sprite\\honudefeat\\HonuDefeat_2.png")),
            new TextureRegion(new Texture("sprite\\honudefeat\\HonuDefeat_3.png")),
        };
        defeatAnimation =  new Animation<>(time, defeatFrames);
    }
    public void drawDefeatAnimation(SpriteBatch batch) {
        TextureRegion currentFrame = defeatAnimation.getKeyFrame(defeatTimer, false);
        batch.draw(currentFrame, full.x, full.y);
    }
    // Check if Honu is defeated
    public boolean isDefeated() {
        return isDefeated;
    }
    // Check if defeat animation is finised
    public boolean isDefeatAnimationFinished() {
        return defeatAnimation.isAnimationFinished(defeatTimer);
    }
    public String bodyType(int choice){
        String s = "sprite\\body\\";
        switch(choice){
            case(1): //body 1
                s += "honuHawaiiBody.png";
            case(2):
                s+= "";//for future bodies
            default:
                s += "honuBody.png";
                break;
        }
        return s;
    }
    public void setArmAnimation(float time){
        if (armTextures == null) {
            armTextures = new Texture[] {
                new Texture("sprite\\arm\\arm0.png"),
                new Texture("sprite\\arm\\arm1.png"),
                new Texture("sprite\\arm\\arm2.png"),
                new Texture("sprite\\arm\\arm3.png")
            };
        }
        TextureRegion[] armFrames = new TextureRegion[armTextures.length];
        for (int i = 0; i < armTextures.length; i++) {
            armFrames[i] = new TextureRegion(armTextures[i]);
        }
        armAnimation = new Animation<>(time, armFrames);
    }
    public void setHeadAnimation(float time){
        if (headTextures == null) {
            headTextures = new Texture[] {
                new Texture("sprite\\head\\head1.png"),
                new Texture("sprite\\head\\head2.png"),
                new Texture("sprite\\head\\head3.png"),
            };
        }
        TextureRegion[] headFrames = new TextureRegion[headTextures.length];
        for (int i = 0; i < headTextures.length; i++) {
            headFrames[i] = new TextureRegion(headTextures[i]);
        }
        headAnimation = new Animation<>(time, headFrames);
    }
    // Handle lives of Honu
    public int getLives() {
        return lives;
    }
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
        // If lives == 0, set defeating state to true, start timer and immobilize Honu
        if (lives == 0) {
            isDefeated = true;
            defeatTimer = 0f;
            velocityX = 0;
            velocityY = 0;
        }
    }

    public void gainLife() {
        if (lives < MAX_LIVES) {
            lives++;
        }
    }
    //Use to set live when Honu is revived
    public void setLives(int lives) {
        this.lives = Math.min(lives, MAX_LIVES); // Clamp to MAX_LIVES
    }
    //Honu shoot waterball
    public WaterBall shoot() {
        if (isDefeated) return null;

        float startX = full.x + full.width - 30; // In front of Honu
        float startY = full.y + (full.height / 2f) - 32; // Vertically centered if WaterBall is 64x64

        shoot = true;       //Trigger animation
        gameTime = 0f;      //Reset animation timer

        //Play shooting sfx
        if (shootSound != null) {
            shootSound.play(1.0f); // Volume = 1.0f
        }

        return new WaterBall(startX, startY, waterBallSpeed);
    }
    //Handle hitBox collision logic
    public int hit(Rectangle r) {
        if (left.overlaps(r)) {
            return 2;
        }
        if (right.overlaps(r)) {
            return 3;
        }
        if (bottom.overlaps(r)) {
            return 1;
        }
        if (top.overlaps(r)) {
            return 4;
        }
        return -1;
    }
    //Handling actions of Honu
    public void action(int actionType, float x, float y) {
        //Collision logic
        if (actionType == 1 ||actionType == 4) {
            velocityY = 0;
            setPosition(full.x, y);
        }
        if (actionType == 2 || actionType == 3) {
            velocityX = 0;
            setPosition(x, full.y);
        }
    }
    public void update(float delta) {
        float gravity = -0.8f;  // Reduced gravity underwater
        float terminalVelocity = -2.5f;  // Prevents sinking too fast
        float waterResistance = 0.98f; // Slows down movement over time
        float buoyancy = 0.7f; // Slight natural push upwards

        // Apply gravity and buoyancy
        velocityY += (gravity + buoyancy) * delta;
        velocityY *= waterResistance;

        // Clamp velocityY to terminal velocity
        if (velocityY < terminalVelocity) {
            velocityY = terminalVelocity;
        }

        //Apply movement
        full.y += velocityY;
        full.x += velocityX;

        //If honu is defeated, play the animation
        if (isDefeated) {
            defeatTimer += delta;
            return;
        }

        setPosition(full.x, full.y);
    }
    public void setPosition(float x, float y) {
        //TODO: check set position of Honu after updating assets
        full.setPosition(x, y);
        bottom.setPosition(x, y);
        left.setPosition(x, y + 8);
        right.setPosition(x + 32, y + 8);
        top.setPosition(x, y + 56);
        sprite.setPosition(x, y);
    }
    public void moveLeft(float delta) {
        if (isDefeated) return;
        full.x -= (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    public void moveRight(float delta) {
        if (isDefeated) return;
        full.x += (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    public void moveUp(float delta) {
        if (isDefeated) return;
        full.y += (200 * delta);
        sprite.setPosition(full.x, full.y);
    }

    public void moveDown(float delta) {
        if (isDefeated) return;
        full.y -= (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    //This will trigger when Honu collide to an enemy Object
    public void knockBack(float xAmount, float yAmount) {
        full.x += xAmount;
        full.y += yAmount;
        setPosition(full.x, full.y);
    }
    public void draw(SpriteBatch batch) {
        gameTime += Gdx.graphics.getDeltaTime(); //for animations
        sprite.draw(batch);

        //Play arm animation
        if(armAnimation != null){
            TextureRegion armFrame = armAnimation.getKeyFrame(gameTime, true);
            batch.draw(armFrame, full.x, full.y);
        }
        // If Honu shoots, play head animation
        if (shoot && headAnimation != null) {
            TextureRegion headFrame = headAnimation.getKeyFrame(gameTime, false);
            batch.draw(headFrame, full.x, full.y);
        } else if(headAnimation != null){
            batch.draw(headIdle, full.x, full.y);
        }
        if (shoot && headAnimation.isAnimationFinished(gameTime)) {
            shoot = false;
        }
        //If Honu is defeated, update the defeating animation
        if (isDefeated) {
            TextureRegion defeatFrame = defeatAnimation.getKeyFrame(defeatTimer, false);
            batch.draw(defeatFrame, full.x, full.y);
        }
    }

    @Override
    public Rectangle getHitBox() {
        return full;
    }

    @Override
    public int hitAction() {
        return 0; //No action
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

}
