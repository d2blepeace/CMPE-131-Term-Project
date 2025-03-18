package io.github.ReefGuardianProject;

import com.badlogic.gdx.Game;

public class ReefGuardian extends Game {
    //Game Screen
    public ReefGuardianScreen reefGuardianScreen;

    @Override
    public void create() {
        //Load assets screen: sprite and texture
        Assets.load();

        reefGuardianScreen = new ReefGuardianScreen(this);

        //Set the screen
        setScreen(reefGuardianScreen);

    }
}
