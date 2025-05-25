package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;
import com.nopalsoft.sharkadventure.handlers.GameServicesHandler;
import com.nopalsoft.sharkadventure.handlers.GoogleGameServicesHandler;

public class Achievements {

	static boolean didInit = false;
	static GameServicesHandler gameHandler;

	static String firstStep, swimer, submarineKiller, submarineHunter, submarineSlayer, youCanNotHitMe;
	private static boolean doneFristStep, doneSwimer, doneYouCanNotHitMe;

	public static void init(MainShark game) {
		gameHandler = game.gameServiceHandler;

		if (gameHandler instanceof GoogleGameServicesHandler) {
			firstStep = "CgkIwICJ-poIEAIQAw";
			swimer = "CgkIwICJ-poIEAIQBA";
			submarineKiller = "CgkIwICJ-poIEAIQBQ";
			submarineHunter = "CgkIwICJ-poIEAIQBg";
			submarineSlayer = "CgkIwICJ-poIEAIQBw";
			youCanNotHitMe = "CgkIwICJ-poIEAIQCA";

		}
		else {
			firstStep = "FirstStepID";
			swimer = "SwimmerID";
			submarineKiller = "SubmarinekillerID";
			submarineHunter = "SubmarinehunterID";
			submarineSlayer = "SubmarineslayerID";
			youCanNotHitMe = "YouCantHitMe";
		}
		didInit = true;
	}

	/**
	 * Called when u start a new game so u can try to do achievements once more
	 */
	public static void tryAgainAchievements() {
		doneFristStep = false;
		doneYouCanNotHitMe = false;
		doneSwimer = false;
	}

	private static boolean didInit() {
		if (didInit)
			return true;
		Gdx.app.log("Achievements", "You must call first Achievements.init()");
		return false;

	}

	public static void unlockKilledSubmarines(long num) {
		didInit();
		if (num == 3) {
			gameHandler.unlockAchievement(submarineKiller);
		}
		else if (num == 6) {
			gameHandler.unlockAchievement(submarineHunter);
		}
		else if (num == 10) {
			gameHandler.unlockAchievement(submarineSlayer);
		}

	}

	public static void distance(long distance, boolean didGetHurt) {
		didInit();
		if (distance > 1 && !doneFristStep) {
			doneFristStep = true;
			gameHandler.unlockAchievement(firstStep);
		}
		if (distance > 2500 && !doneSwimer) {
			doneSwimer = true;
			gameHandler.unlockAchievement(swimer);
		}
		if (distance > 850 && !doneYouCanNotHitMe && !didGetHurt) {
			doneYouCanNotHitMe = true;
			gameHandler.unlockAchievement(youCanNotHitMe);
		}

	}
}