package io.github.ReefGuardianProject.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

//Abstract class that would be a guide for the game moving objects
public abstract class GameObjects {
    public abstract int hit(Rectangle rectangle);
    public abstract void action(int type, float x, float y);
    public abstract void update(float delta);
    public abstract void setPosition(float x, float y);
    public abstract void moveLeft(float delta);
    public abstract void moveRight(float delta);
    public abstract void moveUp(float delta);

    public abstract void moveDown(float delta);
    public abstract void draw(SpriteBatch batch);
    public abstract Rectangle getHitBox();

    //Return int: 1 = normal block, 2 = receive dmg, 3 = collectible, 4 = Save Progress; 5 = Next Level
    public abstract int hitAction();

    public abstract boolean isEnemy();
}
