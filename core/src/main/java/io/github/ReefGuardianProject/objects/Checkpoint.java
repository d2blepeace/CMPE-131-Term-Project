package io.github.ReefGuardianProject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Checkpoint extends GameObjects {
    Rectangle hitBox;
    Sprite sprite;
    Texture texture;
    //Constructor
    public Checkpoint (int x, int y) {
        hitBox = new Rectangle(0,0,64,64);
        texture = new Texture(Gdx.files.internal("PNG\\Checkpoint.png"));
        sprite = new Sprite(texture, 0,0,64,64);
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
        hitBox.x = x;
        hitBox.y = y;
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
        //1- Collision to normal object, 2- Character die, 3- Collect the item; 4- reach checkpoint
        return 4;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

}
