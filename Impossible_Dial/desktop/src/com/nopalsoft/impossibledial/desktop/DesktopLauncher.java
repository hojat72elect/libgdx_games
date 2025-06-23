package com.nopalsoft.impossibledial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.handlers.FacebookHandler;
import com.nopalsoft.impossibledial.handlers.GoogleGameServicesHandler;
import com.nopalsoft.impossibledial.handlers.RequestHandler;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800;
		config.width = 480;
		new LwjglApplication(new MainGame( handler, gameHandler, faceHandler), config);
	}

	static RequestHandler handler = new RequestHandler() {

		@Override
		public void showRater() {

		}


		@Override
		public void showInterstitial() {

		}

		@Override
		public void showAdBanner() {

		}


		@Override
		public void removeAds() {

		}

		@Override
		public void hideAdBanner() {

		}

		@Override
		public void shareAPK() {

		}

		@Override
		public void loadInterstitial() {

		}
	};

	static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

		@Override
		public void submitScore(long score, String leaderboardID) {

		}

		@Override
		public void unlockAchievement(String achievementId) {

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
		public void unlockStepAchievement(float steps, String achievementID) {

		}


		@Override
		public void getAchievements() {

		}


	};

	static FacebookHandler faceHandler = new FacebookHandler() {

		@Override
		public void showFacebook() {

		}


	};
}
