package io.github.ReefGuardianProject.objects.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface HealthBar {
    void update(int currLives);
    void draw(SpriteBatch batch, OrthographicCamera camera);
}
