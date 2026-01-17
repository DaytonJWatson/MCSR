package com.daytonjwatson.mcsr.managers;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.InventorySnapshot;
import com.daytonjwatson.mcsr.managers.RunAnnouncementManager;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class RunManager {
	private static final long OFFLINE_TIMEOUT_TICKS = 20L * 120;
	private static final String NETHER_SUFFIX = "_nether";
	private static final String END_SUFFIX = "_the_end";

	public static final HashMap<UUID, RunSession> activeRuns = new HashMap<>();

	public static void startRun(Player player, String baseWorldName) {
		UUID uuid = player.getUniqueId();
		RunSession existing = activeRuns.remove(uuid);
		if (existing != null) {
			cancelForfeitTask(existing);
		}
		RunSession session = new RunSession(baseWorldName);
		session.setInventorySnapshot(InventorySnapshot.fromPlayer(player));
		InventorySnapshot.clearPlayerInventory(player);
		activeRuns.put(uuid, session);
	}

	public static boolean isInRun(UUID uuid) {
		return activeRuns.containsKey(uuid);
	}

	public static RunSession getSession(UUID uuid) {
		return activeRuns.get(uuid);
	}

	public static void handleQuit(Player player) {
		RunSession session = activeRuns.get(player.getUniqueId());
		if (session == null) {
			return;
		}
		session.setLastLocation(player.getLocation());
		scheduleForfeit(player.getUniqueId());
	}

	public static void handleJoin(Player player) {
		RunSession session = activeRuns.get(player.getUniqueId());
		if (session == null) {
			return;
		}
		cancelForfeitTask(session);
		Location lastLocation = session.getLastLocation();
		if (lastLocation != null && isSpeedrunWorld(session, lastLocation.getWorld())) {
			Bukkit.getScheduler().runTaskLater(MCSR.instance, () -> player.teleport(cloneWithWorld(lastLocation)), 1L);
		}
	}

	public static void forfeitRun(Player player, String message) {
		forfeitRun(player.getUniqueId(), message, true);
	}

	public static void forfeitRun(UUID uuid, String message, boolean teleportToSpawn) {
		RunSession session = activeRuns.remove(uuid);
		if (session == null) {
			return;
		}
		cancelForfeitTask(session);

		Player online = Bukkit.getPlayer(uuid);
		if (online != null && online.isOnline()) {
			restoreInventory(online, session);
			if (teleportToSpawn) {
				online.teleport(Utils.spawnLocation());
			}
			if (message != null && !message.isEmpty()) {
				online.sendMessage(Utils.color(message));
			}
		}

		TimerManager.stopStopwatch(uuid);
		RunAnnouncementManager.reset(uuid);
		cleanupWorlds(session.getBaseWorldName());
	}

	public static void endRun(UUID uuid) {
		RunSession session = activeRuns.remove(uuid);
		if (session == null) {
			return;
		}
		cancelForfeitTask(session);
		Player online = Bukkit.getPlayer(uuid);
		if (online != null && online.isOnline()) {
			restoreInventory(online, session);
		}
	}

	private static void scheduleForfeit(UUID uuid) {
		RunSession session = activeRuns.get(uuid);
		if (session == null) {
			return;
		}
		cancelForfeitTask(session);
		BukkitTask task = Bukkit.getScheduler().runTaskLater(MCSR.instance, () -> {
			Player online = Bukkit.getPlayer(uuid);
			if (online != null && online.isOnline()) {
				return;
			}
			forfeitRun(uuid, null, false);
		}, OFFLINE_TIMEOUT_TICKS);
		session.setForfeitTask(task);
	}

	private static void cancelForfeitTask(RunSession session) {
		BukkitTask task = session.getForfeitTask();
		if (task != null) {
			task.cancel();
			session.setForfeitTask(null);
		}
	}

	private static void restoreInventory(Player player, RunSession session) {
		InventorySnapshot snapshot = session.getInventorySnapshot();
		if (snapshot != null) {
			snapshot.restore(player);
		}
	}

	private static boolean isSpeedrunWorld(RunSession session, World world) {
		if (world == null) {
			return false;
		}
		String name = world.getName();
		String base = session.getBaseWorldName();
		return name.equals(base) || name.equals(base + NETHER_SUFFIX) || name.equals(base + END_SUFFIX);
	}

	private static Location cloneWithWorld(Location location) {
		if (location == null) {
			return null;
		}
		World locationWorld = location.getWorld();
		if (locationWorld == null) {
			return Utils.spawnLocation();
		}
		World world = Bukkit.getWorld(locationWorld.getName());
		if (world == null) {
			return Utils.spawnLocation();
		}
		return new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	private static void cleanupWorlds(String baseName) {
		if (baseName == null || baseName.isEmpty()) {
			return;
		}
		World overworld = Bukkit.getWorld(baseName);
		World nether = Bukkit.getWorld(baseName + NETHER_SUFFIX);
		World theEnd = Bukkit.getWorld(baseName + END_SUFFIX);

		if (!unloadIfLoaded(overworld) || !unloadIfLoaded(nether) || !unloadIfLoaded(theEnd)) {
			return;
		}

		File worldContainer = Bukkit.getWorldContainer();
		Utils.deleteWorldFolder(new File(worldContainer, baseName));
		Utils.deleteWorldFolder(new File(worldContainer, baseName + NETHER_SUFFIX));
		Utils.deleteWorldFolder(new File(worldContainer, baseName + END_SUFFIX));
	}

	private static boolean unloadIfLoaded(World world) {
		return world == null || Bukkit.unloadWorld(world, false);
	}
}
