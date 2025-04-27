package io.github.ReefGuardianProject.objects.finalBoss.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.core.ReefGuardian;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.finalBoss.phase.SubmarinePhase;
import io.github.ReefGuardianProject.objects.projectile.SubmarineProjectile;
import java.util.ArrayList;

public class SubmarineBoss extends GameObjects {

    private float x, y;
    private int health;
    private Sprite sprite;
    private Texture texture;
    private float moveTimer = 0f;
    private float moveInterval = 2f; // seconds
    // Movement of Submarine
    private float velocityY = 250; // Speed in pixels per second
    private float upperBound = 800f;
    private float lowerBound = 100f;
    // Pause movement
    private boolean isPaused = false;
    private float pauseTimer = 0f;
    private float timeUntilNextPause = 2f; // how long until boss pauses again
    private float pauseCooldownTimer = 0f;
    private final float pauseDuration = 1.8f; // 2.0f second pause
    //Submarine Projectile rendering
    private ArrayList<SubmarineProjectile> projectiles = new ArrayList<>();
    private boolean hasFired = false;
    private SubmarinePhase state = SubmarinePhase.MOVING;

    //Constructor
    public SubmarineBoss(float x, float y, int startingHealth) {
        this.x = x;
        this.y = y;
        this.health = startingHealth;
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
        if (state != SubmarinePhase.MOVING) return;

        if (isPaused) {
            pauseTimer += delta;

            // Fire projectile ONCE when entering pause
            if (!hasFired) {
                fireProjectile();
                hasFired = true;
            }
            if (pauseTimer >= pauseDuration) {
                isPaused = false;
                pauseTimer = 0f;
                hasFired = false; // Reset for next pause
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

        // Update projectiles
        for (SubmarineProjectile p : projectiles) {
            p.update(delta);
        }
        // Remove projectiles that are no longer active
        projectiles.removeIf(p -> !p.isActive());
    }

    private void fireProjectile() {
        // Adjust the location of the projectile's animation
        // NOTE: DO NOT ADJUST xFixingPixel and yFixingPixel
        float xFixingPixel = 30;
        float yFixingPixel = ((float) 128 / 2) - 38;
        float projectileStartX = this.x + xFixingPixel ;
        float projectileStartY =  this.y + yFixingPixel;

        SubmarineProjectile projectile = new SubmarineProjectile(projectileStartX, projectileStartY, 500f);
        projectiles.add(projectile);
        ReefGuardian.getInstance().scheduleGameObjectAdd(projectile);
    }

    //Generate randomly pause interval
    private float getRandomPauseInterval() {
        // Random between 1 and 3 s
        return 1f + (float)Math.random() * 2f;
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
        for (SubmarineProjectile p : projectiles) {
            p.draw(batch);
        }
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
    public boolean isWall() {
        return false;
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
}
