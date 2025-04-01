    package io.github.ReefGuardianProject.objects;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.Sprite;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Rectangle;


    public class BrickBlock extends GameObjects{
        public Rectangle hitBox;
        Sprite sprite;
        Texture texture;
        //Change the size of the brick block's size according to the asset here
        float width = 72.0f;
        float height = 57.0f;
        //Constructor
        public BrickBlock (int x, int y) {
            hitBox = new Rectangle(x, y, width, height); //Change according to the game assets
            texture = new Texture(Gdx.files.internal("map\\Rock1.png"));
            sprite = new Sprite(texture, 0, 0, 72, 57);
            setPosition(x, y);
        }

        @Override
        public void hit(Rectangle rectangle) {
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
    }
