package com.nopalsoft.zombiedash;

import com.badlogic.gdx.Gdx;
import com.nopalsoft.zombiedash.handlers.GameServicesHandler;
import com.nopalsoft.zombiedash.handlers.GoogleGameServicesHandler;

public class Achievements {

	static boolean didInit = false;
	static GameServicesHandler gameHandler;

	static String firstStep, zombieKiller, zombieHunter, zombieSlayer, zombieDestroyer, youCanNotHitMe;
	private static boolean doneFristStep, doneZombieYouCanNotHitMe;

	public static void init(MainZombieDash game) {
		gameHandler = game.gameServiceHandler;

		if (gameHandler instanceof GoogleGameServicesHandler) {
			firstStep = "CgkIpeX96dkTEAIQAw";
			zombieKiller = "CgkIpeX96dkTEAIQBA";
			zombieHunter = "CgkIpeX96dkTEAIQBQ";
			zombieSlayer = "CgkIpeX96dkTEAIQBg";
			zombieDestroyer = "CgkIpeX96dkTEAIQCA";
			youCanNotHitMe = "CgkIpeX96dkTEAIQBw";

		}
		else {
			firstStep = "20640";
			zombieKiller = "20642";
			zombieHunter = "20644";
			zombieSlayer = "20646";
			zombieDestroyer = "20648";
			youCanNotHitMe = "20650";
		}
		didInit = true;
	}

	/**
	 * Called when u start a new game so u can try to do achievements once more
	 */
	public static void tryAgainAchievements() {
		doneFristStep = false;
		doneZombieYouCanNotHitMe = false;
	}

	private static boolean didInit() {
		if (didInit)
			return true;
		Gdx.app.log("Achievements", "You must call first Achievements.init()");
		return false;

	}

	public static void unlockKilledZombies(long num) {
		didInit();
		if (num == 10) {
			gameHandler.unlockAchievement(zombieKiller);
		}
		else if (num == 25) {
			gameHandler.unlockAchievement(zombieHunter);
		}
		else if (num == 75) {
			gameHandler.unlockAchievement(zombieSlayer);
		}
		else if (num == 100) {
			gameHandler.unlockAchievement(zombieDestroyer);
		}

	}

	public static void distance(int distance, boolean didGetHurt) {
		didInit();
		if (distance > 1 && !doneFristStep) {
			doneFristStep = true;
			gameHandler.unlockAchievement(firstStep);
		}
		else if (distance > 500 && !doneZombieYouCanNotHitMe && !didGetHurt) {
			doneZombieYouCanNotHitMe = true;
			gameHandler.unlockAchievement(youCanNotHitMe);
		}

	}
}