package io.github.ReefGuardianProject.objects.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class WaterBall extends Projectile {
    private TextureRegion[] frames;

    // Constructor sets animation, speed, direction
    public WaterBall(float x, float y, float speed) {
        super(x, y, speed);

        //Animation
        frames = new TextureRegion[] {
            new TextureRegion(new Texture("sprite\\waterball\\waterBall1.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBall2.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBall3.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBallFull1.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBallFull2.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBallFull3.png")),
            new TextureRegion(new Texture("sprite\\waterball\\waterBallFull4.png")),
        };
        animation = new Animation<>(0.1f, frames);
    }
    @Override
    public void update(float delta) {
        x += speed * delta;
        stateTime += delta;
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currFrame, x, y, 64, 64);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
}
