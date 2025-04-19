package io.github.ReefGuardianProject.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;

public class WaterBottle extends GameObjects {
    public Rectangle hitBox;
    Sprite sprite;
    Texture texture;

    //Constructor
    public WaterBottle (int x, int y) {
        hitBox = new Rectangle(x, y, 64, 64); //Change according to the game assets
        texture = new Texture(Gdx.files.internal("PNG\\Water_Bottle.png"));
        sprite = new Sprite(texture, 0, 0, 64, 64);
        setPosition(x, y);
    }
    @Override
    public int hit(Rectangle rectangle) {
        return 0;
    }

    @Override
    public void action(int type, float x, float y) {
    //does not do anything
    }

    @Override
    public void update(float delta) {
    //Static object
    }

    @Override
    public void setPosition(float x, float y) {
        hitBox.setPosition(x, y);
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
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public int hitAction() {
        return 2;
    }

    @Override
    public boolean isEnemy() {
        return true; //All Enemy will return True
    }
}
