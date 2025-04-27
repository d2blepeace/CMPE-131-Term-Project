package io.github.ReefGuardianProject.objects.finalBoss.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.finalBoss.phase.BossPhase;

public class FinalBoss extends GameObjects {
    private BossPhase phase;
    /**
     * Phase 1: Submarine
     * Phase 2: Diver
     */
    private SubmarineBoss phase1;
    private DiverBoss phase2;

    //Constructor
    public FinalBoss(float x, float y, int startingHealth) {
        this.phase = BossPhase.SUBMARINE;
        this.phase1 = new SubmarineBoss(x, y, 2);

        this.phase2 = new DiverBoss(x, y);
    }
    @Override
    public int hit(Rectangle rectangle) {
        if (this.getHitBox().overlaps(rectangle)) {
            return 2;
        }
        return -1; // No collision otherwise
    }

    @Override
    public void action(int type, float x, float y) {

    }

    @Override
    public void update(float delta) {
        //Phase 1
        if (phase == BossPhase.SUBMARINE) {
            phase1.update(delta);
            if (phase1.getHealth() <= 0) { //If health reaches 0, goes to phase 2
                phase = BossPhase.DIVER;    //phase 2
                phase2.setPosition(phase1.getX(), phase1.getY());   //Phase 2 loads at the previous location of phase 1
            }
        } else if (phase == BossPhase.DIVER) {
            phase2.update(delta);
        }
    }
    public boolean isDefeated() {
        if (phase == BossPhase.SUBMARINE) {
            return phase1.getHealth() <= 0;
        } //else if (phase == BossPhase.DIVER) {
           // return phase2.getHealth() <= 0;
       // }
        return false;
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
        if (phase == BossPhase.SUBMARINE) {
            phase1.draw(batch);
        } else if (phase == BossPhase.DIVER) {
            phase2.draw(batch);
        }
    }

    @Override
    public Rectangle getHitBox() {
        return phase == BossPhase.SUBMARINE ? phase1.getHitBox() : phase2.getHitBox();
    }

    @Override
    public int hitAction() {
        return 2; // Create damage to Honu
    }
    public void receiveDamage() {
        if (phase == BossPhase.SUBMARINE) {
            phase1.receiveDamage();
        }
        if (phase == BossPhase.DIVER) {
            phase2.receiveDamage();
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
