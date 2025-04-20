package io.github.ReefGuardianProject.objects.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


//The health bar UI of Honu
public class HonuHealthBar implements HealthBar{
    private Texture bar0, bar1, bar2, bar3;
    private int currLives;
    //Constructor
    public HonuHealthBar() {
        bar0 = new Texture("gameUI\\0Hearts_Honu.png");
        bar1 = new Texture("gameUI\\1Hearts_Honu.png");
        bar2 = new Texture("gameUI\\2Hearts_Honu.png");
        bar3 = new Texture("gameUI\\3Hearts_Honu.png");
    }
    @Override
    public void update(int currLives) {
        this.currLives = currLives;
    }

    @Override
    public void draw(SpriteBatch batch, OrthographicCamera camera) {
        Texture currBar;
        switch (currLives) {
            case 1:
                currBar = bar1;
                break;
            case 2:
                currBar = bar2;
                break;
            case 3:
                currBar = bar3;
                break;
            default:
                currBar = bar0;
                break;
        }
        batch.draw(currBar, camera.position.x - camera.viewportWidth / 2f + 20,
            980, 128, 32);
    }
}
