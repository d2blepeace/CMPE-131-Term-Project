package io.github.ReefGuardianProject.objects.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.objects.GameObjects;


public class KelpBlock extends GameObjects {
    public Rectangle hitBox;
    Sprite sprite;
    Texture texture;

    //Constructor
    public KelpBlock (int x, int y) {
        hitBox = new Rectangle(x, y, 64, 64); //Change according to the game assets
        texture = new Texture(Gdx.files.internal("PNG\\environment_block\\Kelp_Block.png"));
        sprite = new Sprite(texture, 0, 0, 64, 64);
        setPosition(x, y);
    }

    @Override
    public int hit(Rectangle rectangle) {
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
        hitBox.x = x;
        hitBox.y = y;
        sprite.setPosition(x,y);
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
        //1- Collision to normal object, 2- Character die, 3- Collect the item; 4-reach checkpoint
        return 1;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public void dispose() {

    }

}
