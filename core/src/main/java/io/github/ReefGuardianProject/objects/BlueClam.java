package io.github.ReefGuardianProject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
// Blue clams that would provide Honu a shield
public class BlueClam extends GameObjects{
    private Rectangle hitBox;
    private Sprite sprite;
    private Sound collectSound;
    public BlueClam (float x, float y) {
        hitBox = new Rectangle(x, y, 32, 32);
        sprite = new Sprite(new Texture(Gdx.files.internal("PNG\\32x32_Clam_Blue.png")));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\shieldCollect.mp3"));
        sprite.setPosition(x, y);
    }

    @Override
    public int hit(Rectangle rectangle) {
        if (hitBox.overlaps(rectangle)) return 3;   // 3 = collectible
        return -1;
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
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public int hitAction() {
        return 3;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        collectSound.dispose();
    }
    public void playCollectSfx() {
        collectSound.play();
    }
}
