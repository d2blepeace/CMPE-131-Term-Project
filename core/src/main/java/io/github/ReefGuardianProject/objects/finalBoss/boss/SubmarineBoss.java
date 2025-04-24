package io.github.ReefGuardianProject.objects.finalBoss.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.finalBoss.phase.SubmarinePhase;

public class SubmarineBoss extends GameObjects {
    private float x, y;
    private int health = 7;
    private Sprite sprite;
    private Texture texture;
    private float moveTimer = 0f;
    private float moveInterval = 2f; // seconds
    // Movement of Submarine
    private float velocityY = 200; // Speed in pixels per second
    private float upperBound = 800f;
    private float lowerBound = 100f;
    // Pause movement
    private boolean isPaused = false;
    private float pauseTimer = 0f;
    private float timeUntilNextPause = 2f; // how long until boss pauses again
    private float pauseCooldownTimer = 0f;
    private final float pauseDuration = 1.0f; // 1 second pause


    private SubmarinePhase state = SubmarinePhase.MOVING;

    //Constructor
    public SubmarineBoss(float x, float y) {
        this.x = x;
        this.y = y;

        //Load Sprite
        this.texture = new Texture(Gdx.files.internal("sprite\\finalboss\\256x128_SubmarineBoss.png"));
        this.sprite = new Sprite(texture, 0, 0, 256, 128);
        sprite.setPosition(x, y);
    }

    @Override
    public int hit(Rectangle rectangle) {
        return 0;
    }

    @Override
    public void action(int type, float x, float y) {

    }
    //Boss AI update according to game time
    @Override
    public void update(float delta) {
        //1. MOVING STATE
        if (state != SubmarinePhase.MOVING) return;

        if (isPaused) {
            pauseTimer += delta;
            if (pauseTimer >= pauseDuration) {
                isPaused = false;
                pauseTimer = 0f;
                // Set new cooldown for next pause
                timeUntilNextPause = getRandomPauseInterval();
                pauseCooldownTimer = 0f;
            }
        } else {
            // Boss is moving
            y += velocityY * delta;
            sprite.setPosition(x, y);

            // Track time until next pause
            pauseCooldownTimer += delta;
            if (pauseCooldownTimer >= timeUntilNextPause) {
                isPaused = true;
                pauseCooldownTimer = 0f;
            }

            // Flip direction if hit bounds
            if (y >= upperBound) {
                y = upperBound;
                velocityY = -velocityY;
            } else if (y <= lowerBound) {
                y = lowerBound;
                velocityY = -velocityY;
            }
        }
        //2. ATTACKING STATE
    }
    //Generate randomly pause interval
    private float getRandomPauseInterval() {
        // Random between 1 and 4 seconds
        return 1f + (float)Math.random() * 2.5f;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }

    @Override
    public void moveLeft(float delta) {

    }

    @Override
    public void moveRight(float delta) {

    }

    @Override
    public void moveUp(float delta) {

    }

    @Override
    public void moveDown(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(x, y, 256, 128);
    }

    @Override
    public int hitAction() {
        return 0;
    }

    @Override
    public boolean isEnemy() {
        return true;
    }

    @Override
    public void dispose() {

    }

    public int getHealth() {
        return health;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void receiveDamage() {
        health--;
        if (health <= 0) {
            // Handle death or transition
        }
    }
    //Logic
}
