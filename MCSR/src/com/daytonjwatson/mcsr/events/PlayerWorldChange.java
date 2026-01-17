package com.daytonjwatson.mcsr.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.daytonjwatson.mcsr.managers.RunManager;

public class PlayerWorldChange implements Listener {
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (event.getPlayer() == null) {
			return;
		}
		RunManager.handleWorldChange(event.getPlayer(), event.getFrom(), event.getPlayer().getWorld());
	}
}
