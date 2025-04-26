package io.github.ReefGuardianProject.objects.projectile;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;

public abstract class EnemyProjectile extends GameObjects {
    protected float x, y;
    protected float speed;
    protected Rectangle hitBox;
    protected boolean active = true;

    public EnemyProjectile(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hitBox = new Rectangle(x, y, 32, 17); // Default size
    }

    @Override
    public int hit(Rectangle rectangle) {
        if (hitBox.overlaps(rectangle)) {
            delete();
            return 2; // Inflict damage (same as enemy)
        }
        return -1;
    }

    @Override
    public int hitAction() {
        return 2; // Boss projectile = damage Honu
    }

    @Override
    public boolean isEnemy() {
        return true; // Treated as enemy
    }

    @Override
    public boolean isWall() {
        return false;
    }

    public void delete() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

}
