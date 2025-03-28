package io.github.ReefGuardianProject.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Honu {
    public Rectangle bottom, top, right, left;
    Sprite sprite;
    Texture texture;
    int action;
    float velocityX;
    float velocityY;
    float speed;
    //Entry points of Honu
    public Honu() {
        bottom = new Rectangle(0.0f,0.0f,64.0f,64.0f);
        texture = new Texture(Gdx.files.internal("sprite\\Honu.png"));
        sprite = new Sprite(texture, 0, 0, 64, 64);

        this.setPosition(0,0);
        //Setup gravity
        velocityY = 0;
    }
    public int hitBox(Rectangle r){
        if (bottom.overlaps(r)) {
            return 1;
        }
        return -1;
    }

    //Handling actions of Honu
    public void action(int actionType) {

    }
    public void update(float delta) {
        float gravity = -0.8f;  // Reduced gravity underwater
        float terminalVelocity = -2.5f;  // Prevents sinking too fast
        float waterResistance = 0.98f; // Slows down movement over time
        float buoyancy = 0.7f; // Slight natural push upwards

        //Set up velocity Y to replicate gravity underwater
        velocityY += gravity * delta;

        //Apply buoyancy
        velocityY += buoyancy * delta;

        // Apply water resistance (reduces velocity over time)
        velocityY *= waterResistance;

        // Clamp velocityY to terminal velocity
        if (velocityY < terminalVelocity) {
            velocityY = terminalVelocity;
        }
        // Apply movement
        bottom.y += velocityY;
        setPosition(bottom.x, bottom.y);
    }
    public void setPosition(float x, float y) {
        bottom.x = x;
        bottom.y = y;
        sprite.setPosition(x, y);

    }
    public void moveLeft(float delta) {
        bottom.x -= (100 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void moveRight(float delta) {
        bottom.x += (100 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void moveUp(float delta) {
        bottom.y += (100 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }

    public void moveDown(float delta) {
        bottom.y -= (100 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
