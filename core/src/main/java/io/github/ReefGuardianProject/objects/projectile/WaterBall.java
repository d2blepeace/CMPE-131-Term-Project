package io.github.ReefGuardianProject.objects.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class WaterBall extends Projectile {
    private float stateTime;
    private boolean hasGrown;       //Check if the waterBall has grown to full size
    // Two different array to store the animation frames
    private Animation<TextureRegion> growAnimation;
    private Animation<TextureRegion> spinAnimation;
    private float startX;           //Track waterBall starting X position
    private static final float MAX_TRAVEL_DISTANCE = 1000f; //Distance cap of waterball's traveling
    private boolean active = true;         // To keep track of the state of the waterball

    // Constructor sets animation, speed, direction
    public WaterBall(float x, float y, float speed) {
        super(x, y, speed);
        this.startX = x;            //Initial location of waterBall
        stateTime = 0;
        hasGrown = false;

        //Animation
            //1. Growing stage
            growAnimation = new Animation<>(0.1f,
                new TextureRegion(new Texture("sprite\\waterball\\waterBall1.png")),
                new TextureRegion(new Texture("sprite\\waterball\\waterBall2.png")),
                new TextureRegion(new Texture("sprite\\waterball\\waterBall3.png")));
            //2. Spinning stage
            spinAnimation = new Animation<>(0.25f,
                new TextureRegion(new Texture("sprite\\waterball\\waterBallFull1.png")),
                new TextureRegion(new Texture("sprite\\waterball\\waterBallFull2.png")),
                new TextureRegion(new Texture("sprite\\waterball\\waterBallFull3.png")),
                new TextureRegion(new Texture("sprite\\waterball\\waterBallFull4.png")));
    }
    @Override
    public void update(float delta) {
        x += speed * delta;
        stateTime += delta;

        if (!hasGrown && growAnimation.isAnimationFinished(stateTime)) {
            hasGrown = true;
            // reset for spin loop
            stateTime = 0f;
        }
        //Destroy waterball after traveling max distance
        if (x - startX > MAX_TRAVEL_DISTANCE) {
            delete();
        }
            hitBox.x = (int) x;
        hitBox.y = (int) y;
    }
    //Implement these manually now
    public boolean isActive() {
        return active;
    }
    //Delete the waterball projectile = set active to false
    public void delete() {
        active = false;
    }
    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currFrame = !hasGrown
            ? growAnimation.getKeyFrame(stateTime, false)
            : spinAnimation.getKeyFrame(stateTime, true);
        batch.draw(currFrame, x, y, 64, 64);
    }
    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

}
