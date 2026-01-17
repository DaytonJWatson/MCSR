package com.daytonjwatson.mcsr.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.config.Config;

public class PlayerRespawn implements Listener {
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(event.getPlayer() == null)
			return;
		
		if(Config.getBoolean("spawn-settings.teleport-on-death"))
			event.setRespawnLocation(Utils.spawnLocation());
	}
}
