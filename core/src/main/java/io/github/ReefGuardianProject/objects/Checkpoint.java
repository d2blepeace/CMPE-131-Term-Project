package io.github.ReefGuardianProject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Checkpoint extends GameObjects {
    Rectangle hitBox;
    Sprite sprite;
    Texture inactiveTexture, activeTexture;
    private boolean activated = false;
    private Sound checkpointSound;
    //Constructor
    public Checkpoint (int x, int y) {
        hitBox = new Rectangle(0,0,64,64);
        inactiveTexture = new Texture(Gdx.files.internal("PNG\\Checkpoint1.png"));
        activeTexture = new Texture(Gdx.files.internal("PNG\\Checkpoint2.png"));
        sprite = new Sprite(inactiveTexture, 0,0,64,64);

        //TODO: add checkpoint Sound
        checkpointSound = Gdx.audio.newSound(Gdx.files.internal("sfx\\checkpoint.mp3"));
        setPosition(x, y);
    }
    @Override
    public int hit(Rectangle rectangle) {
        if (!activated && hitBox.overlaps(rectangle)) {
            activateCheckpoint();
            return 4;   //Save Progress
        }
        return -1;      //already activate
    }

    public void activateCheckpoint() {
        activated = true;
        sprite.setTexture(activeTexture);
        //Play Sound
        if (checkpointSound != null) {
            checkpointSound.play();
        }
    }
    public boolean isActivated() {
        return activated;
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
        return 4; //Reach checkpoint
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public void dispose() {
        //Dispose texture and sound
        inactiveTexture.dispose();
        activeTexture.dispose();
        if (checkpointSound != null) checkpointSound.dispose();
    }

}
