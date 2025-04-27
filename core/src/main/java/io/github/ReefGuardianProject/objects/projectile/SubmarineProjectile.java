package io.github.ReefGuardianProject.objects.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SubmarineProjectile extends EnemyProjectile {

    private float startX; // Track starting position
    private static final float MAX_TRAVEL_DISTANCE = 900f; // Max distance of the projectile

    private Animation<TextureRegion> shootAnimation;
    private Sound chargeSound, shootSound;
    private boolean hasPlayedChargeSound = false; //To check if the charge sound has played
    private float shootStateTime;
    private Animation<TextureRegion> chargeAnimation;

    private float chargeStateTime;
    private boolean isCharging;
    private float chargeDuration = 2.0f;

    public SubmarineProjectile(float x, float y, float speed) {
        super(x, y, speed);
        this.hitBox.setSize(32, 17);
        this.startX = x;
        chargeSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\BossCharging.mp3"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\BossProjectileShot.mp3"));

        // CHARGING ANIMATION
        chargeStateTime = 0f;
        this.isCharging = true;
        chargeAnimation = new Animation<>(0.1f,
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging1.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging2.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging3.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging4.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging5.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x32_Charging6.png"))
        );

        // SHOOTING
        shootStateTime = 0f;
        shootAnimation = new Animation<>(0.1f,
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_1.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_2.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_3.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_4.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_5.png")),
            new TextureRegion(new Texture("sprite\\finalboss\\SubmarineAttack\\32x17_BossProjectile_6.png"))
        );

    }

    @Override
    public void action(int type, float x, float y) {

    }

    @Override
    public void update(float delta) {
        if (isCharging) {
            chargeStateTime += delta;
            if (!hasPlayedChargeSound) {
                chargeSound.play();
                hasPlayedChargeSound = true;
            }
            if (chargeStateTime >= chargeDuration) {
                // Done charging, now start shooting
                isCharging = false;
                shootStateTime = 0f;
                shootSound.play();
            }
        } else {
            //  Only move after charging is finished
            x -= speed * delta;
            hitBox.setPosition(x, y);

            shootStateTime += delta;
            // Check travel distance
            if (startX - x > MAX_TRAVEL_DISTANCE) {
                delete();
            }
        }


    }

    @Override
    public void setPosition(float x, float y) {

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
        //Only play the charging animation when isCharging = true
        if (isCharging) {
            TextureRegion chargeFrame = chargeAnimation.getKeyFrame(chargeStateTime, true);
            batch.draw(chargeFrame, x, y, 32, 32);
        }
        else {
            TextureRegion shootFrame = shootAnimation.getKeyFrame(shootStateTime, true);
            batch.draw(shootFrame, x, y, 32, 17);
        }
    }


    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    @Override
    public int hitAction() {
        return 6; // 6 = Boss projectile that damage Honu
    }

    @Override
    public boolean isEnemy() {
        return true; // Treated as enemy
    }


    @Override
    public void dispose() {
    }
}
