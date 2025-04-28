package io.github.ReefGuardianProject.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.ReefGuardianProject.core.ReefGuardian;
import io.github.ReefGuardianProject.objects.GameObjects;
import io.github.ReefGuardianProject.objects.player.Honu;
// TrashBag enemy that will follow Honu and destroy itself once hit honu
public class TrashBag extends GameObjects {
    public Rectangle hitBox;
    private Animation<TextureRegion> animation;
    private float stateTime = 0.0f;
    private boolean isChasing = false;
    private boolean active = true; // to check if the trashbag is active to delete TrashBag safely
    private Sound honuDmgSound;
    private float speed = 100f; // speed of trashbag (pixels/sec)
    private float detectionRange = 350f; // detection range
    private float x, y;

    //Constructor
    public TrashBag (int x, int y) {
        this.x = x;
        this.y = y;
        honuDmgSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\Pixel_Honu_Dmg_SFX.mp3"));
        hitBox = new Rectangle(x, y, 54, 54); //Change according to the game assets

        //Animation
        animation = new Animation<>(0.1f,
            new TextureRegion(new Texture("sprite\\enemyPNG\\54x54_PlasticBag_Stages1.png")),
            new TextureRegion(new Texture("sprite\\enemyPNG\\54x54_PlasticBag_Stages2.png")),
            new TextureRegion(new Texture("sprite\\enemyPNG\\54x54_PlasticBag_Stages3.png")),
            new TextureRegion(new Texture("sprite\\enemyPNG\\54x54_PlasticBag_Stages4.png"))
            );
        setPosition(x, y);
    }
    @Override
    public int hit(Rectangle rectangle) {
        if (!active) return -1;
        if (hitBox.overlaps(rectangle)) {
            active = false; // Mark itself for removal if hit Honu
            return 2;       // TrashBag damages Honu
        }
        return -1; // No collision
    }

    @Override
    public void action(int type, float x, float y) {

    }

    @Override
    public void update(float delta) {
        if (!active) return; // Don't update if inactive

        //get Honu location
        Honu honu = ReefGuardian.getInstance().getHonu();
        float honuX = honu.getHitBox().x;
        float honuY = honu.getHitBox().y;
        float distanceX = Math.abs(honuX - x);
        float distanceY = Math.abs(honuY - y);

        //If Honu is in range, chase him horizontally
        if (distanceX < detectionRange && distanceY < 100f) {
            isChasing = true;
            stateTime += delta;

            if (honuX < x) {
                x -= speed * delta;
            } else if (honuX > x) {
                x += speed * delta;
            }
            hitBox.setPosition(x, y);

            // Collision detection while chasing
            if (hitBox.overlaps(honu.getHitBox())) {
                active = false;  // Disappear

                // Honu takes damage
                honu.loseLife();
                if (honuDmgSound != null) honuDmgSound.play();

                // Honu gets knocked back
                if (honuX < x) {
                    honu.knockBack(-40f, 0f); // Honu left of TrashBag -> knock left
                } else {
                    honu.knockBack(40f, 0f); // Honu right of TrashBag -> knock right
                }
            }
        } else {
            isChasing = false;
            stateTime = 0f;
        }
    }

    @Override
    public void setPosition(float x, float y) {
        hitBox.setPosition(x, y);
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
        if (!active) return;

        TextureRegion currentFrame;
        if (isChasing) {
            currentFrame = animation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = animation.getKeyFrames()[0];
        }
        batch.draw(currentFrame, x, y, 54, 54);
    }

    @Override
    public Rectangle getHitBox() {
        return active ? hitBox : null; // If inactive, no hitbox;
    }

    @Override
    public int hitAction() {
        return active ? 2 : -1;
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
