package com.nopalsoft.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(480, 800);
        new Lwjgl3Application(new MainSuperJumper(gameHandler), config);
    }


    static GameServicesHandler gameHandler = new GameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {

        }

        @Override
        public void submitScore(long score) {

        }

        @Override
        public void signOut() {

        }

        @Override
        public void signIn() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public void getLeaderboard() {

        }

        @Override
        public void getAchievements() {

        }
    };
}
