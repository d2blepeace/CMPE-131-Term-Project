package io.github.ReefGuardianProject.objects.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SubmarineProjectile extends EnemyProjectile {
    private float stateTime;
    private float startX; // Track starting position
    private static final float MAX_TRAVEL_DISTANCE = 800f; // Max distance (e.g., 800 px)

    private Animation<TextureRegion> shootAnimation;

    public SubmarineProjectile(float x, float y, float speed) {
        super(x, y, speed);
        this.hitBox.setSize(32, 17);
        this.startX = x;
        stateTime = 0f;

        // Load the shooting animation frames
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
        // Move LEFT (boss projectile moves leftward)
        x -= speed * delta;
        hitBox.setPosition(x, y);

        stateTime += delta;

        // Check travel distance
        if (startX - x > MAX_TRAVEL_DISTANCE) {
            delete(); // If traveled beyond the max distance, mark for deletion
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
        TextureRegion frame = shootAnimation.getKeyFrame(stateTime, true);
        batch.draw(frame, x, y, 32, 17);
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
