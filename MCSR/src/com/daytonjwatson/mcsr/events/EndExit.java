package com.daytonjwatson.mcsr.events;

import org.bukkit.World;
import org.bukkit.boss.DragonBattle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class EndExit implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onPortal(PlayerPortalEvent e) {
	    if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;

	    World fromWorld = e.getFrom().getWorld();
	    World toWorld = (e.getTo() != null) ? e.getTo().getWorld() : null;
	    if (fromWorld == null || toWorld == null) return;

	    if (fromWorld.getEnvironment() != World.Environment.THE_END) return;
	    if (toWorld.getEnvironment() != World.Environment.NORMAL) return;

	    DragonBattle battle = fromWorld.getEnderDragonBattle();
	    if (battle == null) return;

	    if (battle.hasBeenPreviouslyKilled()) {
	        String finalTime = TimerManager.stopStopwatch(e.getPlayer());
	        e.getPlayer().sendMessage("§6Final time: §a" + finalTime);
	        
	        e.setTo(Utils.spawnLocation());
	    }
	}

}
