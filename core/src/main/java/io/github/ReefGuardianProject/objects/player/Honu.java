package io.github.ReefGuardianProject.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ReefGuardianProject.objects.GameObjects;

import java.awt.*;

public class Honu extends GameObjects {
    Rectangle bottom, top, right, left, full;
    Sprite sprite;
    Texture texture;
    int action;
    float velocityX;
    float velocityY;
    float speed;
    //Entry points of Honu
    public Honu() {
        //Hitbox
        //TODO: Check hitbox after updating the asset files, especially bottom
        full = new Rectangle(0,0, 64, 64);
        bottom = new Rectangle(0,0, 64, 64);
        left = new Rectangle(0,16,32, 96);
        right = new Rectangle(32, 16,32, 96);
        top = new Rectangle(0, 64, 64, 16);

        texture = new Texture(Gdx.files.internal("sprite\\Honu.png"));
        sprite = new Sprite(texture, 0, 0, 64, 64);

        this.setPosition(0,0);
        //Setup gravity
        velocityY = 0;
    }
    public int hit(Rectangle r) {
        if (left.overlaps(r)) {
            return 2;
        }
        if (right.overlaps(r)) {
            return 3;
        }
        if (bottom.overlaps(r)) {
            return 1;
        }
        if (top.overlaps(r)) {
            return 4;
        }


        return -1;
    }

    //Handling actions of Honu
    public void action(int actionType,float x, float y) {
        //Collision logic

        if (actionType == 1 ||actionType == 4) {
            velocityY = 0;
            setPosition(bottom.x, y);
        }
        if (actionType == 2 || actionType == 3) {
            velocityX = 0;
            setPosition(x, bottom.y);
        }


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
        top.y +=  velocityY;
        setPosition(bottom.x, bottom.y);
    }
    public void setPosition(float x, float y) {
        //TODO: check set position of Honu after updating assets
        full.x = x;
        full.y = y;

        left.x = x;
        left.y = y + 16;

        right.x = x + 32;
        right.y = y + 16;

        top.x = x;
        top.y = y + 64;

        bottom.x = x;
        bottom.y = y;

        sprite.setPosition(x, y);
    }
    public void moveLeft(float delta) {
        bottom.x -= (200 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void moveRight(float delta) {
        bottom.x += (200 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void moveUp(float delta) {
        bottom.y += (200 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }

    public void moveDown(float delta) {
        bottom.y -= (200 * delta);
        sprite.setPosition(bottom.x, bottom.y);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        //TODO:
        return bottom;
    }
}
