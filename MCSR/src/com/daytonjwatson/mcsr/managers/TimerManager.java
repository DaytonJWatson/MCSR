package com.daytonjwatson.mcsr.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.daytonjwatson.mcsr.MCSR;

import at.actionbar.main.ActionbarAPI;

public class TimerManager {
	public static final Map<UUID, Long> elapsedMs = new HashMap<>();
	public static final Set<UUID> autoResume = new HashSet<>();
	public static final Map<UUID, BukkitTask> tasks = new HashMap<>();

	public static void startStopwatch(Player player) {
	    UUID uuid = player.getUniqueId();

	    autoResume.add(uuid);                 // mark that we should resume on join
	    stopTask(uuid);                       // prevent duplicates

	    long already = elapsedMs.getOrDefault(uuid, 0L);
	    long startEpoch = System.currentTimeMillis() - already;

	    BukkitTask task = new BukkitRunnable() {
	        @Override
	        public void run() {
	            Player p = Bukkit.getPlayer(uuid);
	            if (p == null || !p.isOnline()) {
	                cancel();
	                tasks.remove(uuid);
	                return;
	            }

	            long elapsed = System.currentTimeMillis() - startEpoch;
	            elapsedMs.put(uuid, elapsed);

	            long totalSeconds = elapsed / 1000;
	            long minutes = totalSeconds / 60;
	            long seconds = totalSeconds % 60;
	            long hundredths = (elapsed % 1000) / 10;

	            String msg =
	                "§6⏱ §a" +
	                (minutes < 10 ? "0" : "") + minutes + ":" +
	                (seconds < 10 ? "0" : "") + seconds + "." +
	                (hundredths < 10 ? "0" : "") + hundredths;

	            ActionbarAPI.sendActionbar(p, msg);
	        }
	    }.runTaskTimer(MCSR.instance, 0L, 2L);

	    tasks.put(uuid, task);
	}

	public static void stopTask(UUID uuid) {
	    BukkitTask existing = tasks.remove(uuid);
	    if (existing != null) existing.cancel();
	}
	
	public static long getElapsedMs(UUID uuid) {
	    return elapsedMs.getOrDefault(uuid, 0L);
	}

	public static String formatElapsed(long elapsed) {
	    long totalSeconds = elapsed / 1000;
	    long minutes = totalSeconds / 60;
	    long seconds = totalSeconds % 60;
	    long hundredths = (elapsed % 1000) / 10;

	    return (minutes < 10 ? "0" : "") + minutes + ":" +
	           (seconds < 10 ? "0" : "") + seconds + "." +
	           (hundredths < 10 ? "0" : "") + hundredths;
	}

	public static String getFinalTime(Player player) {
	    UUID uuid = player.getUniqueId();

	    // Ensure map has the latest value even if stop is called between ticks
	    long elapsed = getElapsedMs(uuid);
	    elapsedMs.put(uuid, elapsed);

	    return formatElapsed(elapsed);
	}
	
	public static String stopStopwatch(Player player) {
	    UUID uuid = player.getUniqueId();

	    String finalTime = getFinalTime(player);

	    autoResume.remove(uuid);
	    stopTask(uuid);

	    // optionally keep elapsedMs for later display or remove it:
	    // elapsedMs.remove(uuid);

	    return finalTime;
	}

	public static void stopStopwatch(UUID uuid) {
	    autoResume.remove(uuid);
	    stopTask(uuid);
	}
}
