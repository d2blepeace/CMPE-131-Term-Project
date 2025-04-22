package io.github.ReefGuardianProject.objects.finalBoss.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.finalBoss.phase.DiverPhase;

public class DiverBoss extends GameObjects {
    private float x, y;
    private int health = 5;
    private DiverPhase state = DiverPhase.MOVING;

    //Constructor
    public DiverBoss(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hit(Rectangle rectangle) {
        return 0;
    }

    @Override
    public void action(int type, float x, float y) {

    }

    @Override
    public void update(float delta) {

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

    }

    @Override
    public Rectangle getHitBox() {
        return null;
    }

    @Override
    public int hitAction() {
        return 0;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    public void receiveDamage() {
    }

}
