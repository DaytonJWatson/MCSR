package com.daytonjwatson.mcsr.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.managers.LeaderboardEntry;

public class PlayerDataManager {
	private static final int MAX_RECENT = 10;
	private static final Map<UUID, PlayerRunData> cache = new HashMap<>();
	private static File dataFile;
	private static YamlConfiguration dataConfig;

	public static void initialize(MCSR plugin) {
		dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
		if (!dataFile.exists()) {
			plugin.getDataFolder().mkdirs();
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		loadFromConfig();
	}

	private static void loadFromConfig() {
		cache.clear();
		ConfigurationSection players = dataConfig.getConfigurationSection("players");
		if (players == null) {
			return;
		}
		for (String key : players.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(key);
				List<Long> recent = dataConfig.getLongList("players." + key + ".recentTimes");
				long personalBest = dataConfig.getLong("players." + key + ".personalBest", 0L);
				cache.put(uuid, new PlayerRunData(recent, personalBest));
			} catch (IllegalArgumentException ignored) {
				// Skip invalid UUIDs
			}
		}
	}

	public static void recordRun(UUID uuid, long elapsedMs) {
		if (elapsedMs <= 0L) {
			return;
		}
		PlayerRunData data = cache.computeIfAbsent(uuid, key -> new PlayerRunData());
		data.addRecentTime(elapsedMs, MAX_RECENT);
		data.updatePersonalBest(elapsedMs);
		savePlayer(uuid, data);
	}

	public static List<Long> getRecentTimes(UUID uuid) {
		PlayerRunData data = cache.get(uuid);
		if (data == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(new ArrayList<>(data.getRecentTimes()));
	}

	public static long getPersonalBest(UUID uuid) {
		PlayerRunData data = cache.get(uuid);
		return data == null ? 0L : data.getPersonalBestMs();
	}

	public static List<LeaderboardEntry> getTopRuns(int limit) {
		List<LeaderboardEntry> entries = new ArrayList<>();
		for (Map.Entry<UUID, PlayerRunData> entry : cache.entrySet()) {
			long best = entry.getValue().getPersonalBestMs();
			if (best > 0L) {
				entries.add(new LeaderboardEntry(entry.getKey(), best));
			}
		}
		entries.sort((a, b) -> Long.compare(a.getTimeMs(), b.getTimeMs()));
		if (limit > 0 && entries.size() > limit) {
			return new ArrayList<>(entries.subList(0, limit));
		}
		return entries;
	}

	public static String formatTime(long elapsedMs) {
		if (elapsedMs <= 0L) {
			return "N/A";
		}
		return TimerManager.formatElapsed(elapsedMs);
	}

	private static void savePlayer(UUID uuid, PlayerRunData data) {
		String basePath = "players." + uuid.toString();
		dataConfig.set(basePath + ".recentTimes", data.getRecentTimes());
		dataConfig.set(basePath + ".personalBest", data.getPersonalBestMs());
		try {
			dataConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
