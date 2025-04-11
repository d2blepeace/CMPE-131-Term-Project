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
        full = new Rectangle(0, 0, 64, 64);
        bottom = new Rectangle(0, 0, 64, 8);
        left = new Rectangle(0, 8, 32, 48);
        right = new Rectangle(32, 8, 32, 48);
        top = new Rectangle(0, 56, 64, 8);

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
    public void action(int actionType, float x, float y) {
        //Collision logic
        if (actionType == 1 ||actionType == 4) {
            velocityY = 0;
            setPosition(full.x, y);
        }
        if (actionType == 2 || actionType == 3) {
            velocityX = 0;
            setPosition(x, full.y);
        }
    }
    public void update(float delta) {
        float gravity = -0.8f;  // Reduced gravity underwater
        float terminalVelocity = -2.5f;  // Prevents sinking too fast
        float waterResistance = 0.98f; // Slows down movement over time
        float buoyancy = 0.7f; // Slight natural push upwards

        // Apply gravity and buoyancy
        velocityY += (gravity + buoyancy) * delta;
        velocityY *= waterResistance;

        // Clamp velocityY to terminal velocity
        if (velocityY < terminalVelocity) {
            velocityY = terminalVelocity;
        }

        //Apply movement
        full.y += velocityY;
        full.x += velocityX;

        setPosition(full.x, full.y);
    }
    public void setPosition(float x, float y) {
        //TODO: check set position of Honu after updating assets
        full.setPosition(x, y);
        bottom.setPosition(x, y);
        left.setPosition(x, y + 8);
        right.setPosition(x + 32, y + 8);
        top.setPosition(x, y + 56);
        sprite.setPosition(x, y);
    }
    public void moveLeft(float delta) {
        full.x -= (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    public void moveRight(float delta) {
        full.x += (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    public void moveUp(float delta) {
        full.y += (200 * delta);
        sprite.setPosition(full.x, full.y);
    }

    public void moveDown(float delta) {
        full.y -= (200 * delta);
        sprite.setPosition(full.x, full.y);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public Rectangle getHitBox() {
        //TODO:
        return full;
    }
}
