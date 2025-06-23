package com.nopalsoft.zombiekiller;

import com.badlogic.gdx.Gdx;
import com.nopalsoft.zombiekiller.handlers.GameServicesHandler;
import com.nopalsoft.zombiekiller.handlers.GoogleGameServicesHandler;

public class Achievements {

	static boolean didInit = false;
	static GameServicesHandler gameHandler;

	static String collector, collectorMaster, aLittlePatience, anHonorableKill, stayDead, cleaningUp;

	public static void init(MainZombie game) {
		gameHandler = game.gameServiceHandler;

		if (gameHandler instanceof GoogleGameServicesHandler) {
			collector = "CgkI8sWCvZ0NEAIQAw";
			collectorMaster = "CgkI8sWCvZ0NEAIQBA";
			aLittlePatience = "CgkI8sWCvZ0NEAIQCA";
			anHonorableKill = "CgkI8sWCvZ0NEAIQCQ";
			stayDead = "CgkI8sWCvZ0NEAIQBQ";
			cleaningUp = "CgkI8sWCvZ0NEAIQBg";

		}
		else {
			collector = "CgkI8sWCvZ0NEAIQAw";
			collectorMaster = "CgkI8sWCvZ0NEAIQBA";
			aLittlePatience = "CgkI8sWCvZ0NEAIQCA";
			anHonorableKill = "CgkI8sWCvZ0NEAIQCQ";
			stayDead = "CgkI8sWCvZ0NEAIQBQ";
			cleaningUp = "CgkI8sWCvZ0NEAIQBg";
		}
		didInit = true;
	}

	private static boolean didInit() {
		if (didInit)
			return true;
		Gdx.app.log("Achievements", "You must call first Achievements.init()");
		return false;

	}

	public static void unlockKilledZombies(long num) {
		didInit();
		if (num >= 150)
			gameHandler.unlockAchievement(cleaningUp);
		else if (num >= 50)
			gameHandler.unlockAchievement(stayDead);
		else if (num >= 1)
			gameHandler.unlockAchievement(anHonorableKill);

	}

	public static void unlockCollectedSkulls(int skulls) {
		didInit();
		if (skulls >= 50)
			gameHandler.unlockAchievement(collectorMaster);
		else if (skulls >= 10)
			gameHandler.unlockAchievement(collector);

		if (Settings.NUM_MAPS * 3 == skulls) {
			gameHandler.unlockAchievement(aLittlePatience);
		}
	}
}
