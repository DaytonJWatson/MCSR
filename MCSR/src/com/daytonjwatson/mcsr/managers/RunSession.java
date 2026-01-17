package com.daytonjwatson.mcsr.managers;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import com.daytonjwatson.mcsr.managers.InventorySnapshot;

public class RunSession {
	private final String baseWorldName;
	private Location lastLocation;
	private Location lastOverworldPortalLocation;
	private BukkitTask forfeitTask;
	private InventorySnapshot inventorySnapshot;

	public RunSession(String baseWorldName) {
		this.baseWorldName = baseWorldName;
	}

	public String getBaseWorldName() {
		return baseWorldName;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Location getLastOverworldPortalLocation() {
		return lastOverworldPortalLocation;
	}

	public void setLastOverworldPortalLocation(Location lastOverworldPortalLocation) {
		this.lastOverworldPortalLocation = lastOverworldPortalLocation;
	}

	public BukkitTask getForfeitTask() {
		return forfeitTask;
	}

	public void setForfeitTask(BukkitTask forfeitTask) {
		this.forfeitTask = forfeitTask;
	}

	public InventorySnapshot getInventorySnapshot() {
		return inventorySnapshot;
	}

	public void setInventorySnapshot(InventorySnapshot inventorySnapshot) {
		this.inventorySnapshot = inventorySnapshot;
	}
}
