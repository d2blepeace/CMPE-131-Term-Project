package io.github.ReefGuardianProject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Assets {
    public static SpriteBatch sprite;
    public static Texture textureBack;
    public static Sprite spriteBack;
    public static Music menuMusic;

    public static void load() {
        //render menu screen for test
        sprite = new SpriteBatch();
        textureBack = new Texture(Gdx.files.internal("assets/ReefGuardian.png"));
        textureBack.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteBack = new Sprite(textureBack);
        spriteBack.flip(false, true);

        //Play MainMenuTheme when open MainMenu
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\MainMenuTheme.mp3"));
        menuMusic.setLooping(true); //Loop the music
        menuMusic.setVolume(0.5f);  // Set volume (0.0 - 1.0)
    }

    public static void dispose() {
        //Clean up resource
        menuMusic.dispose();
    }
}
