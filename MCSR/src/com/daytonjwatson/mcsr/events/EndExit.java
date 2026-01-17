package com.daytonjwatson.mcsr.events;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.PlayerDataManager;
import com.daytonjwatson.mcsr.managers.RunAnnouncementManager;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class EndExit implements Listener {
	private static final String NETHER_SUFFIX = "_nether";
	private static final String END_SUFFIX = "_the_end";

	@EventHandler(ignoreCancelled = true)
	public void onPortal(PlayerPortalEvent e) {
	    if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;

	    World fromWorld = e.getFrom().getWorld();
	    if (fromWorld == null) return;

	    if (fromWorld.getEnvironment() != World.Environment.THE_END) return;

	    long elapsedMs = TimerManager.getElapsedMs(e.getPlayer().getUniqueId());
	    long previousBest = PlayerDataManager.getPersonalBest(e.getPlayer().getUniqueId());
	    String finalTime = TimerManager.stopStopwatch(e.getPlayer());
	    PlayerDataManager.recordRun(e.getPlayer().getUniqueId(), elapsedMs);
	    RunAnnouncementManager.announceFinish(e.getPlayer(), elapsedMs, previousBest);
	    e.getPlayer().sendMessage("§6Final time: §a" + finalTime);
	    
	    e.setTo(Utils.spawnLocation());

	    String baseName = getBaseWorldName(fromWorld.getName());
	    if (!baseName.endsWith("_speedrun")) {
	        return;
	    }
	    Bukkit.getScheduler().runTaskLater(MCSR.instance, () -> cleanupWorlds(baseName), 1L);
	}

	private void cleanupWorlds(String baseName) {
	    World overworld = Bukkit.getWorld(baseName);
	    World nether = Bukkit.getWorld(baseName + NETHER_SUFFIX);
	    World theEnd = Bukkit.getWorld(baseName + END_SUFFIX);

	    if (hasPlayers(overworld) || hasPlayers(nether) || hasPlayers(theEnd)) {
	        return;
	    }

	    if (!unloadIfLoaded(overworld) || !unloadIfLoaded(nether) || !unloadIfLoaded(theEnd)) {
	        return;
	    }

	    File worldContainer = Bukkit.getWorldContainer();
	    Utils.deleteWorldFolder(new File(worldContainer, baseName));
	    Utils.deleteWorldFolder(new File(worldContainer, baseName + NETHER_SUFFIX));
	    Utils.deleteWorldFolder(new File(worldContainer, baseName + END_SUFFIX));
	}

	private boolean unloadIfLoaded(World world) {
	    return world == null || Bukkit.unloadWorld(world, false);
	}

	private boolean hasPlayers(World world) {
	    return world != null && !world.getPlayers().isEmpty();
	}

	private String getBaseWorldName(String worldName) {
	    if (worldName.endsWith(NETHER_SUFFIX)) {
	        return worldName.substring(0, worldName.length() - NETHER_SUFFIX.length());
	    }
	    if (worldName.endsWith(END_SUFFIX)) {
	        return worldName.substring(0, worldName.length() - END_SUFFIX.length());
	    }
	    return worldName;
	}
}
