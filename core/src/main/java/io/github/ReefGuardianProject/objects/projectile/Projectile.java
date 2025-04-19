package io.github.ReefGuardianProject.objects.projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

// Handle the Projectile
public abstract class Projectile {
    protected float x, y;
    protected float speed;
    protected Rectangle hitBox;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;
    protected boolean alive = true;
    //Constructor
    public Projectile (float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hitBox = new Rectangle(x, y, 64, 64);
    }
    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);
    public abstract Rectangle getHitBox();
    public boolean isAlive() {
        return alive;
    }
    public void destroy() {
        alive = false;
    }

}
