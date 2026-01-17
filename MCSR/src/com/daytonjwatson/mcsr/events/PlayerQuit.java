package com.daytonjwatson.mcsr.events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.RunManager;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class PlayerQuit implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
	    UUID uuid = e.getPlayer().getUniqueId();
	    TimerManager.stopTask(uuid);
	    RunManager.handleQuit(e.getPlayer());
	    // elapsedMs already stored by the runnable; no need to remove it
	    
	    e.setQuitMessage(Utils.color("&8[&c-&8] &7"+ e.getPlayer().getName()));
	}
}
