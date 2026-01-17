package com.daytonjwatson.mcsr.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerRunData {
	private final List<Long> recentTimes;
	private long personalBestMs;

	public PlayerRunData() {
		this.recentTimes = new ArrayList<>();
		this.personalBestMs = 0L;
	}

	public PlayerRunData(List<Long> recentTimes, long personalBestMs) {
		this.recentTimes = new ArrayList<>(recentTimes == null ? Collections.emptyList() : recentTimes);
		this.personalBestMs = personalBestMs;
	}

	public List<Long> getRecentTimes() {
		return recentTimes;
	}

	public long getPersonalBestMs() {
		return personalBestMs;
	}

	public void addRecentTime(long elapsedMs, int maxRecent) {
		recentTimes.add(0, elapsedMs);
		if (recentTimes.size() > maxRecent) {
			recentTimes.subList(maxRecent, recentTimes.size()).clear();
		}
	}

	public void updatePersonalBest(long elapsedMs) {
		if (personalBestMs == 0L || elapsedMs < personalBestMs) {
			personalBestMs = elapsedMs;
		}
	}
}
