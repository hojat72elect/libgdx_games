package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.nopalsoft.slamthebird.handlers.GameServicesHandler;
import com.nopalsoft.slamthebird.handlers.GoogleGameServicesHandler;

public class Achievements {

	static boolean didInit = false;
	static GameServicesHandler gameHandler;

	static String SuperJump, SlamCombo, SuperSlam, InvencibleSlam, YouGotAnySpareChange, coinMaster;

	public static void init(MainSlamBird game) {
		gameHandler = game.gameServiceHandler;

		if (gameHandler instanceof GoogleGameServicesHandler) {
			SuperJump = "CgkI5qq7_pkVEAIQBQ";
			SlamCombo = "CgkI5qq7_pkVEAIQBw";
			SuperSlam = "CgkI5qq7_pkVEAIQCA";
			InvencibleSlam = "CgkI5qq7_pkVEAIQCg";
			YouGotAnySpareChange = "CgkI5qq7_pkVEAIQCQ";
			coinMaster = "CgkI5qq7_pkVEAIQBg";

		}
		else {
			SuperJump = "20274";
			SlamCombo = "20276";
			SuperSlam = "20278";
			InvencibleSlam = "20280";
			YouGotAnySpareChange = "20282";
			coinMaster = "20284";
		}
		didInit = true;
	}

	private static boolean didInit() {
		if (didInit)
			return true;
		Gdx.app.log("Achievements", "You must call first Achievements.init()");
		return false;

	}

	public static void unlockSuperJump() {
		didInit();
		gameHandler.unlockAchievement(SuperJump);
	}

	public static void unlockCombos(int combo, boolean isInvencible) {
		didInit();
		if (combo >= 15) {
			gameHandler.unlockAchievement(SuperSlam);
		}
		else if (combo >= 10) {
			gameHandler.unlockAchievement(SlamCombo);
			if (isInvencible)
				gameHandler.unlockAchievement(InvencibleSlam);

		}
	}

	public static void unlockCoins(int coins) {
		didInit();
		if (coins >= 100)
			gameHandler.unlockAchievement(YouGotAnySpareChange);
		else if (coins >= 500)
			gameHandler.unlockAchievement(coinMaster);
	}

	class ass implements Serializable {

		@Override
		public void write(Json json) {
			// TODO Auto-generated method stub

		}

		@Override
		public void read(Json json, JsonValue jsonData) {
			// TODO Auto-generated method stub

		}

	}

}
