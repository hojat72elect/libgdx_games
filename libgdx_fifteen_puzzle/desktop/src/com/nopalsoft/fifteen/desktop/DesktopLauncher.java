package com.nopalsoft.fifteen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.fifteen.MainFifteen;
import com.nopalsoft.fifteen.handlers.GoogleGameServicesHandler;
import com.nopalsoft.fifteen.handlers.RequestHandler;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FifteenPuzzle";
		cfg.width = 480;
		cfg.height = 800;

		new LwjglApplication(
				new MainFifteen( handler, gameHandler), cfg);
	}

	static RequestHandler handler = new RequestHandler() {

		@Override
		public void showRater() {}

		@Override
		public void showMoreGames() {}

		@Override
		public void showInterstitial() {}

		@Override
		public void showFacebook() {}

		@Override
		public void showAdBanner() {}

		@Override
		public void shareOnTwitter(String mensaje) {}

		@Override
		public void shareOnFacebook(String mensaje) {}

		@Override
		public void restorePurchases() {}

		@Override
		public void removeAds() {}

		@Override
		public void hideAdBanner() {}

		@Override
		public void buyNoAds() {}
	};

	static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

		@Override
		public void unlockAchievement(String achievementId) {}

		@Override
		public void submitScore(long score, String leaderboard) {}

		@Override
		public void signOut() {}

		@Override
		public void signIn() {}

		@Override
		public boolean isSignedIn() {return false;}

		@Override
		public void getLeaderboard() {}

		@Override
		public void getAchievements() {}
	};
}
