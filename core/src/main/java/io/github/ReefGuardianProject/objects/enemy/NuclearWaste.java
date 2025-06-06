package io.github.ReefGuardianProject.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;

public class NuclearWaste extends GameObjects {
    public Rectangle hitBox;
    Sprite sprite;
    Texture texture;

    //Constructor
    public NuclearWaste (int x, int y) {
        hitBox = new Rectangle(x, y, 64, 64); //Change according to the game assets
        texture = new Texture(Gdx.files.internal("sprite\\enemyPNG\\nuclearWaste.png"));
        sprite = new Sprite(texture, 0, 0, 64, 64);
        setPosition(x, y);
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
