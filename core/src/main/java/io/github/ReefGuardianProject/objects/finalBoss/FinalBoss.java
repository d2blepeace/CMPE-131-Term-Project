package io.github.ReefGuardianProject.objects.finalBoss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private boolean active = false;
    //Health field
    private int health;
    private int initialHealth;
    //Texture Field
    private Sprite sprite;
    private Texture texture, damagedTexture;
    private boolean isDamaged = false;
    //Movement fields
    private float velocityY = 400f;
    private float upperBound = 800f;
    private float lowerBound = 100f;
    private boolean isPaused = false;
    private float pauseTimer = 0f;
    private float timeUntilNextPause = 2f;
    private float pauseCooldownTimer = 0f;
    private final float pauseDuration = 1.8f; // seconds
    private boolean isHurt = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.75f;  // how long to flash
    private Sound damageSfx;
    private Sound explosionSfx1, explosionSfx2;
    // Sinking after defeat
    private boolean isDefeated = false;
    private boolean hasExploded = false;
    private boolean sinking = false;
    private float sinkSpeed = 50f;      // pixels per second
    // the Y‐coordinate of the top of the floor rock
    private static final float GROUND_Y = 64f;
    // Explosion sequence
    private float explosionTimer = 0f;
    // 0 = not started, 1 = waiting to play #1, 2 = waiting to play #2, 3 = done
    private int explosionStage = 0;


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
        damageSfx = Gdx.audio.newSound(Gdx.files.internal("sfx\\Pixel_Honu_Dmg_SFX.mp3"));
        explosionSfx1 = Gdx.audio.newSound(Gdx.files.internal("sfx\\Explosion_Sfx1.mp3"));
        explosionSfx2 = Gdx.audio.newSound(Gdx.files.internal("sfx\\Explosion_Sfx2.mp3"));


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
    // Call when Honu gets close enough
    public void activate() {
        active = true;
    }

    @Override
    public void action(int type, float x, float y) {

    }

    //Boss AI update according to game time
    @Override
    public void update(float delta) {
        // --- death / sinking logic ---
        if (sinking) {
            // advance timer even while sinking, so we can drive the explosion sequence
            explosionTimer += delta;

            // 1) play explosion #1 after 1s
            if (explosionStage == 1 && explosionTimer >= 1f) {
                explosionSfx1.play();
                explosionStage = 2;
                explosionTimer = 0f;
            }
            // 2) play explosion #2 one second later
            else if (explosionStage == 2 && explosionTimer >= 1f) {
                explosionSfx2.play();
                explosionStage = 3;
            }

            // sink down, but stop at the floor:
            y -= sinkSpeed * delta;
            if (y <= GROUND_Y) {
                y = GROUND_Y;
                sinking = false;
                active  = false;
            }
            sprite.setPosition(x, y);
            return;
        }

        // if he’s been defeated, do nothing else
        if (isDefeated) return;

        // otherwise only run your normal AI once active
        if (!active) return;

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
        // Check if the boss is hurt
        if (isHurt) {
            hurtTimer += delta;
            if (hurtTimer >= HURT_DURATION) {
                isHurt = false;
            }
        }

        // Update projectiles
        for (SubmarineProjectile p : projectiles) {
            p.update(delta);
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    private void fireProjectile() {
        if (isDefeated) return;
        // Adjust the location of the projectile's animation
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
        // flicker during hurt: skip every other 0.1s
        if (isHurt && !sinking) {
            int frame = (int)(hurtTimer * 10) % 2;
            if (frame == 1) return;
        }
        if (!active) {
            // draw just the idle sprite
            sprite.setPosition(x, y);
            sprite.draw(batch);
            return;
        }

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
        return isDefeated ? 0 : 2; // Create damage to Honu unless defeated
    }
    public void receiveDamage() {
        // Play sounds
        if (damageSfx != null) damageSfx.play();
        health--;
        isHurt = true;
        hurtTimer = 0f;
        if (health <= initialHealth/2 && !isDamaged) {
            sprite.setTexture(damagedTexture);
            isDamaged = true;
        }
        // only trigger the “death” once
        if (health <= 0 && !hasExploded) {
            hasExploded   = true;
            isDefeated    = true;
            sinking       = true;     // start sink+explosion sequence next frame

            // start the timer so that explosionStage==1 means “waiting 1s for SFX#1”
            explosionStage   = 1;
            explosionTimer   = 0f;
        }
        // Purge the pending shooting animation
        for (SubmarineProjectile p : projectiles) {
            p.delete();
        }
        projectiles.clear();
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
        if (explosionSfx1 != null) explosionSfx1.dispose();
        if (explosionSfx2 != null) explosionSfx2.dispose();
        if (damageSfx != null) damageSfx.dispose();
    }
    public boolean isActive() {
        return active;
    }
}
