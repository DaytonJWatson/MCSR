package com.daytonjwatson.mcsr.managers;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RunAnnouncementManager {
	public enum Stage {
		NETHER("Entered the Nether"),
		STRONGHOLD("Found the Stronghold"),
		END("Entered the End");

		private final String label;

		Stage(String label) {
			this.label = label;
		}
	}

	private static final Map<UUID, EnumSet<Stage>> announcedStages = new HashMap<>();

	public static void reset(UUID uuid) {
		announcedStages.remove(uuid);
	}

	public static void announceStage(Player player, Stage stage) {
		UUID uuid = player.getUniqueId();
		if (!markStage(uuid, stage)) {
			return;
		}

		long elapsedMs = TimerManager.getElapsedMs(uuid);
		if (elapsedMs <= 0L) {
			return;
		}

		String elapsedText = TimerManager.formatElapsed(elapsedMs);
		long personalBest = PlayerDataManager.getPersonalBest(uuid);
		String deltaText = formatDelta(elapsedMs, personalBest);

		String message = String.format(
				"§6[MCSR] §e%s §7— §fStage: §b%s §7| §fTime: §a%s §7| %s",
				player.getName(),
				stage.label,
				elapsedText,
				deltaText);
		Bukkit.broadcastMessage(message);
	}

	public static void announceFinish(Player player, long elapsedMs, long previousPersonalBest) {
		UUID uuid = player.getUniqueId();
		String elapsedText = TimerManager.formatElapsed(elapsedMs);
		String deltaText = formatDelta(elapsedMs, previousPersonalBest);
		String message = String.format(
				"§6[MCSR] §e%s §7— §fRun Complete: §a%s §7| %s",
				player.getName(),
				elapsedText,
				deltaText);
		Bukkit.broadcastMessage(message);
		reset(uuid);
	}

	private static boolean markStage(UUID uuid, Stage stage) {
		EnumSet<Stage> stages = announcedStages.computeIfAbsent(uuid, key -> EnumSet.noneOf(Stage.class));
		if (stages.contains(stage)) {
			return false;
		}
		stages.add(stage);
		return true;
	}

	private static String formatDelta(long elapsedMs, long personalBestMs) {
		if (personalBestMs <= 0L) {
			return "§7PB: §fN/A";
		}
		long deltaMs = elapsedMs - personalBestMs;
		String sign = deltaMs >= 0 ? "+" : "-";
		String deltaText = TimerManager.formatElapsed(Math.abs(deltaMs));
		String color = deltaMs <= 0 ? "§a" : "§c";
		return "§7Δ vs PB: " + color + sign + deltaText;
	}
}
