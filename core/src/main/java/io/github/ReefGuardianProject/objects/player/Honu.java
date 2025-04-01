package io.github.ReefGuardianProject.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ReefGuardianProject.objects.GameObjects;

import java.awt.*;

public class Honu extends GameObjects {
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
    public void hit(Rectangle r) {
        // Calculate overlap in both axes
        float overlapX = Math.min(bottom.x + bottom.width - r.x, r.x + r.width - bottom.x);
        float overlapY = Math.min(bottom.y + bottom.height - r.y, r.y + r.height - bottom.y);

        // Determine whether to resolve collision on X or Y axis
        if (overlapX < overlapY) {
            // Horizontal collision: Push Honu out sideways
            if (bottom.x < r.x) {
                setPosition(r.x - bottom.width, bottom.y); // Left side
            } else {
                setPosition(r.x + r.width, bottom.y); // Right side
            }
            velocityX = 0; // Stop horizontal movement
        } else {
            // Vertical collision: Push Honu out vertically
            if (bottom.y < r.y) {
                setPosition(bottom.x, r.y - bottom.height); // Bottom side (landing)
            } else {
                setPosition(bottom.x, r.y + r.height); // Top side (hitting head)
            }
            velocityY = 0; // Stop vertical movement
        }
    }

    //Handling actions of Honu
    public void action(int actionType,float x, float y) {
        //Collision logic
        // Landed on top of an object
        if (actionType == 1) {
            setPosition(bottom.x, y); // Stop Honu exactly at the top of the object
            velocityY = 0;
        }
        else if (actionType == 2) {  // Collided from the left
            setPosition(x - bottom.width, bottom.y); // Stop at left edge
            velocityX = 0;
        }
        else if (actionType == 3) {  // Collided from the right
            setPosition(x, bottom.y); // Stop at right edge (previously incorrect)
            velocityX = 0;
        }
        else if (actionType == 4) {  // Hit from below (bumping into the object)
            setPosition(bottom.x, y - bottom.height); // Prevent clipping through the bottom
            velocityY = 0;
        }
    }
    public void handleCollision(Rectangle r) {
        // Calculate overlap in both axes
        float overlapX = Math.min(bottom.x + bottom.width - r.x, r.x + r.width - bottom.x);
        float overlapY = Math.min(bottom.y + bottom.height - r.y, r.y + r.height - bottom.y);

        // Determine whether to resolve collision on X or Y axis
        if (overlapX < overlapY) {
            // Horizontal collision: Push Honu out sideways
            if (bottom.x < r.x) {
                setPosition(r.x - bottom.width, bottom.y); // Left side
            } else {
                setPosition(r.x + r.width, bottom.y); // Right side
            }
            velocityX = 0; // Stop horizontal movement
        } else {
            // Vertical collision: Push Honu out vertically
            if (bottom.y < r.y) {
                setPosition(bottom.x, r.y - bottom.height); // Bottom side
            } else {
                setPosition(bottom.x, r.y + r.height); // Top side
            }
            velocityY = 0; // Stop vertical movement
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

    @Override
    public Rectangle getHitBox() {
        //TODO:
        return bottom;
    }
}
