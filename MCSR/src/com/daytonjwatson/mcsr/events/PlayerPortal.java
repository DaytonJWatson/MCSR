package com.daytonjwatson.mcsr.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {

    private static final String NETHER_SUFFIX = "_nether";
    private static final String END_SUFFIX = "_the_end";

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent e) {

        PlayerTeleportEvent.TeleportCause cause = e.getCause();
        if (cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL
                && cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }

        World fromWorld = e.getFrom().getWorld();
        if (fromWorld == null) return;

        World.Environment fromEnv = fromWorld.getEnvironment();
        String baseName = getBaseWorldName(fromWorld.getName());

        World targetWorld = null;

        // ---------- NETHER ----------
        if (cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (fromEnv == World.Environment.NORMAL) {
                targetWorld = Bukkit.getWorld(baseName + NETHER_SUFFIX);
            } else if (fromEnv == World.Environment.NETHER) {
                targetWorld = Bukkit.getWorld(baseName);
            }
        }

        // ---------- END ----------
        if (cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (fromEnv == World.Environment.NORMAL) {
                // Enter end from this overworld
                targetWorld = Bukkit.getWorld(baseName + END_SUFFIX);
            } else if (fromEnv == World.Environment.THE_END) {
                // Exit end back to this overworld (final portal after dragon)
                targetWorld = Bukkit.getWorld(baseName);
            }
        }

        if (targetWorld == null) {
            return; // don't override if we can't resolve the paired world
        }

        Location dest = e.getTo();
        if (dest == null) dest = e.getFrom().clone();

        // Always force the destination world to the paired world
        dest.setWorld(targetWorld);

        // Final end exit portal should ALWAYS go to overworld spawn
        if (cause == PlayerTeleportEvent.TeleportCause.END_PORTAL
                && fromEnv == World.Environment.THE_END) {
            dest = targetWorld.getSpawnLocation();
        }

        e.setTo(dest);
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