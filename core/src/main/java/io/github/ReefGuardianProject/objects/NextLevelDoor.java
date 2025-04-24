package io.github.ReefGuardianProject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class NextLevelDoor extends GameObjects {
    private Rectangle hitBox;
    private Sprite sprite;
    public NextLevelDoor(int x, int y) {
        hitBox = new Rectangle(x, y, 128, 128);
        sprite = new Sprite(new Texture(Gdx.files.internal("PNG/128x128_Next_Level_Door.png")));
        sprite.setPosition(x, y);
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
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public int hitAction() {
        return 5; // Load next Level
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    @Override
    public void dispose() {

    }
}
