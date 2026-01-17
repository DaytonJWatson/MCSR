package com.daytonjwatson.mcsr.managers;

import java.util.UUID;

public class LeaderboardEntry {
	private final UUID uuid;
	private final long timeMs;

	public LeaderboardEntry(UUID uuid, long timeMs) {
		this.uuid = uuid;
		this.timeMs = timeMs;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getTimeMs() {
		return timeMs;
	}
}
