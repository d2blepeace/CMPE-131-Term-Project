package io.github.ReefGuardianProject.objects.finalBoss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;

//Invisible barrier that prevent player to swim pass the boss
public class BossBarrier extends GameObjects {
    private float x, y;
    private Rectangle hitBox;
    private boolean active = true;
    //Constructor
    public BossBarrier(float x, float y) {
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x, y, 32, 1024); // Tall wall
    }
    @Override
    public int hit(Rectangle rectangle) {
        if (!active) return -1; // No collision anymore
        return 10; // otherwise act like a barrier
    }


    @Override
    public void action(int type, float x, float y) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        hitBox.setPosition(x, y);
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
    }

    @Override
    public Rectangle getHitBox() {
        return active ? hitBox : null;
    }

    @Override
    public int hitAction() {
        return active ? 10 : -1;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    @Override
    public boolean isWall() {
        return false; //Special wall
    }

    @Override
    public void dispose() {
    }
    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
