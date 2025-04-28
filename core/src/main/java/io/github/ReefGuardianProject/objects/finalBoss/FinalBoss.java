package io.github.ReefGuardianProject.objects.finalBoss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.core.ReefGuardian;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.projectile.SubmarineProjectile;

import java.util.ArrayList;

public class FinalBoss extends GameObjects {
    private float x, y;
    //Health field
    private int health;
    private int initialHealth;
    //Texture Field
    private Sprite sprite;
    private Texture texture, damagedTexture;
    private boolean isDamaged = false;
    //Movement fields
    private float velocityY = 250f;
    private float upperBound = 800f;
    private float lowerBound = 100f;
    private boolean isPaused = false;
    private float pauseTimer = 0f;
    private float timeUntilNextPause = 2f;
    private float pauseCooldownTimer = 0f;
    private final float pauseDuration = 1.8f; // seconds
    //Projectile field
    private ArrayList<SubmarineProjectile> projectiles = new ArrayList<>();
    private boolean hasFired = false;


    //Constructor
    public FinalBoss(float x, float y, int startingHealth) {
        this.x = x;
        this.y = y;
        this.health = startingHealth;
        this.initialHealth =  startingHealth;

        //Load Sprite
        this.texture = new Texture(Gdx.files.internal("sprite\\finalboss\\SubmarineBoss\\256x128_SubmarineBoss.png"));
        this.damagedTexture = new Texture(Gdx.files.internal("sprite\\finalboss\\SubmarineBoss\\256x128_Damaged_SubmarineBoss.png"));

        this.sprite = new Sprite(texture, 0, 0, 256, 128);
        sprite.setPosition(x, y);
    }
    @Override
    public int hit(Rectangle rectangle) {
        if (this.getHitBox().overlaps(rectangle)) {
            return 2; // Damage Honu
        }
        return -1;
    }

    @Override
    public void action(int type, float x, float y) {

    }

    //Boss AI update according to game time
    @Override
    public void update(float delta) {
        if (isPaused) {
            pauseTimer += delta;

            // Fire once when pausing
            if (!hasFired) {
                fireProjectile();
                hasFired = true;
            }

            if (pauseTimer >= pauseDuration) {
                isPaused = false;
                pauseTimer = 0f;
                hasFired = false;
                timeUntilNextPause = getRandomPauseInterval();
                pauseCooldownTimer = 0f;
            }
        } else {
            // Moving phase
            y += velocityY * delta;
            sprite.setPosition(x, y);

            pauseCooldownTimer += delta;
            if (pauseCooldownTimer >= timeUntilNextPause) {
                isPaused = true;
                pauseCooldownTimer = 0f;
            }

            // Bounce off bounds
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
    public boolean isDefeated() {
        return health <= 0;
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
        return 2; // Create damage to Honu
    }
    public void receiveDamage() {
        health--;
        // If health is half or less, switch to damaged
        if (health <= initialHealth / 2 && !isDamaged) {
            sprite.setTexture(damagedTexture);
            isDamaged = true;
        }

        if (health <= 0) {
            // Handle death
        }
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
}
